package ru.android.hikanumaruapp.ui.reader.viewer.pager.pages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.chrisbanes.photoview.OnViewTapListener
import com.github.chrisbanes.photoview.PhotoViewAttacher
import ru.android.hikanumaruapp.databinding.TrasitorPageReaderItemBinding
import ru.android.hikanumaruapp.databinding.ViewPageReaderItemBinding
import ru.android.hikanumaruapp.ui.reader.ReaderViewModel
import ru.android.hikanumaruapp.ui.reader.model.ReaderChapterPage
import ru.android.hikanumaruapp.ui.reader.model.TransItem

class EndTransferPagePagerFragment(
    val type: Int,
    val vm: ReaderViewModel,
    val readerChapterPage: TransItem,
    val onPageTapListener: OnViewTapListener
) : Fragment(){

    private var _binding: TrasitorPageReaderItemBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = TrasitorPageReaderItemBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val attacher = PhotoViewAttacher(binding.viewPageImage)
        attacher.setOnViewTapListener(onPageTapListener)
        attacher.update()

        initUI()
    }

    private fun initUI() {
        when(type){
            -1 ->  //Start
            {
                binding.tvTopTitle.text = "Start"
                binding.tvBottomTitle.text = readerChapterPage.second
                binding.ImageViewArrow.rotation = 270F
            }
            3 ->  //Finish
            {
                binding.tvTopTitle.text = "End"
                binding.tvBottomTitle.text = readerChapterPage.second
                binding.ImageViewArrow.rotation = 90F
            }
        }
    }
}