package com.zhuyue.permissioncheck.base;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by zhuyue on 2017/3/29
 */

public abstract class BaseCheckRunPermissionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        initView();
        initViewClick();
    }

    /**
     * 获取布局Id
     *
     * @return
     */
    public abstract int getLayoutId();

    /**
     * 初始化View
     */
    public abstract void initView();

    /**
     * 初始化View监听
     */
    public abstract void initViewClick();

    /**
     * 显示获取权限失败后提示用户进入设置界面开启权限弹窗
     */
    private void showRequestPermissionFailHint() {
        new AlertDialog.Builder(this)
                .setTitle("权限提示")
                .setMessage("当前应用缺少必要权限，该功能暂时无法使用。如若需要，请单击【确定】按钮前往设置中心进行权限授权。")
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startAppSettings();
                    }
                }).show();
    }

    /**
     * 开启设置界面
     */
    private void startAppSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + getPackageName()));
        startActivity(intent);
    }

}
