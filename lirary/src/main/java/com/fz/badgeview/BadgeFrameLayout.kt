package com.fz.badgeview

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.FrameLayout

/**
 */
class BadgeFrameLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(
    context, attrs, defStyleAttr
), IBadgeable {
    private val mBadgeViewHelper: BadgeViewHelper =
        BadgeViewHelper(this, context, attrs, BadgeViewHelper.BadgeGravity.RightCenter)

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return mBadgeViewHelper.onTouchEvent(event)
    }

    override fun callSuperOnTouchEvent(event: MotionEvent): Boolean {
        return super.onTouchEvent(event)
    }

    override fun dispatchDraw(canvas: Canvas) {
        super.dispatchDraw(canvas)
        mBadgeViewHelper.drawBadge(canvas)
    }

    override fun showCirclePointBadge() {
        mBadgeViewHelper.showCirclePointBadge()
    }

    override fun showTextBadge(badgeText: String) {
        mBadgeViewHelper.showTextBadge(badgeText)
    }

    override fun hiddenBadge() {
        mBadgeViewHelper.hiddenBadge()
    }

    override fun showDrawableBadge(bitmap: Bitmap) {
        mBadgeViewHelper.showDrawable(bitmap)
    }

    override fun setDragDismissDelegate(delegate: DragDismissDelegate) {
        mBadgeViewHelper.setDragDismissDelegate(delegate)
    }

    override val isShowBadge: Boolean
        get() = mBadgeViewHelper.isShowBadge
    override val badgeViewHelper: BadgeViewHelper
        get() = mBadgeViewHelper

    override fun setBadgeBgColorInt(badgeBgColor: Int) {
        mBadgeViewHelper.setBadgeBgColorInt(badgeBgColor)
    }

    override fun setBadgeTextColorInt(badgeTextColor: Int) {
        mBadgeViewHelper.setBadgeTextColorInt(badgeTextColor)
    }

    override fun setBadgeTextSizeSp(badgetextSize: Int) {
        mBadgeViewHelper.setBadgeTextSizeSp(badgetextSize)
    }

    override fun setBadgeVerticalMarginDp(badgeVerticalMargin: Int) {
        mBadgeViewHelper.setBadgeVerticalMarginDp(badgeVerticalMargin)
    }

    override fun setBadgeHorizontalMarginDp(badgeHorizontalMargin: Int) {
        mBadgeViewHelper.setBadgeHorizontalMarginDp(badgeHorizontalMargin)
    }

    override fun setBadgePaddingDp(badgePadding: Int) {
        mBadgeViewHelper.setBadgePaddingDp(badgePadding)
    }

    override fun setBadgeGravity(badgeGravity: BadgeViewHelper.BadgeGravity) {
        mBadgeViewHelper.setBadgeGravity(badgeGravity)
    }

    override fun setDragable(dragable: Boolean) {
        mBadgeViewHelper.setDragable(dragable)
    }

}