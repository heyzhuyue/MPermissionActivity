package com.zhuyue.permissioncheck.ui.record.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhuyue.permissioncheck.R;
import com.zhuyue.permissioncheck.ui.record.utils.TimeUtils;


/**
 * Created by win7 on 2016/12/16.
 * <p>
 * 录音弹窗
 */

public class RecordDialog extends Dialog {

    private Context mContext;
    private View contentView;
    private ImageView imgVoice;
    private TextView tvRecordTime;
    private TextView tvRecordState;

    public RecordDialog(Context context) {
        super(context);
        this.mContext = context;
        init();
    }

    public RecordDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.mContext = context;
        init();
    }

    /**
     * 初始化
     */
    private void init() {
        contentView = LayoutInflater.from(mContext).inflate(R.layout.dialog_recordbutton_alert_dialog, null);
        setContentView(contentView);
        imgVoice = (ImageView) contentView.findViewById(R.id.zeffect_recordbutton_dialog_imageview);
        tvRecordTime = (TextView) contentView.findViewById(R.id.zeffect_recordbutton_dialog_time_tv);
        tvRecordState = (TextView) contentView.findViewById(R.id.zeffect_recordbutton_dialog_title_tv);
    }


    /**
     * 显示录音弹窗
     */
    public void showRecordDialog() {
        if (!this.isShowing()) {
            this.show();
        }
    }

    /**
     * 隐藏弹窗
     */
    public void hideRecordDialog() {
        if (this.isShowing()) {
            this.dismiss();
        }
    }

    /**
     * 设置录音时间
     *
     * @param time 时间(毫秒)
     */
    public void setRecordTime(long time) {
        tvRecordTime.setText(TimeUtils.long2String(time));
    }

    /**
     * 设置录音状态
     *
     * @param stateTitle
     */
    public void setRecordState(String stateTitle) {
        tvRecordState.setText(stateTitle);
    }

    /**
     * 设置麦克风振幅
     *
     * @param db
     */
    public void setVolumeLevel(double db) {
        imgVoice.getDrawable().setLevel((int) (3000 + 6000 * db / 100));
    }

}
