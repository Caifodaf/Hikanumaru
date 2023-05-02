package ru.android.hikanumaruapp.presentasion.reader.viewer.pager

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.RecyclerView
import coil.ImageLoader
import coil.load
import coil.request.ImageRequest
import com.github.chrisbanes.photoview.OnViewTapListener
import com.github.chrisbanes.photoview.PhotoViewAttacher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.android.hikanumaruapp.databinding.ViewPageReaderItemBinding
import ru.android.hikanumaruapp.presentasion.reader.ReaderViewModel
import ru.android.hikanumaruapp.data.model.reader.ReaderChapterPage


class ViewPager2ReaderAdapter(
    val context: FragmentActivity,
    val vm: ReaderViewModel,
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    private val onPageTapListener: OnViewTapListener,
) : RecyclerView.Adapter<ViewPager2ReaderAdapter.ViewHolder>(){

    companion object {
        private const val TYPE_CHAPTER = 0
        private const val TYPE_TRANSISON = 4
        const val PREV = 0
        const val NEXT = 1
        const val TRANSLATE = 3
        const val FINISH = 4
    }

    var dataSetPage = mutableListOf<ReaderChapterPage>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder{
        val binding = ViewPageReaderItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemViewType(position: Int): Int {
        return TYPE_CHAPTER
    }

    override fun getItemCount(): Int = dataSetPage.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.d("ListT2d2", "Load in createFragment - ${dataSetPage[position]}")
        holder.bind()
    }

    fun onPageSelected(page: Any?, position: Int) {
    }

    fun setChapters(curr:  MutableList<ReaderChapterPage>, reload:Boolean = false) {
        //if (reload)
        dataSetPage.clear()
        dataSetPage = curr as MutableList<ReaderChapterPage>
        Log.d("ListT2d2", "Load in bind holder history - $dataSetPage")
        notifyDataSetChanged()
    }

    fun reversePages() {
        dataSetPage = dataSetPage.reversed().toMutableList()
        Log.d("ListT2d2", "Load in bind holder history - $dataSetPage")
        notifyDataSetChanged()
    }

    inner class ViewHolder (val binding: ViewPageReaderItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("CheckResult")
        fun bind() {
            Log.d("ListT", "Load in bind holder history - ${dataSetPage[absoluteAdapterPosition]}")

            val attacher = PhotoViewAttacher(binding.viewPageImage)
            attacher.setOnViewTapListener(onPageTapListener)
            attacher.update()

            if (binding.viewPageImage.drawable?.current != null) {
                binding.viewPageImage.load(null)
                binding.rlLoaderChapterPageItem.visibility = View.VISIBLE
            }

            vm.viewModelScope.launch(Dispatchers.Default) {
                imageLoad(dataSetPage[absoluteAdapterPosition].linkImage)
            }
        }

        private fun imageLoad(any: Any) {
            try {
                val imageLoader = ImageLoader(context)
                val request = ImageRequest.Builder(context)
                    .data(any)
                    .target { drawable ->
                        Log.d("ListT2d2", "Load in imga - $drawable")
                        when (drawable) {
                            null -> {
                                binding.tvTopChapterPageItem.text = "Ошибка загрузки."
                                binding.circleBarReaderPageItem.visibility = View.GONE
                                binding.tvBottomChapterPageItem.visibility = View.VISIBLE
                                onClickErrorBtn()
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

        private fun onClickErrorBtn() {
            binding.tvBottomChapterPageItem.setOnClickListener() {
                binding.tvTopChapterPageItem.text = "Загрузка"
                binding.tvBottomChapterPageItem.visibility = View.GONE
                binding.circleBarReaderPageItem.visibility = View.VISIBLE

                vm.viewModelScope.launch(Dispatchers.IO) {
                    imageLoad(dataSetPage[absoluteAdapterPosition].linkImage)
                }
            }
        }

    }
}







