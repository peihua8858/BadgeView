package com.fz.badgeview.compiler

import com.squareup.javapoet.TypeSpec

/**
 * Created by dingpeihua on 2017/8/1.
 */
interface IBadgeMethod{
    fun constructor(typeBuilder: TypeSpec.Builder, clazz: String)
    /**
     * 显示圆点徽章
     */
    fun showCirclePointBadge(typeBuilder: TypeSpec.Builder)

    /**
     * 显示文字徽章
     *
     * @param typeBuilder
     */
    fun showTextBadge(typeBuilder: TypeSpec.Builder)

    /**
     * 隐藏徽章
     */
    fun hiddenBadge(typeBuilder: TypeSpec.Builder)

    /**
     * 显示图像徽章
     *
     * @param typeBuilder
     */
    fun showDrawableBadge(typeBuilder: TypeSpec.Builder)

    /**
     * 调用父类的onTouchEvent方法
     *
     * @param typeBuilder
     * @return
     */
    fun onTouchEvent(typeBuilder: TypeSpec.Builder)
    /**
     * 调用父类的onTouchEvent方法
     *
     * @param typeBuilder
     * @return
     */
    fun callSuperOnTouchEvent(typeBuilder: TypeSpec.Builder)

    /**
     * 拖动大于BGABadgeViewHelper.mMoveHiddenThreshold后抬起手指徽章消失的代理
     *
     * @param typeBuilder
     */
    fun setDragDismissDelegate(typeBuilder: TypeSpec.Builder)
    fun onDraw(typeBuilder: TypeSpec.Builder, clazz: String)
    fun dispatchDraw(typeBuilder: TypeSpec.Builder)
    fun isDraggable(typeBuilder: TypeSpec.Builder)
    fun isDragging(typeBuilder: TypeSpec.Builder)
    /**
     * 是否显示徽章
     *
     * @return
     */
    fun isShowBadge(typeBuilder: TypeSpec.Builder)
    fun getBadgeViewHelper(typeBuilder: TypeSpec.Builder)
//    fun getGlobalVisibleRect(typeBuilder: TypeSpec.Builder)
    fun setBadgeBgColorInt(typeBuilder: TypeSpec.Builder)
    fun setBadgeTextColorInt(typeBuilder: TypeSpec.Builder)
    fun setBadgeTextSizeSp(typeBuilder: TypeSpec.Builder)
    fun setBadgeVerticalMarginDp(typeBuilder: TypeSpec.Builder)
    fun setBadgeHorizontalMarginDp(typeBuilder: TypeSpec.Builder)
    fun setBadgePaddingDp(typeBuilder: TypeSpec.Builder)
    fun setBadgeGravity(typeBuilder: TypeSpec.Builder)
    fun setDraggable(typeBuilder: TypeSpec.Builder)
}