package ru.android.hikanumaruapp.ui.reader.viewer.pager

import android.annotation.SuppressLint
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.github.chrisbanes.photoview.OnViewTapListener
import ru.android.hikanumaruapp.ui.reader.ReaderFragment
import ru.android.hikanumaruapp.ui.reader.ReaderViewModel
import ru.android.hikanumaruapp.ui.reader.model.ReaderChapter
import ru.android.hikanumaruapp.ui.reader.model.ReaderChapterPage
import ru.android.hikanumaruapp.ui.reader.model.TransItem
import ru.android.hikanumaruapp.ui.reader.viewer.pager.pages.EndTransferPagePagerFragment
import ru.android.hikanumaruapp.ui.reader.viewer.pager.pages.ImagePagePagerFragment
import ru.android.hikanumaruapp.ui.reader.viewer.pager.pages.TransferPagePagerFragment


class ViewPager2ReaderAdapter(
    val context: ReaderFragment,
    val vm: ReaderViewModel,
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    private val onPageTapListener: OnViewTapListener,
) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    var dataSetPage = mutableListOf<Any>()

    override fun getItemCount(): Int = dataSetPage.size

    override fun createFragment(position: Int): Fragment {
        Log.d("ListT2d2", "Load in createFragment - ${dataSetPage[position]}")
        return when (dataSetPage[position]) {
            is ReaderChapterPage ->  ImagePagePagerFragment(vm,
                dataSetPage[position] as ReaderChapterPage,onPageTapListener)
            is TransItem -> {
                when ((dataSetPage[position] as TransItem).type) {
                    -1 ->  //Start
                        EndTransferPagePagerFragment(-1,vm,
                            dataSetPage[position] as TransItem,onPageTapListener)
                    0 ->  //Prev
                        TransferPagePagerFragment(0,vm,
                            dataSetPage[position] as TransItem,onPageTapListener)
                    1 ->  //next
                        TransferPagePagerFragment(1,vm,
                            dataSetPage[position] as TransItem,onPageTapListener)
                    2 ->  //translate
                        EndTransferPagePagerFragment(2,vm,
                            dataSetPage[position] as TransItem,onPageTapListener)
                    3 ->  //Finish
                        EndTransferPagePagerFragment(3,vm,
                            dataSetPage[position] as TransItem,onPageTapListener)
                    else ->
                        EndTransferPagePagerFragment(3,vm,
                        dataSetPage[position] as TransItem,onPageTapListener)
                }
            }
            else -> {
                Log.d("ListT2d2", "Load in else - ${dataSetPage[position]}")
                ImagePagePagerFragment(vm,
                dataSetPage[position] as ReaderChapterPage,onPageTapListener)
            }
        }
    }

    fun onPageSelected(page: Any?, position: Int) {
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setChapters(curr: Any, reload:Boolean = false) {
        if (reload)
            dataSetPage.clear()
        dataSetPage = curr as MutableList<Any>
        Log.d("ListT2d2", "Load in bind holder history - $dataSetPage")
        notifyDataSetChanged()
    }
}






