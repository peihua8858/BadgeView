package com.fz.badgeview.annotation

import android.view.View
import kotlin.reflect.KClass

/**
 * 徽章视图
 *
 * @author dingpeihua
 * @version 1.0
 * @date 2022/2/16 9:14
 */
@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.CLASS)
annotation class BadgeView(val values: Array<KClass<out View>>)