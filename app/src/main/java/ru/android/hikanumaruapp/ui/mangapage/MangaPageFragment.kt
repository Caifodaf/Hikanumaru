package ru.android.hikanumaruapp.ui.mangapage

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import coil.load
import coil.size.Scale
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import ru.android.hikanumaruapp.R
import ru.android.hikanumaruapp.databinding.FragmentMangaPageBinding
import ru.android.hikanumaruapp.model.Manga
import ru.android.hikanumaruapp.model.MangaPageTextDate
import ru.android.hikanumaruapp.utilits.navigation.Events
import ru.android.hikanumaruapp.utilits.navigation.NavigationFragmentinViewModel
import ru.android.hikanumaruapp.utilits.UIUtils

@AndroidEntryPoint
class MangaPageFragment : Fragment(),UIUtils {

    private var _binding: FragmentMangaPageBinding? = null
    private val binding get() = _binding!!
    private val vm by viewModels<MangaPageViewModel>()

    private lateinit var urlPage: String
    private var tryLoadPage = 0

    private var isLoadPage: Boolean = false
    private var isLoadChapter: Boolean = false

    private lateinit var listPage : Manga

    private val navigationEventsObserver = Events.EventObserver { event ->
        when (event) {
            is NavigationFragmentinViewModel.NavigationFrag -> navigateNav(event.navigation, event.bundle)
        }
    }

    init { onBack() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMangaPageBinding.inflate(inflater, container, false)
        val root: View = binding.root

        urlPage = arguments?.getString("url").toString()
        vm.urlPage = urlPage

        //sh = requireActivity().getSharedPreferences(urlPage.removePrefix("https://readmanga.io/"), Context.MODE_PRIVATE)
        //isBookmark = sh.getBoolean("bookmark", false)
        //if (isBookmark)
        //    bookmarkName = sh.getString("bookmark_mark", "").toString()

        binding.placeholderMangaPage.visibility = View.VISIBLE
        //binding.layoutMainPageMangaPage.visibility = View.GONE

        btnInitUnLoad()

        observeListPage()
        observeChapterList()
        observeBookmark()
        getMangaPage()

        vm.emitter.observe(viewLifecycleOwner, navigationEventsObserver)

        return root
    }

    @SuppressLint("UseCompatLoadingForColorStateLists")
    private fun observeBookmark() {
        vm.isBookmark.observe(viewLifecycleOwner, Observer { it ->
            when(it){
                // Todo rework
                true -> binding.ImageBtnBookmark.imageTintList = resources.getColorStateList(R.color.red_old)
                false -> binding.ImageBtnBookmark.imageTintList = resources.getColorStateList(R.color.grey_light_alternative_2)
            }
        })
    }

    private fun getMangaPage() {
        isLoadPage = true
        vm.getDataPage(urlPage)
    }

    private fun observeListPage(){
        vm.listPage.observe(viewLifecycleOwner, Observer { listPage ->
            if (listPage == null) {
                if (tryLoadPage >= 3) {
                    tryLoadPage += 1
                    vm.getDataPage(urlPage)
                } else {
                    isLoadPage = false
                    Log.e("ErrorLoadMangaPage", "Error download page2")
                    //binding.frameMangaPage.visibility = View.GONE
                    binding.frameMangaPageError.visibility = View.VISIBLE

                    binding.llErrorBlockMangaPage.visibility= View.VISIBLE
                    binding.tvBtnErrorMangaPage.visibility= View.VISIBLE
                    binding.progressCircleBarErrorMangaPage.visibility = View.GONE
                }
            } else {
                vm.initBDLibrary(requireActivity())
                isLoadPage = false
                this.listPage = listPage
                initRecyclerView()
                updatePage()
                getChaptersPage()
                btnInitLoad()
            }
        })
    }

    private fun initRecyclerView(){
        //vm.initRecyclerView(binding.RVTagsBlock)
        vm.initRecyclerView(binding.RVChapterList)
        vm.initRecyclerView(binding.RVTranslatorsChapterBlock)
    }

