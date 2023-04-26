package ru.android.hikanumaruapp.presentasion.reader.viewer.pager.pages

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.viewModelScope
import coil.ImageLoader
import coil.load
import coil.request.ImageRequest
import com.github.chrisbanes.photoview.OnViewTapListener
import com.github.chrisbanes.photoview.PhotoViewAttacher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import ru.android.hikanumaruapp.databinding.ViewPageReaderItemBinding
import ru.android.hikanumaruapp.presentasion.reader.ReaderViewModel
import ru.android.hikanumaruapp.presentasion.reader.model.ReaderChapterPage
import ru.android.hikanumaruapp.utilits.ProgressListener


class ImagePagePagerFragment(
    val vm: ReaderViewModel,
    val chapter: ReaderChapterPage,
    val onPageTapListener: OnViewTapListener,
) : Fragment() {

    private var _binding: ViewPageReaderItemBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = ViewPageReaderItemBinding.inflate(inflater, container, false)
        return binding.root
    }

    private lateinit var job: Job

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    private fun initUI() {
        Log.d("ListT", "Load in bind page - $chapter")

        val attacher = PhotoViewAttacher(binding.viewPageImage)
        attacher.setOnViewTapListener(onPageTapListener)
        attacher.update()

       job = vm.viewModelScope.launch(Dispatchers.Default) {
           //imageLoadGlide(chapter.linkImage)
           imageLoad(chapter.linkImage)
       }
    }

    @SuppressLint("SetTextI18n")
    private fun imageLoadGlide(linkImage: String) {
        ProgressListener(requireActivity(),linkImage,binding.viewPageImage,binding.tvTopChapterPageItem)
    }

    private fun imageLoad(any: Any) {
        try {
            val imageLoader = ImageLoader(requireActivity())
            val request = ImageRequest.Builder(requireActivity())
                .data(any)
                .target { drawable ->
                    Log.d("ListT2d2", "Load in imga - $drawable")
                    when (drawable) {
                        null -> {
                            binding.tvTopChapterPageItem.text = "Ошибка загрузки."
                            binding.circleBarReaderPageItem.visibility = View.GONE
                            binding.tvBottomChapterPageItem.visibility = View.VISIBLE
                            onClickErrorBtn(any as String)
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
//    var bitmap: Bitmap? = null
//
//    Glide.with(requireActivity())
//        .asBitmap()
//        .load(any as String)
//        .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
////        .listener(object : SimpleTarget<Drawable> {
////            override fun onResourceReady(
////                resource: Drawable?,
////                model: Any?,
////                target: Target<Drawable>?,
////                dataSource: com.bumptech.glide.load.DataSource?,
////                isFirstResource: Boolean,
////            ): Boolean {
////                bitmap = (attr.resource as BitmapDrawable).bitmap
////                binding.rlLoaderChapterPageItem.visibility = View.GONE
////                return false
////            }
////
////            override fun onLoadFailed(
////                e: GlideException?, model: Any?, target: Target<Drawable>?,
////                isFirstResource: Boolean,
////            ): Boolean {
////                binding.tvTopChapterPageItem.text = "Ошибка загрузки."
////                binding.circleBarReaderPageItem.visibility = View.GONE
////                binding.tvBottomChapterPageItem.visibility = View.VISIBLE
////                onClickErrorBtn(any as String)
////                return false
////            }
////        })
//        .into(object : BitmapImageViewTarget(binding.viewPageImage) {
//            override fun setResource(resource: Bitmap?) {
//                //Play with bitmap
//                super.setResource(resource)
//            }
//        })
//
//        return bitmap


    private fun onClickErrorBtn(url: String) {
        binding.tvBottomChapterPageItem.setOnClickListener() {
            binding.tvTopChapterPageItem.text = "Загрузка"
            binding.tvBottomChapterPageItem.visibility = View.GONE
            binding.circleBarReaderPageItem.visibility = View.VISIBLE

            job = vm.viewModelScope.launch(Dispatchers.IO) {
                imageLoad(chapter.linkImage)
            }
        }
    }

    private fun onReLoad() {

    }
}