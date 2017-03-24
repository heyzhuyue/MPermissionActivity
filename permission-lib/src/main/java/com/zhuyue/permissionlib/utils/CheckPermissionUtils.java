package com.zhuyue.permissionlib.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.content.pm.PackageManager;
import android.os.Build;

import com.zhuyue.annotation.PermissionDenied;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by win7 on 2017/3/8.
 */

public class CheckPermissionUtils {

    /**
     * 判断是否Android M以上
     *
     * @return
     */
    public static boolean isOverMarshmallow() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    /**
     * 获取集中需要申请权限列表
     *
     * @param activity   Activity
     * @param permission 权限集合
     * @return
     */
    @TargetApi(value = Build.VERSION_CODES.M)
    public static List<String> findDeniedPermissions(Activity activity, String... permission) {
        List<String> denyPermissions = new ArrayList<>();
        for (String value : permission) {
            if (activity.checkSelfPermission(value) != PackageManager.PERMISSION_GRANTED) {
                denyPermissions.add(value);
            }
        }
        return denyPermissions;
    }

    /**
     * 查找Annotation注解Methods
     *
     * @param clazz  Class
     * @param clazz1 Annotation Class
     * @return
     */
    public static List<Method> findAnnotationMethods(Class clazz, Class<? extends Annotation> clazz1) {
        List<Method> methods = new ArrayList<>();
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(clazz1)) {
                methods.add(method);
            }
        }
        return methods;
    }

    public static Method findMethodWithRequestCode(Class clazz, Class<? extends Annotation> annotation, int requestCode) {
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(annotation)) {
                if (isEqualRequestCodeFromAnntation(method, annotation, requestCode)) {
                    return method;
                }
            }
        }
        return null;
    }

    private static boolean isEqualRequestCodeFromAnntation(Method method, Class<? extends Annotation> annotation, int requestCode) {

        return false;
    }


    /**
     * 获取Activity
     *
     * @param object 对象
     * @return
     */
    public static Activity getActivity(Object object) {
        if (object instanceof Fragment) {
            return ((Fragment) object).getActivity();
        } else if (object instanceof Activity) {
            return (Activity) object;
        }
        return null;
    }
}
