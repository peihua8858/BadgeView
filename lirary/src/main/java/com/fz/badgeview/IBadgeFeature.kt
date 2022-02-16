package com.fz.badgeview

import android.graphics.Bitmap
import android.graphics.Rect
import android.view.MotionEvent

/**
 * IBadgeable
 * Created by dingpeihua on 2017/8/1.
 */
interface IBadgeFeature :IBadgeView{
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
    val badgeViewHelper: BadgeViewHelper

    fun setBadgeBgColorInt(badgeBgColor: Int)
    fun setBadgeTextColorInt(badgeTextColor: Int)
    fun setBadgeTextSizeSp(badgetextSize: Int)
    fun setBadgeVerticalMarginDp(badgeVerticalMargin: Int)
    fun setBadgeHorizontalMarginDp(badgeHorizontalMargin: Int)
    fun setBadgePaddingDp(badgePadding: Int)
    fun setBadgeGravity(badgeGravity: BadgeViewHelper.BadgeGravity)
    fun setDraggable(draggable: Boolean)
}