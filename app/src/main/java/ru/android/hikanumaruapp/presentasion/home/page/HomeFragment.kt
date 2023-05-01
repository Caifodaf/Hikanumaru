package ru.android.hikanumaruapp.presentasion.home.page

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import dagger.hilt.android.AndroidEntryPoint
import ru.android.hikanumaruapp.presentasion.BaseFragment
import ru.android.hikanumaruapp.presentasion.ConstPages
import ru.android.hikanumaruapp.R
import ru.android.hikanumaruapp.api.api.main.MainApiViewModel
import ru.android.hikanumaruapp.api.api.token.AuthViewModel
import ru.android.hikanumaruapp.api.init.ApiResponse
import ru.android.hikanumaruapp.data.debugModels
import ru.android.hikanumaruapp.databinding.FragmentHomeBinding
import ru.android.hikanumaruapp.data.local.storage.local.home.HomeCacheModel
import ru.android.hikanumaruapp.data.local.storage.local.home.LocalCacheHomeViewModel
import ru.android.hikanumaruapp.data.model.MangaList
import ru.android.hikanumaruapp.data.model.MangaMainModel
import ru.android.hikanumaruapp.data.model.MangaPopularMainModel
import ru.android.hikanumaruapp.presentasion.hellopage.HelloPageViewModel
import ru.android.hikanumaruapp.presentasion.home.page.adapters.*
import ru.android.hikanumaruapp.presentasion.search.SearchTabViewModel
import ru.android.hikanumaruapp.utilits.ItemSnapHelper
import ru.android.hikanumaruapp.utilits.popdialog.PopAlertDialog
import ru.android.hikanumaruapp.utilits.recyclerviews.RecyclerViewClickListener
import ru.android.hikanumaruapp.utilits.recyclerviews.SpaceItemDecoration

@AndroidEntryPoint
class HomeFragment : BaseFragment(), RecyclerViewClickListener,debugModels {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val vm by activityViewModels<HomeViewModel>()

    private val vmApi by viewModels<MainApiViewModel>()
    private val vmCache by viewModels<LocalCacheHomeViewModel>()
    private val vmAuth by viewModels<AuthViewModel>()
    private val vmSearch by viewModels<SearchTabViewModel>()

