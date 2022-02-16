package com.fz.badgeview

import android.content.Context
import android.content.res.TypedArray
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class BadgeViewHelper(
    private val mBadgeFeature: IBadgeFeature,
    context: Context,
    attrs: AttributeSet?,
    defaultBadgeGravity: BadgeGravity
) {
    var bitmap: Bitmap? = null
        private set
    private var mBadgePaint: Paint

    /**
     * 徽章背景色
     */
    var badgeBgColor = 0
        private set

    /**
     * 徽章文本的颜色
     */
    var badgeTextColor = 0
        private set

    /**
     * 徽章文本字体大小
     */
    var badgeTextSize = 0
        private set

    /**
     * 徽章背景与宿主控件上下边缘间距离
     */
    private var mBadgeVerticalMargin = 0

    /**
     * 徽章背景与宿主控件左右边缘间距离
     */
    private var mBadgeHorizontalMargin = 0

    /***
     * 徽章文本边缘与徽章背景边缘间的距离
     */
    var badgePadding = 0
        private set

    /**
     * 徽章文本
     */
    var badgeText: String? = null
        private set

    /**
     * 徽章文本所占区域大小
     */
    private var mBadgeNumberRect: Rect = Rect()

    /**
     * 是否显示Badge
     */
    var isShowBadge = false
        private set

    /**
     * 徽章在宿主控件中的位置
     */
    private var mBadgeGravity: BadgeGravity

    /**
     * 整个徽章所占区域
     */
    var badgeRectF: RectF = RectF()
        private set

    /**
     * 是否可拖动
     */
    private var mDraggable = false

    /**
     * 拖拽徽章超出轨迹范围后，再次放回到轨迹范围时，是否恢复轨迹
     */
    private var mIsResumeTravel = false

    /***
     * 徽章描边宽度
     */
    private var mBadgeBorderWidth = 0

    /***
     * 徽章描边颜色
     */
    private var mBadgeBorderColor = 0

    /**
     * 触发开始拖拽徽章事件的扩展触摸距离
     */
    private var mDragExtra = 0

    /**
     * 整个徽章加上其触发开始拖拽区域所占区域
     */
    private var mBadgeDragExtraRectF: RectF = RectF()

    /**
     * 拖动时的徽章控件
     */
    private val mDropBadgeView: DragBadgeView

    /**
     * 是否正在拖动
     */
    var isDragging = false
        private set

    /**
     * 拖动大于BGABadgeViewHelper.mMoveHiddenThreshold后抬起手指徽章消失的代理
     */
    private var mDelegate: DragDismissDelegate? = null
    var isShowDrawable = false
        private set

    private fun initDefaultAttrs(context: Context) {
        badgeBgColor = Color.RED
        badgeTextColor = Color.WHITE
        badgeTextSize = BadgeViewUtil.sp2px(context, 10f)
        mBadgePaint = Paint()
        mBadgePaint.isAntiAlias = true
        mBadgePaint.style = Paint.Style.FILL
        // 设置mBadgeText居中，保证mBadgeText长度为1时，文本也能居中
        mBadgePaint.textAlign = Paint.Align.CENTER
        badgePadding = BadgeViewUtil.dp2px(context, 4f)
        mBadgeVerticalMargin = BadgeViewUtil.dp2px(context, 4f)
        mBadgeHorizontalMargin = BadgeViewUtil.dp2px(context, 4f)
        isShowBadge = false
        badgeText = null
        bitmap = null
        isDragging = false
        mDraggable = false
        mBadgeBorderColor = Color.WHITE
        mDragExtra = BadgeViewUtil.dp2px(context, 4f)
    }

    private fun initCustomAttrs(context: Context, attrs: AttributeSet?) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.BadgeView)
        val N = typedArray.indexCount
        for (i in 0 until N) {
            initCustomAttr(typedArray.getIndex(i), typedArray)
        }
        typedArray.recycle()
    }

    private fun initCustomAttr(attr: Int, typedArray: TypedArray) {
        when (attr) {
            R.styleable.BadgeView_badge_bgColor -> {
                badgeBgColor = typedArray.getColor(attr, badgeBgColor)
            }
            R.styleable.BadgeView_badge_textColor -> {
                badgeTextColor = typedArray.getColor(attr, badgeTextColor)
            }
            R.styleable.BadgeView_badge_textSize -> {
                badgeTextSize = typedArray.getDimensionPixelSize(attr, badgeTextSize)
            }
            R.styleable.BadgeView_badge_verticalMargin -> {
                mBadgeVerticalMargin = typedArray.getDimensionPixelSize(attr, mBadgeVerticalMargin)
            }
            R.styleable.BadgeView_badge_horizontalMargin -> {
                mBadgeHorizontalMargin =
                    typedArray.getDimensionPixelSize(attr, mBadgeHorizontalMargin)
            }
            R.styleable.BadgeView_badge_padding -> {
                badgePadding = typedArray.getDimensionPixelSize(attr, badgePadding)
            }
            R.styleable.BadgeView_badge_gravity -> {
                val ordinal = typedArray.getInt(attr, mBadgeGravity.ordinal)
                mBadgeGravity = BadgeGravity.values()[ordinal]
            }
            R.styleable.BadgeView_badge_draggable -> {
                mDraggable = typedArray.getBoolean(attr, mDraggable)
            }
            R.styleable.BadgeView_badge_isResumeTravel -> {
                mIsResumeTravel = typedArray.getBoolean(attr, mIsResumeTravel)
            }
            R.styleable.BadgeView_badge_borderWidth -> {
                mBadgeBorderWidth = typedArray.getDimensionPixelSize(attr, mBadgeBorderWidth)
            }
            R.styleable.BadgeView_badge_borderColor -> {
                mBadgeBorderColor = typedArray.getColor(attr, mBadgeBorderColor)
            }
            R.styleable.BadgeView_badge_dragExtra -> {
                mDragExtra = typedArray.getDimensionPixelSize(attr, mDragExtra)
            }
            R.styleable.BadgeView_badge_text -> {
                badgeText = typedArray.getString(attr)
            }
        }
    }

    private fun afterInitDefaultAndCustomAttrs() {
        mBadgePaint.textSize = badgeTextSize.toFloat()
    }

    fun setBadgeBgColorInt(badgeBgColor: Int) {
        this.badgeBgColor = badgeBgColor
        mBadgeFeature.postInvalidate()
    }

    fun setBadgeTextColorInt(badgeTextColor: Int) {
        this.badgeTextColor = badgeTextColor
        mBadgeFeature.postInvalidate()
    }

    fun setBadgeTextSizeSp(badgetextSize: Int) {
        if (badgetextSize >= 0) {
            badgeTextSize = BadgeViewUtil.sp2px(mBadgeFeature.context, badgetextSize.toFloat())
            mBadgePaint.textSize = badgeTextSize.toFloat()
            mBadgeFeature.postInvalidate()
        }
    }

    fun setBadgeVerticalMarginDp(badgeVerticalMargin: Int) {
        if (badgeVerticalMargin >= 0) {
            mBadgeVerticalMargin =
                BadgeViewUtil.dp2px(mBadgeFeature.context, badgeVerticalMargin.toFloat())
            mBadgeFeature.postInvalidate()
        }
    }

    fun setBadgeHorizontalMarginDp(badgeHorizontalMargin: Int) {
        if (badgeHorizontalMargin >= 0) {
            mBadgeHorizontalMargin =
                BadgeViewUtil.dp2px(mBadgeFeature.context, badgeHorizontalMargin.toFloat())
            mBadgeFeature.postInvalidate()
        }
    }

    fun setBadgePaddingDp(badgePadding: Int) {
        if (badgePadding >= 0) {
            this.badgePadding = BadgeViewUtil.dp2px(mBadgeFeature.context, badgePadding.toFloat())
            mBadgeFeature.postInvalidate()
        }
    }

    fun setBadgeGravity(badgeGravity: BadgeGravity?) {
        if (badgeGravity != null) {
            mBadgeGravity = badgeGravity
            mBadgeFeature.postInvalidate()
        }
    }

    fun setBadgeBorderWidthDp(badgeBorderWidthDp: Int) {
        if (badgeBorderWidthDp >= 0) {
            mBadgeBorderWidth =
                BadgeViewUtil.dp2px(mBadgeFeature.context, badgeBorderWidthDp.toFloat())
            mBadgeFeature.postInvalidate()
        }
    }

    fun setBadgeBorderColorInt(badgeBorderColor: Int) {
        mBadgeBorderColor = badgeBorderColor
        mBadgeFeature.postInvalidate()
    }

    fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                mBadgeDragExtraRectF.left = badgeRectF.left - mDragExtra
                mBadgeDragExtraRectF.top = badgeRectF.top - mDragExtra
                mBadgeDragExtraRectF.right = badgeRectF.right + mDragExtra
                mBadgeDragExtraRectF.bottom = badgeRectF.bottom + mDragExtra
                if ((mBadgeBorderWidth == 0 || isShowDrawable) && mDraggable && isShowBadge && mBadgeDragExtraRectF.contains(
                        event.x,
                        event.y
                    )
                ) {
                    isDragging = true
                    mBadgeFeature.parent.requestDisallowInterceptTouchEvent(true)
                    val badgeableRect = Rect()
                    mBadgeFeature.getGlobalVisibleRect(badgeableRect)
                    mDropBadgeView.setStickCenter(
                        badgeableRect.left + badgeRectF.left + badgeRectF.width() / 2,
                        badgeableRect.top + badgeRectF.top + badgeRectF.height() / 2
                    )
                    mDropBadgeView.onTouchEvent(event)
                    mBadgeFeature.postInvalidate()
                    return true
                }
            }
            MotionEvent.ACTION_MOVE -> if (isDragging) {
                mDropBadgeView.onTouchEvent(event)
                return true
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> if (isDragging) {
                mDropBadgeView.onTouchEvent(event)
                isDragging = false
                return true
            }
            else -> {
            }
        }
        return mBadgeFeature.callSuperOnTouchEvent(event)
    }

    fun endDragWithDismiss() {
        hiddenBadge()
        mDelegate?.onDismiss(mBadgeFeature)
    }

    fun endDragWithoutDismiss() {
        mBadgeFeature.postInvalidate()
    }

    fun drawBadge(canvas: Canvas) {
        if (isShowBadge && !isDragging) {
            if (isShowDrawable) {
                drawDrawableBadge(canvas)
            } else {
                drawTextBadge(canvas)
            }
        }
    }

    /**
     * 绘制图像徽章
     *
     * @param canvas
     */
    private fun drawDrawableBadge(canvas: Canvas) {
        val bitmap = this.bitmap ?: return
        badgeRectF.left = (mBadgeFeature.width - mBadgeHorizontalMargin - bitmap.width).toFloat()
        badgeRectF.top = mBadgeVerticalMargin.toFloat()
        when (mBadgeGravity) {
            BadgeGravity.RightTop -> badgeRectF.top = mBadgeVerticalMargin.toFloat()
            BadgeGravity.RightCenter -> badgeRectF.top =
                ((mBadgeFeature.height - bitmap.height) / 2).toFloat()
            BadgeGravity.RightBottom -> badgeRectF.top =
                (mBadgeFeature.height - bitmap.height - mBadgeVerticalMargin).toFloat()
            BadgeGravity.LeftTop -> {
                badgeRectF.left = mBadgeVerticalMargin.toFloat()
                badgeRectF.top = mBadgeHorizontalMargin.toFloat()
                badgeRectF.right = bitmap.width.toFloat()
                badgeRectF.bottom = badgeRectF.top + bitmap.height
            }
            else -> {
            }
        }
        canvas.drawBitmap(bitmap, badgeRectF.left, badgeRectF.top, mBadgePaint)
        badgeRectF.right = badgeRectF.left + bitmap.width
        badgeRectF.bottom = badgeRectF.top + bitmap.height
    }

    /**
     * 绘制文字徽章
     *
     * @param canvas
     */
    private fun drawTextBadge(canvas: Canvas) {
        val badgeText = this.badgeText ?: ""
        // 获取文本宽所占宽高
        mBadgePaint.getTextBounds(badgeText, 0, badgeText.length, mBadgeNumberRect)
        // 计算徽章背景的宽高
        val badgeHeight = mBadgeNumberRect.height() + badgePadding * 2
        // 当mBadgeText的长度为1或0时，计算出来的高度会比宽度大，此时设置宽度等于高度
        val badgeWidth: Int = if (badgeText.length == 1 || badgeText.isEmpty()) {
            badgeHeight
        } else {
            mBadgeNumberRect.width() + badgePadding * 2
        }

        // 计算徽章背景上下的值
        badgeRectF.top = mBadgeVerticalMargin.toFloat()
        badgeRectF.bottom = (mBadgeFeature.height - mBadgeVerticalMargin).toFloat()
        badgeRectF.right = (mBadgeFeature.width - mBadgeHorizontalMargin).toFloat()
        badgeRectF.left = badgeRectF.right - badgeWidth
        when (mBadgeGravity) {
            BadgeGravity.RightTop -> badgeRectF.bottom = badgeRectF.top + badgeHeight
            BadgeGravity.RightCenter -> {
                badgeRectF.top = (mBadgeFeature.height - badgeHeight) / 2f
                badgeRectF.bottom = badgeRectF.top + badgeHeight
            }
            BadgeGravity.RightBottom -> badgeRectF.top = badgeRectF.bottom - badgeHeight
            BadgeGravity.LeftTop -> {
                badgeRectF.left = mBadgeVerticalMargin.toFloat()
                badgeRectF.top = mBadgeHorizontalMargin.toFloat()
                badgeRectF.right = badgeRectF.left + badgeWidth
                badgeRectF.bottom = badgeRectF.top + badgeHeight
            }
            else -> {
            }
        }

        // 计算徽章背景左右的值
        badgeRectF.right = (mBadgeFeature.width - mBadgeHorizontalMargin).toFloat()
        badgeRectF.left = badgeRectF.right - badgeWidth
        if (mBadgeBorderWidth > 0) {
            // 设置徽章边框景色
            mBadgePaint.color = mBadgeBorderColor
            // 绘制徽章边框背景
            canvas.drawRoundRect(badgeRectF, badgeHeight / 2f, badgeHeight / 2f, mBadgePaint)

            // 设置徽章背景色
            mBadgePaint.color = badgeBgColor
            // 绘制徽章背景
            canvas.drawRoundRect(
                RectF(
                    badgeRectF.left + mBadgeBorderWidth,
                    badgeRectF.top + mBadgeBorderWidth,
                    badgeRectF.right - mBadgeBorderWidth,
                    badgeRectF.bottom - mBadgeBorderWidth
                ),
                ((badgeHeight - 2 * mBadgeBorderWidth) / 2).toFloat(),
                ((badgeHeight - 2 * mBadgeBorderWidth) / 2).toFloat(),
                mBadgePaint
            )
        } else {
            // 设置徽章背景色
            mBadgePaint.color = badgeBgColor
            // 绘制徽章背景
            canvas.drawRoundRect(badgeRectF, badgeHeight / 2f, badgeHeight / 2f, mBadgePaint)
        }
        if (!this.badgeText.isNullOrEmpty()) {
            // 设置徽章文本颜色
            mBadgePaint.color = badgeTextColor
            // initDefaultAttrs方法中设置了mBadgeText居中，此处的x为徽章背景的中心点y
            val x = badgeRectF.left + badgeWidth / 2f
            // 注意：绘制文本时的y是指文本底部，而不是文本的中间
            val y = badgeRectF.bottom - badgePadding
            // 绘制徽章文本
            canvas.drawText(badgeText, x, y, mBadgePaint)
        }
    }

    fun showCirclePointBadge() {
        showTextBadge(null)
    }

    fun showTextBadge(badgeText: String?) {
        isShowDrawable = false
        this.badgeText = badgeText
        isShowBadge = true
        mBadgeFeature.postInvalidate()
    }

    fun hiddenBadge() {
        isShowBadge = false
        mBadgeFeature.postInvalidate()
    }

    fun showDrawable(bitmap: Bitmap) {
        this.bitmap = bitmap
        isShowDrawable = true
        isShowBadge = true
        mBadgeFeature.postInvalidate()
    }

    fun setDragDismissDelegate(delegate: DragDismissDelegate?) {
        mDelegate = delegate
    }

    val rootView: View?
        get() = mBadgeFeature.rootView
    var isResumeTravel: Boolean
        get() = mIsResumeTravel
        set(isResumeTravel) {
            mIsResumeTravel = isResumeTravel
            mBadgeFeature.postInvalidate()
        }
    var isDraggable: Boolean
        get() = mDraggable
        set(draggable) {
            mDraggable = draggable
            mBadgeFeature.postInvalidate()
        }

    enum class BadgeGravity {
        RightTop, RightCenter, RightBottom, LeftTop
    }

    init {
        badgeBgColor = Color.RED
        badgeTextColor = Color.WHITE
        badgeTextSize = BadgeViewUtil.sp2px(context, 10f)
        mBadgePaint = Paint()
        mBadgeGravity = defaultBadgeGravity
        initDefaultAttrs(context)
        initCustomAttrs(context, attrs)
        afterInitDefaultAndCustomAttrs()
        mDropBadgeView = DragBadgeView(context, this)
    }
}