    private fun getChaptersPage(){
        isLoadChapter = true

        if (!listPage.chapters.isNullOrEmpty()) {
            if (!listPage.chapters!![0].notChapter) {
                binding.LLSortBtn.visibility = View.VISIBLE
                binding.TVChaptersError.visibility = View.GONE
                binding.CCChaptersBLock.visibility = View.VISIBLE
                binding.ProgressLoadChapterList.visibility = View.VISIBLE
                binding.RVTranslatorsChapterBlock.visibility = View.GONE
                binding.RVChapterList.visibility = View.GONE
                binding.TVBtnMoreChapterList.visibility = View.GONE

                // Load 1-8 chapter for page
                vm.setListChapterData(listPage.chapters!!)
                // Load other chapters
                if (listPage.chapters!!.size >= 5)
                    vm.getDataChapter(urlPage)

                binding.ProgressLoadChapterList.visibility = View.GONE
                binding.RVChapterList.visibility = View.VISIBLE
                binding.RVTranslatorsChapterBlock.visibility = View.VISIBLE
                binding.TVBtnMoreChapterList.visibility = View.VISIBLE
            } else {
                binding.LLSortBtn.visibility = View.GONE
                binding.TVChaptersError.visibility = View.VISIBLE
                binding.CCChaptersBLock.visibility = View.GONE
            }
        }

//        if(!listPage[0].chapter!![0].notChapter){
//            rlChapterError.visibility = View.GONE
//            rlChapter.visibility = View.VISIBLE
//            adapterChapter.setMain(listPage[0].chapter!!)
//            isChaptersNon=false
//
//            isUnlockLoadChapter = true
//            getDataPageChapter()
//        }else{
//            isChaptersNon=true
//            rlChapterError.visibility = View.VISIBLE
//            rlChapter.visibility = View.GONE
//        }


//        if(!isChaptersNon) {
//            val params = binding.rl_chapters_manga.layoutParams
//            val heightElem:Int = when(listPage[0].chapter!!.size){
//                1 -> 196
//                2 -> 392
//                3 -> 588
//                4 -> 810
//                else-> 810
//            }
//            params.height = heightElem
//            binding.rl_chapters_manga.layoutParams = params
//
//            binding.tv_count_chapter_manga.text = "${listPage[0].chapterCount!!-1}"
//
//            if (listPage[0].chapter!!.size > 4) {
//                binding.tv_btn_more_chapter_list.visibility = View.VISIBLE
//            }else{
//                binding.tv_btn_more_chapter_list.visibility = View.GONE
//            }
//        }
    }

    private fun observeChapterList(){
        vm.listChapters.observe(viewLifecycleOwner, Observer { listChapters ->
            if (listPage == null) {
                if (tryLoadPage >= 3) {
                    tryLoadPage += 1
                    vm.getDataPage(urlPage)
                } else {
                    isLoadChapter = false
                    Log.e("ErrorLoadMangaPage", "Error download page1")
                    //binding.frameMangaPage.visibility = View.GONE
                    binding.frameMangaPageError.visibility = View.VISIBLE

                    binding.llErrorBlockMangaPage.visibility= View.VISIBLE
                    binding.tvBtnErrorMangaPage.visibility= View.VISIBLE
                    binding.progressCircleBarErrorMangaPage.visibility = View.GONE
                }
            } else {
                isLoadChapter = false
                this.listPage = listPage
                initRecyclerView()
                updatePage()
                btnInitLoad()
            }
        })
    }