    //// Сохраняем данные в Bundle
    //override fun onSaveInstanceState(outState: Bundle) {
    //    super.onSaveInstanceState(outState)
//
    //    outState.putStringArrayList("mainBlocks", ArrayList(mainBlocks))
    //    // Сохраняем состояние RecyclerView
    //    val layoutManager = RVGenres.layoutManager as LinearLayoutManager
    //    val position = layoutManager.findFirstVisibleItemPosition()
    //    outState.putInt("rvGenresPosition", position)
    //}
//
    //// Восстанавливаем данные из Bundle
    //override fun onViewStateRestored(savedInstanceState: Bundle?) {
    //    super.onViewStateRestored(savedInstanceState)
//
    //    if (savedInstanceState != null) {
    //        mainBlocks = savedInstanceState.getStringArrayList("mainBlocks")!!
    //        // Восстанавливаем состояние RecyclerView
    //        val position = savedInstanceState.getInt("rvGenresPosition")
    //        RVGenres.post { RVGenres.scrollToPosition(position) }
    //    }
    //}

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        requireActivity().findViewById<com.google.android.material.bottomnavigation.BottomNavigationView>(R.id.nav_view).visibility = View.VISIBLE
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        requireActivity().findViewById<ConstraintLayout>(R.id.CCSearchTab).visibility = View.VISIBLE
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupOnBackPressed()
        binding.apply {
            initMainList()
            statePage(ConstPages.LOADING_PAGE_VIEW)

            loadPage()
            observeAllPageData()
            observeErrors()

            getEndleesList()

            initMoreBtn()

            dbInit()
        }
    }

    private fun FragmentHomeBinding.dbInit() {
        vm.mainMangaList.value = home().toMutableList()
        binding.root.postDelayed({
            statePage(ConstPages.DEFAULT_PAGE_VIEW)
        }, 1000L)
    }

    private fun  FragmentHomeBinding.loadPage() {
        vm.apply { requireActivity().loadPage(vmCache,mainBlocks,vmApi)}

        vmCache.homeListModelCache.observe(viewLifecycleOwner, Observer { list ->
            Log.d("apiRequestFlow", "homeListModelCache - $list") //todo
            if (list != null) {
                vm.mainGenresList.value = list.genresList
                vm.mainMangaList.value = list.mangaList
                vm.mainManhvaList.value = list.manhvaList
            } else
                vm.getPage(mainBlocks,vmApi)
        })
    }

    private var mainBlocks = listOf<String>()
    private var mainRV = listOf<RecyclerView>()
    private var mainEndlessBlocks = listOf<String>()
    private var mainEndlessRV = listOf<RecyclerView>()

    private fun initMainList() {
        mainBlocks = listOf(
            "History",
            "Genres",
            "Manga",
            "Manhva",
        )
        mainRV = listOf(
            binding.RVHistory,
            binding.RVGenres,
            binding.RVManga,
            binding.RVManhva,
        )
        mainEndlessBlocks = listOf(
            "Popular"
        )
        mainEndlessRV = listOf(
            binding.RVPopular
        )
    }

    //private lateinit var historyAdapter : HomeHistoryMangaAdapter
    private lateinit var genresAdapter : HomeGenresMangaAdapter
    private lateinit var genres2Adapter : HomeGenresMangaAdapter
    private lateinit var mangaAdapter : HomeMangaAdapter
    private lateinit var manhvaAdapter : HomeMangaAdapter
    private lateinit var popularAdapter : HomePopularMangaAdapter

    private fun FragmentHomeBinding.getEndleesList() {
        mainEndlessBlocks.forEachIndexed { index, block ->
            //todo
        }
    }

    private fun FragmentHomeBinding.observeErrors() {
        vm.error.observe(viewLifecycleOwner, Observer { error->
            val pop = PopAlertDialog(requireActivity(),lifecycleScope)
            when(error.code){
                502 -> pop.setDataDialog("Ошибка сервера")
                504 -> pop.setDataDialog("Время ожидания первышенно")
                -1 -> pop.setDataDialog("Нестабильная работа сети")
                else -> pop.setDataDialog("Неверная почта или пароль")
            }
            statePage(ConstPages.ERROR_PAGE_VIEW)
        })
    }

    private fun FragmentHomeBinding.observeAllPageData() {
        mainBlocks.forEachIndexed { index, block ->
            when (block) {
                "History" -> {
                    vm.listHistoryData.observe(viewLifecycleOwner, Observer {

                    })
                }
                "Genres" -> {
                    vm.mainGenresResponse.observe(viewLifecycleOwner, Observer {
                        when (it) {
                            is ApiResponse.Failure -> {
                                Log.d("vmApi",
                                    "mainGenresResponse falure - ${it.code}: " + it.errorMessage)
                                statePage(ConstPages.ERROR_PAGE_VIEW)
                            }
                            is ApiResponse.Loading -> Log.d("vmApi",
                                "mainGenresResponse loading - ")
                            is ApiResponse.Success -> vm.mainGenresList.value = it.data
                        }
                    })
                    vm.mainGenresList.observe(viewLifecycleOwner, Observer { list ->
                        val listTop = list.filterIndexed { index, _ -> index % 2 == 0 }
                        val listBot = list.filterIndexed { index, _ -> index % 2 == 1 }

                        var scrollListenersGenres = arrayOfNulls<RecyclerView.OnScrollListener>(2)
                        scrollListenersGenres =
                            vm.doubleScrollGenres(RVGenres, RVGenres2, scrollListenersGenres)

                        genresAdapter = HomeGenresMangaAdapter(this@HomeFragment)
                        genres2Adapter = HomeGenresMangaAdapter(this@HomeFragment)
                        RVGenres.apply {
                            addOnScrollListener(scrollListenersGenres[0]!!)
                            adapter = genresAdapter
                            addItemDecoration(SpaceItemDecoration())
                            genresAdapter.setMain(listTop)
                        }
                        RVGenres2.apply {
                            addOnScrollListener(scrollListenersGenres[1]!!)
                            adapter = genres2Adapter
                            addItemDecoration(SpaceItemDecoration())
                            genres2Adapter.setMain(listBot)
                        }
                        checkInitRecyclerViews()
                    })
                }
                "Manga" -> {
                    vm.mainMangaResponse.observe(viewLifecycleOwner, Observer {
                        when (it) {
                            is ApiResponse.Failure -> {
                                Log.d("vmApi",
                                    "mainManhvaResponse falure -  ${it.code}:" + it.errorMessage)
                                statePage(ConstPages.ERROR_PAGE_VIEW)
                            }
                            is ApiResponse.Loading -> Log.d("vmApi", "mainMangaResponse loading - ")
                            is ApiResponse.Success -> vm.mainMangaList.value = it.data
                        }
                    })
                    vm.mainMangaList.observe(viewLifecycleOwner, Observer { list ->
                        mangaAdapter = HomeMangaAdapter(requireActivity(), this@HomeFragment)
                        mainRV[index].apply {
                            val snapHelper = ItemSnapHelper()
                            snapHelper.attachToRecyclerView(this)
                            addItemDecoration(SpaceItemDecoration())
                            adapter = mangaAdapter
                        }
                        mangaAdapter.setMain(list)
                        checkInitRecyclerViews()
                    })
                }
                "Manhva" -> {
                    vm.mainManhvaResponse.observe(viewLifecycleOwner, Observer {
                        when (it) {
                            is ApiResponse.Failure -> {
                                Log.d("vmApi",
                                    "mainManhvaResponse falure -  ${it.code}:" + it.errorMessage)
                                statePage(ConstPages.ERROR_PAGE_VIEW)
                            }
                            is ApiResponse.Loading -> Log.d("vmApi", "mainManhvaResponse loading - ")
                            is ApiResponse.Success -> vm.mainManhvaList.value = it.data
                        }
                    })
                    vm.mainManhvaList.observe(viewLifecycleOwner, Observer { list ->
                        manhvaAdapter = HomeMangaAdapter(requireActivity(), this@HomeFragment)
                        mainRV[index].apply {
                            val snapHelper:SnapHelper = ItemSnapHelper()
                            snapHelper.attachToRecyclerView(this)
                            addItemDecoration(SpaceItemDecoration())
                            adapter = manhvaAdapter
                        }
                        manhvaAdapter.setMain(list)
                        checkInitRecyclerViews()
                    })
                }
            }
        }
    }

    override fun onRecyclerViewItemClick(view: View, list: Any?) {
        when (view.id) {
            R.id.CCMainMangaBlock -> {
                val bundle = Bundle()
                bundle.putString("id", (list as MangaList).id)
                bundle.putString("url", (list as MangaList).sourceLink)
                bundle.putString("source", (list as MangaList).sourceLink)

                findNavController().navigate(R.id.action_navigation_home_to_navigation_mangapage, bundle)
            }
            //R.id.rl_back_genres_item -> {
            //    Log.d("testCrated", "rl_back_genres_item click $list")
            //}
            //R.id.rl_back_journal_item -> {
            //    Log.d("testCrated", "rl_back_genres_item click")
            //}
            //R.id.CCMainLargeMangaBlock -> {
            //    bundle.putString("url", (list as MangaPopularMainModel).linkPage)
            //    //emitter.emitAndExecute(NavigationFragmentinViewModel.NavigationFrag(
            //    //    R.id.action_navigation_home_to_navigation_mangapage, bundle))
            //}
        }
    }

    private fun FragmentHomeBinding.checkInitRecyclerViews(){
        if (::genresAdapter.isInitialized && ::genres2Adapter.isInitialized &&
            ::mangaAdapter.isInitialized && ::manhvaAdapter.isInitialized
            //::popularAdapter.isInitialized
        ){
            if(!vm.mainGenresList.value.isNullOrEmpty() &&
                !vm.mainMangaList.value.isNullOrEmpty() &&
                !vm.mainManhvaList.value.isNullOrEmpty()
            ) {
                vm.isLoadedPages = true
                vmCache.apply { requireActivity().saveHomeCacheVM(HomeCacheModel(
                    vm.mainGenresList.value!!,vm.mainMangaList.value!!,
                    vm.mainManhvaList.value!!
                ))
                }
            }
            statePage(ConstPages.DEFAULT_PAGE_VIEW)
        }else{
            statePage(ConstPages.LOADING_PAGE_VIEW)
        }
    }


    private fun FragmentHomeBinding.initMoreBtn(){
        TVSeeHistory
        TVSeeGenres
        TVSeeManga
        TVSeeManhva
        TVSeePopualar
    }

        //initRecyclerView()
        //initSearchTab()
        //binding.ImageRandom.setOnClickListener{
        //    // Определяем анимацию для перехода вперед
        //    //val navOptions = NavOptions.Builder()
        //    //    .setEnterAnim(R.anim.slide_up) // Устанавливаем анимацию slide up для входа
        //    //    .build()
