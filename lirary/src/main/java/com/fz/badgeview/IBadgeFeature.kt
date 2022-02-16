package com.fz.badgeview

import android.graphics.Bitmap
import android.graphics.Rect
import android.view.MotionEvent
import androidx.annotation.ColorInt
import androidx.annotation.Px

/**
 * IBadgeable
 * Created by dingpeihua on 2017/8/1.
 */
interface IBadgeFeature : IBadgeView {
    /**
     * 显示圆点徽章
     */
    fun showCirclePointBadge()

    /**
     * 显示文字徽章
     *
     * @param badgeText
     */
    fun showTextBadge(badgeText: String)

    /**
     * 隐藏徽章
     */
    fun hiddenBadge()

    /**
     * 显示图像徽章
     *
     * @param bitmap
     */
    fun showDrawableBadge(bitmap: Bitmap)

    /**
     * 调用父类的onTouchEvent方法
     *
     * @param event
     * @return
     */
    fun callSuperOnTouchEvent(event: MotionEvent): Boolean

    /**
     * 拖动大于BGABadgeViewHelper.mMoveHiddenThreshold后抬起手指徽章消失的代理
     *
     * @param delegate
     */
    fun setDragDismissDelegate(delegate: DragDismissDelegate)

    /**
     * 是否显示徽章
     *
     * @return
     */
    val isShowBadge: Boolean

    /**
     * 是否可拖动
     *
     * @return
     */
    fun isDraggable(): Boolean

    /**
     * 是否正在拖动
     *
     * @return
     */
    fun isDragging(): Boolean

    /**
     * 徽章视图辅助类
     */
    val badgeViewHelper: BadgeViewHelper

    /**
     * 设置徽章背景色
     * @param badgeBgColor
     */
    fun setBadgeBgColorInt(@ColorInt badgeBgColor: Int)

    /**
     * 设置徽章文本字体颜色
     * @param badgeTextColor
     */
    fun setBadgeTextColorInt(@ColorInt badgeTextColor: Int)

    /**
     * 设置徽章文本字体大小
     * @param badgeTextSize
     */
    fun setBadgeTextSizeSp(badgeTextSize: Int)

    /**
     * 设置徽章锤子间距
     * @param badgeVerticalMargin
     */
    fun setBadgeVerticalMarginDp(badgeVerticalMargin: Int)

    /**
     * 设置徽章水平间距
     * @param badgeHorizontalMargin
     */
    fun setBadgeHorizontalMarginDp(badgeHorizontalMargin: Int)

    /**
     * 设置徽章内边距
     * @param badgePadding
     */
    fun setBadgePaddingDp(badgePadding: Int)

    /**
     * 设置徽章位置
     * @param badgeGravity
     */
    fun setBadgeGravity(badgeGravity: BadgeViewHelper.BadgeGravity)

    /**
     * 设置是否可拖动
     * @param draggable
     */
    fun setDraggable(draggable: Boolean)
}