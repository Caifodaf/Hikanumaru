package ru.android.hikanumaruapp.ui.reader.viewer.pager

interface OnOverScrollListener {

    companion object {
        const val LEFT = 0
        const val RIGHT = 1
        const val TOP = 2
        const val BOTTOM = 3
    }

    fun onOverScrollFlying(direction: Int, distance: Int)
    fun onOverScrollFinished(direction: Int, distance: Int): Boolean
    fun onOverScrollStarted(direction: Int)
    fun onOverScrolled(direction: Int)
}