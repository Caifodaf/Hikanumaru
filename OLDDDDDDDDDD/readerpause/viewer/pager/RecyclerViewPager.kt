package ru.android.hikanumaruapp.ui.reader.viewer.pager

import android.content.Context
import android.graphics.PointF
import android.os.Build
import android.os.Parcelable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import ru.android.hikanumaruapp.ui.reader.viewer.pager.ViewUtils.getCenterXChildPosition

open class RecyclerViewPager(context: Context, attrs: AttributeSet?, defStyleAttr: Int):
    RecyclerView(context, attrs, defStyleAttr) {

    private var mViewPagerAdapter: Adapter<*>? = null
    private var mTriggerOffset = 0.25f
    private var mFlingFactor = 0.15f
    private var mTouchSpan = 0f
    private var mOnPageChangedListeners: List<OnPageChangedListener>? = null
    private var mSmoothScrollTargetPosition = -1
    private var mPositionBeforeScroll = -1

    private var mSinglePageFling = false
    private var mSticky = true

    var mNeedAdjust = false
    var mFisrtLeftWhenDragging = 0
    var mFirstTopWhenDragging = 0
    var mCurView: View? = null
    var mMaxLeftWhenDragging = Int.MIN_VALUE
    var mMinLeftWhenDragging = Int.MAX_VALUE
    var mMaxTopWhenDragging = Int.MIN_VALUE
    var mMinTopWhenDragging = Int.MAX_VALUE
    private var mPositionOnTouchDown = -1
    private var mHasCalledOnPageChanged = true
    private var reverseLayout = false

    //fun RecyclerViewPager(context: Context?) {
    //    this(context, null)
    //}
//
    //fun RecyclerViewPager(context: Context?, attrs: AttributeSet?) {
    //    this(context, attrs, 0)
    //}
//
    //fun RecyclerViewPager(context: Context?, attrs: AttributeSet?, defStyle: Int) {
    //    super(context, attrs, defStyle)
    //    //initAttrs(context, attrs, defStyle);
    //    isNestedScrollingEnabled = false
    //}

    open fun setFlingFactor(flingFactor: Float) {
        mFlingFactor = flingFactor
    }

    open fun getFlingFactor(): Float {
        return mFlingFactor
    }

    open fun setTriggerOffset(triggerOffset: Float) {
        mTriggerOffset = triggerOffset
    }

    open fun getTriggerOffset(): Float {
        return mTriggerOffset
    }

    open fun setSinglePageFling(singlePageFling: Boolean) {
        mSinglePageFling = singlePageFling
    }

    open fun isSinglePageFling(): Boolean {
        return mSinglePageFling
    }

    open fun setSticky(sticky: Boolean) {
        mSticky = sticky
    }

    open fun isSticky(): Boolean {
        return mSticky
    }

    override fun onRestoreInstanceState(state: Parcelable) {
        try {
            val fLayoutState = state.javaClass.getDeclaredField("mLayoutState")
            fLayoutState.isAccessible = true
            val layoutState = fLayoutState[state]
            val fAnchorOffset = layoutState.javaClass.getDeclaredField("mAnchorOffset")
            val fAnchorPosition = layoutState.javaClass.getDeclaredField("mAnchorPosition")
            fAnchorPosition.isAccessible = true
            fAnchorOffset.isAccessible = true
            if (fAnchorOffset.getInt(layoutState) > 0) {
                fAnchorPosition[layoutState] = fAnchorPosition.getInt(layoutState) - 1
            } else if (fAnchorOffset.getInt(layoutState) < 0) {
                fAnchorPosition[layoutState] = fAnchorPosition.getInt(layoutState) + 1
            }
            fAnchorOffset.setInt(layoutState, 0)
        } catch (e: Throwable) {
            e.printStackTrace()
        }
        super.onRestoreInstanceState(state)
    }

    override fun setAdapter(adapter: Adapter<*>?) {
        mViewPagerAdapter = adapter
        super.setAdapter(mViewPagerAdapter)
    }

    override fun swapAdapter(adapter: Adapter<*>?, removeAndRecycleExistingViews: Boolean) {
        mViewPagerAdapter = adapter
        super.swapAdapter(mViewPagerAdapter, removeAndRecycleExistingViews)
    }

    override fun setLayoutManager(layout: LayoutManager?) {
        super.setLayoutManager(layout)
        if (layout is LinearLayoutManager) {
            reverseLayout = layout.reverseLayout
        }
    }

    fun getCurrentPosition(): Int {
        val lm = layoutManager ?: return NO_POSITION
        var curPosition: Int
        curPosition = if (lm.canScrollHorizontally()) {
            getCenterXChildPosition(this)
        } else {
            ViewUtils.getCenterYChildPosition(this)
        }
        if (curPosition < 0) {
            curPosition = mSmoothScrollTargetPosition
        }
        return curPosition
    }

    override fun fling(velocityX: Int, velocityY: Int): Boolean {
        if (!mSticky) {
            return super.fling(velocityX, velocityY)
        }
        val flinging = super.fling(
            (velocityX * mFlingFactor).toInt(),
            (velocityY * mFlingFactor).toInt()
        )
        if (flinging) {
            if (layoutManager!!.canScrollHorizontally()) {
                adjustPositionX(velocityX)
            } else {
                adjustPositionY(velocityY)
            }
        }
        return flinging
    }

    override fun scrollToPosition(position: Int) {
        mPositionBeforeScroll = getCurrentPosition()
        mSmoothScrollTargetPosition = position
        super.scrollToPosition(position)
        viewTreeObserver.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                if (Build.VERSION.SDK_INT < 16) {
                    viewTreeObserver.removeGlobalOnLayoutListener(this)
                } else {
                    viewTreeObserver.removeOnGlobalLayoutListener(this)
                }
                if (mSmoothScrollTargetPosition >= 0 && mSmoothScrollTargetPosition < getItemCount()) {
                    if (mOnPageChangedListeners != null) {
                        for (onPageChangedListener in mOnPageChangedListeners!!) {
                            onPageChangedListener.OnPageChanged(
                                mPositionBeforeScroll,
                                getCurrentPosition()
                            )
                        }
                    }
                }
            }
        })
    }

    override fun smoothScrollToPosition(position: Int) {
        mPositionBeforeScroll = getCurrentPosition()
        mSmoothScrollTargetPosition = position
        if (layoutManager != null && layoutManager is LinearLayoutManager) {
            // exclude item decoration
            val linearSmoothScroller: LinearSmoothScroller =
                object : LinearSmoothScroller(context) {
                    override fun computeScrollVectorForPosition(targetPosition: Int): PointF? {
                        return if (layoutManager == null) {
                            null
                        } else (layoutManager as LinearLayoutManager?)
                            ?.computeScrollVectorForPosition(targetPosition)
                    }

                    override fun onTargetFound(targetView: View, state: State, action: Action) {
                        if (layoutManager == null) {
                            return
                        }
                        var dx = calculateDxToMakeVisible(
                            targetView,
                            horizontalSnapPreference
                        )
                        var dy = calculateDyToMakeVisible(
                            targetView,
                            verticalSnapPreference
                        )
                        dx = if (dx > 0) {
                            dx - layoutManager!!
                                .getLeftDecorationWidth(targetView)
                        } else {
                            dx + layoutManager!!
                                .getRightDecorationWidth(targetView)
                        }
                        dy = if (dy > 0) {
                            dy - layoutManager!!
                                .getTopDecorationHeight(targetView)
                        } else {
                            dy + layoutManager!!
                                .getBottomDecorationHeight(targetView)
                        }
                        val distance = Math.sqrt((dx * dx + dy * dy).toDouble()).toInt()
                        val time = calculateTimeForDeceleration(distance)
                        if (time > 0) {
                            action.update(-dx, -dy, time, mDecelerateInterpolator)
                        }
                    }
                }
            linearSmoothScroller.targetPosition = position
            if (position == NO_POSITION) {
                return
            }
            layoutManager!!.startSmoothScroll(linearSmoothScroller)
        } else {
            super.smoothScrollToPosition(position)
        }
    }

    private fun adjustPositionX(velocityX: Int) {
        var velocityX = velocityX
        if (reverseLayout) velocityX *= -1
        val childCount = childCount
        if (childCount > 0) {
            val curPosition: Int = getCenterXChildPosition(this)
            val childWidth = width - paddingLeft - paddingRight
            var flingCount: Int = getFlingCount(velocityX, childWidth)
            var targetPosition = curPosition + flingCount
            if (mSinglePageFling) {
                flingCount = Math.max(-1, Math.min(1, flingCount))
                targetPosition =
                    if (flingCount == 0) curPosition else mPositionOnTouchDown + flingCount
            }
            targetPosition = Math.max(targetPosition, 0)
            targetPosition = Math.min(targetPosition, getItemCount() - 1)
            if (targetPosition == curPosition
                && (!mSinglePageFling || mPositionOnTouchDown == curPosition)
            ) {
                val centerXChild: View = ViewUtils.getCenterXChild(this)
                if (mTouchSpan > centerXChild.width * mTriggerOffset * mTriggerOffset && targetPosition != 0) {
                    if (!reverseLayout) targetPosition-- else targetPosition++
                } else if (mTouchSpan < centerXChild.width * -mTriggerOffset && targetPosition != getItemCount() - 1) {
                    if (!reverseLayout) targetPosition++ else targetPosition--
                }
            }
            smoothScrollToPosition(safeTargetPosition(targetPosition, getItemCount()))
        }
    }

    protected open fun adjustPositionY(velocityY: Int) {
        var velocityY = velocityY
        if (reverseLayout) velocityY *= -1
        val childCount = childCount
        if (childCount > 0) {
            val curPosition = ViewUtils.getCenterYChildPosition(this)
            val childHeight = height - paddingTop - paddingBottom
            var flingCount = getFlingCount(velocityY, childHeight)
            var targetPosition = curPosition + flingCount
            if (mSinglePageFling) {
                flingCount = Math.max(-1, Math.min(1, flingCount))
                targetPosition =
                    if (flingCount == 0) curPosition else mPositionOnTouchDown + flingCount
            }
            targetPosition = Math.max(targetPosition, 0)
            targetPosition = Math.min(targetPosition, getItemCount() - 1)
            if (targetPosition == curPosition
                && (!mSinglePageFling || mPositionOnTouchDown == curPosition)
            ) {
                val centerYChild = ViewUtils.getCenterYChild(this)
                if (centerYChild != null) {
                    if (mTouchSpan > centerYChild.height * mTriggerOffset && targetPosition != 0) {
                        if (!reverseLayout) targetPosition-- else targetPosition++
                    } else if (mTouchSpan < centerYChild.height * -mTriggerOffset && targetPosition != getItemCount() - 1) {
                        if (!reverseLayout) targetPosition++ else targetPosition--
                    }
                }
            }
            smoothScrollToPosition(safeTargetPosition(targetPosition, getItemCount()))
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (ev.action == MotionEvent.ACTION_DOWN && layoutManager != null) {
            mPositionOnTouchDown =
                if (layoutManager!!.canScrollHorizontally()) getCenterXChildPosition(this) else ViewUtils.getCenterYChildPosition(
                    this
                )
        }
        return super.dispatchTouchEvent(ev)
    }

    override fun onTouchEvent(e: MotionEvent): Boolean {
        // recording the max/min value in touch track
        if (e.action == MotionEvent.ACTION_MOVE) {
            if (mCurView != null) {
                mMaxLeftWhenDragging = Math.max(mCurView!!.left, mMaxLeftWhenDragging)
                mMaxTopWhenDragging = Math.max(mCurView!!.top, mMaxTopWhenDragging)
                mMinLeftWhenDragging = Math.min(mCurView!!.left, mMinLeftWhenDragging)
                mMinTopWhenDragging = Math.min(mCurView!!.top, mMinTopWhenDragging)
            }
        }
        return super.onTouchEvent(e)
    }

    private fun getFlingCount(velocity: Int, cellSize: Int): Int {
        if (velocity == 0) {
            return 0
        }
        val sign = if (velocity > 0) 1 else -1
        return (sign * Math.ceil(
            (velocity * sign * mFlingFactor / cellSize
                    - mTriggerOffset).toDouble()
        )).toInt()
    }

    open fun getItemCount(): Int {
        return if (mViewPagerAdapter == null) 0 else mViewPagerAdapter!!.itemCount
    }

    private fun safeTargetPosition(position: Int, count: Int): Int {
        if (position < 0) {
            return 0
        }
        return if (position >= count) {
            count - 1
        } else position
    }

    override fun onScrollStateChanged(state: Int) {
        super.onScrollStateChanged(state)
        if (state == SCROLL_STATE_DRAGGING) {
            mNeedAdjust = true
            mCurView =
                if (layoutManager!!.canScrollHorizontally()) ViewUtils.getCenterXChild(this) else ViewUtils.getCenterYChild(
                    this
                )
            if (mCurView != null) {
                if (mHasCalledOnPageChanged) {
                    // While rvp is scrolling, mPositionBeforeScroll will be previous value.
                    mPositionBeforeScroll = getChildLayoutPosition(mCurView!!)
                    mHasCalledOnPageChanged = false
                }
                mFisrtLeftWhenDragging = mCurView!!.left
                mFirstTopWhenDragging = mCurView!!.top
            } else {
                mPositionBeforeScroll = -1
            }
            mTouchSpan = 0f
        } else if (state == SCROLL_STATE_SETTLING) {
            mNeedAdjust = false
            mTouchSpan = if (mCurView != null) {
                if (layoutManager!!.canScrollHorizontally()) {
                    (mCurView!!.left - mFisrtLeftWhenDragging).toFloat()
                } else {
                    (mCurView!!.top - mFirstTopWhenDragging).toFloat()
                }
            } else {
                0f
            }
            mCurView = null
        } else if (state == SCROLL_STATE_IDLE) {
            if (mNeedAdjust && mSticky) {
                var targetPosition =
                    if (layoutManager!!.canScrollHorizontally()) getCenterXChildPosition(this) else ViewUtils.getCenterYChildPosition(
                        this
                    )
                if (mCurView != null) {
                    targetPosition = getChildAdapterPosition(mCurView!!)
                    if (layoutManager!!.canScrollHorizontally()) {
                        val spanX = mCurView!!.left - mFisrtLeftWhenDragging
                        // if user is tending to cancel paging action, don't perform position changing
                        if (spanX > mCurView!!.width * mTriggerOffset && mCurView!!.left >= mMaxLeftWhenDragging) {
                            if (!reverseLayout) targetPosition-- else targetPosition++
                        } else if (spanX < mCurView!!.width * -mTriggerOffset && mCurView!!.left <= mMinLeftWhenDragging) {
                            if (!reverseLayout) targetPosition++ else targetPosition--
                        }
                    } else {
                        val spanY = mCurView!!.top - mFirstTopWhenDragging
                        if (spanY > mCurView!!.height * mTriggerOffset && mCurView!!.top >= mMaxTopWhenDragging) {
                            if (!reverseLayout) targetPosition-- else targetPosition++
                        } else if (spanY < mCurView!!.height * -mTriggerOffset && mCurView!!.top <= mMinTopWhenDragging) {
                            if (!reverseLayout) targetPosition++ else targetPosition--
                        }
                    }
                }
                smoothScrollToPosition(safeTargetPosition(targetPosition, getItemCount()))
                mCurView = null
            } else if (mSmoothScrollTargetPosition != mPositionBeforeScroll) {
                if (mOnPageChangedListeners != null) {
                    for (onPageChangedListener in mOnPageChangedListeners!!) {
                        onPageChangedListener.OnPageChanged(
                            mPositionBeforeScroll,
                            mSmoothScrollTargetPosition
                        )
                    }
                }
                mHasCalledOnPageChanged = true
                mPositionBeforeScroll = mSmoothScrollTargetPosition
            }
            // resetZoom
            mMaxLeftWhenDragging = Int.MIN_VALUE
            mMinLeftWhenDragging = Int.MAX_VALUE
            mMaxTopWhenDragging = Int.MIN_VALUE
            mMinTopWhenDragging = Int.MAX_VALUE
        }
    }






































    interface OnPageChangedListener {
        fun OnPageChanged(oldPosition: Int, newPosition: Int)
    }
}