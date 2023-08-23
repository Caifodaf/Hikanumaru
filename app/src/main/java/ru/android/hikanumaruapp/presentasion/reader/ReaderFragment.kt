package ru.android.hikanumaruapp.presentasion.reader

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.View.*
import android.widget.*
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import ru.android.hikanumaruapp.R
import ru.android.hikanumaruapp.data.model.Chapter
import ru.android.hikanumaruapp.databinding.FragmentReaderBinding
import ru.android.hikanumaruapp.data.model.Manga
import ru.android.hikanumaruapp.data.model.reader.ReaderChapter
import ru.android.hikanumaruapp.presentasion.BaseInnerFragment
import ru.android.hikanumaruapp.presentasion.reader.chapterList.ChapterListAdapter
import ru.android.hikanumaruapp.presentasion.reader.data.ApplicationSettingsProvider
import ru.android.hikanumaruapp.presentasion.reader.data.NetworkResponse
import ru.android.hikanumaruapp.presentasion.reader.data.ParserViewModel
import ru.android.hikanumaruapp.presentasion.reader.viewer.init.BaseViewer
import ru.android.hikanumaruapp.presentasion.reader.viewer.init.ReaderCallback
import ru.android.hikanumaruapp.presentasion.reader.viewer.init.ReaderConfigViewModel
import ru.android.hikanumaruapp.presentasion.reader.viewer.pager.ReaderPagerConst.Companion.READ_MODE_BOTTOM
import ru.android.hikanumaruapp.presentasion.reader.viewer.pager.ReaderPagerConst.Companion.READ_MODE_LEFT
import ru.android.hikanumaruapp.presentasion.reader.viewer.pager.ReaderPagerConst.Companion.READ_MODE_RIGHT
import ru.android.hikanumaruapp.utilits.recyclerviews.RecyclerViewClickListener


@AndroidEntryPoint
class ReaderFragment : BaseInnerFragment(), RecyclerViewClickListener {

    data class mOverScrollBinding(
        var ltFrame: FrameLayout,
        val imArrow: ImageView,
        val tvText: TextView,
    )

