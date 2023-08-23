package ru.android.hikanumaruapp.presentasion.reader.viewer.init

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.View
import android.widget.SeekBar
import android.widget.TextView
import androidx.core.view.children
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.github.chrisbanes.photoview.OnViewTapListener
import com.github.chrisbanes.photoview.PhotoView
import me.everything.android.ui.overscroll.IOverScrollDecor
import me.everything.android.ui.overscroll.IOverScrollState
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper
import ru.android.hikanumaruapp.R
import ru.android.hikanumaruapp.data.model.reader.ReaderChapter
import ru.android.hikanumaruapp.presentasion.reader.MangaConst
import ru.android.hikanumaruapp.presentasion.reader.ReaderFragment
import ru.android.hikanumaruapp.presentasion.reader.ViewTapConst
import ru.android.hikanumaruapp.presentasion.reader.viewer.SimpleSeekBarListener
import ru.android.hikanumaruapp.presentasion.reader.viewer.pager.ReaderPagerConst
import ru.android.hikanumaruapp.presentasion.reader.viewer.pager.ReaderPagerConst.Companion.READ_MODE_BOTTOM
import ru.android.hikanumaruapp.presentasion.reader.viewer.pager.ReaderPagerConst.Companion.READ_MODE_LEFT
import ru.android.hikanumaruapp.presentasion.reader.viewer.pager.ReaderPagerConst.Companion.READ_MODE_RIGHT
import ru.android.hikanumaruapp.presentasion.reader.viewer.pager.ViewPager2ReaderAdapter
import ru.android.hikanumaruapp.utilits.ViewPager2ViewHeightAnimator
import kotlin.math.abs

class BaseViewer private constructor(){

    private lateinit var context: Context

    private lateinit var adapter: ViewPager2ReaderAdapter
    private lateinit var viewer: ViewPager2

    private lateinit var seekBar: SeekBar
    private lateinit var seekBarText: TextView

    private lateinit var mOverScroll: ReaderFragment.mOverScrollBinding

    private var readMode: Int = -1

    private var pagesSize = 0
    private var currentPageSB = 0

    private lateinit var chapter: ReaderChapter

    private var callback: ReaderCallback? = null

    companion object {
        fun builder(context: Context): BaseViewer.Builder {
            return BaseViewer().Builder(context)
        }
    }

    private fun build() {
        adapter = ViewPager2ReaderAdapter(onPageTapListener())
        createViewer()
        initSeekBar()
    }

    fun reversePages(){
        adapter.reversePages()
    }

    private fun onPageTapListener(): OnViewTapListener {
        return OnViewTapListener { view, x, y ->
            try {
                view as PhotoView
                when {
                    x < view.width * ViewTapConst.LEFT_REGION -> {
                        viewer.currentItem = viewer.currentItem - 1

                    }
                    x > view.width * ViewTapConst.RIGHT_REGION -> {
                        viewer.currentItem = viewer.currentItem + 1
                        //readerPager.currentItem = when (!isNavigateRevesed) {
                        //    ReaderPagerConst.IS_REVERSE_TRUE -> readerPager.currentItem + 1
                        //    ReaderPagerConst.IS_REVERSE_FALSE -> readerPager.currentItem - 1
                        //    else -> readerPager.currentItem
                        //}
                    }
                    x > view.width * ViewTapConst.LEFT_REGION &&
                            x < view.width * ViewTapConst.RIGHT_REGION -> {
                        callback!!.onToggleMenu()
                    }
                    else -> {
                        //Todo: Error
                    }
                }
            }catch (e:Exception){
                Log.d("ListT2d2sdfghjk", "Load in bind holder history - ${e}")
            }
        }
    }

