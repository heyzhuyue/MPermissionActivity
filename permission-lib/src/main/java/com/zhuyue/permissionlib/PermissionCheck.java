package com.zhuyue.permissionlib;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.content.pm.PackageManager;
import android.os.Build;

import com.zhuyue.permissionlib.utils.CheckPermissionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhuyue on 2017/3/8.
 */

public class PermissionCheck {

    private static final String TAG = "PermissionCheck";
    private static final String SUFFIX = "$$PermissionProxy";

    /**
     * 获取运行时权限
     *
     * @param activity    Activity
     * @param requestCode 运行时权限呵验证Code
     * @param permissions 权限集合
     */
    public static void requestPermission(Activity activity, int requestCode, String[] permissions) {
        requestPermissionForApp(activity, requestCode, permissions);
    }

    /**
     * 获取运行时权限
     *
     * @param fragment    Fragment
     * @param requestCode 运行时权限呵验证Code
     * @param permissions 权限集合
     */
    public static void requestPermission(Fragment fragment, int requestCode, String[] permissions) {
        requestPermissionForApp(fragment, requestCode, permissions);
    }

    /**
     * 获取应用相关运行时权限
     *
     * @param object      对象
     * @param requestCode 运行时权限呵验证Code
     * @param permissions 权限集合
     */
    @TargetApi(value = Build.VERSION_CODES.M)
    private static void requestPermissionForApp(Object object, int requestCode, String[] permissions) {
        if (!CheckPermissionUtils.isOverMarshmallow()) {
            requestPermissionSucess(object, requestCode);
        } else {
            List<String> needDenyPermissions = CheckPermissionUtils.findDeniedPermissions(CheckPermissionUtils.getActivity(object), permissions);
            if (needDenyPermissions.size() > 0) {
                if (object instanceof Activity) {
                    ((Activity) object).requestPermissions(needDenyPermissions.toArray(new String[needDenyPermissions.size()]), requestCode);
                } else if (object instanceof Fragment) {
                    ((Fragment) object).requestPermissions(needDenyPermissions.toArray(new String[needDenyPermissions.size()]), requestCode);
                } else {
                    throw new IllegalArgumentException(object.getClass().getName() + " is not supported!");
                }
            } else {
                requestPermissionSucess(object, requestCode);
            }
        }
    }

    /**
     * 获取运行时权限注解式
     *
     * @param activity    Activity
     * @param requestCode 运行时权限呵验证Code
     * @param permissions 权限集合
     */
    public static void requestPermissionByCompiler(Activity activity, int requestCode, String[] permissions) {
        requestPermissionForAppByCompiler(activity, requestCode, permissions);
    }

    /**
     * 获取运行时权限注解式
     *
     * @param fragment    Fragment
     * @param requestCode 运行时权限呵验证Code
     * @param permissions 权限集合
     */
    public static void requestPermissionByCompiler(Fragment fragment, int requestCode, String[] permissions) {
        requestPermissionForAppByCompiler(fragment, requestCode, permissions);
    }

    /**
     * 获取应用相关运行时权限注解式
     *
     * @param object      对象
     * @param requestCode 运行时权限呵验证Code
     * @param permissions 权限集合
     */
    @TargetApi(value = Build.VERSION_CODES.M)
    private static void requestPermissionForAppByCompiler(Object object, int requestCode, String[] permissions) {
        if (!CheckPermissionUtils.isOverMarshmallow()) {
            requestPermissionSucessByCompiler(object, requestCode);
        } else {
            List<String> needDenyPermissions = CheckPermissionUtils.findDeniedPermissions(CheckPermissionUtils.getActivity(object), permissions);
            if (needDenyPermissions.size() > 0) {
                if (object instanceof Activity) {
                    ((Activity) object).requestPermissions(needDenyPermissions.toArray(new String[needDenyPermissions.size()]), requestCode);
                } else if (object instanceof Fragment) {
                    ((Fragment) object).requestPermissions(needDenyPermissions.toArray(new String[needDenyPermissions.size()]), requestCode);
                } else {
                    throw new IllegalArgumentException(object.getClass().getName() + " is not supported!");
                }
            } else {
                requestPermissionSucessByCompiler(object, requestCode);
            }
        }
    }

    /**
     * 处理权限申请回调
     *
     * @param activity     Activity
     * @param requestCode  运行时权限呵验证Code
     * @param permissions  权限集合
     * @param grantResults 权限申请结果集合
     */
    public static void onRequestPermissionsResult(Activity activity, int requestCode, String[] permissions, int[] grantResults) {
        requestResult(activity, requestCode, permissions, grantResults);
    }

