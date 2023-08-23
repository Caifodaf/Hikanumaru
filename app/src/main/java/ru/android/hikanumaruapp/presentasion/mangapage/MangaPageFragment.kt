package ru.android.hikanumaruapp.presentasion.mangapage

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import coil.load
import coil.size.Dimension
import coil.size.Scale
import coil.transform.RoundedCornersTransformation
import com.commit451.coiltransformations.BlurTransformation
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import dagger.hilt.android.AndroidEntryPoint
import ru.android.hikanumaruapp.presentasion.BaseInnerFragment
import ru.android.hikanumaruapp.presentasion.ConstPages
import ru.android.hikanumaruapp.presentasion.ConstPages.Companion.DEFAULT_PAGE_VIEW
import ru.android.hikanumaruapp.presentasion.ConstPages.Companion.ERROR_PAGE_VIEW
import ru.android.hikanumaruapp.presentasion.ConstPages.Companion.LOADING_PAGE_VIEW
import ru.android.hikanumaruapp.R
import ru.android.hikanumaruapp.api.api.main.MainApiViewModel
import ru.android.hikanumaruapp.api.init.ApiResponse
import ru.android.hikanumaruapp.data.debugModels
import ru.android.hikanumaruapp.databinding.FragmentMangaPageBinding
import ru.android.hikanumaruapp.data.local.user.UserDataViewModel
import ru.android.hikanumaruapp.presentasion.mangapage.adapter.MangaPageFlexTagsAdapter
import ru.android.hikanumaruapp.data.model.mangapage.TagsMangaPageId
import ru.android.hikanumaruapp.data.model.mangapage.TagsMangaPageModel
import ru.android.hikanumaruapp.presentasion.mangapage.pages.MangaPageViewPagerAdapter
import ru.android.hikanumaruapp.presentasion.reader.data.NetworkResponse
import ru.android.hikanumaruapp.presentasion.reader.data.ParserViewModel
import ru.android.hikanumaruapp.utilits.navigation.Events
import ru.android.hikanumaruapp.utilits.navigation.NavigationFragmentinViewModel

@AndroidEntryPoint
class MangaPageFragment : BaseInnerFragment(), debugModels {

    private var _binding: FragmentMangaPageBinding? = null
    private val binding get() = _binding!!

    private val vm by viewModels<MangaPageViewModel>()
    private val vmApi by viewModels<MainApiViewModel>()
    private val vmUser by viewModels<UserDataViewModel>()
    private val vmParser by viewModels<ParserViewModel>()

    private lateinit var vpAdapter : MangaPageViewPagerAdapter
    private lateinit var tagsAdapter: MangaPageFlexTagsAdapter

    private val navigationEventsObserver = Events.EventObserver { event ->
        when (event) {
            is NavigationFragmentinViewModel.NavigationFrag -> navigateNav(event.navigation, event.bundle)
        }
    }

    //init { setupOnBackPressed() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        requireActivity().findViewById<com.google.android.material.bottomnavigation.BottomNavigationView>(R.id.nav_view).visibility = View.GONE
        _binding = FragmentMangaPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vm.getArguments(arguments)

