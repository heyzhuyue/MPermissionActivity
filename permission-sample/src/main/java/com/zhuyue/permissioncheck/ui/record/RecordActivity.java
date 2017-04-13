package com.zhuyue.permissioncheck.ui.record;

import android.Manifest;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.zhuyue.annotation.PermissionDenied;
import com.zhuyue.annotation.PermissionGrant;
import com.zhuyue.permissioncheck.R;
import com.zhuyue.permissioncheck.base.BaseCheckRunPermissionActivity;
import com.zhuyue.permissioncheck.ui.record.weight.RecordTouchButton;
import com.zhuyue.permissionlib.PermissionCheck;

/**
 * Created by zhuyue on 2017/3/29
 */

public class RecordActivity extends BaseCheckRunPermissionActivity implements OnRecordStateInterface {

    private RecordTouchButton mRecordTouchButton;
    private LinearLayout llRecord;
    public static final int RECORD_CODE = 100;

    @Override
    public int getLayoutId() {
        return R.layout.activity_record;
    }

    @Override
    public void initView() {
        llRecord = (LinearLayout) findViewById(R.id.ll_record);
        mRecordTouchButton = (RecordTouchButton) findViewById(R.id.bt_start_record);
        mRecordTouchButton.setRecordPopView(llRecord);
    }

    @Override
    public void initViewClick() {
        mRecordTouchButton.setOnRecordStateInterface(this);
    }

    /**
     * 开始录音
     *
     * @param view view
     */
    public void onStartRecord(View view) {
        PermissionCheck.requestPermissionByCompiler(RecordActivity.this, RECORD_CODE, new String[]{
                Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE});
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionCheck.onRequestPermissionsResultByCompiler(RecordActivity.this, RECORD_CODE, permissions, grantResults);
    }

    @PermissionGrant(RECORD_CODE)
    public void requestRecordSuccess() {
        Toast.makeText(this, "GRANT ACCESS SDCARD!", Toast.LENGTH_SHORT).show();
    }

    @PermissionDenied(RECORD_CODE)
    public void requestRecordFailed() {
        Toast.makeText(this, "DENY ACCESS SDCARD!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStartRecord() {
        Toast.makeText(RecordActivity.this, "录音开始", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStopRecord(String filePath) {
        Toast.makeText(RecordActivity.this, "录音停止", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCancleRecord() {
        Toast.makeText(RecordActivity.this, "录音取消", Toast.LENGTH_SHORT).show();
    }
}
