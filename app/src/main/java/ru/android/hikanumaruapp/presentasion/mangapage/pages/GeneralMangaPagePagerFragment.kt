package ru.android.hikanumaruapp.presentasion.mangapage.pages

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import dagger.hilt.android.AndroidEntryPoint
import ru.android.hikanumaruapp.presentasion.ConstPages
import ru.android.hikanumaruapp.databinding.GeneralTabMangaPageBinding
import ru.android.hikanumaruapp.data.model.Chapter
import ru.android.hikanumaruapp.presentasion.mangapage.MangaPageViewModel
import ru.android.hikanumaruapp.presentasion.mangapage.adapter.MangaPageChapterAdapter
import ru.android.hikanumaruapp.utilits.UIUtils

@SuppressLint("UseCompatLoadingForDrawables", "SetTextI18n")
@AndroidEntryPoint
class GeneralMangaPagePagerFragment(private val vm: MangaPageViewModel) : Fragment(),UIUtils {

    private var _binding: GeneralTabMangaPageBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = GeneralTabMangaPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    private lateinit var chapterAdapter: MangaPageChapterAdapter
    private var isDescriptionOpen = false
    private var isChapterReverse = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            updateDataUI()
            stateChapterList(ConstPages.LOADING_PAGE_VIEW)
            observeChapterList()
            initBtn()
        }
        vm.getDataChapter()
    }

    private fun GeneralTabMangaPageBinding.initBtn() {
        LLSortBtn.setOnClickListener{ // Sort
            it.timerDoubleButton(100)
            isChapterReverse = when(isChapterReverse) {
                true -> {
                    ImageSort.rotation = 0f
                    false
                }
                false -> {
                    ImageSort.rotation = 180f
                    true
                }
            }
            chapterAdapter.reverseList()
        }
        ImageMoreDescription.setOnClickListener{ // Description
            it.timerDoubleButton(100)
            isDescriptionOpen = if (TVDescriptionDetail.maxLines == 4) {
                TVDescriptionDetail.maxLines = Int.MAX_VALUE
                ViewDescriptionAlpha.visibility = View.GONE
                ImageMoreDescription.visibility = View.VISIBLE
                ImageMoreDescription.rotation = 180f
                true
            } else {
                TVDescriptionDetail.maxLines = 4
                false
            }
        }
        TVBtnMoreChapterList.setOnClickListener{
            it.timerDoubleButton()
            vm.openChapterListFromGeneralPage()
        }

        val btnLoadChapters = listOf(
            TVBtnAgainCheckChapters,TVBtnAgainLoadChapters)
        btnLoadChapters.forEachIndexed { index, viewBtn ->
            viewBtn.setOnClickListener{ // Error | Empty
                it.timerDoubleButton()
                vm.listPage.value!!.chapters = null

                vm.isErrorLoadChapters = false
                vm.getDataChapter()
                stateChapterList(ConstPages.LOADING_PAGE_VIEW)
            }
        }
    }

    private fun GeneralTabMangaPageBinding.updateDataUI() {
        val data = vm.listPage.value!!
        if (!data.description.isNullOrBlank()){
            TVDescriptionDetail.viewTreeObserver.addOnGlobalLayoutListener {
                if (!isDescriptionOpen) {
                    if (TVDescriptionDetail.lineCount >= 4) {
                        ViewDescriptionAlpha.visibility = View.VISIBLE
                        ImageMoreDescription.visibility = View.VISIBLE
                        TVDescriptionDetail.maxLines = 4
                        ImageMoreDescription.rotation = 0f
                    } else {
                        ViewDescriptionAlpha.visibility = View.GONE
                        ImageMoreDescription.visibility = View.GONE
                    }
                }
            }
        }

        TVDescriptionDetail.text = data.description
        // Rating score
        TVScoreRating.text = "0.0/5.0"
        TVScoreRatingCounter.text = "0 оценок"
        RatingBarMangaPage.progress = 0
    }

    private fun GeneralTabMangaPageBinding.observeChapterList(){
        vm.listChapters.observe(viewLifecycleOwner, Observer { listChapters ->
            if (listChapters == null)
                stateChapterList(ConstPages.ERROR_PAGE_VIEW)
            else{
                vm.listPage.value!!.chapters = (listChapters as MutableList<Chapter>?)!!
                initRecyclerView()
                updateChapters()
            }
        })
    }

    private fun GeneralTabMangaPageBinding.initRecyclerView(){
        chapterAdapter = MangaPageChapterAdapter(vm)
        RVChapterList.apply {
            adapter = chapterAdapter
        }
    }

    private fun GeneralTabMangaPageBinding.updateChapters(){
        val chaptersList = vm.listPage.value!!.chapters!!
        if (!chaptersList.isNullOrEmpty()) {
            Log.d("ListT", "Load in bind holder history - $chaptersList")
            // Load 1-8 chapter for page
            chapterAdapter.setMain(chaptersList)
            // Load other chapters
            val isMoreChapters = when {
                chaptersList.size >= 5 -> {
                    stateChapterList(ConstPages.MORE_CHAPTER_PAGE_VIEW)
                    true
                }
                chaptersList[0].notChapter -> {
                    stateChapterList(ConstPages.EMPTY_CHAPTER_PAGE_VIEW)
                    false
                }
                else -> {
                    stateChapterList(ConstPages.LESS_CHAPTER_PAGE_VIEW)
                    true
                }
            }

            if (isMoreChapters){
                val rlChapterParams = RVChapterList.layoutParams
                requireActivity().apply {
                    val heightElem: Int = when (chaptersList.size) {
                        1 -> pixelToDP(66f+8f)
                        2 -> pixelToDP((66f+8f)*2)
                        3 -> pixelToDP((66f+8f)*3)
                        4 -> pixelToDP((66f+8f)*4)
                        else -> pixelToDP((66f+8f)*4)
                    }
                    rlChapterParams.height = heightElem
                }
                RVChapterList.layoutParams = rlChapterParams
            }

            TVCountChapter.text = "${chaptersList.size}"
        }
    }

    private fun GeneralTabMangaPageBinding.stateChapterList(state: Int){
        when (state) {
            ConstPages.MORE_CHAPTER_PAGE_VIEW -> {
                ShimmerChapterFrame.visibility = View.GONE
                RVTranslatorsChapterBlock.visibility = View.VISIBLE
                RVChapterList.visibility = View.VISIBLE
                TVBtnMoreChapterList.visibility = View.VISIBLE
                LLErrorLoadChapter.visibility = View.GONE
                LLEmptyListChapter.visibility = View.GONE
                LLSortBtn.isClickable = true
            }
            ConstPages.LESS_CHAPTER_PAGE_VIEW -> {
                ShimmerChapterFrame.visibility = View.GONE
                RVTranslatorsChapterBlock.visibility = View.VISIBLE
                RVChapterList.visibility = View.VISIBLE
                TVBtnMoreChapterList.visibility = View.GONE
                LLErrorLoadChapter.visibility = View.GONE
                LLEmptyListChapter.visibility = View.GONE
                LLSortBtn.isClickable = true
            }
            ConstPages.EMPTY_CHAPTER_PAGE_VIEW -> {
                ShimmerChapterFrame.visibility = View.GONE
                RVTranslatorsChapterBlock.visibility = View.GONE
                RVChapterList.visibility = View.GONE
                TVBtnMoreChapterList.visibility = View.GONE
                LLErrorLoadChapter.visibility = View.GONE
                LLEmptyListChapter.visibility = View.VISIBLE
                LLSortBtn.isClickable = false
                TVCountChapter.text = "0"
            }
            ConstPages.DEFAULT_PAGE_VIEW -> {
                ShimmerChapterFrame.visibility = View.GONE
                RVTranslatorsChapterBlock.visibility = View.VISIBLE
                RVChapterList.visibility = View.VISIBLE
                TVBtnMoreChapterList.visibility = View.VISIBLE
                LLErrorLoadChapter.visibility = View.GONE
                LLEmptyListChapter.visibility = View.GONE
            }
            ConstPages.LOADING_PAGE_VIEW -> {
                ShimmerChapterFrame.visibility = View.VISIBLE
                RVTranslatorsChapterBlock.visibility = View.GONE
                RVChapterList.visibility = View.GONE
                TVBtnMoreChapterList.visibility = View.GONE
                LLErrorLoadChapter.visibility = View.GONE
                LLEmptyListChapter.visibility = View.GONE
                LLSortBtn.isClickable = false
                TVCountChapter.text = "..."
            }
            ConstPages.ERROR_PAGE_VIEW -> {
                ShimmerChapterFrame.visibility = View.GONE
                RVTranslatorsChapterBlock.visibility = View.GONE
                RVChapterList.visibility = View.GONE
                TVBtnMoreChapterList.visibility = View.GONE
                LLErrorLoadChapter.visibility = View.VISIBLE
                LLEmptyListChapter.visibility = View.GONE
                LLSortBtn.isClickable = false
                TVCountChapter.text = "..."
            }
        }
    }
}