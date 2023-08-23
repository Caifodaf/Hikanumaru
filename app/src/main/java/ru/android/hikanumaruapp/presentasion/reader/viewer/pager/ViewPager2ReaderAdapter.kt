package ru.android.hikanumaruapp.presentasion.reader.viewer.pager

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.github.chrisbanes.photoview.OnViewTapListener
import ru.android.hikanumaruapp.data.model.reader.ReaderChapterPage
import ru.android.hikanumaruapp.presentasion.reader.ReaderFragment
import ru.android.hikanumaruapp.presentasion.reader.ReaderViewModel
import ru.android.hikanumaruapp.presentasion.reader.viewer.pager.pages.ImagePagePagerFragment


class ViewPager2ReaderAdapterNew(
    val context: ReaderFragment,
    val vm: ReaderViewModel,
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    private val onPageTapListener: OnViewTapListener,
) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    var dataSetPage = mutableListOf<ReaderChapterPage>()

    override fun getItemCount(): Int = dataSetPage.size

//    override fun getItemId(position: Int): Long =
//        dataSetPage[position].numberImageChapter.toLong()
//
//    override fun containsItem(itemId: Long): Boolean {
//        return dataSetPage.contains()
//    }

    override fun createFragment(position: Int): Fragment {
        Log.d("ListT2d2", "Load in createFragment - ${dataSetPage[position]}")
        return when (dataSetPage[position]) {
            is ReaderChapterPage ->  ImagePagePagerFragment(vm,
                dataSetPage[position] as ReaderChapterPage,onPageTapListener)
            //is TransItem -> {
            //    when ((dataSetPage[position] as TransItem).type) {
            //        -1 ->  //Start
            //            EndTransferPagePagerFragment(-1,vm,
            //                dataSetPage[position] as TransItem,onPageTapListener)
            //        0 ->  //Prev
            //            TransferPagePagerFragment(0,vm,
            //                dataSetPage[position] as TransItem,onPageTapListener)
            //        1 ->  //next
            //            TransferPagePagerFragment(1,vm,
            //                dataSetPage[position] as TransItem,onPageTapListener)
            //        2 ->  //translate
            //            EndTransferPagePagerFragment(2,vm,
            //                dataSetPage[position] as TransItem,onPageTapListener)
            //        3 ->  //Finish
            //            EndTransferPagePagerFragment(3,vm,
            //                dataSetPage[position] as TransItem,onPageTapListener)
            //        else ->
            //            EndTransferPagePagerFragment(3,vm,
            //            dataSetPage[position] as TransItem,onPageTapListener)
            //    }
            //}
            else -> {
                ImagePagePagerFragment(vm,
                dataSetPage[position] as ReaderChapterPage,onPageTapListener)
            }
        }
    }

    fun onPageSelected(page: ReaderChapterPage?, position: Int) {
    }

    fun setChapters(curr:  MutableList<ReaderChapterPage>, reload:Boolean = false) {
        //if (reload)
        dataSetPage.clear()
        dataSetPage = curr as MutableList<ReaderChapterPage>
        Log.d("ListT2d2", "Load in bind holder history - $dataSetPage")
        notifyDataSetChanged()
        refreshItems()
    }

    fun reversePages() {
        dataSetPage = dataSetPage.reversed().toMutableList()
        Log.d("ListT2d2", "Load in bind holder history - $dataSetPage")
        notifyDataSetChanged()
        refreshItems()
    }

    private fun refreshItems(){
        for (index in 0 until dataSetPage.size) {
            //fragments[index] = fragment
            notifyItemChanged(index)
        }
    }
}






