package ru.android.hikanumaruapp.presentasion.reader

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.View.*
import android.widget.*
import androidx.activity.OnBackPressedCallback
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.github.chrisbanes.photoview.OnViewTapListener
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.AndroidEntryPoint
import me.everything.android.ui.overscroll.IOverScrollDecor
import me.everything.android.ui.overscroll.IOverScrollState
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper.ORIENTATION_HORIZONTAL
import ru.android.hikanumaruapp.R
import ru.android.hikanumaruapp.databinding.FragmentReaderBinding
import ru.android.hikanumaruapp.data.model.Chapter
import ru.android.hikanumaruapp.presentasion.reader.ViewTapConst.LEFT_REGION
import ru.android.hikanumaruapp.presentasion.reader.ViewTapConst.RIGHT_REGION
import ru.android.hikanumaruapp.presentasion.reader.chapterList.ChapterListAdapter
import ru.android.hikanumaruapp.presentasion.reader.model.ReaderChapter
import ru.android.hikanumaruapp.presentasion.reader.model.ReaderChapterPage
import ru.android.hikanumaruapp.presentasion.reader.viewer.SimpleSeekBarListener
import ru.android.hikanumaruapp.presentasion.reader.viewer.pager.ReaderPagerConst
import ru.android.hikanumaruapp.presentasion.reader.viewer.pager.ReaderPagerConst.Companion.READ_MODE_BOTTOM
import ru.android.hikanumaruapp.presentasion.reader.viewer.pager.ReaderPagerConst.Companion.READ_MODE_LEFT
import ru.android.hikanumaruapp.presentasion.reader.viewer.pager.ReaderPagerConst.Companion.READ_MODE_RIGHT
import ru.android.hikanumaruapp.presentasion.reader.viewer.pager.ViewPager2ReaderAdapter
import ru.android.hikanumaruapp.utilits.recyclerviews.RecyclerViewClickListener
import java.lang.reflect.Type
import kotlin.math.abs


@AndroidEntryPoint
class ReaderFragment : Fragment(), RecyclerViewClickListener {

