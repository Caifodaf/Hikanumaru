package ru.android.hikanumaruapp.ui.reader

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.view.View.*
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.widget.*
import androidx.core.view.children
import androidx.core.view.get
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
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
import ru.android.hikanumaruapp.model.Chapter
import ru.android.hikanumaruapp.ui.reader.MangaConst.COMICS_TYPE
import ru.android.hikanumaruapp.ui.reader.MangaConst.MANGA_TYPE
import ru.android.hikanumaruapp.ui.reader.MangaConst.MANHUYA_TYPE
import ru.android.hikanumaruapp.ui.reader.MangaConst.MANHVA_TYPE
import ru.android.hikanumaruapp.ui.reader.MangaConst.OTHER_LOCAL_TYPE
import ru.android.hikanumaruapp.ui.reader.MangaConst.WEB_TYPE
import ru.android.hikanumaruapp.ui.reader.ViewTapConst.LEFT_REGION
import ru.android.hikanumaruapp.ui.reader.ViewTapConst.RIGHT_REGION
import ru.android.hikanumaruapp.ui.reader.model.ReaderChapter
import ru.android.hikanumaruapp.ui.reader.model.ReaderChapterPage
import ru.android.hikanumaruapp.ui.reader.model.TransItem
import ru.android.hikanumaruapp.ui.reader.viewer.SimpleSeekBarListener
import ru.android.hikanumaruapp.ui.reader.viewer.pager.MoveViewTouchListener
import ru.android.hikanumaruapp.ui.reader.viewer.pager.ViewPager2ReaderAdapter
import ru.android.hikanumaruapp.ui.reader.viewer.pager.pages.ImagePagePagerFragment
import java.lang.reflect.Type
import kotlin.math.abs

@AndroidEntryPoint
class ReaderFragment : Fragment() {

    private var _binding: FragmentReaderBinding? = null
    private val binding get() = _binding!!
    private val vm by viewModels<ReaderViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
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

    lateinit var mOverScrollFrame: FrameLayout
    lateinit var mOverScrollArrow: ImageView
    lateinit var mOverScrollText: TextView

    private lateinit var readerPager: ViewPager2
    private lateinit var readerPagerAdapter: ViewPager2ReaderAdapter

    private lateinit var chapter: ReaderChapter
    private var loadNewChapter: Boolean = false
    private var isTypeLoadPagesReader: Boolean = false

    private lateinit var pageSeekBar: SeekBar
    private var currentPage: Int? = 0
    private var maxCountSeekBar: Int = 1

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

