package ru.android.hikanumaruapp.presentasion.reader.viewer.pager

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.ImageLoader
import coil.load
import coil.request.ImageRequest
import com.github.chrisbanes.photoview.OnViewTapListener
import com.github.chrisbanes.photoview.PhotoViewAttacher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.android.hikanumaruapp.databinding.ViewPageReaderItemBinding
import ru.android.hikanumaruapp.data.model.reader.ReaderChapterPage


class ViewPager2ReaderAdapter(
    private val onPageTapListener: OnViewTapListener,
) : RecyclerView.Adapter<ViewPager2ReaderAdapter.ViewHolderPager>(){

    companion object {
        private const val TYPE_CHAPTER = 0
        private const val TYPE_TRANSISON = 4
        const val PREV = 0
        const val NEXT = 1
        const val TRANSLATE = 3
        const val FINISH = 4
    }

    var dataSetPage = mutableListOf<ReaderChapterPage>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderPager{
        val binding = ViewPageReaderItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolderPager(binding)
    }

    override fun getItemViewType(position: Int): Int {
        return TYPE_CHAPTER
    }

    override fun getItemCount(): Int = if(dataSetPage.size == 0) 1 else dataSetPage.size
    //override fun getItemCount() = dataSetPage.size

    override fun onBindViewHolder(holder: ViewHolderPager, position: Int) {
        if (dataSetPage.size != 0) {
            holder.bind(dataSetPage[position])
        }else{
            holder.binding.viewPageImage.load(null)
            holder.binding.rlLoaderChapterPageItem.visibility = View.VISIBLE
        }
    }

    fun onPageSelected(page: Any?, position: Int) {}

    fun setChapters(curr:  MutableList<ReaderChapterPage>, reload:Boolean = false) {
        dataSetPage.clear()
        dataSetPage = curr
        notifyDataSetChanged()
    }

    fun reversePages() {
        dataSetPage = dataSetPage.reversed().toMutableList()
        notifyDataSetChanged()
    }

    inner class ViewHolderPager (val binding: ViewPageReaderItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

        fun bind(data: ReaderChapterPage) {
            Log.d("ListT2d2sdfghjk", "Load in bind holder history - ${data}")

            val attacher = PhotoViewAttacher(binding.viewPageImage)
            attacher.setOnViewTapListener(onPageTapListener)
            attacher.update()

            if (binding.viewPageImage.drawable?.current != null) {
                binding.viewPageImage.load(null)
                binding.rlLoaderChapterPageItem.visibility = View.VISIBLE
            }

            CoroutineScope(Dispatchers.IO).launch {
                imageLoad(data.linkImage)
            }
        }

        private fun imageLoad(any: Any) {
            try {
                val imageLoader = ImageLoader(itemView.context)
                val request = ImageRequest.Builder(itemView.context)
                    .data(any)
                    .target { drawable ->
                        Log.d("ListT2d21221", "Load in imga - $drawable")
                        when (drawable) {
                            null -> {
                                binding.tvTopChapterPageItem.text = "Ошибка загрузки."
                                binding.circleBarReaderPageItem.visibility = View.GONE
                                binding.tvBottomChapterPageItem.visibility = View.VISIBLE
                                onClickErrorBtn(any)
                            }
                            else -> {
                                binding.viewPageImage.load(drawable)
                                binding.rlLoaderChapterPageItem.visibility = View.GONE
                            }
                        }
                    }
                    .build()
                imageLoader.enqueue(request)
            }catch (e:Exception){
                Log.e("imageLoad", e.toString())
            }catch (e:IllegalStateException){
                Log.e("imageLoad", e.toString())
            }
        }

        private fun onClickErrorBtn(any: Any) {
            binding.tvBottomChapterPageItem.setOnClickListener() {
                binding.tvTopChapterPageItem.text = "Загрузка"
                binding.tvBottomChapterPageItem.visibility = View.GONE
                binding.circleBarReaderPageItem.visibility = View.VISIBLE

                CoroutineScope(Dispatchers.IO).launch {
                    imageLoad(any)
                }
            }
        }

    }
}







