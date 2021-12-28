package com.fz.badgeview

import android.content.Context
import android.graphics.Bitmap
import android.graphics.PointF
import android.graphics.Rect
import android.util.TypedValue
import android.view.View

/**
 */
object BadgeViewUtil {
    @JvmStatic
    fun dp2px(context: Context, dpValue: Float): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dpValue,
            context.resources.displayMetrics
        ).toInt()
    }

    fun sp2px(context: Context, spValue: Float): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP,
            spValue,
            context.resources.displayMetrics
        ).toInt()
    }

    fun getStatusBarHeight(view: View?): Int {
        val rectangle = Rect()
        view?.rootView?.getWindowVisibleDisplayFrame(rectangle)
        return rectangle.top
    }

    fun createBitmapSafely(dragBadgeView: DragBadgeView, rect: Rect, retryCount: Int): Bitmap? {
        return try {
            dragBadgeView.isDrawingCacheEnabled = true
            // 只裁剪徽章区域,不然会很卡
            Bitmap.createBitmap(
                dragBadgeView.drawingCache,
                Math.max(rect.left, 0),
                Math.max(rect.top, 0),
                rect.width(),
                rect.height()
            )
        } catch (e: OutOfMemoryError) {
            if (retryCount > 0) {
                System.gc()
                return createBitmapSafely(dragBadgeView, rect, retryCount - 1)
            }
            null
        }
    }

    fun getDistanceBetween2Points(p0: PointF, p1: PointF): Float {
        return Math.sqrt(
            Math.pow(
                (p0.y - p1.y).toDouble(),
                2.0
            ) + Math.pow((p0.x - p1.x).toDouble(), 2.0)
        ).toFloat()
    }

    fun getMiddlePoint(p1: PointF, p2: PointF): PointF {
        return PointF((p1.x + p2.x) / 2.0f, (p1.y + p2.y) / 2.0f)
    }

    fun getPointByPercent(p1: PointF, p2: PointF, percent: Float): PointF {
        return PointF(evaluate(percent, p1.x, p2.x), evaluate(percent, p1.y, p2.y))
    }

    // 从FloatEvaluator中拷贝过来,这样就不用每次都new FloatEvaluator了
    fun evaluate(fraction: Float, startValue: Number, endValue: Number): Float {
        val startFloat = startValue.toFloat()
        return startFloat + fraction * (endValue.toFloat() - startFloat)
    }

    fun getIntersectionPoints(pMiddle: PointF, radius: Float, lineK: Double?): Array<PointF> {
        val points = arrayListOf<PointF>()
        val radian: Float
        var xOffset = 0f
        var yOffset = 0f
        if (lineK != null) {
            radian = Math.atan(lineK).toFloat()
            xOffset = (Math.sin(radian.toDouble()) * radius).toFloat()
            yOffset = (Math.cos(radian.toDouble()) * radius).toFloat()
        } else {
            xOffset = radius
            yOffset = 0f
        }
        points[0] = PointF(pMiddle.x + xOffset, pMiddle.y - yOffset)
        points[1] = PointF(pMiddle.x - xOffset, pMiddle.y + yOffset)
        return points.toTypedArray()
    }
}