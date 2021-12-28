package com.fz.badgeview

/**
 * 拖动大于BadgeViewHelper.mMoveHiddenThreshold后抬起手指徽章消失的代理
 */
interface DragDismissDelegate {
    /**
     * 拖动大于BadgeViewHelper.mMoveHiddenThreshold后抬起手指徽章消失的回调方法
     *
     * @param badgeable
     */
    fun onDismiss(badgeable: IBadgeable)
}