//
        //    // Выполняем навигацию на следующий фрагмент с использованием определенных анимаций
        //    //findNavController().navigate(R.id.action_navigation_home_to_searchPageFragment)
        //}

        //todo DEV refresh main page - usability function now
        //swipe_refresh_home.setOnRefreshListener {
        //    swipe_refresh_home.isRefreshing = true
        //    onRefreshData()
        //}


    private fun initSearchTab() {
        binding.ETSearchBar.setText(vmSearch.searchText.value)

        binding.ETSearchBar.doOnTextChanged { text, start, count, after ->
            vmSearch.searchText.value = text.toString()
        }
        vmSearch.searchText.observe(viewLifecycleOwner) { text ->
            binding.ETSearchBar.setText(text)
        }

        binding.ETSearchBar.setOnEditorActionDoneListener {
            Log.e("Error UI initialisation", "Unselect hide type")
            findNavController().navigate(R.id.action_navigation_home_to_searchPageFragment)
        }
    }

    private fun FragmentHomeBinding.statePage(state: Int){
        when (state) {
            ConstPages.DEFAULT_PAGE_VIEW -> {
                FramePlaceholder.visibility = View.GONE
                FrameError.visibility = View.GONE
                LLMainBlock.visibility = View.VISIBLE
                CCSearchTab.visibility = View.VISIBLE
            }
            ConstPages.LOADING_PAGE_VIEW -> {
                FramePlaceholder.visibility = View.VISIBLE
                FrameError.visibility = View.GONE
                LLMainBlock.visibility = View.GONE
                CCSearchTab.visibility = View.VISIBLE
            }
            ConstPages.ERROR_PAGE_VIEW -> {
                FramePlaceholder.visibility = View.GONE
                FrameError.visibility = View.VISIBLE
                LLMainBlock.visibility = View.GONE
                CCSearchTab.visibility = View.VISIBLE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }




}