package ru.android.hikanumaruapp.ui.reader.viewer.pager

import android.content.Context

interface MangaReader {

    fun applyConfig(vertical: Boolean, reverse: Boolean, sticky: Boolean, showNumbers: Boolean)
    fun scrollToNext(animate: Boolean): Boolean
    fun scrollToPrevious(animate: Boolean): Boolean
    fun getCurrentPosition(): Int
    fun scrollToPosition(position: Int)
    fun setTapNavs(`val`: Boolean)

    fun addOnPageChangedListener(listener: RecyclerViewPager.OnPageChangedListener?)

    fun setOnOverScrollListener(listener: OnOverScrollListener?)

    fun isReversed(): Boolean

    fun getItemCount(): Int

    //fun initAdapter(context: Context?, linkListener: InternalLinkMovement.OnLinkClickListener?)

    //fun getLoader(): PageLoader?

    fun notifyDataSetChanged()

    //fun getItem(position: Int): PageWrapper?

    fun setScaleMode(scaleMode: Int)

    fun reload(position: Int)

    //fun setPages(mangaPages: List<MangaPage?>?)

    fun finish()

    //fun getPages(): List<MangaPage?>?
}