    private fun viewPager2SetRegisterOnPageChangeCallback(viewPager2: ViewPager2) {
        viewPager2.registerOnPageChangeCallback(
            object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    currentProgressSeekBar()
                    val page = adapter.dataSetPage.getOrNull(position)
                    if (page != null) {
                        adapter.onPageSelected(page, position)
                    }
                }
                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int,
                ) {}
                override fun onPageScrollStateChanged(state: Int) {}
            })
    }

    @SuppressLint("SetTextI18n")
    private fun currentProgressSeekBar() {
        pagesSize = adapter.dataSetPage.size
        currentPageSB = viewer.currentItem+1

        seekBar.max = pagesSize-1
        seekBar.progress = currentPageSB ?: 1
        seekBarText.text = "$currentPageSB/$pagesSize"
    }

    private fun initSeekBar() {
        currentProgressSeekBar()
        seekBar.setOnSeekBarChangeListener(
            object : SimpleSeekBarListener() {
                @SuppressLint("SetTextI18n")
                override fun onProgressChanged(seekBar: SeekBar, value: Int, fromUser: Boolean) {
                    if (viewer != null && fromUser) {
                        seekBarText.text = (1 + value).toString() + "/$pagesSize"
                        viewer.currentItem = value
                    }
                }
            }
        )
    }

    private fun decorSetOverScrollStateListener(decor: IOverScrollDecor?) {
        decor!!.setOverScrollStateListener(object :
            ru.android.hikanumaruapp.presentasion.reader.viewer.oversscrool.IOverScrollStateListener,
            me.everything.android.ui.overscroll.IOverScrollStateListener {

            override fun onOverScrollStateChange(
                decor: ru.android.hikanumaruapp.presentasion.reader.viewer.oversscrool.IOverScrollDecor?,
                oldState: Int,
                newState: Int,
            ) {}

            override fun onOverScrollStateChange(
                decor: IOverScrollDecor?,
                oldState: Int,
                newState: Int,
            ) {
                when (newState) {
                    IOverScrollState.STATE_IDLE -> mOverScroll.ltFrame.visibility = View.GONE
                    IOverScrollState.STATE_DRAG_START_SIDE -> {
                        when (readMode) {
                            READ_MODE_RIGHT -> onFrameOverScrollTransform(
                                ReaderPagerConst.PREV)
                            READ_MODE_LEFT -> onFrameOverScrollTransform(
                                ReaderPagerConst.NEXT)
                            READ_MODE_BOTTOM -> onFrameOverScrollTransform(
                                ReaderPagerConst.PREV)
                        }
                    }
                    IOverScrollState.STATE_DRAG_END_SIDE -> {
                        when (readMode) {
                            READ_MODE_RIGHT -> onFrameOverScrollTransform(
                                ReaderPagerConst.PREV)
                            READ_MODE_LEFT -> onFrameOverScrollTransform(
                                ReaderPagerConst.NEXT)
                            READ_MODE_BOTTOM -> onFrameOverScrollTransform(
                                ReaderPagerConst.PREV)
                        }
                    }
                    IOverScrollState.STATE_BOUNCE_BACK -> if (oldState == IOverScrollState.STATE_DRAG_START_SIDE) {
                        when (readMode) {
                            READ_MODE_RIGHT -> callback!!.preloadPrev()
                            READ_MODE_LEFT ->  callback!!.preloadNext()
                            READ_MODE_BOTTOM ->  callback!!.preloadPrev()
                        }
                        mOverScroll.ltFrame.visibility = View.GONE
                        // Dragging stopped -- view is starting to bounce back from the *left-end* onto natural position.
                    }
                    else {
                        when (readMode) {
                            READ_MODE_RIGHT ->  callback!!.preloadNext()
                            READ_MODE_LEFT ->  callback!!.preloadPrev()
                            READ_MODE_BOTTOM ->  callback!!.preloadNext()
                        }
                        mOverScroll.ltFrame.visibility = View.GONE
                        // i.e. (oldState == STATE_DRAG_END_SIDE)
                        // View is starting to bounce back from the *right-end*.
                    }
                }
            }
        })

        decor.setOverScrollUpdateListener { decorIt, state, offset ->
            var view = decorIt.view;
            // \|/ onOverScrollFlying(0,offset)
            mOverScroll.ltFrame.alpha = abs(offset / 50f)
        }
    }

    private fun onFrameOverScrollTransform(type: Int) {
        mOverScroll.ltFrame.alpha = 0f
        mOverScroll.ltFrame.visibility = View.VISIBLE

        when (type) {
            ReaderPagerConst.PREV -> {
                if (chapter.linkPagePrev == MangaConst.START_CHAPTER) {
                    mOverScroll.ltFrame.visibility = View.GONE
                } else {
                    val tom:String = chapter.pageNextId!!.substringBefore(' ')
                    val num:String = chapter.pageNextId!!.substringAfter(' ')
                    mOverScroll.tvText.text = context.getString(R.string.prev_chapter_reader_trans,
                        tom,num,chapter.pagePrevTitle.toString())
                    mOverScroll.imArrow.rotation = when(readMode){
                        READ_MODE_RIGHT ->  180f
                        READ_MODE_LEFT ->  0f
                        READ_MODE_BOTTOM ->  90f
                        else -> 0f
                    }
                }
            }
            ReaderPagerConst.NEXT -> {
                if (chapter.linkPageNext == MangaConst.FINISH_CHAPTER) {
                    mOverScroll.ltFrame.visibility = View.GONE
                } else {
                    val tom:String = chapter.pageNextId!!.substringBefore(' ')
                    val num:String = chapter.pageNextId!!.substringAfter(' ')
                    mOverScroll.tvText.text = context.getString(R.string.next_chapter_reader_trans,
                        tom,num,chapter.pageNextTitle.toString())
                    mOverScroll.imArrow.rotation = when(readMode){
                        READ_MODE_RIGHT ->  0f
                        READ_MODE_LEFT ->  180f
                        READ_MODE_BOTTOM ->  90f
                        else -> 0f
                    }
                }
            }
        }
    }

    fun setCurrentItem(page: Int) {
        if (page in 0..chapter.pages!!.size)
            viewer.currentItem = page
    }

    fun setPages(chapter: ReaderChapter) {
        this.chapter = chapter
        adapter.setChapters(chapter.pages!!)
        adapter.notifyItemRangeInserted(0, chapter.pages!!.size-1)
        currentProgressSeekBar()
        viewer.currentItem = when (readMode) {
            READ_MODE_RIGHT -> 0
            READ_MODE_LEFT -> chapter.pages!!.lastIndex
            READ_MODE_BOTTOM -> 0
            else -> 0
        }
    }

    private fun createViewer() {
        viewer.apply {
            orientation = ViewPager2.ORIENTATION_HORIZONTAL
            //offscreenPageLimit = 2
            this.adapter = this@BaseViewer.adapter
        }

        viewPager2SetRegisterOnPageChangeCallback(viewer)
        val decor : IOverScrollDecor? = viewer.children.filterIsInstance<RecyclerView>().firstOrNull()?.let {
            OverScrollDecoratorHelper.setUpOverScroll(it,
                OverScrollDecoratorHelper.ORIENTATION_HORIZONTAL)
        }
        decorSetOverScrollStateListener(decor)

        val viewPager2ViewHeightAnimator = ViewPager2ViewHeightAnimator()
        viewPager2ViewHeightAnimator.viewPager2 = viewer
    }

    inner class Builder internal constructor(context: Context) {
        init {
            this@BaseViewer.context = context
        }

        fun setView(view: ViewPager2): Builder {
            this@BaseViewer.viewer = view
            return this
        }

        fun setSeekBarView(view: SeekBar,text: TextView): Builder {
            this@BaseViewer.seekBar = view
            this@BaseViewer.seekBarText = text
            return this
        }

        fun setOverScrollView(mOverScroll: ReaderFragment.mOverScrollBinding): Builder {
            this@BaseViewer.mOverScroll = mOverScroll
            return this
        }

        fun setConfig(readMode: Int): Builder {
            this@BaseViewer.readMode = readMode
            return this
        }

        //fun setChapterData(chapter: ReaderChapter): Builder {
        //    this@BaseViewer.chapter = chapter
        //    return this
        //}

        fun setReaderCallback(callback: ReaderCallback): Builder {
            this@BaseViewer.callback = callback
            return this
        }

        fun build(): BaseViewer {
            this@BaseViewer.build()
            return this@BaseViewer
        }
    }
}


abstract class ReaderCallback {
    //abstract fun OnFinishListener()
    //abstract fun OnStopListener()
    abstract fun onToggleMenu()
    abstract fun preloadPrev()
    abstract fun preloadNext()
}