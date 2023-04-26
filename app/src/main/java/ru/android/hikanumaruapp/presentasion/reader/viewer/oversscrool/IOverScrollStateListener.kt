package ru.android.hikanumaruapp.presentasion.reader.viewer.oversscrool

interface IOverScrollStateListener {
    /**
     * The invoked callback.
     *
     * @param decor The associated over-scroll 'decorator'.
     * @param oldState The old over-scroll state; ID's specified by [IOverScrollState], e.g.
     * [IOverScrollState.STATE_IDLE].
     * @param newState The **new** over-scroll state; ID's specified by [IOverScrollState],
     * e.g. [IOverScrollState.STATE_IDLE].
     */
    fun onOverScrollStateChange(decor: IOverScrollDecor?, oldState: Int, newState: Int)
}