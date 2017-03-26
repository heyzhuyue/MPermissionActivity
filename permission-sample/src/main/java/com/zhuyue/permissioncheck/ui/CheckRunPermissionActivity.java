package com.zhuyue.permissioncheck.ui;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.zhuyue.annotation.PermissionDenied;
import com.zhuyue.annotation.PermissionGrant;
import com.zhuyue.permissioncheck.R;
import com.zhuyue.permissionlib.PermissionCheck;

/**
 * Created by zhuyue on 2017/3/26
 */

public class CheckRunPermissionActivity extends AppCompatActivity {
    private static final String TAG = "CheckRunPermissionActiv";
    public static final int CAMERA_CODE = 100;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_runpermission_check);
    }

    public void OnCamera(View view) {
        Log.i(TAG, "拍照");
        PermissionCheck.requestPermissionByCompiler(CheckRunPermissionActivity.this, CAMERA_CODE, new String[]{
                Manifest.permission.CAMERA});
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionCheck.onRequestPermissionsResultByCompiler(CheckRunPermissionActivity.this, CAMERA_CODE, permissions, grantResults);
    }

    @PermissionGrant(CAMERA_CODE)
    public void requestCameraSuccess() {
        Toast.makeText(this, "GRANT ACCESS SDCARD!", Toast.LENGTH_SHORT).show();
    }

    @PermissionDenied(CAMERA_CODE)
    public void requestCameraFailed() {
        Toast.makeText(this, "DENY ACCESS SDCARD!", Toast.LENGTH_SHORT).show();
    }

}