    private var _binding: FragmentReaderBinding? = null
    private val binding get() = _binding!!
    private val vm by viewModels<ReaderViewModel>()
    private val vmConfig by activityViewModels<ReaderConfigViewModel>()
    private val vmParser by viewModels<ParserViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentReaderBinding.inflate(inflater, container, false)
        return binding.root
    }

    init { setupOnBackPressed() }

    private lateinit var settingsProvider: ApplicationSettingsProvider

    private lateinit var chapterData: ReaderChapter
    private lateinit var chapterAdapter: ChapterListAdapter

    private lateinit var mOverScroll: mOverScrollBinding

    private lateinit var viewer: BaseViewer

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().findViewById<BottomNavigationView>(R.id.nav_view).visibility = View.GONE

        settingsProvider = ApplicationSettingsProvider(requireContext())

        // Получение данных
        val page = arguments?.getString("page").toString()
        vm.currentChapterID = arguments?.getString("chapterID").toString()
        vm.creativeWorkPage =  Gson().fromJson(page, Manga::class.java)

        binding.apply {
            // Frame OverScrolling
            mOverScroll = mOverScrollBinding(overscrollFrameReader,ivArrowReader,textViewTitleReader)

            vmConfig.loadConfig() // ReadMode observe
            getChaptersPager()

            initPager()

            observeList()
            observeError()

            initializeMenu()
        }
    }

    private fun FragmentReaderBinding.initPager() {
        viewer = BaseViewer.builder(requireContext())
            .setView(view = viewPagerReader)
            .setSeekBarView(view = readerSeekBar, text = TVPagesSeekBar)
            .setOverScrollView(mOverScroll = mOverScroll)
            //.setChapterData(chapter = chapterData)
            .setConfig(readMode = vmConfig.readMode)
            .setReaderCallback(object : ReaderCallback() {
                override fun onToggleMenu() { toggleMenu() }
                override fun preloadPrev() { preloadPrevOver() }
                override fun preloadNext() { preloadNextOver()}
            })
            .build()
    }

    private fun preloadPrevOver() {
        when (chapterData.linkPagePrev) {
            MangaConst.START_CHAPTER -> {
                Toast.makeText(requireContext(), requireContext().getString(R.string.toast_reader_start_page), Toast.LENGTH_SHORT)
                    .show()
            }
            null -> {
                Toast.makeText(requireContext(), requireContext().getString(R.string.toast_reader_start_page), Toast.LENGTH_SHORT)
                    .show()
            }
            else -> {
                if (vm.checkLoadChapterPrev) {
                    vm.preloadDataChapterPrev(vmParser,chapterData.linkPagePrev!!,settingsProvider.source()!!)

                    requireActivity().window.addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                    // Затенение при переходе
                    binding.ivBackLoaderReader.visibility = View.VISIBLE
                    //lrLoader.visibility = VISIBLE
                    //lrErrorLoader.visibility = GONE
                }
            }
        }
    }

    private fun preloadNextOver() {
        if (chapterData.linkPageNext?.substringAfterLast('/') ?: "" == MangaConst.FINISH_CHAPTER)
            chapterData.linkPageNext = MangaConst.FINISH_CHAPTER

        when (chapterData.linkPageNext) {
            MangaConst.FINISH_CHAPTER -> {
                Toast.makeText(requireContext(), requireContext().getString(R.string.toast_reader_last_page), Toast.LENGTH_SHORT)
                    .show()
            }
            null -> {
                Toast.makeText(requireContext(), requireContext().getString(R.string.toast_reader_last_page), Toast.LENGTH_SHORT)
                    .show()
            }
            else -> {
                if (vm.checkLoadChapterNext) {
                    vm.preloadDataChapterNext(vmParser,chapterData.linkPageNext!!,settingsProvider.source()!!)

                    requireActivity().window.addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                    // Затенение при переходе
                    binding.ivBackLoaderReader.visibility = View.VISIBLE
                    //lrLoader.visibility = VISIBLE
                    //lrErrorLoader.visibility = GONE
                }
            }
        }
    }

    private fun toggleMenu() {
        binding.readerMenu.visibility = when(binding.readerMenu.visibility) {
            VISIBLE -> GONE
            GONE -> VISIBLE
            else -> GONE
        }
        closeAllInsideMenu()
    }

    private fun FragmentReaderBinding.initializeMenu() {
        menuListeners()
        // Init Chapters List
        initChaptersList()
        // Открытие меню при запуске
        readerMenu.visibility = View.VISIBLE


    }

    // Изменение кнопки для режима чтения
    // Добавить проверку для чтения
    @SuppressLint("UseCompatLoadingForDrawables")
    private fun FragmentReaderBinding.menuSelectReadModeView(modeType: Int) {
        changeReadModeReader(modeType)
        ivBtnRightNav.setImageResource(
            if (modeType == READ_MODE_RIGHT) R.drawable.ic_reader_mode_right_reader_menu_active
            else R.drawable.ic_reader_mode_right_reader_menu
        )
        ivBtnLeftNav.setImageResource(
            if (modeType == READ_MODE_LEFT) R.drawable.ic_reader_mode_left_reader_menu_active
            else R.drawable.ic_reader_mode_left_reader_menu
        )
        ivBtnBottomNav.setImageResource(
            if (modeType == READ_MODE_BOTTOM) R.drawable.ic_reader_mode_vertical_reader_menu_active
            else R.drawable.ic_reader_mode_vertical_reader_menu
        )
    }

    private fun changeReadModeReader(type:Int) {
        vmConfig.readMode = type
            when (type) {
            READ_MODE_RIGHT -> {
                if (type == READ_MODE_RIGHT) return
                chapterData.pages!!.reverse()
                viewer.reversePages()
            }
            READ_MODE_LEFT -> {
                if (type == READ_MODE_LEFT) return
                chapterData.pages!!.reverse()
                viewer.reversePages()
            }
            READ_MODE_BOTTOM -> {
                if (type == READ_MODE_BOTTOM) return
                // todo toast
                false
            }
        }
    }

    private fun FragmentReaderBinding.menuListeners(){
        // Top | ImageBack
        ivArrowBackMenu.setOnClickListener{ findNavController().navigateUp() }
        // Top | ChaptersList
        ivMoreChapterMenu.setOnClickListener{
            closeAllInsideMenu()
            readerChapterList.menuVisibilitySwitch()
        }
        // Bottom | Settings
        ivBtnSettingsMenu.setOnClickListener{
            if(LLSeattingsMenu.visibility == GONE) closeAllInsideMenu()
            LLSeattingsMenu.menuVisibilitySwitch()
        }
        // Bottom | Downloader
        ivBtnDownloaderMenu.setOnClickListener{
            closeAllInsideMenu()
            //menuDownloadImage()
        }
        // Bottom | Translaters
        ivBtnTranslateMenu.setOnClickListener{
            if(llTranslatorsMenuLayout.visibility == GONE) closeAllInsideMenu()
            llTranslatorsMenuLayout.menuVisibilitySwitch()
        }
        // Bottom | Read Mode
        ivBtnChangeModeMenu.setOnClickListener{
            if(llReadModeMenuLayout.visibility == GONE) closeAllInsideMenu()
            llReadModeMenuLayout.menuVisibilitySwitch()
        }
        ivBtnRightNav.setOnClickListener{ menuSelectReadModeView(READ_MODE_RIGHT) }
        ivBtnLeftNav.setOnClickListener{ menuSelectReadModeView(READ_MODE_LEFT) }
        ivBtnBottomNav.setOnClickListener{
            TODO("Add error")
            //menuSelectReadModeView(READ_MODE_BOTTOM)
        }
        // Bottom | AutoPlay
        ivBtnAutoplayMenu.setOnClickListener{
            closeAllInsideMenu()
            // todo autoplay
        }
    }

    private fun View.menuVisibilitySwitch(){
        this.visibility = if (this.visibility == View.VISIBLE) View.GONE else View.VISIBLE
    }

    // Top | ChaptersList
    // Bottom | Translaters | Read Mode | Settings | TODO Autoplay
    private fun closeAllInsideMenu() {
        binding.apply {
            val views = arrayOf(readerChapterList,
                llTranslatorsMenuLayout, llReadModeMenuLayout, LLSeattingsMenu)
            views.forEach { it.visibility = View.GONE }
        }
    }


    private fun FragmentReaderBinding.initChaptersList(){
        binding.readerChapterList.setOnClickListener(){}
        chapterAdapter = ChapterListAdapter(this@ReaderFragment)

        RVChapterList.adapter = chapterAdapter
        chapterAdapter.setMain(vm.creativeWorkPage.chapters!!)
        chapterAdapter.setSelect(vm.currentChapterID)
        RVChapterList.scrollToPosition(chapterAdapter.posLastChapter)

        // Настройка кнопок для chapterList меню
        TVBtnChapterListClose.setOnClickListener{ readerChapterList.visibility = GONE }
        LLBtnSortChapterList.setOnClickListener{
            chapterAdapter.reversePages()
            RVChapterList.scrollToPosition(chapterAdapter.posLastChapter)
        }

        // Скрыть прогрессбар после создания
        progressLoadingChapterList.visibility = View.GONE
    }

    private fun observeError() {
        vm.error.observe(viewLifecycleOwner, Observer { error ->
            Log.e("ErrorNetwork", "Reader observeError rdf ${error.code}: ${error.message}")
            when(error.code){
                -1 -> {}
                else -> {}
            }
        })
    }

    @SuppressLint("SetTextI18n")
    private fun observeList() {
        // ------------ CHAPTER ------------
        vmParser.chapterReader.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResponse.Failure -> {}
                is NetworkResponse.Loading -> {}
                is NetworkResponse.Success -> vm.loadChapter.value = it.data
            }
        }
        vm.loadChapter.observe(viewLifecycleOwner) { list ->
            chapterData = list
            setLoadedChapterData(list)
        }
        // ------------ NEXT ------------
        vmParser.chapterReaderNext.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResponse.Failure -> {}
                is NetworkResponse.Loading -> {}
                is NetworkResponse.Success -> vm.preloadChapterNext.value = it.data
            }
        }
        vm.preloadChapterNext.observe(viewLifecycleOwner) { list ->
            setLoadedChapterData(list)
            when (vmConfig.readMode) {
                READ_MODE_RIGHT -> viewer.setCurrentItem(0)
                READ_MODE_LEFT -> viewer.setCurrentItem(list.pages!!.lastIndex)
                READ_MODE_BOTTOM -> viewer.setCurrentItem(0)
            }
        }
        // ------------ PREV ------------
        vmParser.chapterReaderPrev.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResponse.Failure -> {}
                is NetworkResponse.Loading -> {}
                is NetworkResponse.Success -> vm.preloadChapterPrev.value = it.data
            }
        }
        vm.preloadChapterPrev.observe(viewLifecycleOwner) { list ->
            setLoadedChapterData(list)
            when (vmConfig.readMode) {
                READ_MODE_RIGHT -> viewer.setCurrentItem(list.pages!!.lastIndex)
                READ_MODE_LEFT -> viewer.setCurrentItem(0)
                READ_MODE_BOTTOM -> viewer.setCurrentItem(list.pages!!.lastIndex)
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setLoadedChapterData(list: ReaderChapter) {
        //loadPagesReader(listPage)
        if (chapterAdapter.itemCount != 0)
            chapterAdapter.setSelect(list.idChapter)
        requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)

        binding.tvTitleChapterNumber.text = ("${requireContext().getString(R.string.chapter_number)} ${list.number}")
        binding.ivBackLoaderReader.visibility = View.GONE

        viewer.setPages(list)
    }

    // Получения текущей главы для начала
    private fun getChaptersPager() {
        var indexID:Int = 0
        vm.creativeWorkPage.chapters!!.onEachIndexed { index, chapter -> if (chapter.id == vm.currentChapterID) indexID = index }
        vm.creativeWorkPage.chapters!![indexID]
            .url?.let {
            vm.getDataChapter(vmParser, url = it, source = settingsProvider.source()!!)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        //requireActivity().findViewById<BottomNavigationView>(R.id.nav_view).visibility = View.VISIBLE
        _binding = null
    }

    override fun onRecyclerViewItemClick(view: View, list: Any?) {
        when(view.id){
            R.id.cc_main_block_chapter_reader ->{
                val chapter = list as Chapter
                    if (!chapter.url.isNullOrBlank()) {
                        closeAllInsideMenu()
                        requireActivity().window.addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                        // Затенение при переходе
                        binding.ivBackLoaderReader.visibility = VISIBLE
                        vm.getDataChapter(vmParser, url = chapter.url, source = settingsProvider.source()!!)
                    }else{
                        Toast.makeText(requireActivity(),"Ссылка не найдена",Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }


}
