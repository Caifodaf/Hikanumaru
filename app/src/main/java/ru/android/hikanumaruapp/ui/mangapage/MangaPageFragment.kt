package ru.android.hikanumaruapp.ui.mangapage

import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import coil.load
import coil.size.Scale
import coil.transform.RoundedCornersTransformation
import com.commit451.coiltransformations.BlurTransformation
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import ru.android.hikanumaruapp.R
import ru.android.hikanumaruapp.databinding.FragmentMangaPageBinding
import ru.android.hikanumaruapp.model.Manga
import ru.android.hikanumaruapp.model.MangaPageTextDate
import ru.android.hikanumaruapp.utilits.Events
import ru.android.hikanumaruapp.utilits.NavigationFragmentinViewModel

@AndroidEntryPoint
class MangaPageFragment : Fragment() {

    private var _binding: FragmentMangaPageBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<MangaPageViewModel>()

    private lateinit var urlPage: String
    private var tryLoadPage = 0

    private var isLoadPage: Boolean = false
    private var isLoadChapter: Boolean = false

    private var listPage = mutableListOf<Manga>()

    private val navigationEventsObserver = Events.EventObserver { event ->
        when (event) {
            is NavigationFragmentinViewModel.NavigationFrag -> navigateNav(event.navigation, event.bundle)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMangaPageBinding.inflate(inflater, container, false)
        val root: View = binding.root

        urlPage = arguments?.getString("url").toString()
        viewModel.urlPage = urlPage

        //sh = requireActivity().getSharedPreferences(urlPage.removePrefix("https://readmanga.io/"), Context.MODE_PRIVATE)
        //isBookmark = sh.getBoolean("bookmark", false)
        //if (isBookmark)
        //    bookmarkName = sh.getString("bookmark_mark", "").toString()

        binding.placeholderMangaPage.visibility = View.VISIBLE
        binding.layoutMainPageMangaPage.visibility = View.GONE

        btnInitUnLoad()

        getMangaPage()

        viewModel.emitter.observe(viewLifecycleOwner, navigationEventsObserver)

        return root
    }

    private fun getMangaPage() {
        isLoadPage = true

        observeChapterList()
        viewModel.listPage.observe(viewLifecycleOwner, Observer { listPage ->
            if (listPage.isNullOrEmpty()) {
                if (tryLoadPage >= 3) {
                    tryLoadPage += 1
                    viewModel.getDataPage(urlPage)
                } else {
                    isLoadPage = false
                    Log.e("ErrorLoadMangaPage", "Error download page")
                    binding.frameMangaPage.visibility = View.GONE
                    binding.frameMangaPageError.visibility = View.VISIBLE

                    binding.llErrorBlockMangaPage.visibility= View.VISIBLE
                    binding.tvBtnErrorMangaPage.visibility= View.VISIBLE
                    binding.progressCircleBarErrorMangaPage.visibility = View.GONE
                }
            } else {
                isLoadPage = false
                this.listPage = listPage.toMutableList()
                initRecyclerView()
                updatePage()
                getChaptersPage()
                btnInitLoad()
            }
        })

        viewModel.getDataPage(urlPage)
    }

    private fun initRecyclerView(){
        viewModel.initRecyclerView(binding.scrollTextBlock)
        viewModel.initRecyclerView(binding.rvChaptersManga)
    }

    private fun getChaptersPage(){
        isLoadChapter = true

        if (!listPage[0].chapter.isNullOrEmpty()) {
            if (!listPage[0].chapter!![0].notChapter) {
                binding.llSortBtn.visibility = View.VISIBLE
                binding.tvChaptersMangaPageError.visibility = View.GONE
                binding.clBlockChapterMangaPage.visibility = View.VISIBLE
                binding.progressLoadChapterMangaPage.visibility = View.VISIBLE
                binding.rvChaptersManga.visibility = View.GONE
                binding.llBtnMoreChapterList.visibility = View.GONE

                // Load 1-8 chapter for page
                viewModel.setListChapterData(listPage[0].chapter!!)
                // Load other chapters
                if (listPage[0].chapter!!.size >= 5)
                    viewModel.getDataChapter(urlPage)

                binding.progressLoadChapterMangaPage.visibility = View.GONE
                binding.rvChaptersManga.visibility = View.VISIBLE
                binding.llBtnMoreChapterList.visibility = View.VISIBLE
            } else {
                binding.llSortBtn.visibility = View.GONE
                binding.tvChaptersMangaPageError.visibility = View.VISIBLE
                binding.clBlockChapterMangaPage.visibility = View.GONE
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
        viewModel.listChapters.observe(viewLifecycleOwner, Observer { listChapters ->
            if (listPage.isNullOrEmpty()) {
                if (tryLoadPage >= 3) {
                    tryLoadPage += 1
                    viewModel.getDataPage(urlPage)
                } else {
                    isLoadChapter = false
                    Log.e("ErrorLoadMangaPage", "Error download page")
                    binding.frameMangaPage.visibility = View.GONE
                    binding.frameMangaPageError.visibility = View.VISIBLE

                    binding.llErrorBlockMangaPage.visibility= View.VISIBLE
                    binding.tvBtnErrorMangaPage.visibility= View.VISIBLE
                    binding.progressCircleBarErrorMangaPage.visibility = View.GONE
                }
            } else {
                isLoadChapter = false
                this.listPage = listPage.toMutableList()
                initRecyclerView()
                updatePage()
                btnInitLoad()
            }
        })
    }

    private fun updatePage() {
        // Show Main layout
        binding.frameMangaPageError.visibility = View.GONE
        binding.frameMangaPage.visibility = View.VISIBLE
        binding.layoutMainPageMangaPage.visibility = View.VISIBLE

        // Load image background
        if (!listPage[0].imageBack.isNullOrEmpty())
            binding.ivBackgroundMangaPage.load(listPage[0].imageBack){
                // todo add place and error
                //placeholder(R.color.grey_light_alternative_2)
                //error(R.drawable.placeholder)
                scale(Scale.FILL)
                transformations(BlurTransformation(requireContext(), sampling = 0.3f))
            }
        // Load central image
        binding.ivPageImage.load(listPage[0].image){
            // todo add place and error
            scale(Scale.FILL)
            transformations(RoundedCornersTransformation(15f*4))
        }

        // Load image in rating layout
        binding.ivRatingBlockImage.load(listPage[0].image){
            scale(Scale.FILL)
        }

        // Set data for scroll text data
        val listText = mutableListOf<MangaPageTextDate>()
        var author = ""
        if(!listPage[0].info[0].author.isNullOrEmpty())
            author = listPage[0].info[0].author[0]

        listText.add(
            MangaPageTextDate(
                listPage[0].type!!,
                listPage[0].status,
                listPage[0].statusTranslated,
                listPage[0].year,
                author
            )
        )
        viewModel.setListTextData(listText)

        // Set 1-8 chapter for page
        if (!listPage[0].chapter.isNullOrEmpty()) {
            if (!listPage[0].chapter!![0].notChapter) {
                val params = binding.rvChaptersManga.layoutParams
                val heightElem: Int = when (listPage[0].chapter!!.size) {
                    1 -> 202
                    2 -> 408
                    3 -> 612
                    4 -> 710
                    else -> 710
                }
                params.height = heightElem
                binding.rvChaptersManga.layoutParams = params
                binding.tvCountChapterManga.text = "${listPage[0].chapterCount!! - 1}"
                if (listPage[0].chapter!!.size > 4) {
                    binding.llBtnMoreChapterList.visibility = View.VISIBLE
                } else {
                    binding.llBtnMoreChapterList.visibility = View.GONE
                }
            } else{
                binding.llSortBtn.visibility = View.GONE
                binding.tvChaptersMangaPageError.visibility = View.VISIBLE
            }
        }

        // Rating score
        binding.tvScoreMangaPage.text = listPage[0].score
        // Name manga
        binding.tvNameManga.text = listPage[0].name
        // Alternative name check
        if (!listPage[0].alternativeName.isNullOrEmpty()) {
            binding.tvAlternativeNameManga.visibility = View.VISIBLE
            binding.tvAlternativeNameManga.text = listPage[0].alternativeName
        } else
            binding.tvAlternativeNameManga.visibility = View.GONE
        // Description check
        if (!listPage[0].description.isNullOrEmpty()) {
            binding.tvDescriptionManga.visibility = View.VISIBLE
            binding.tvBtnMoreDescriptionManga.visibility = View.VISIBLE
            binding.tvDescriptionIsEmpty.visibility = View.GONE
            binding.tvDescriptionManga.text = listPage[0].description
        } else {
            binding.tvDescriptionIsEmpty.visibility = View.VISIBLE
            binding.tvDescriptionManga.visibility = View.GONE
            binding.tvBtnMoreDescriptionManga.visibility = View.GONE
        }
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
        binding.ivBtnDownloadMangaPage.setOnClickListener(){
            //todo add fun
        }
        binding.ivBtnBookmarkMangaPage.setOnClickListener(){
            //todo add fun
        }
        binding.ivBtnShareMangaPage.setOnClickListener(){
            //todo add fun
        }
        binding.ivBtnReadMangaPage.setOnClickListener(){
            //todo add fun
        }
        binding.ivBtnOpenMoreInfo.setOnClickListener(){
            lifecycleScope.launchWhenResumed {
                val bundle = Bundle()
                val str = Gson().toJson(listPage)
                bundle.putString("list", str)
                findNavController().navigate(R.id.navigation_mangapage_more_info_sheet_panel, bundle)
            }
        }
        binding.tvBtnMoreDescriptionManga.setOnClickListener() {
            val readMore = requireContext().getString(R.string.read_more_manga_page)
            val collapse = requireContext().getString(R.string.collapse_manga_page)
            when (binding.tvBtnMoreDescriptionManga.text) {
                readMore -> {
                    binding.tvDescriptionManga.maxLines = Integer.MAX_VALUE
                    binding.tvBtnMoreDescriptionManga.text = collapse
                }
                collapse -> {
                    binding.tvDescriptionManga.maxLines = 3
                    binding.tvBtnMoreDescriptionManga.text = readMore
                }
            }
        }
        viewModel.initBtn(binding.llSortBtn,requireContext(),binding.ivSortImage)
        viewModel.initBtn(binding.llBtnMoreChapterList)
    }

    fun navigateNav(navigation: Int, bundle: Bundle?) {
        findNavController().navigate(navigation, bundle)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}