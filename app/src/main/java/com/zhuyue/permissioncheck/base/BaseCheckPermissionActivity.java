package com.zhuyue.permissioncheck.base;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.zhuyue.permissionlib.PermissionCheck;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by win7 on 2017/3/8.
 */

/**
 * 运行时权限方法说明
 * <p>
 * ① ContextCompat.checkSelfPermission  用于检测某个权限是否已经被授予，方法返回值为PackageManager.PERMISSION_DENIED或者PackageManager.PERMISSION_GRANTED，当返回DENIED就需要进行申请授权了
 * ② ActivityCompat.shouldShowRequestPermissionRationale 这个API主要用于给用户一个申请权限的解释，该方法只有在用户在上一次已经拒绝过你的这个权限申请。也就是说，用户已经拒绝一次了，你又弹个授权框，你需要给用户一个解释，为什么要授权，则使用该方法。
 * ③ ActivityCompat.requestPermissions  权限申请 支持多个权限申请,系统会根据权限逐一向用户询问授权
 * ④ onRequestPermissionsResult  权限申请回调
 */

public abstract class BaseCheckPermissionActivity extends AppCompatActivity {

    private static final String TAG = "BaseCheckPermissionActivity";
    /**
     * 权限申请自定义RequestCode,用于权限申请验证Code
     */
    private int REQUEST_CODE_PERMISSION = 0x00099;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
    }

    /**
     * 系统申请权限回调
     *
     * @param requestCode  requestCode权限申请验证Code
     * @param permissions  权限集合
     * @param grantResults 所有申请权限申请结果
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSION) {
            if (verifyPermissions(grantResults)) {
                requestPermissionSucess(REQUEST_CODE_PERMISSION);
            } else {
                requestPermissionFail(REQUEST_CODE_PERMISSION);
                showRequestPermissionFailHint();
            }
        }
    }

    /**
     * 获取布局id
     *
     * @return
     */
    public abstract int getLayoutId();

    /**
     * 申请权限获取
     *
     * @param permissions 权限集合
     * @param requestCode requestCode
     */
    public void requestPermission(String[] permissions, int requestCode) {
        this.REQUEST_CODE_PERMISSION = requestCode;
        if (checkPermission(permissions)) {
            requestPermissionSucess(REQUEST_CODE_PERMISSION);
        } else {
            List<String> needPermissions = getDeniedPermissions(permissions);
            ActivityCompat.requestPermissions(this, needPermissions.toArray(new String[needPermissions.size()]), REQUEST_CODE_PERMISSION);
        }
    }

    /**
     * 检测所有权限是否已经授权
     *
     * @param permissions 权限集合
     * @return
     */
    private boolean checkPermission(String[] permissions) {
        /**
         * SDK版本<Android M(Api 26)
         */
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) !=
                    PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    /**
     * 获取集中需要申请权限列表
     *
     * @param permissions 权限集合
     * @return
     */
    private List<String> getDeniedPermissions(String[] permissions) {
        List<String> needRequestPermissionList = new ArrayList<>();
        for (String permission : permissions) {
            /**
             * PackageManager.PERMISSION_GRANTED 申请授权,PackageManager.PERMISSION_GRANTED 不需要申请授权
             */
            if (ContextCompat.checkSelfPermission(this, permission) !=
                    PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                needRequestPermissionList.add(permission);
            }
        }
        return needRequestPermissionList;
    }

    /**
     * 确认所有的权限是否都已授权
     *
     * @param grantResults 权限申请结果
     * @return
     */
    private boolean verifyPermissions(int[] grantResults) {
        for (int grantResult : grantResults) {
            if (grantResult != PackageManager.PERMISSION_GRANTED) {
                /**
                 *  PackageManager.PERMISSION_GRANTED 权限申请成功，不需要申请授权
                 */
                return false;
            }
        }
        return true;
    }

    /**
     * 权限获取成功
     */
    public void requestPermissionSucess(int requestCode) {
        Log.i(TAG, "权限获取成功" + requestCode);
    }

    /**
     * 权限获取失败
     */
    public void requestPermissionFail(int requestCode) {
        Log.i(TAG, "权限获取失败" + requestCode);
    }

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