    private var _binding: FragmentReaderBinding? = null
    private val binding get() = _binding!!
    private val vm by viewModels<ReaderViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentReaderBinding.inflate(inflater, container, false)
        return binding.root
    }

    private var urlChapter: String = ""
    private var titleChapter: String = ""
    private var typeManga = 0
    private var countChapters = ""
    //private var isReversedList = false
    private var urlPage = ""
    private lateinit var chapterList: MutableList<Chapter>

    private var readModeType = 0
    private var  isNavigateRevesed = false

    private var  isChapterReload = false

    lateinit var mOverScrollFrame: FrameLayout
    lateinit var mOverScrollArrow: ImageView
    lateinit var mOverScrollText: TextView

    private lateinit var readerPager: ViewPager2
    private lateinit var readerPagerAdapter: ViewPager2ReaderAdapter

    private var chapterAdapter = ChapterListAdapter(this)

    private lateinit var chapter: ReaderChapter
    private var loadNewChapter: Boolean = false
    private var isTypeLoadPagesReader: Boolean = false

    private lateinit var pageSeekBar: SeekBar
    private var currentPage: Int? = 0
    private var maxCountSeekBar: Int = 1

    init { onBack() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().findViewById<BottomNavigationView>(R.id.nav_view).visibility = View.GONE

        urlChapter = arguments?.getString("url").toString()
        titleChapter = arguments?.getString("title").toString()
        typeManga = arguments?.getInt("type")!!.toInt()
        countChapters = arguments?.getString("count").toString()
        urlPage = arguments?.getString("urlPage").toString()

        val str: String? = arguments?.getString("list")

        val collectionType: Type = object : TypeToken<Collection<Chapter?>?>() {}.type
        val enums: Collection<Chapter> = Gson().fromJson(str, collectionType)
        chapterList = enums.toMutableList()

        vm.getDataChapter(urlChapter)
        observeList()
        loadConfig()
//        notifyDataSetChanged()
        initUI()
        //toggleMenu()
    }

    private fun observeList() {
        vm.chapterList.observe(viewLifecycleOwner, Observer { listPage ->
            Log.d("ListT2d2", "Load 3 - $listPage")
            loadChapterList(listPage)
        })
        vm.loadChapter.observe(viewLifecycleOwner, Observer { listPage ->
            Log.d("ListT2d2", "Load 0 - $listPage")
            loadPagesReader(listPage)
        })
        vm.preloadChapterPrev.observe(viewLifecycleOwner, Observer { listPage ->
            Log.d("ListT2d2", "Load 1 - $listPage")
            when (!isNavigateRevesed) {
                ReaderPagerConst.IS_REVERSE_TRUE -> loadPagesReader(listPage,listPage.pages!!.lastIndex)
                ReaderPagerConst.IS_REVERSE_FALSE -> loadPagesReader(listPage)
            }
        })
        vm.preloadChapterNext.observe(viewLifecycleOwner, Observer { listPage ->
            Log.d("ListT2d2", "Load 2 - $listPage")
            when (!isNavigateRevesed) {
                ReaderPagerConst.IS_REVERSE_TRUE -> loadPagesReader(listPage)
                ReaderPagerConst.IS_REVERSE_FALSE -> loadPagesReader(listPage,listPage.pages!!.lastIndex)
            }
        })
    }

    private fun loadChapterList(listPage: MutableList<Chapter>) {
        isChapterReload = false
        chapterList = listPage!!
        chapterAdapter.setMain(listPage!!.toList())
        chapterAdapter.setSelect(chapter.idChapter)
        binding.progressLoadingChapterList.visibility = View.GONE
    }

    @SuppressLint("SetTextI18n")
    private fun loadPagesReader(listPage: ReaderChapter,set:Int = 0) {
        val isFirst = chapterAdapter.itemCount == 0
        chapter = (listPage as ReaderChapter)
        readerPagerAdapter.setChapters(chapter.pages!!)
        readerPager.currentItem = set

        if (!isFirst)
            chapterAdapter.setSelect(chapter.idChapter)

        requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)

        binding.tvTitleChapterNumber.text =  ("${getString(R.string.chapter_number)} ${chapter.number}")
        binding.ivBackLoaderReader.visibility = GONE
    }

    // todo rework
    private fun loadConfig(){
        //val mConfig:SharedPreferences = getSharedPreferences("readerConfig", Context.MODE_PRIVATE)
        //
        //if(mConfig.contains("readerConfig")) {
        //    readModeType = mConfig.getInt("viewpager_read_mode", 0).toInt() ?: 0
        //}

        readModeType = 0

        if(readModeType == 1)
            isNavigateRevesed = true
    }

    private fun initUI(){
        initializeMenu()
        menuListeners()
    }

    private fun initializeMenu() {
        // Frame OverScrolling
        mOverScrollFrame = binding.overscrollFrameReader
        mOverScrollArrow = binding.ivArrowReader
        mOverScrollText = binding.textViewTitleReader

        // Init Chapters List
        initChaptersList()
        if(chapterList.size != (countChapters.toInt()))
            reLoadChapter()

        // Set ReadMode Type
        readModeType = when(typeManga){
            MangaConst.MANGA_TYPE -> READ_MODE_RIGHT
            MangaConst.MANHVA_TYPE -> READ_MODE_RIGHT
            MangaConst.MANHUYA_TYPE -> READ_MODE_RIGHT
            MangaConst.WEB_TYPE -> READ_MODE_RIGHT
            MangaConst.COMICS_TYPE -> READ_MODE_RIGHT
            MangaConst.OTHER_LOCAL_TYPE -> READ_MODE_RIGHT
            else->{READ_MODE_RIGHT}
        }
        menuSelectReadModeChangeView()
        binding.readerMenu.visibility = View.GONE

        initViewPagerReader()
    }

    private fun initViewPagerReader(){
        //ViewPager2
        readerPager = binding.viewPagerReader
        readerPagerAdapter = ViewPager2ReaderAdapter(
            context = requireActivity(),
            vm = vm,
            fragmentManager = parentFragmentManager,
            lifecycle = lifecycle,
            onPageTapListener = onPageTapListener())

        readerPager.apply {
            orientation = ViewPager2.ORIENTATION_HORIZONTAL
            //offscreenPageLimit = 2
            adapter = readerPagerAdapter
        }

        viewPager2SetRegisterOnPageChangeCallback(readerPager)
        // WARN: we use knowledge of internal structure of ViewPager2 to setup overscroll behavior
        val decor : IOverScrollDecor? = readerPager.children.filterIsInstance<RecyclerView>().firstOrNull()?.let {
            OverScrollDecoratorHelper.setUpOverScroll(it, ORIENTATION_HORIZONTAL)
        }
        decorSetOverScrollStateListener(decor)

        initSeekBar()
    }

    private fun initChaptersList(){
        binding.readerChapterList.setOnClickListener(){}

        val rvChapters = binding.rvChapterListReaderMenu
        rvChapters.apply {
            adapter = chapterAdapter
        }

        if (isChapterReload) {
            chapterAdapter.setMain(chapterList)
            chapterAdapter.setSelect(chapter.idChapter)
            binding.rvChapterListReaderMenu.scrollToPosition(chapterAdapter.posLastChapter)
        }

        binding.tvBtnCloseChapterListReader.setOnClickListener{
            binding.readerChapterList.visibility = GONE
        }
        binding.llBntSortChapterListReader.setOnClickListener{
            chapterAdapter.reversePages()
            binding.rvChapterListReaderMenu.scrollToPosition(chapterAdapter.posLastChapter)
        }
    }

    // todo rework
    private fun initSeekBar() {
        pageSeekBar = binding.readerSeekBar
        maxCountSeekBar = readerPagerAdapter.dataSetPage.size
        currentPage = readerPager.currentItem.toInt()

        currentProgressSeekBar()
        pageSeekBar.setOnSeekBarChangeListener(
            object : SimpleSeekBarListener() {
                @SuppressLint("SetTextI18n")
                override fun onProgressChanged(seekBar: SeekBar, value: Int, fromUser: Boolean) {
                    if (readerPager != null && fromUser) {
                        binding.tvCurrentPageSeekbar.text = (1 + value).toString()
                        readerPager.currentItem = value
                    }
                }
            }
        )
    }

    private fun currentProgressSeekBar() {
        val count = readerPagerAdapter.dataSetPage.size
        val curr = readerPager.currentItem+1
        pageSeekBar.max = count-1
        pageSeekBar.progress = curr ?: 1
        binding.tvCurrentPageSeekbar.text = curr.toString()
        binding.tvMaxPageSeekbar.text = count.toString()
    }

    private fun decorSetOverScrollStateListener(decor: IOverScrollDecor?) {
        decor!!.setOverScrollStateListener(object :
            ru.android.hikanumaruapp.presentasion.reader.viewer.oversscrool.IOverScrollStateListener,
            me.everything.android.ui.overscroll.IOverScrollStateListener {

            override fun onOverScrollStateChange(
                decor: ru.android.hikanumaruapp.presentasion.reader.viewer.oversscrool.IOverScrollDecor?,
                oldState: Int,
                newState: Int,
            ) {}

            override fun onOverScrollStateChange(
                decor: IOverScrollDecor?,
                oldState: Int,
                newState: Int,
            ) {
                when (newState) {
                    IOverScrollState.STATE_IDLE -> mOverScrollFrame.visibility = GONE
                    IOverScrollState.STATE_DRAG_START_SIDE -> {
                        when (!isNavigateRevesed) {
                            ReaderPagerConst.IS_REVERSE_TRUE -> onFrameOverScrollTransform(
                                ReaderPagerConst.PREV)
                            ReaderPagerConst.IS_REVERSE_FALSE -> onFrameOverScrollTransform(
                                ReaderPagerConst.NEXT)
                        }
                    }
                    IOverScrollState.STATE_DRAG_END_SIDE -> {
                        when (!isNavigateRevesed) {
                            ReaderPagerConst.IS_REVERSE_TRUE -> onFrameOverScrollTransform(
                                ReaderPagerConst.PREV)
                            ReaderPagerConst.IS_REVERSE_FALSE -> onFrameOverScrollTransform(
                                ReaderPagerConst.NEXT)
                        }
                    }
                    IOverScrollState.STATE_BOUNCE_BACK -> if (oldState == IOverScrollState.STATE_DRAG_START_SIDE) {
                        when (!isNavigateRevesed) {
                            ReaderPagerConst.IS_REVERSE_TRUE -> preloadPrevOver()
                            ReaderPagerConst.IS_REVERSE_FALSE -> preloadNextOver()
                        }
                        mOverScrollFrame.visibility = GONE
                        // Dragging stopped -- view is starting to bounce back from the *left-end* onto natural position.
                    }
                    else {
                        when (!isNavigateRevesed) {
                            ReaderPagerConst.IS_REVERSE_TRUE -> preloadNextOver()
                            ReaderPagerConst.IS_REVERSE_FALSE -> preloadPrevOver()
                        }
                        mOverScrollFrame.visibility = GONE
                        // i.e. (oldState == STATE_DRAG_END_SIDE)
                        // View is starting to bounce back from the *right-end*.
                    }
                }
            }
        })

        decor!!.setOverScrollUpdateListener { decor, state, offset ->
            var view = decor!!.view;
            // \|/ onOverScrollFlying(0,offset)
            mOverScrollFrame.alpha = abs(offset / 50f)
        }
    }

    // todo rework
    @SuppressLint("SetTextI18n")
    private fun onFrameOverScrollTransform(type: Int) {
        mOverScrollFrame.alpha = 0f
        mOverScrollFrame.visibility = VISIBLE

        when (type) {
            ReaderPagerConst.PREV -> {
                if (chapter.linkPagePrev == MangaConst.START_CHAPTER) {
                    mOverScrollFrame.visibility = GONE
                } else {
                    val tom:String = chapter.pageNextId!!.substringBefore(' ')
                    val num:String = chapter.pageNextId!!.substringAfter(' ')
                    mOverScrollText.text = getString(R.string.prev_chapter_reader_trans,
                        tom,num,chapter.pagePrevTitle.toString())
                    mOverScrollArrow.rotation = when(readModeType){
                            READ_MODE_RIGHT ->  180f
                            READ_MODE_LEFT ->  0f
                            READ_MODE_BOTTOM ->  90f
                        else -> 0f
                    }
                }
            }
            ReaderPagerConst.NEXT -> {
                if (chapter.linkPageNext == MangaConst.FINISH_CHAPTER) {
                    mOverScrollFrame.visibility = GONE
                } else {
                    val tom:String = chapter.pageNextId!!.substringBefore(' ')
                    val num:String = chapter.pageNextId!!.substringAfter(' ')
                    mOverScrollText.text = getString(R.string.next_chapter_reader_trans,
                        tom,num,chapter.pageNextTitle.toString())
                    mOverScrollArrow.rotation = when(readModeType){
                        READ_MODE_RIGHT ->  0f
                        READ_MODE_LEFT ->  180f
                        READ_MODE_BOTTOM ->  90f
                        else -> 0f
                    }
                }
            }
        }
    }

    private fun preloadPrevOver() {
        when (chapter.linkPagePrev) {
            MangaConst.START_CHAPTER -> {
                // Todo toast
                Toast.makeText(
                    requireActivity(), getString(R.string.toast_reader_start_page),
                    Toast.LENGTH_SHORT
                ).show()
            }
            null -> {
                // Todo toast
                Toast.makeText(
                    requireActivity(), getString(R.string.toast_reader_start_page),
                    Toast.LENGTH_SHORT
                ).show()
            }
            else -> {
                if (vm.checkLoadChapterPrev) {
                    vm.getDataChapterPrev(chapter.linkPagePrev!!)

                    requireActivity().window.addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                    // Затенение при переходе
                    binding.ivBackLoaderReader.visibility = VISIBLE
                    //lrLoader.visibility = VISIBLE
                    //lrErrorLoader.visibility = GONE
                }
            }
        }
    }

    private fun preloadNextOver() {
        if (chapter.linkPageNext?.substringAfterLast('/') ?: "" == "finish")
            chapter.linkPageNext = "finish"

        when (chapter.linkPageNext) {
            MangaConst.FINISH_CHAPTER -> {
                // Todo toast
                Toast.makeText(
                    requireActivity(), getString(R.string.toast_reader_last_page),
                    Toast.LENGTH_SHORT
                ).show()
            }
            null -> {
                // Todo toast
                Toast.makeText(
                    requireActivity(), getString(R.string.toast_reader_last_page),
                    Toast.LENGTH_SHORT
                ).show()
            }
            else -> {
                if (vm.checkLoadChapterNext) {
                    vm.getDataChapterNext(chapter.linkPageNext!!)

                    requireActivity().window.addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                    // Затенение при переходе
                    binding.ivBackLoaderReader.visibility = VISIBLE
                    //lrLoader.visibility = VISIBLE
                    //lrErrorLoader.visibility = GONE
                }
            }
        }
    }

    // todo rework
    private fun onPageTapListener(): OnViewTapListener {
        return OnViewTapListener { view, x, y ->
            when {
                x < readerPager.width * LEFT_REGION -> {
                    readerPager.currentItem = readerPager.currentItem - 1

                }
                x > readerPager.width * RIGHT_REGION -> {
                    readerPager.currentItem = readerPager.currentItem + 1
                    //readerPager.currentItem = when (!isNavigateRevesed) {
                    //    ReaderPagerConst.IS_REVERSE_TRUE -> readerPager.currentItem + 1
                    //    ReaderPagerConst.IS_REVERSE_FALSE -> readerPager.currentItem - 1
                    //    else -> readerPager.currentItem
                    //}
                }
                x > readerPager.width * LEFT_REGION &&
                        x < readerPager.width * RIGHT_REGION -> {
                    toggleMenu()
                }
                else -> {
                    //Todo: Error
                }
            }
        }
    }

    private fun viewPager2SetRegisterOnPageChangeCallback(viewPager2: ViewPager2) {
        viewPager2.registerOnPageChangeCallback(
            object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    currentProgressSeekBar()
                    val page = readerPagerAdapter.dataSetPage.getOrNull(position)
                    if (page != null) {
                        readerPagerAdapter.onPageSelected(page, position)
                    }
                }
                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int,
                ) {}
                override fun onPageScrollStateChanged(state: Int) {}
            })
    }

    private fun reLoadChapter(){
        isChapterReload = true
        vm.reloadChapterList(urlChapter)
    }

    private fun menuChapterList (){
        binding.readerChapterList.visibility =
            when(binding.readerChapterList.visibility){
                VISIBLE -> GONE
                GONE -> VISIBLE
                else -> GONE
            }
    }

    private fun toggleMenu() {
        val menu = binding.readerMenu
        val readerView = binding.readerContainerView

        // todo add anim select item menu
        //ShowAnim().collapse(rl_read_mode_menu_layout)
        //ShowAnim().collapse(rl_translators_menu_layout)

        when (menu.visibility) {
            VISIBLE ->{
                menu.visibility = GONE
                closeAllInsideMenu()

               //val params = readerView.layoutParams
               //params.width = FrameLayout.LayoutParams.MATCH_PARENT
               //params.height = FrameLayout.LayoutParams.MATCH_PARENT
               //readerView.setPadding(0)
               //readerView.layoutParams = params
            }
            GONE ->{
                menu.visibility = VISIBLE
                closeAllInsideMenu()

                //val lp = FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, 0)
                //lp.setMargins(40,0,40,0)
                //readerView.setPadding(10)
                //readerView.layoutParams = lp
            }
            INVISIBLE -> {
                menu.visibility = VISIBLE
                closeAllInsideMenu()
            }
        }
    }

    private fun closeAllInsideMenu(){
        binding.llTranslatorsMenuLayout.visibility = GONE
        binding.llReadModeMenuLayout.visibility = GONE
        binding.readerChapterList.visibility = GONE
        binding.scrollLlReaderMenuSettings.visibility = GONE
    }

    private fun menuListeners(){
        // Top
        // ImageBack
        binding.ivArrowBackMenu.setOnClickListener{
           parentFragmentManager.popBackStack()
        }
        // ChaptersList
        binding.ivMoreChapterMenu.setOnClickListener{
            closeAllInsideMenu()
            menuChapterList()
        }

        // Bottom
        // Settings
        binding.ivBtnSettingsMenu.setOnClickListener{
            if(binding.scrollLlReaderMenuSettings.visibility == GONE)
                closeAllInsideMenu()
            menuVisibilitySwitch(binding.scrollLlReaderMenuSettings)
        }
        // Downloader
        binding.ivBtnDownloaderMenu.setOnClickListener{
            closeAllInsideMenu()
            menuDownloadImage()
        }
        // Translaters
        binding.ivBtnTranslateMenu.setOnClickListener{
            if(binding.llTranslatorsMenuLayout.visibility == GONE)
                closeAllInsideMenu()
            menuVisibilitySwitch(binding.llTranslatorsMenuLayout)
        }
        // Read Mode
        binding.ivBtnChangeModeMenu.setOnClickListener{
            if(binding.llReadModeMenuLayout.visibility == GONE)
                closeAllInsideMenu()
            menuVisibilitySwitch(binding.llReadModeMenuLayout)
        }
        binding.ivBtnRightNav.setOnClickListener{
            changeModeReader(READ_MODE_RIGHT)
        }
        binding.ivBtnLeftNav.setOnClickListener{
            changeModeReader(READ_MODE_LEFT)
        }
        binding.ivBtnBottomNav.setOnClickListener{
            changeModeReader(READ_MODE_BOTTOM)
        }
        // AutoPlay
        binding.ivBtnAutoplayMenu.setOnClickListener{
            closeAllInsideMenu()
            // todo autoplay
        }
    }

    private fun changeModeReader(type:Int) {
        isNavigateRevesed = when (type) {
            READ_MODE_RIGHT -> {
                if (isNavigateRevesed) {
                    chapter.pages!!.reverse()
                    readerPagerAdapter.reversePages()
                    readModeType = READ_MODE_RIGHT
                    menuSelectReadModeChangeView()
                    false
                } else true
            }
            READ_MODE_LEFT -> {
                if (!isNavigateRevesed) {
                    chapter.pages!!.reverse()
                    readerPagerAdapter.reversePages()
                    readModeType = READ_MODE_LEFT
                    menuSelectReadModeChangeView()
                    true
                } else false
            }
            READ_MODE_BOTTOM -> {
                // todo toast
                false
            }
            else -> false
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun menuSelectReadModeChangeView (){
        menuSelectReadModeClearView()
        when(readModeType){
            READ_MODE_RIGHT->
                binding.ivBtnRightNav.setImageDrawable(
                    resources.getDrawable(R.drawable.ic_reader_mode_right_reader_menu_active))
            READ_MODE_LEFT->
                binding.ivBtnLeftNav.setImageDrawable(
                    resources.getDrawable(R.drawable.ic_reader_mode_left_reader_menu_active))
            READ_MODE_BOTTOM->
                binding.ivBtnBottomNav.setImageDrawable(
                    resources.getDrawable(R.drawable.ic_reader_mode_vertical_reader_menu_active))
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun menuSelectReadModeClearView() {
        binding.ivBtnRightNav.setImageDrawable(
            resources.getDrawable(R.drawable.ic_reader_mode_right_reader_menu))
        binding.ivBtnLeftNav.setImageDrawable(
            resources.getDrawable(R.drawable.ic_reader_mode_left_reader_menu))
        binding.ivBtnBottomNav.setImageDrawable(
            resources.getDrawable(R.drawable.ic_reader_mode_vertical_reader_menu))
    }

    private fun menuDownloadImage () {
        val url: ReaderChapterPage? = readerPagerAdapter.dataSetPage.getOrNull(readerPager.currentItem)
        vm.saveImageChapterInLocal(requireActivity(),
            url!!.linkImage, readerPager.currentItem,binding.ivBtnDownloaderMenu)
    }

    private fun menuVisibilitySwitch(layout:LinearLayout){
        layout.visibility =
            when(layout.visibility){
            VISIBLE -> GONE
            GONE -> VISIBLE
            else -> GONE
        }
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
        requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        requireActivity().findViewById<BottomNavigationView>(R.id.nav_view).visibility = View.VISIBLE
        _binding = null
    }

    override fun onRecyclerViewItemClick(view: View, list: Any?) {
        when(view.id){
            R.id.cc_main_block_chapter_reader ->{
                val chapter = list as Chapter
                    if (!chapter.url.isNullOrEmpty()) {
                        closeAllInsideMenu()
                        loadNewChapter = true
                        requireActivity().window.addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                        // Затенение при переходе
                        binding.ivBackLoaderReader.visibility = VISIBLE
                        vm.getDataChapter(chapter.url)
                    }else{
                        Toast.makeText(requireActivity(),"Ссылка не найдена",Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }


}