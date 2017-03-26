package com.zhuyue.permissionlib;

/**
 * Created by zhy on 16/2/21.
 */
public interface PermissionProxy<T> {
    /**
     * 授予
     *
     * @param source
     * @param requestCode
     */
    void grant(T source, int requestCode);

    /**
     * 拒绝
     *
     * @param source
     * @param requestCode
     */
    void denied(T source, int requestCode);

    /**
     * 阐述授权理由
     *
     * @param source
     * @param requestCode
     */
    void rationale(T source, int requestCode);

    boolean needShowRationale(int requestCode);
}
