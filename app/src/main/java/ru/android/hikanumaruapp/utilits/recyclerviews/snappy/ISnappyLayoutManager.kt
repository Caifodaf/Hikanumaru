package ru.android.hikanumaruapp.utilits.recyclerviews.snappy

/**
 * An interface that LayoutManagers that should snap to grid should implement.
 */
interface ISnappyLayoutManager {
    /**
     * @param velocityX
     * @param velocityY
     * @return the resultant position from a fling of the given velocity.
     */
    fun getPositionForVelocity(velocityX: Int, velocityY: Int): Int

    /**
     * @return the position this list must scroll to to fix a state where the
     * views are not snapped to grid.
     */
    fun getFixScrollPos(): Int
}