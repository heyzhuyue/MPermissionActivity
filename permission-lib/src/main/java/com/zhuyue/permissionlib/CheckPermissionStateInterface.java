package com.zhuyue.permissionlib;

/**
 * Created by zhuyue on 2017/3/8.
 */

public interface CheckPermissionStateInterface<T> {

    /**
     * 授予权限
     *
     * @param source
     * @param requestCode
     */
    void grant(T source, int requestCode);

    /**
     * 拒绝授予权限
     *
     * @param source
     * @param requestCode
     */
    void denied(T source, int requestCode);

    /**
     * 用户拒绝授权一次,再次获取此权限，向用户解释权限意义
     *
     * @param source
     * @param requestCode
     */
    void rationale(T source, int requestCode);
}