    /**
     * 处理权限申请回调
     *
     * @param fragment     Fragment
     * @param requestCode  运行时权限呵验证Code
     * @param permissions  权限集合
     * @param grantResults 权限申请结果集合
     */
    public static void onRequestPermissionsResult(Fragment fragment, int requestCode, String[] permissions, int[] grantResults) {
        requestResult(fragment, requestCode, permissions, grantResults);
    }

    /**
     * 处理权限申请回调注解式
     *
     * @param activity     Activity
     * @param requestCode  运行时权限呵验证Code
     * @param permissions  权限集合
     * @param grantResults 权限申请结果集合
     */
    public static void onRequestPermissionsResultByCompiler(Activity activity, int requestCode, String[] permissions, int[] grantResults) {
        requestResultForCompiler(activity, requestCode, permissions, grantResults);
    }

    /**
     * 处理权限申请回调注解式
     *
     * @param fragment     Fragment
     * @param requestCode  运行时权限呵验证Code
     * @param permissions  权限集合
     * @param grantResults 权限申请结果集合
     */
    public static void onRequestPermissionsResultByCompiler(Fragment fragment, int requestCode, String[] permissions, int[] grantResults) {
        requestResultForCompiler(fragment, requestCode, permissions, grantResults);
    }

    /**
     * 处理权限申请返回结果
     *
     * @param obj          对象
     * @param requestCode  运行时权限呵验证Code
     * @param permissions  权限集合
     * @param grantResults 权限申请结果集合
     */
    private static void requestResult(Object obj, int requestCode, String[] permissions,
                                      int[] grantResults) {
        List<String> deniedPermissions = new ArrayList<>();
        for (int i = 0; i < grantResults.length; i++) {
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                deniedPermissions.add(permissions[i]);
            }
        }
        if (deniedPermissions.size() > 0) {
            requestPermissionFail(obj, requestCode);
        } else {
            requestPermissionSucess(obj, requestCode);
        }
    }

    /**
     * 处理权限申请返回结果注解式
     *
     * @param obj          对象
     * @param requestCode  运行时权限呵验证Code
     * @param permissions  权限集合
     * @param grantResults 权限申请结果集合
     */
    private static void requestResultForCompiler(Object obj, int requestCode, String[] permissions,
                                                 int[] grantResults) {
        List<String> deniedPermissions = new ArrayList<>();
        for (int i = 0; i < grantResults.length; i++) {
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                deniedPermissions.add(permissions[i]);
            }
        }
        if (deniedPermissions.size() > 0) {
            requestPermissionFailByCompiler(obj, requestCode);
        } else {
            requestPermissionSucessByCompiler(obj, requestCode);
        }
    }

    /**
     * 权限获取成功
     *
     * @param activity    对象
     * @param reuqestCode 运行时权限呵验证Code
     */
    private static void requestPermissionSucess(Object activity, int reuqestCode) {
        if (CheckPermissionUtils.getActivity(activity) instanceof PermissionProxy) {
            PermissionProxy permissionProxy = (PermissionProxy) CheckPermissionUtils.getActivity(activity);
            permissionProxy.grant(activity, reuqestCode);
        }
    }

    /**
     * 权限获取成功注解式
     *
     * @param activity    对象
     * @param reuqestCode 运行时权限呵验证Code
     */
    private static void requestPermissionSucessByCompiler(Object activity, int reuqestCode) {
        findPermissionProxy(activity).grant(activity, reuqestCode);
    }

    /**
     * 权限获取失败
     *
     * @param activity    对象
     * @param reuqestCode 运行时权限呵验证Code
     */
    private static void requestPermissionFail(Object activity, int reuqestCode) {
        if (CheckPermissionUtils.getActivity(activity) instanceof PermissionProxy) {
            PermissionProxy permissionProxy = (PermissionProxy) CheckPermissionUtils.getActivity(activity);
            permissionProxy.denied(activity, reuqestCode);
        }
    }

    /**
     * 权限获取失败注解式
     *
     * @param activity    对象
     * @param reuqestCode 运行时权限呵验证Code
     */
    private static void requestPermissionFailByCompiler(Object activity, int reuqestCode) {
        findPermissionProxy(activity).denied(activity, reuqestCode);
    }


    /**
     * 查找实现PermissionProxy接口类
     *
     * @param activity
     * @return
     */
    private static PermissionProxy findPermissionProxy(Object activity) {
        try {
            Class clazz = activity.getClass();
            Class injectorClazz = Class.forName(clazz.getName() + SUFFIX);
            return (PermissionProxy) injectorClazz.newInstance();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        throw new RuntimeException(String.format("can not find %s , something when compiler.", activity.getClass().getSimpleName() + SUFFIX));
    }

}