        binding.apply {
            stateMangaPage(LOADING_PAGE_VIEW)

            getMangaPage()
            vm.getDataChapter(vmParser)

            observeListPage()

            btnInitLoad()

            //observeBookmark()
            vm.emitter.observe(viewLifecycleOwner, navigationEventsObserver)
            dbInit()
        }
    }

    private fun FragmentMangaPageBinding.dbInit() {
        vm.listPage.value = mangaPage()
        binding.root.postDelayed({
            stateMangaPage(ConstPages.DEFAULT_PAGE_VIEW)
        }, 1000L)
    }

    private fun FragmentMangaPageBinding.btnInitLoad() {
        ImageBtnDownload.setOnClickListener() {
            it.timerDoubleButton()
            //todo add fun

        }
        ImageBtnBookmark.setOnClickListener() {
            it.timerDoubleButton()
            vm.changeBookmark()
        }
        ImageBtnShare.setOnClickListener() {
            it.timerDoubleButton()
            //todo add fun

        }
        TVBtnRead.setOnClickListener() {
            it.timerDoubleButton()
            //viewModel.openReaderPageFastRead(binding.ivBtnReadMangaPage)
        }
    }

    private fun FragmentMangaPageBinding.getMangaPage() {
        vm.getDataPage(vmApi)
    }

    private fun FragmentMangaPageBinding.observeListPage() {
        vmApi.mangaPageResponse.observe(viewLifecycleOwner, Observer {
            when (it) {
                is ApiResponse.Failure -> {
                    Log.d("vmApi", "mangaPageResponse falure - " + it.errorMessage)
                    stateMangaPage(ERROR_PAGE_VIEW)
                    when (it.code) {
                        502 -> TVErrorMessageMangaPage.text = "Ошибка сервера"
                        else -> TVErrorMessageMangaPage.text = "Неверная почта или пароль"
                    }
                }
                ApiResponse.Loading -> {
                    Log.d("vmApi", "mangaPageResponse Loading")
                    stateMangaPage(LOADING_PAGE_VIEW)
                }
                is ApiResponse.Success -> {
                    Log.d("vmApi", "mangaPageResponse refresh - " + it.data)
                    vm.apply {
                        listPage.value = it.data
                        initBDLibrary(requireActivity())
                    }
                }
            }
        })

        vm.listPage.observe(viewLifecycleOwner) {
            Log.d("vm", "listPage - ${vm.listPage.value}")
            updatePage()
            initFlexTags()
            initPagerBlock()
        }
    }

    private fun FragmentMangaPageBinding.initFlexTags() {
        val listPage = vm.listPage.value
        val tags: MutableList<TagsMangaPageModel> = mutableListOf()
        if (!listPage!!.genres.isNullOrEmpty())
            listPage.genres!!.forEachIndexed { index, genres ->
                tags.add(TagsMangaPageModel(type = TagsMangaPageId.GENRE_TAG,
                    genres.id, genres.title))
                tags.add(TagsMangaPageModel(type = TagsMangaPageId.DIVIDE))
            }

        tags.add(TagsMangaPageModel(type = TagsMangaPageId.RATING_TAG, text = "0.0"))
        tags.add(TagsMangaPageModel(type = TagsMangaPageId.DIVIDE))

        tags.add(TagsMangaPageModel(type = TagsMangaPageId.YEAR_TAG, text = listPage.releaseYear.toString()))
        tags.add(TagsMangaPageModel(type = TagsMangaPageId.DIVIDE))

        val crTypes = resources.getStringArray(R.array.creativeworks_types)
        var positionCRTypes = crTypes.indexOf(listPage.type)
        if (positionCRTypes == -1) positionCRTypes = 0
        tags.add(TagsMangaPageModel(type = TagsMangaPageId.TYPE_WORK_TAG, text =
            resources.getStringArray(R.array.creativeworks_type_texts)[positionCRTypes]))
        tags.add(TagsMangaPageModel(type = TagsMangaPageId.DIVIDE))

        tags.add(TagsMangaPageModel(type = TagsMangaPageId.AGE_RATING_TAG, text = "13+"))
        tags.add(TagsMangaPageModel(type = TagsMangaPageId.DIVIDE))

        tags.add(TagsMangaPageModel(type = TagsMangaPageId.PUBLISH_STATUS_TAG, text = "ongoing"))

        //vm.listTagsDataLoad.value = tags
        tagsAdapter = MangaPageFlexTagsAdapter(vm)
        val ll = FlexboxLayoutManager(requireActivity().applicationContext)
        ll.apply{
            flexDirection = FlexDirection.ROW
            justifyContent = JustifyContent.CENTER
        }
        FlexTagsBlock.apply{
            layoutManager = ll
            adapter = tagsAdapter
        }
        tagsAdapter.setMain(tags)
    }

    private fun FragmentMangaPageBinding.initPagerBlock() {
        vpAdapter = MangaPageViewPagerAdapter(vm,childFragmentManager, lifecycle)
        ViewPager2.apply {
            isUserInputEnabled = false
            adapter = vpAdapter
        }

        var backLayout = ViewTabBackGround.width.toFloat()
        ViewTabBackGround.viewTreeObserver
            .addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    backLayout = ViewTabBackGround.width.toFloat()
                    ViewTabBackGround.viewTreeObserver.removeOnGlobalLayoutListener(this)
                }
            })

        val isActiveColor = ContextCompat.getColor(requireContext(), R.color.grey_900)
        val isDeactiveColor = ContextCompat.getColor(requireContext(), R.color.grey_500)

        TVTabGeneral.setOnClickListener {
            ViewPager2.setCurrentItem(0,false)
            TVTabGeneral.setTextColor(isActiveColor)
            TVTabInfo.setTextColor(isDeactiveColor)
            TVTabStatistic.setTextColor(isDeactiveColor)
        }
        TVTabInfo.setOnClickListener {
            ViewPager2.setCurrentItem(1,false)
            TVTabGeneral.setTextColor(isDeactiveColor)
            TVTabInfo.setTextColor(isActiveColor)
            TVTabStatistic.setTextColor(isDeactiveColor)
        }
        TVTabStatistic.setOnClickListener {
            ViewPager2.setCurrentItem(2,false)
            TVTabGeneral.setTextColor(isDeactiveColor)
            TVTabInfo.setTextColor(isDeactiveColor)
            TVTabStatistic.setTextColor(isActiveColor)
        }

        ViewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                val selectorLayout = backLayout / 3
                val posX = when (position) {
                    0 -> 0f
                    1 -> selectorLayout
                    2 -> (selectorLayout * 2)
                    else -> 0f
                }

                ViewTab.animate()
                    .x(posX)
                    .setDuration(300)
                    .start()
            }
        })
    }

    private fun FragmentMangaPageBinding.updatePage() {
        TVNameManga.text = vm.listPage.value!!.title
        TVAlternativeNameManga.text = vm.listPage.value!!.additionalTitle

        if (!vm.listPage.value!!.coverLinks.isNullOrEmpty()) {
            //TODO
            val source = when(vm.listPage.value!!.source){
                "" -> "https://staticrm.rmr.rocks"
                else -> "https://staticrm.rmr.rocks"
            }

            ImageManga.load(source+vm.listPage.value!!.coverLinks!!.last()) {
                //scale(Scale.FILL)
                //size(
                //    width = 1000,
                //    height = requireContext().pixelToDP(560f))
                placeholder(R.drawable.ic_rectangle_error)
                error(R.drawable.ic_rectangle_error)
                transformations(RoundedCornersTransformation(
                    requireContext().pixelToDP(16f).toFloat()))
            }
            ImageBack.load(source+vm.listPage.value!!.coverLinks!!.last()) {
                // todo add place and error
                placeholder(R.color.light_grey_3)
                error(R.color.light_grey_3)
                //scale(Scale.FILL)
                transformations(BlurTransformation(requireContext(), sampling = 0.8f))
            }
        }

        stateMangaPage(DEFAULT_PAGE_VIEW)
    }

    private fun FragmentMangaPageBinding.stateMangaPage(state: Int){
        when (state) {
            ConstPages.DEFAULT_PAGE_VIEW -> {
                FrameMangaPageError.visibility = View.GONE
                PlaceholderMangaPage.visibility = View.GONE
                NSWMangaPage.visibility = View.VISIBLE
            }
            ConstPages.LOADING_PAGE_VIEW -> {
                PlaceholderMangaPage.visibility = View.VISIBLE
                FrameMangaPageError.visibility = View.GONE
                NSWMangaPage.visibility = View.GONE
            }
            ConstPages.ERROR_PAGE_VIEW -> {
                FrameMangaPageError.visibility = View.VISIBLE
                PlaceholderMangaPage.visibility = View.GONE
                NSWMangaPage.visibility = View.GONE
            }
        }
    }

    fun navigateNav(navigation: Int, bundle: Bundle?) {
        findNavController().navigate(navigation, bundle)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        //requireActivity().findViewById<com.google.android.material.bottomnavigation.BottomNavigationView>(R.id.nav_view).visibility = View.VISIBLE
        _binding = null
    }















    //@SuppressLint("UseCompatLoadingForColorStateLists")
    //private fun observeBookmark() {
    //    vm.isBookmark.observe(viewLifecycleOwner, Observer { it ->
    //        when(it){
    //            // Todo rework
    //            true -> binding.ImageBtnBookmark.imageTintList = resources.getColorStateList(R.color.red_old)
    //            false -> binding.ImageBtnBookmark.imageTintList = resources.getColorStateList(R.color.grey_light_alternative_2)
    //        }
    //    })
    //}

    //private fun FragmentMangaPageBinding.btnInitLoad(){
    //    binding.ImageBtnDownload.setOnClickListener(){
    //        //todo add fun
    //    }
    //    binding.ImageBtnBookmark.setOnClickListener(){
    //        vm.changeBookmark()
    //    }
    //    binding.ImageBtnShare.setOnClickListener(){
    //        //todo add fun
    //    }
    //    binding.TVBtnRead.setOnClickListener(){
    //        binding.TVBtnRead.timerDoubleButton()
    //        //viewModel.openReaderPageFastRead(binding.ivBtnReadMangaPage)
    //    }
        //binding.ImageBtnOpenMoreInfo.setOnClickListener(){
        //    binding.ImageBtnOpenMoreInfo.timerDoubleButton()
        //    lifecycleScope.launchWhenResumed {
        //        val bundle = Bundle()
        //        val str = Gson().toJson(listPage)
        //        bundle.putString("list", str)
        //        findNavController().navigate(R.id.action_navigation_mangapage_to_fragmentMangaPageBottomSheetPanel, bundle)
        //    }
        //}
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
        //vm.initBtn(binding.LLSortBtn,requireContext(),binding.ImageSort)
        //vm.initBtn(binding.TVBtnMoreChapterList) todo rework
    //}


}