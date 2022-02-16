package com.fz.badgeview.annotation;

import android.view.View;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 徽章视图
 *
 * @author dingpeihua
 * @version 1.0
 * @date 2022/2/16 9:14
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE)
public @interface BadgeView {
    Class<? extends View>[] values();
}
