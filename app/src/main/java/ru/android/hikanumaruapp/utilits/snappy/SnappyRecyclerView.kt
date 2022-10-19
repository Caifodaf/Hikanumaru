package ru.android.hikanumaruapp.utilits.snappy

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent

import androidx.recyclerview.widget.RecyclerView


class SnappyRecyclerView : RecyclerView {
    constructor(context: Context?) : super(context!!) {}
    constructor(context: Context?, attrs: AttributeSet?) : super(context!!, attrs) {}
    constructor(context: Context?, attrs: AttributeSet?, defStyle: Int) : super(
        context!!,
        attrs,
        defStyle
    ) {
    }

    override fun fling(velocityX: Int, velocityY: Int): Boolean {
        val lm = layoutManager
        if (lm is ISnappyLayoutManager) {
            super.smoothScrollToPosition(
                (layoutManager as ISnappyLayoutManager?)
                    !!.getPositionForVelocity(velocityX, velocityY)
            )
            return true
        }
        return super.fling(velocityX, velocityY)
    }

    override fun onTouchEvent(e: MotionEvent): Boolean {
        val ret = super.onTouchEvent(e)
        val lm = layoutManager
        if (lm is ISnappyLayoutManager
            && (e.action == MotionEvent.ACTION_UP ||
                    e.action == MotionEvent.ACTION_CANCEL)
            && scrollState == SCROLL_STATE_IDLE
        ) {
            smoothScrollToPosition((lm as ISnappyLayoutManager?)!!.getFixScrollPos())
        }
        return ret
    }
}