        initUI()
        //toggleMenu()
    }

    private fun observeList() {
        vm.loadChapter.observe(viewLifecycleOwner, Observer { listPage ->
            Log.d("ListT2d2", "Load 0 - $listPage")
            loadPagesReader()
        })
    }

    // todo rework
    @SuppressLint("SetTextI18n")
    private fun loadPagesReader () {
        chapter = (vm.loadChapter.value as ReaderChapter)
        readerPagerAdapter.setChapters(chapter.pages!!)
        readerPager.currentItem = 1

        binding.tvTitleChapterNumber.text =  ("${getString(R.string.chapter_number)} ${chapter.number}")
        binding.ivBackLoaderReader.visibility = GONE
        //lrLoader.visibility = GONE
        //lrErrorLoader.visibility = GONE

//        if(!isNavigateRevesed)
//            readerPager.currentItem = 0
//        else
//            readerPager.currentItem = loadPages[0].pages!!.size-1
        //requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)

        //if (!isTypeLoadPagesReader){
        //    if(!isNavigateRevesed)
        //        readerPager.currentItem = loadPages[0].pages!!.size-1
        //    else
        //        readerPager.currentItem = 0
        //}
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

    // todo rework
    private fun initUI(){
        initializeMenu()
        menuListeners()
    }

    // todo rework
    private fun initializeMenu() {
        // todo for chapter dialog
        //dialogInterface = cDialogInterface()

        // Frame OverScrolling
        mOverScrollFrame = binding.overscrollFrameReader
        mOverScrollArrow = binding.ivArrowBackMenu
        mOverScrollText = binding.textViewTitleReader

        // List chapters todo
        //ivBackDecor = findViewById(R.id.iv_back_loader_reader)
        //lrLoader = findViewById(R.id.text_loading_reader)
        //lrErrorLoader = findViewById(R.id.text_error_reader)
        //tv_title_chapter.text = titleChapter

        // TODO rework
        if(chapterList.size != (countChapters.toInt()))
            reLoadChapter()

        // todo rework
        readModeType = when(typeManga){
            //0-Manga 1-Manhva 2-Manhuya 3-Web 4-Comick 5-ru
            MANGA_TYPE->0
            MANHVA_TYPE->0
            MANHUYA_TYPE->0
            WEB_TYPE->0
            COMICS_TYPE->0
            OTHER_LOCAL_TYPE->0
            else->{0}
        }

        menuSelectMode(readModeType)
        binding.readerMenu.visibility = View.GONE
        menuListeners()

        // todo add to error page
        //btnReloadPage()

        initViewPager2()
    }

    // todo rework
    private fun initViewPager2(){
        //ViewPager2
        readerPager = binding.viewPagerReader
        readerPagerAdapter = ViewPager2ReaderAdapter(
            context = this,
            vm = vm,
            fragmentManager = parentFragmentManager,
            lifecycle = lifecycle,
            onPageTapListener = onPageTapListener())

        readerPager.apply {
            orientation = ViewPager2.ORIENTATION_HORIZONTAL
            offscreenPageLimit = 2
            adapter = readerPagerAdapter
        }

        viewPager2SetRegisterOnPageChangeCallback(readerPager)
        // WARN: we use knowledge of internal structure of ViewPager2 to setup overscroll behavior
        val decor : IOverScrollDecor? = readerPager.children.filterIsInstance<RecyclerView>().firstOrNull()?.let {
            OverScrollDecoratorHelper.setUpOverScroll(it, ORIENTATION_HORIZONTAL)
        }
        decorSetOverScrollStateListener(decor)

        initSeekBar()

        initTouhcEvent()

    }


    private fun initTouhcEvent() {
//        binding.viewPagerReader.getChildAt(0).setOnTouchListener(this)
        binding.viewPagerReader.
        setOnTouchListener(object: MoveViewTouchListener(binding.viewPagerReader){

        })
    }
//
    //@SuppressLint("ClickableViewAccessibility")
    //override fun onTouch(view: View?, motionEvent: MotionEvent): Boolean {
    //    val y = motionEvent.y
    //    val x = motionEvent.x
//
    //    Log.d("dadawdmove","motion1 - ${motionEvent.action}")
    //    Log.d("dadawdmove","motion1 - ${motionEvent.actionMasked}")
    //    Log.d("dadawdmove","motion2 - ${motionEvent}")
    //    if (motionEvent.actionMasked == MotionEvent.ACTION_DOWN)
    //    when {
    //        x < readerPager.width * LEFT_REGION -> {
    //            moveToPrevious()
    //        }
    //        x > readerPager.width * RIGHT_REGION -> {
    //            moveToNext()
    //        }
    //        x > readerPager.width * LEFT_REGION && x < readerPager.width * RIGHT_REGION -> {
    //            toggleMenu()
    //        }
    //        else -> {
    //            //Todo: Error
    //            //toggleMenu()
    //        }
    //    }
//
    //    return false
    //}

//    Log.d("dadawdmove","view - ${view}")
//    Log.d("dadawdmove","motion - ${motion}")
//    Log.d("dadawdmove","motion1 - ${motion.action}")
//    Log.d("dadawdmove","motion2 - ${motion.rawX}")
//    Log.d("dadawdmove","motion3 - ${motion.rawY}")
//    Log.d("dadawdmove","motion4 - ${motion.x}")
//    Log.d("dadawdmove","motion5 - ${motion.y}")
//    Log.d("dadawdmove","motion6 - ${motion.size}")
//    Log.d("dadawdmove","motion7 - ${motion.downTime}")
//    Log.d("dadawdmove","motion8 - ${motion.orientation}")

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

    // todo rework
    private fun currentProgressSeekBar() {
        val count = readerPagerAdapter.dataSetPage.size
        val curr = readerPager.currentItem+1
        pageSeekBar.max = count-1
        pageSeekBar.progress = curr ?: 1
        binding.tvCurrentPageSeekbar.text = curr.toString()
        binding.tvMaxPageSeekbar.text = count.toString()
    }

    // todo rework
    private fun decorSetOverScrollStateListener(decor: IOverScrollDecor?) {
        decor!!.setOverScrollStateListener(object :
            ru.android.hikanumaruapp.ui.reader.viewer.oversscrool.IOverScrollStateListener,
            me.everything.android.ui.overscroll.IOverScrollStateListener {

            override fun onOverScrollStateChange(
                decor: ru.android.hikanumaruapp.ui.reader.viewer.oversscrool.IOverScrollDecor?,
                oldState: Int,
                newState: Int
            ) {}

            override fun onOverScrollStateChange(
                decor: IOverScrollDecor?,
                oldState: Int,
                newState: Int
            ) {
                var chapterLast: Boolean = false

                when (newState) {
                    IOverScrollState.STATE_IDLE -> {
                        Log.d("dadadadwa","OVERSCROLL:: idle")
                        mOverScrollFrame.visibility = GONE
                    }
                    IOverScrollState.STATE_DRAG_START_SIDE -> {
                        Log.d("dadadadwa","OVERSCROLL:: start side")
                        if(!isNavigateRevesed)
                            onFrameOverScrollTransform("PREV")
                        else
                            onFrameOverScrollTransform("NEXT")
                    }
                    IOverScrollState.STATE_DRAG_END_SIDE -> {
                        Log.d("dadadadwa","OVERSCROLL:: end side")
                        if(!isNavigateRevesed)
                            onFrameOverScrollTransform("NEXT")
                        else
                            onFrameOverScrollTransform("PREV")
                    }
                    IOverScrollState.STATE_BOUNCE_BACK -> if (oldState == IOverScrollState.STATE_DRAG_START_SIDE) {
                        Log.d("dadadadwa","OVERSCROLL:: drag stop")
                        //if(!isNavigateRevesed)
                        //    preloadPrevOver()
                        //else
                        //    preloadNextOver()

                        mOverScrollFrame.visibility = GONE
                        // Dragging stopped -- view is starting to bounce back from the *left-end* onto natural position.
                    } else {
                        //if(!isNavigateRevesed)
                        //    preloadNextOver()
                        //else
                        //    preloadPrevOver()

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
    private fun onFrameOverScrollTransform(type: String) {
        mOverScrollFrame.alpha = 0f
        mOverScrollFrame.visibility = VISIBLE

        when (type) {
            "PREV" -> {
                if (chapter.linkPagePrev == "Is start page") {
                    mOverScrollFrame.visibility = GONE
                } else {
                    mOverScrollText.text = "${chapter.pagePrevId} ${chapter.pagePrevTitle}"
                    mOverScrollArrow.rotation = 180f
                }
            }
            "NEXT" -> {
                if (chapter.linkPageNext == "finish") {
                    mOverScrollFrame.visibility = GONE
                } else {
                    mOverScrollText.text = "${chapter.pageNextId} ${chapter.pageNextTitle}"
                    mOverScrollArrow.rotation = 0f
                }
            }
        }
    }

    // todo rework
    private fun preloadPrevOver(){
        // Prev
        if(!loadNewChapter) {

            if (chapter.linkPagePrev == "Is start page") {
                Toast.makeText(
                    requireActivity(), getString(R.string.toast_reader_start_page),
                    Toast.LENGTH_SHORT
                ).show()
            } else {

                val prev = chapter.linkPagePrev
                if (prev != null) {
                    loadNewChapter = true

                    //requireActivity().window.addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                    // Затенение при переходе
                    binding.ivBackLoaderReader.visibility = VISIBLE
                    //lrLoader.visibility = VISIBLE
                    //lrErrorLoader.visibility = GONE

                    //chapter.clear()

                    isTypeLoadPagesReader = false
                    vm.getDataChapter(prev)
                }
            }
        }
    }

    // todo rework


    // todo rework
    private fun onPageTapListener(): OnViewTapListener {
        return OnViewTapListener { view, x, y ->
            when {
                x < readerPager.width * LEFT_REGION -> {
                    moveToPrevious()
                }
                x > readerPager.width * RIGHT_REGION -> {
                    moveToNext()
                }
                x > readerPager.width * LEFT_REGION &&
                        x < readerPager.width * RIGHT_REGION -> {
                    toggleMenu()
                }
                else -> {
                    //Todo: Error
                    //toggleMenu()
                }
            }
        }
    }

    // todo rework
    private fun moveToNext() {
        //if (viewPager2.childCount != viewPager2.currentItem)
        //if(!isNavigateRevesed)
        readerPager.currentItem = readerPager.currentItem + 1
        //else
        //viewPager2.currentItem = viewPager2.currentItem - 1
    }

    // todo rework
    private fun moveToPrevious() {
        //if (viewPager2.currentItem != 0)
        //if(!isNavigateRevesed)
        readerPager.currentItem = readerPager.currentItem - 1
        //else
        //    viewPager2.currentItem = viewPager2.currentItem + 1
    }

    // todo rework
    private fun viewPager2SetRegisterOnPageChangeCallback(viewPager2: ViewPager2) {
        viewPager2.registerOnPageChangeCallback(
            object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    when (val dataPos = readerPagerAdapter.dataSetPage[position]) {
                        is ReaderChapterPage -> {}
                        is TransItem -> {
                            when(dataPos.type){
                                0 -> {
                                    if (position == 0)
                                        preLoadPrev()
                                } //Prev
                                1 -> {
                                    if (position == readerPagerAdapter.dataSetPage.lastIndex)
                                        preLoadNext()
                                } //next
                            }
                        }
                    }
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
                ) {

                }

                @SuppressLint("SwitchIntDef")
                override fun onPageScrollStateChanged(state: Int) {
                    when(state){
                        AbsListView.OnScrollListener.SCROLL_STATE_IDLE -> {}
                        RecyclerView.SCROLL_STATE_DRAGGING -> {}
                        RecyclerView.SCROLL_STATE_SETTLING -> {}
                    }
                }
            })
    }

    private fun preLoadPrev(){
        vm.preloadChapterPrev.observe(viewLifecycleOwner, Observer { listPage ->
            Log.d("ListT2d2", "Load 10 - $listPage")
            val list = (listPage as ReaderChapter).pages!!.minus(listPage.pages!!.lastIndex)
            if(readerPagerAdapter.dataSetPage[1] != list[0]) {
                for (i in 0..list.size - 1) {
                    readerPagerAdapter.dataSetPage.add(i, list[i])
                }
            }
        })

        vm.getDataChapterPrev(chapter.linkPagePrev!!)
    }
    private fun preLoadNext(){
        vm.preloadChapterNext.observe(viewLifecycleOwner, Observer { listPage ->
            Log.d("ListT2d2", "Load 20 - $listPage")
            val list = (listPage as ReaderChapter).pages!!.minus(0)

            //for (i in 0..list.size-1){
            //    readerPagerAdapter.dataSetPage.add(list)
            //}
            readerPagerAdapter.notifyDataSetChanged()
        })

        vm.getDataChapterNext(chapter.linkPageNext!!)
    }

    private fun preloadNextOver(){
// Next
            if(chapter.linkPageNext?.substringAfterLast('/') ?: "" == "finish")
                chapter.linkPageNext = "finish"


            if (chapter.linkPageNext == "finish") {
                Toast.makeText(
                    requireActivity(), getString(R.string.toast_reader_last_page),
                    Toast.LENGTH_SHORT
                ).show()
            } else {

                val next = chapter.linkPageNext
                if (next != null) {
                    loadNewChapter = true

//                    ivBackDecor.visibility = VISIBLE
//                    lrLoader.visibility = GONE
//                    lrErrorLoader.visibility = VISIBLE

                    // loadPages.clear()

                    isTypeLoadPagesReader = true
                    vm.getDataChapter(next)
                }
            }
        }

    // TODO rework
    private fun reLoadChapter(){
        // todo add reload chapter
        //if(!isLoadChapter) {
        //    CoroutineScope(Dispatchers.IO).launch {
        //        getDataChapter()
        //    }
        //}
    }

    // TODO rework
    private fun menuSelectMode (type: Int){
        when(readModeType){
            0->{
                //readModeType=0
                readModeIconChange()
                //changeModeReader(0)
            }
            1->{
                //readModeType=1
                readModeIconChange()
                //changeModeReader(1)
            }
            2->{
                //readModeType=2
                readModeIconChange()
                //changeModeReader(2)
            }
            else->{
                //readModeType=0
                readModeIconChange()
                //changeModeReader(0)
            }
        }

        binding.llTranslatorsMenuLayout.visibility = if(binding.llTranslatorsMenuLayout.visibility == VISIBLE)
            GONE
        else{GONE}

        binding.llReadModeMenuLayout.visibility = if(binding.llReadModeMenuLayout.visibility == GONE)
            VISIBLE
        else
            GONE
    }

    // TODO rework add change read mode btn
    @SuppressLint("ResourceAsColor")
    private fun readModeIconChange() {
//        iv_btn_right_nav.imageTintList = getColorStateList(resources,R.color.line_chapter_item,null)
//        iv_btn_right_nav.backgroundTintList = getColorStateList(resources,R.color.black_back,null)
//        iv_btn_left_nav.imageTintList = getColorStateList(resources,R.color.line_chapter_item,null)
//        iv_btn_left_nav.backgroundTintList = getColorStateList(resources,R.color.black_back,null)
//        iv_btn_bottom_nav.imageTintList = getColorStateList(resources,R.color.line_chapter_item,null)
//        iv_btn_bottom_nav.backgroundTintList = getColorStateList(resources,R.color.black_back,null)

//        item.imageTintList = getColorStateList(resources,R.color.white,null)
//        item.backgroundTintList = getColorStateList(resources,R.color.black_back,null)

    }


    private fun toggleMenu() {
        val menu = binding.readerMenu

        // todo add anim select item menu
        //ShowAnim().collapse(rl_read_mode_menu_layout)
        //ShowAnim().collapse(rl_translators_menu_layout)

        when (menu.visibility) {
            VISIBLE -> menu.visibility = GONE
            GONE ->{
                menu.visibility = VISIBLE

                binding.llTranslatorsMenuLayout.visibility = GONE
                binding.llReadModeMenuLayout.visibility = GONE
                binding.readerChapterList.visibility = GONE
                binding.scrollLlReaderMenuSettings.visibility = GONE
            }
            INVISIBLE -> {
                menu.visibility = VISIBLE

                binding.llTranslatorsMenuLayout.visibility = GONE
                binding.llReadModeMenuLayout.visibility = GONE
                binding.readerChapterList.visibility = GONE
                binding.scrollLlReaderMenuSettings.visibility = GONE
            }
        }
    }

    private fun menuListeners(){
        // Top

        binding.ivArrowBackMenu.setOnClickListener{
           parentFragmentManager.popBackStack()
        }
        binding.ivMoreChapterMenu.setOnClickListener{
            // todo reader chapter
        }

        // Bottom
        binding.ivBtnSettingsMenu.setOnClickListener{
            menuVisibilitySwitch(binding.scrollLlReaderMenuSettings)
        }
        binding.ivBtnDownloaderMenu.setOnClickListener{
            menuDownloadImage()
        }
        binding.ivBtnTranslateMenu.setOnClickListener{
            menuVisibilitySwitch(binding.llTranslatorsMenuLayout)
        }
        binding.ivBtnChangeModeMenu.setOnClickListener{
            menuVisibilitySwitch(binding.llReadModeMenuLayout)
        }
        binding.ivBtnAutoplayMenu.setOnClickListener{
            // todo autoplay
        }
    }

    private fun menuDownloadImage () {
        val url: String = readerPagerAdapter.dataSetPage.getOrNull(readerPager.currentItem).toString()
        vm.saveImageChapterInLocal(requireActivity(),url, readerPager.currentItem,binding.ivBtnDownloaderMenu)
    }

    private fun menuVisibilitySwitch(layout:LinearLayout){
        layout.visibility =
            when(layout.visibility){
            VISIBLE -> GONE
            GONE -> VISIBLE
            else -> GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        requireActivity().findViewById<BottomNavigationView>(R.id.nav_view).visibility = View.VISIBLE
        _binding = null
    }



}