    private fun updatePage() {
        // Show Main layout
        binding.frameMangaPageError.visibility = View.GONE
        //binding.frameMangaPage.visibility = View.VISIBLE
        //binding.layoutMainPageMangaPage.visibility = View.VISIBLE

        // Load image background
        if (!listPage.imageBack.isNullOrEmpty())
            binding.ImageBack.load(listPage.imageBack){
                // todo add place and error
                //placeholder(R.color.grey_light_alternative_2)
                //error(R.drawable.placeholder)
                scale(Scale.FILL)
                //transformations(BlurTransformation(requireContext(), sampling = 0.8f))
            }
        // Load central image
        binding.ImageManga.load(listPage.image){
            // todo add place and error
            //scale(Scale.FILL)
            //transformations(RoundedCornersTransformation(15f*4))
        }

        // Load image in rating layout
        //binding.ivRatingBlockImage.load(listPage.image){
        //    scale(Scale.FILL)
        //}

        // Set data for scroll text data
        val listText = mutableListOf<MangaPageTextDate>()
        var author = ""
        if(!listPage.info!!.author.isNullOrEmpty())
            author = listPage.info!!.author[0]

        listText.add(
            MangaPageTextDate(
                listPage.type!!,
                listPage.status,
                listPage.statusTranslated,
                listPage.year,
                author
            )
        )
        vm.setListTextData(listText)

        // Set 1-8 chapter for page
        if (!listPage.chapters.isNullOrEmpty()) {
            if (!listPage.chapters!![0].notChapter) {
                val params = binding.RVChapterList.layoutParams
                val heightElem: Int = when (listPage.chapters!!.size) {
                    1 -> 202
                    2 -> 408
                    3 -> 612
                    4 -> 710
                    else -> 710
                }
                params.height = heightElem
                binding.RVChapterList.layoutParams = params
                binding.TVCountChapter.text = "${listPage.chapterCount!! - 1}"
                if (listPage.chapters!!.size > 4) {
                    binding.TVBtnMoreChapterList.visibility = View.VISIBLE
                } else {
                    binding.TVBtnMoreChapterList.visibility = View.GONE
                }
            } else{
                binding.LLSortBtn.visibility = View.GONE
                binding.TVChaptersError.visibility = View.VISIBLE
            }
        }

        // Rating score
        binding.TVScoreRating.text = listPage.score
        // Name manga
        binding.TVNameManga.text = listPage.name
        // Alternative name check
        if (!listPage.alternativeName.isNullOrEmpty()) {
            binding.TVAlternativeNameManga.visibility = View.VISIBLE
            binding.TVAlternativeNameManga.text = listPage.alternativeName
        } else
            binding.TVAlternativeNameManga.visibility = View.GONE
        // Description check
        // todo visible
        //if (!listPage.description.isNullOrEmpty()) {
        //    binding.tvDescriptionManga.visibility = View.VISIBLE
        //    binding.tvBtnMoreDescriptionManga.visibility = View.VISIBLE
        //    binding.tvDescriptionIsEmpty.visibility = View.GONE
        //    binding.tvDescriptionManga.text = listPage.description
        //} else {
        //    binding.tvDescriptionIsEmpty.visibility = View.VISIBLE
        //    binding.tvDescriptionManga.visibility = View.GONE
        //    binding.tvBtnMoreDescriptionManga.visibility = View.GONE
        //}
        // Placeholder Hide
        binding.placeholderMangaPage.visibility = View.GONE
    }

    private fun btnInitUnLoad(){
        binding.tvBtnErrorMangaPage.setOnClickListener() {
            binding.llErrorBlockMangaPage.visibility = View.GONE
            binding.progressCircleBarErrorMangaPage.visibility = View.VISIBLE
            Handler().postDelayed({
                if (!isLoadPage) {
                    getMangaPage()
                    getChaptersPage()
                }
            }, 1000)
        }
    }

    private fun btnInitLoad(){
        binding.ImageBtnDownload.setOnClickListener(){
            //todo add fun
        }
        binding.ImageBtnBookmark.setOnClickListener(){
            vm.changeBookmark()
        }
        binding.ImageBtnShare.setOnClickListener(){
            //todo add fun
        }
        binding.TVBtnRead.setOnClickListener(){
            binding.TVBtnRead.timerDoubleButton()
            //viewModel.openReaderPageFastRead(binding.ivBtnReadMangaPage)
        }
        binding.ImageBtnOpenMoreInfo.setOnClickListener(){
            binding.ImageBtnOpenMoreInfo.timerDoubleButton()
            lifecycleScope.launchWhenResumed {
                val bundle = Bundle()
                val str = Gson().toJson(listPage)
                bundle.putString("list", str)
                findNavController().navigate(R.id.navigation_mangapage_more_info_sheet_panel, bundle)
            }
        }
        // todo visibility
        //binding.tvBtnMoreDescriptionManga.setOnClickListener() {
        //    val readMore = requireContext().getString(R.string.read_more_manga_page)
        //    val collapse = requireContext().getString(R.string.collapse_manga_page)
        //    when (binding.tvBtnMoreDescriptionManga.text) {
        //        readMore -> {
        //            binding.tvDescriptionManga.maxLines = Integer.MAX_VALUE
        //            binding.tvBtnMoreDescriptionManga.text = collapse
        //        }
        //        collapse -> {
        //            binding.tvDescriptionManga.maxLines = 3
        //            binding.tvBtnMoreDescriptionManga.text = readMore
        //        }
        //    }
        //}
        vm.initBtn(binding.LLSortBtn,requireContext(),binding.ImageSort)
        //vm.initBtn(binding.TVBtnMoreChapterList) todo rework
    }

    fun navigateNav(navigation: Int, bundle: Bundle?) {
        findNavController().navigate(navigation, bundle)
    }

    private fun onBack() {
        activity?.onBackPressedDispatcher?.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().navigateUp()
                }
            })
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}