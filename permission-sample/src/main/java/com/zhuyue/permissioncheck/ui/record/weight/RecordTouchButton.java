package com.zhuyue.permissioncheck.ui.record.weight;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zhuyue.permissioncheck.R;
import com.zhuyue.permissioncheck.ui.record.OnRecordStateInterface;
import com.zhuyue.permissioncheck.ui.record.utils.PopupWindowFactory;
import com.zhuyue.permissioncheck.ui.record.utils.TimeUtils;
import com.zhuyue.permissioncheck.ui.record.utils.WzAudioRecordUtils;

/**
 * Created by zhuyue on 2017/3/29
 */

public class RecordTouchButton extends Button {

    private View view = null;
    private PopupWindowFactory mPop = null;
    private ImageView mImageView;
    private TextView mTextView;
    private WzAudioRecordUtils mAudioRecoderUtils;
    private OnRecordStateInterface onRecordStateInterface;
    private View mRecordPopView;

    public void setOnRecordStateInterface(OnRecordStateInterface onRecordStateInterface) {
        this.onRecordStateInterface = onRecordStateInterface;
    }

    /**
     * 设置显示在某个View上
     *
     * @param view view
     */
    public void setRecordPopView(View view) {
        this.mRecordPopView = view;
    }

    public RecordTouchButton(Context context) {
        super(context);
        initRecordView();
    }

    public RecordTouchButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        initRecordView();
    }

    public RecordTouchButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initRecordView();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                this.setText("松开保存");
                mPop.showAtLocation(mRecordPopView, Gravity.CENTER, 0, 0);
                mAudioRecoderUtils.startRecord();
                onRecordStateInterface.onStartRecord();
                break;
            case MotionEvent.ACTION_UP:
                this.setText("按住说话");
                mAudioRecoderUtils.endRecord(); //结束录音（保存录音文件）
                mPop.dismiss();
                break;
        }
        return true;
    }

    /***
     * 初始化录音相关工具
     */
    public void initRecordView() {
        view = View.inflate(getContext(), R.layout.layout_microphone, null);
        mPop = new PopupWindowFactory(getContext(), view);
        mImageView = (ImageView) view.findViewById(R.id.iv_recording_icon);
        mTextView = (TextView) view.findViewById(R.id.tv_recording_time);
        mAudioRecoderUtils = WzAudioRecordUtils.getInstance();
        if (getContext() instanceof Activity) {
            Activity activity = (Activity) getContext();
            mRecordPopView = activity.getWindow().getDecorView();
        }
        //录音回调
        mAudioRecoderUtils.setOnAudioStatusUpdateListener(new WzAudioRecordUtils.OnAudioStatusUpdateListener() {
            @Override
            public void onUpdate(double db, long time) {
                mImageView.getDrawable().setLevel((int) (3000 + 6000 * db / 100));
                mTextView.setText(TimeUtils.long2String(time));
            }

            @Override
            public void onStop(String filePath) {
                onRecordStateInterface.onStopRecord(filePath);
                Toast.makeText(getContext(), "录音保存在：" + filePath, Toast.LENGTH_SHORT).show();
                mTextView.setText(TimeUtils.long2String(0));
            }
        });
    }
}
