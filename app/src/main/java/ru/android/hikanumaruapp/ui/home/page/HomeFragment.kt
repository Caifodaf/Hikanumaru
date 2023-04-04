package ru.android.hikanumaruapp.ui.home.page

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import ru.android.hikanumaruapp.BaseFragment
import ru.android.hikanumaruapp.R
import ru.android.hikanumaruapp.databinding.FragmentHomeBinding
import ru.android.hikanumaruapp.utilits.navigation.Events
import ru.android.hikanumaruapp.utilits.navigation.NavigationFragmentinViewModel

@AndroidEntryPoint
class HomeFragment : BaseFragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<HomeViewModel>()

    private var scrollListenersGenres = arrayOfNulls<RecyclerView.OnScrollListener>(2)

    private val navigationEventsObserver = Events.EventObserver { event ->
        when (event) {
            is NavigationFragmentinViewModel.NavigationFrag -> navigateNav(event.navigation, event.bundle)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvErrorHome.visibility = View.GONE
        shimmerHide(1)

        initRecyclerView()

        //Todo disable of update
        binding.llRecommendationBlock.visibility = View.GONE

        //todo DEV refresh main page - usability function now
        //swipe_refresh_home.setOnRefreshListener {
        //    swipe_refresh_home.isRefreshing = true
        //    onRefreshData()
        //}
        setupOnBackPressed()

        viewModel.emitter.observe(viewLifecycleOwner, navigationEventsObserver)
    }

    override fun onResume() {
        super.onResume()
        requireActivity().findViewById<ConstraintLayout>(R.id.CCSearchTab).visibility = View.VISIBLE
    }

    private fun initRecyclerView(){
        scrollListenersGenres =
            viewModel.doubleScrollGenres(binding.rvGenresList,binding.rvGenresList2,scrollListenersGenres)

        viewModel.initRecyclerView(binding.rvHistoryList)
        viewModel.initRecyclerView(binding.rvGenresList,scrollListenersGenres)
        viewModel.initRecyclerView(binding.rvGenresList2,scrollListenersGenres)
        viewModel.initRecyclerView(binding.rvSpecialList)
        viewModel.initRecyclerView(binding.rvJournalList)
        viewModel.initRecyclerView(binding.rvNewList)
        viewModel.initRecyclerView(binding.rvPopularList)

        shimmerHide(0)
    }



    fun navigateNav(navigation: Int, bundle: Bundle?) {
        findNavController().navigate(navigation, bundle)
    }

    private fun shimmerHide(state:Int) {
        binding.tvErrorHome.visibility = View.GONE
        when (state) {
            //Hide
            0 -> {
                binding.placeholderHomePage.visibility = View.GONE
                binding.swipeRefreshHome.visibility = View.VISIBLE
            }
            //Show
            1 -> {
                binding.placeholderHomePage.visibility = View.VISIBLE
                binding.swipeRefreshHome.visibility = View.GONE
            }
            else -> {
                binding.tvErrorHome.visibility = View.VISIBLE
                // todo add error
                Log.e("Error UI initialisation", "Unselect hide type")
            }
        }
    }

    //override fun onRecyclerViewItemClick(view: View, list: Any?) {
    //    when (view.id) {
    //        R.id.rl_block_manga_main_item -> {} //todo main item
    //        R.id.rl_back_genres_item -> {} //todo main genres
    //    }
    //}




    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("testCrated", "onCreateView destroy")
        _binding = null
    }




}