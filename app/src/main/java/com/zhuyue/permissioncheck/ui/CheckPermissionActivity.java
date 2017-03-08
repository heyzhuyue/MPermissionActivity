package com.zhuyue.permissioncheck.ui;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.view.View;

import com.zhuyue.permissioncheck.R;
import com.zhuyue.permissioncheck.base.BasePermissioActivity;
import com.zhuyue.permissionlib.PermissionCheck;

/**
 * Created by win7 on 2017/3/8.
 */

public class CheckPermissionActivity extends BasePermissioActivity {

    @Override
    public int getLayoutId() {
        return R.layout.activity_check;
    }

    /**
     * 拍照权限
     *
     * @param view
     */
    public void OnCamera(View view) {
        PermissionCheck.requestPermission(CheckPermissionActivity.this, 0x0003, new String[]{Manifest.permission.CAMERA});
    }

    /**
     * 打电话权限
     *
     * @param view
     */
    public void OnCallPhone(View view) {
        PermissionCheck.requestPermission(CheckPermissionActivity.this, 0x0001, new String[]{Manifest.permission.CALL_PHONE});
    }

    @Override
    public void grant(Object source, int requestCode) {
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

    @Override
    public void rationale(Object source, int requestCode) {

    }
}
