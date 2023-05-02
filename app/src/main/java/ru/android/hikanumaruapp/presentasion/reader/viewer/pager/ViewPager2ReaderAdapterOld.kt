package ru.android.hikanumaruapp.presentasion.reader.viewer.pager

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.github.chrisbanes.photoview.OnViewTapListener
import com.github.chrisbanes.photoview.PhotoViewAttacher
import ru.android.hikanumaruapp.databinding.ViewPageReaderItemBinding
import ru.android.hikanumaruapp.presentasion.reader.ReaderFragment
import ru.android.hikanumaruapp.data.model.reader.ReaderChapter
import ru.android.hikanumaruapp.data.model.reader.ReaderChapterPage
import ru.android.hikanumaruapp.data.model.reader.TransItem


class ViewPager2ReaderAdapterOld(
    val context: ReaderFragment,
    private val onPageTapListener: OnViewTapListener,
) : RecyclerView.Adapter<ViewPager2ReaderAdapterOld.ViewHolder>() {
    companion object {
        private const val TYPE_CHAPTER = 0
        private const val TYPE_TRANSISON = 4
        const val PREV = 0
        const val NEXT = 1
        const val TRANSLATE = 3
        const val FINISH = 4
    }

    var dataSetPage = mutableListOf<Any>()
    var lastPage: Any? = ""
    var transitState = mutableListOf<TransItem>()

    //    private var provider: Provider = Provider()
    private var isPreloadError: Boolean = false
    private var isLoad: Boolean = false
    private var isLoadNext: Boolean = false
    private var isFirstChapter: Boolean = false

    var idCurr = mutableListOf<ReaderChapter>()
    private var idPreloadData = mutableListOf<ReaderChapterPage>()

    /*
        id - 124
        "non chapter"
        "non continuations"
        "final"
     */

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ViewPageReaderItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemViewType(position: Int): Int {
        Log.d("ListT2d2", "TYPE_TRANSISON - $position")
        return TYPE_CHAPTER
    }

    override fun getItemCount(): Int {
        Log.d("ListT2d2", "DataSet size - ${dataSetPage.size}")
        return dataSetPage.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(dataSetPage, position)
    }

    fun onPageSelected(page: Any?, position: Int) {
    }

    fun setChapters(curr: MutableList<ReaderChapter>, ) {
        dataSetPage.clear()
        idCurr = curr

        isFirstChapter = idCurr[0].linkPagePrev == "Is start page"

        dataSetPage.addAll(idCurr[0].pages!!)
        Log.d("ListT2d2", "Load in bind holder history - $dataSetPage")
        notifyDataSetChanged()
    }


    inner class ViewHolder(val binding: ViewPageReaderItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("CheckResult")
        fun bind(dataSetChapter: MutableList<Any>, position: Int) {
            Log.d("ListT", "Load in bind holder history - $dataSetChapter")

            val attacher = PhotoViewAttacher(binding.viewPageImage)
            attacher.setOnViewTapListener(onPageTapListener)
            attacher.update()

            imageLoad(dataSetChapter[position])
        }

        private fun imageLoad(any: Any) {
            Glide.with(itemView.context)
                .load(any as String)
                .listener(object : RequestListener<Drawable> {
                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: com.bumptech.glide.request.target.Target<Drawable>?,
                        dataSource: com.bumptech.glide.load.DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        binding.rlLoaderChapterPageItem.visibility = View.GONE
                        return false
                    }

                    override fun onLoadFailed(
                        e: GlideException?, model: Any?, target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        binding.tvTopChapterPageItem.text = "Ошибка загрузки."
                        binding.circleBarReaderPageItem.visibility = View.GONE
                        binding.tvBottomChapterPageItem.visibility = View.VISIBLE
                        onClickErrorBtn(any as String)
                        return false
                    }
                })
                .into(binding.viewPageImage)
        }

        private fun onClickErrorBtn(url: String) {
            binding.tvBottomChapterPageItem.setOnClickListener() {
                binding.tvTopChapterPageItem.text = "Загрузка"
                binding.tvBottomChapterPageItem.visibility = View.GONE
                binding.circleBarReaderPageItem.visibility = View.VISIBLE
                imageLoad(url)
            }
        }

        private fun onReLoad() {

        }
    }
}






