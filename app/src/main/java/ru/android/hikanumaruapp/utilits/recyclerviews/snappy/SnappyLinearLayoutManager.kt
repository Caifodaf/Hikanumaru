package ru.android.hikanumaruapp.utilits.recyclerviews.snappy

import android.content.Context
import android.graphics.PointF
import android.hardware.SensorManager
import android.view.View
import android.view.ViewConfiguration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.abs
import kotlin.math.exp
import kotlin.math.ln


class SnappyLinearLayoutManager : LinearLayoutManager, ISnappyLayoutManager {
    private var deceleration = 0.0

    constructor(context: Context) : super(context) {
        calculateDeceleration(context)
    }

    constructor(context: Context, orientation: Int, reverseLayout: Boolean) : super(
        context,
        orientation,
        reverseLayout
    ) {
        calculateDeceleration(context)
    }

    private fun calculateDeceleration(context: Context) {
        deceleration = (SensorManager.GRAVITY_EARTH // g (m/s^2)
                * 39.3700787 // inches per meter
                // pixels per inch. 160 is the "default" dpi, i.e. one dip is one pixel on a 160 dpi
                // screen
                * context.resources.displayMetrics.density * 160.0f * FRICTION)
    }

    override fun getPositionForVelocity(velocityX: Int, velocityY: Int): Int {
        if (childCount == 0) {
            return 0
        }
        return if (orientation == HORIZONTAL) {
            calcPosForVelocity(
                velocityX, getChildAt(0)!!.left, getChildAt(0)!!.width,
                getPosition((getChildAt(0))!!)
            )
        } else {
            calcPosForVelocity(
                velocityY, getChildAt(0)!!.top, getChildAt(0)!!.height,
                getPosition((getChildAt(0))!!)
            )
        }
    }

    private fun calcPosForVelocity(
        velocity: Int,
        scrollPos: Int,
        childSize: Int,
        currPos: Int
    ): Int {
        val dist = getSplineFlingDistance(velocity.toDouble())
        val tempScroll = scrollPos + if (velocity > 0) dist else -dist
        return if (velocity < 0) {
            // Not sure if I need to lower bound this here.
            Math.max(currPos + tempScroll / childSize, 0.0).toInt()
        } else {
            (currPos + (tempScroll / childSize) + 1).toInt()
        }
    }

    override fun smoothScrollToPosition(recyclerView: RecyclerView, state: RecyclerView.State?, position: Int) {
        val linearSmoothScroller: LinearSmoothScroller =
            object : LinearSmoothScroller(recyclerView.context) {
                // I want a behavior where the scrolling always snaps to the beginning of
                // the list. Snapping to end is also trivial given the default implementation.
                // If you need a different behavior, you may need to override more
                // of the LinearSmoothScrolling methods.
                override fun getHorizontalSnapPreference(): Int {
                    return SNAP_TO_START
                }

                override fun getVerticalSnapPreference(): Int {
                    return SNAP_TO_START
                }

                override fun computeScrollVectorForPosition(targetPosition: Int): PointF? {
                    return this@SnappyLinearLayoutManager
                        .computeScrollVectorForPosition(targetPosition)
                }
            }
        linearSmoothScroller.targetPosition = position
        startSmoothScroll(linearSmoothScroller)
    }

    private fun getSplineFlingDistance(velocity: Double): Double {
        val l = getSplineDeceleration(velocity)
        val decelMinusOne = DECELERATION_RATE - 1.0
        return (ViewConfiguration.getScrollFriction() * deceleration
                * exp(DECELERATION_RATE / decelMinusOne * l))
    }

    private fun getSplineDeceleration(velocity: Double): Double {
        return Math.log(
            INFLEXION * abs(velocity)
                    / (ViewConfiguration.getScrollFriction() * deceleration)
        )
    }

    /**
     * This implementation obviously doesn't take into account the direction of the
     * that preceded it, but there is no easy way to get that information without more
     * hacking than I was willing to put into it.
     */
    override fun getFixScrollPos(): Int {
        if (this.childCount == 0) {
            return 0
        }
        val child: View? = getChildAt(0)
        val childPos = child?.let { getPosition(it) }
        if (child != null) {
            if ((orientation == HORIZONTAL
                        && abs(child.left) > child.measuredWidth / 2)
            ) {
                // Scrolled first view more than halfway offscreen
                return childPos!! + 1
            } else if (child != null) {
                if ((orientation == VERTICAL
                            && abs(child.top) > child.measuredWidth / 2)
                ) {
                    // Scrolled first view more than halfway offscreen
                    return childPos!! + 1
                }
            }
        }
        return childPos!!
    }

    companion object {
        // These variables are from android.widget.Scroller, which is used, via ScrollerCompat, by
        // Recycler View. The scrolling distance calculation logic originates from the same place. Want
        // to use their variables so as to approximate the look of normal Android scrolling.
        // Find the Scroller fling implementation in android.widget.Scroller.fling().
        private const val INFLEXION = 0.35f // Tension lines cross at (INFLEXION, 1)
        private val DECELERATION_RATE = (ln(0.78) / ln(0.9)).toFloat()
        private const val FRICTION = 0.84
    }
}