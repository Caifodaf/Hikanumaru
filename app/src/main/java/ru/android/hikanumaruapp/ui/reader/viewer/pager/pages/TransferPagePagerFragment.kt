package ru.android.hikanumaruapp.ui.reader.viewer.pager.pages

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.viewModelScope
import com.github.chrisbanes.photoview.OnViewTapListener
import com.github.chrisbanes.photoview.PhotoViewAttacher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import ru.android.hikanumaruapp.databinding.TrasitorPageReaderItemBinding
import ru.android.hikanumaruapp.databinding.ViewPageReaderItemBinding
import ru.android.hikanumaruapp.ui.reader.ReaderViewModel
import ru.android.hikanumaruapp.ui.reader.model.TransItem

class TransferPagePagerFragment(
    val type: Int,
    val vm: ReaderViewModel,
    val readerChapterPage: TransItem,
    val onPageTapListener: OnViewTapListener
    ) : Fragment(){

    private var _binding: TrasitorPageReaderItemBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = TrasitorPageReaderItemBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    private fun initUI() {
        when(type){
            0 ->  //Prev
            {
                binding.tvTopTitle.text = "Prev"
                binding.tvBottomTitle.text = readerChapterPage.second
                binding.ImageViewArrow.rotation = 90F
            }
            1 ->  //next
            {
                binding.tvTopTitle.text = "Next"
                binding.tvBottomTitle.text = readerChapterPage.second
                binding.ImageViewArrow.rotation = 270F
            }
        }
    }
}