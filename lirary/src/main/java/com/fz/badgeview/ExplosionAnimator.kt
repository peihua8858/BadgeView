package com.fz.badgeview

import android.animation.ValueAnimator
import android.graphics.*
import android.view.animation.AccelerateInterpolator
import android.view.animation.Interpolator
import com.fz.badgeview.BadgeViewUtil.dp2px
import java.util.*

/**
 * 描述:参考https://github.com/tyrantgit/ExplosionField改成了只有一个View的情况,只刷新徽章附近的区域
 */
class ExplosionAnimator(dragBadgeView: DragBadgeView, rect: Rect, bitmap: Bitmap) :
    ValueAnimator() {
    private val mParticles: Array<Particle?>
    private val mPaint: Paint
    private val mDragBadgeView: DragBadgeView
    private val mRect: Rect
    private val mInvalidateRect: Rect
    private fun generateParticle(color: Int, random: Random): Particle {
        val particle = Particle()
        particle.color = color
        particle.radius = V
        if (random.nextFloat() < 0.2f) {
            particle.baseRadius = V + (X - V) * random.nextFloat()
        } else {
            particle.baseRadius = W + (V - W) * random.nextFloat()
        }
        val nextFloat = random.nextFloat()
        particle.top = mRect.height() * (0.18f * random.nextFloat() + 0.2f)
        particle.top =
            if (nextFloat < 0.2f) particle.top else particle.top + particle.top * 0.2f * random.nextFloat()
        particle.bottom = mRect.height() * (random.nextFloat() - 0.5f) * 1.8f
        var f =
            if (nextFloat < 0.2f) particle.bottom else if (nextFloat < 0.8f) particle.bottom * 0.6f else particle.bottom * 0.3f
        particle.bottom = f
        particle.mag = 4.0f * particle.top / particle.bottom
        particle.neg = -particle.mag / particle.bottom
        f = mRect.centerX() + Y * (random.nextFloat() - 0.5f)
        particle.baseCx = f + mRect.width() / 2
        particle.cx = particle.baseCx
        f = mRect.centerY() + Y * (random.nextFloat() - 0.5f)
        particle.baseCy = f
        particle.cy = f
        particle.life = END_VALUE / 10 * random.nextFloat()
        particle.overflow = 0.4f * random.nextFloat()
        particle.alpha = 1f
        return particle
    }

    fun draw(canvas: Canvas) {
        if (!isStarted) {
            return
        }
        for (particle in mParticles) {
            particle!!.advance(animatedValue as Float)
            if (particle.alpha > 0f) {
                mPaint.color = particle.color
                mPaint.alpha = (Color.alpha(particle.color) * particle.alpha).toInt()
                canvas.drawCircle(particle.cx, particle.cy, particle.radius, mPaint)
            }
        }
        postInvalidate()
    }

    override fun start() {
        super.start()
        postInvalidate()
    }

    /**
     * 只刷新徽章附近的区域
     */
    private fun postInvalidate() {
        mDragBadgeView.postInvalidate(
            mInvalidateRect.left,
            mInvalidateRect.top,
            mInvalidateRect.right,
            mInvalidateRect.bottom
        )
    }

    private class Particle {
        var alpha = 0f
        var color = 0
        var cx = 0f
        var cy = 0f
        var radius = 0f
        var baseCx = 0f
        var baseCy = 0f
        var baseRadius = 0f
        var top = 0f
        var bottom = 0f
        var mag = 0f
        var neg = 0f
        var life = 0f
        var overflow = 0f
        fun advance(factor: Float) {
            var f = 0f
            var normalization = factor / END_VALUE
            if (normalization < life || normalization > 1f - overflow) {
                alpha = 0f
                return
            }
            normalization = (normalization - life) / (1f - life - overflow)
            val f2 = normalization * END_VALUE
            if (normalization >= 0.7f) {
                f = (normalization - 0.7f) / 0.3f
            }
            alpha = 1f - f
            f = bottom * f2
            cx = baseCx + f
            cy = (baseCy - neg * Math.pow(f.toDouble(), 2.0)).toFloat() - f * mag
            radius = V + (baseRadius - V) * f2
        }
    }

    companion object {
        const val ANIM_DURATION = 300
        private val DEFAULT_INTERPOLATOR: Interpolator = AccelerateInterpolator(0.6f)
        private const val END_VALUE = 1.4f
        private const val REFRESH_RATIO = 3
        private var X: Float = 0.0f
        private var Y: Float = 0.0f
        private var V: Float = 0.0f
        private var W: Float = 0.0f
    }

    init {
        setFloatValues(0.0f, END_VALUE)
        duration = ANIM_DURATION.toLong()
        interpolator = DEFAULT_INTERPOLATOR
        X = dp2px(dragBadgeView.context, 5f).toFloat()
        Y = dp2px(dragBadgeView.context, 20f).toFloat()
        V = dp2px(dragBadgeView.context, 2f).toFloat()
        W = dp2px(dragBadgeView.context, 1f).toFloat()
        mPaint = Paint()
        mDragBadgeView = dragBadgeView
        mRect = rect
        mInvalidateRect = Rect(
            mRect.left - mRect.width() * REFRESH_RATIO,
            mRect.top - mRect.height() * REFRESH_RATIO,
            mRect.right + mRect.width() * REFRESH_RATIO,
            mRect.bottom + mRect.height() * REFRESH_RATIO
        )
        val partLen = 15
        mParticles = arrayOfNulls(partLen * partLen)
        val random = Random(System.currentTimeMillis())
        val w = bitmap.width / (partLen + 2)
        val h = bitmap.height / (partLen + 2)
        for (i in 0 until partLen) {
            for (j in 0 until partLen) {
                mParticles[i * partLen + j] =
                    generateParticle(bitmap.getPixel((j + 1) * w, (i + 1) * h), random)
            }
        }
    }
}