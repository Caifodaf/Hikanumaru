package ru.android.hikanumaruapp.presentasion.search

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearSnapHelper
import dagger.hilt.android.AndroidEntryPoint
import ru.android.hikanumaruapp.R
import ru.android.hikanumaruapp.data.model.MangaList
import ru.android.hikanumaruapp.databinding.SearchPageFragmentBinding
import ru.android.hikanumaruapp.presentasion.search.adapter.LastedSearchAdapter
import ru.android.hikanumaruapp.presentasion.search.adapter.SearchAdapter
import ru.android.hikanumaruapp.utilits.UIUtils
import ru.android.hikanumaruapp.utilits.recyclerviews.RecyclerViewClickListener

@AndroidEntryPoint
class SearchPageFragment : Fragment(), UIUtils, RecyclerViewClickListener {

    private var _binding: SearchPageFragmentBinding? = null
    private val binding get() = _binding!!
    private val vm by viewModels<SearchTabViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = SearchPageFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    private lateinit var searchAdapter: SearchAdapter
    //private lateinit var lastedSearchAdapter: LastedSearchAdapter Todo Skipped

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            ETSearchBar.setText(vm.searchText.value)
            initET()

            //initLastedSearch()
            initSearchList()

            //observeLastedList()
            observeList()

            initSearchBtn()
        }
    }

    private fun initET() {
        binding.ETSearchBar.doOnTextChanged { text, start, count, after ->
            vm.searchText.value = text.toString()
        }
        vm.searchText.observe(viewLifecycleOwner) { text ->
            binding.ETSearchBar.setText(text)
        }
    }

    //private fun SearchPageFragmentBinding.initLastedSearch() {
    //    CCBlockLastedSearch.visibility = View.GONE
    //}

    private fun SearchPageFragmentBinding.initSearchList() {
        searchAdapter = SearchAdapter(requireActivity(), this@SearchPageFragment)

        RVSearchList.apply{
            adapter = searchAdapter
        }
    }

    //private fun SearchPageFragmentBinding.observeLastedList() {
    //   //vm.searchText.observe(viewLifecycleOwner) {}
    //}

    private fun SearchPageFragmentBinding.observeList() {
       //vm.searchText.observe(viewLifecycleOwner) {}
    }

    private fun SearchPageFragmentBinding.initSearchBtn() {
        ImageFilters.setOnClickListener{
            vm.filterButton()
        }
        ImageRandom.setOnClickListener{
            vm.randomButton()
        }
    }

    override fun onRecyclerViewItemClick(view: View, list: Any?) {
        when (view.id) {
            R.id.CCSearchItem -> {
                val bundle = Bundle()
//                bundle.putString("id", (list as MangaList).id)
//                bundle.putString("url", (list as MangaList).sourceLink)
//                bundle.putString("source", (list as MangaList).sourceLink)
                // TODO ADD
                findNavController().navigate(R.id.action_searchPageFragment_to_navigation_mangapage, bundle)
            }
        }
    }

}