package com.zhuyue.permissioncheck.ui;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.zhuyue.permissioncheck.R;
import com.zhuyue.permissioncheck.base.BaseCheckPermissionActivity;
import com.zhuyue.permissionlib.PermissionCheck;

public class CheckActivity extends BaseCheckPermissionActivity {

    private static final String TAG = "CheckActivity";

    @Override
    public int getLayoutId() {
        return R.layout.activity_check;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * 拍照权限
     *
     * @param view
     */
    public void OnCamera(View view) {
        requestPermission(new String[]{Manifest.permission.CAMERA}, 0x0003);
        PermissionCheck.requestPermission(CheckActivity.this, 0x0003, new String[]{Manifest.permission.CAMERA});
    }

    /**
     * 打电话权限
     *
     * @param view
     */
    public void OnCallPhone(View view) {
        requestPermission(new String[]{Manifest.permission.CALL_PHONE}, 0x0001);
    }

    @Override
    public void requestPermissionSucess(int requestCode) {
        super.requestPermissionSucess(requestCode);
        if (requestCode == 0x0003) {
            Intent intent = new Intent(); //调用照相机
            intent.setAction("android.media.action.STILL_IMAGE_CAMERA");
            startActivity(intent);
        } else if (requestCode == 0x0001) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:13045020882"));
            startActivity(intent);
        }
    }
}
