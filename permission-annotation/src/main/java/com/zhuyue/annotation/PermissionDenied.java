package com.zhuyue.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Created by zhuyue on 17/03/14.
 * 权限申请拒绝
 */
@Target(ElementType.METHOD)
public @interface PermissionDenied {
    int value();
}
