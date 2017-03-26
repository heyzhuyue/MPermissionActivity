package com.zhuyue.permissioncheck.utill;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;

/**
 * Created by win7 on 2017/3/8.
 */

public class OpenSettingUtils {

    /**
     * 打开应用权限设置界面
     *
     * @param context 上下文
     */
    public static void openAppSetting(Context context) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + context.getPackageName()));
        context.startActivity(intent);
    }
}
