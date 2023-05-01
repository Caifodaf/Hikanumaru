package ru.android.hikanumaruapp.presentasion.mangapage.chapterspage

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.AndroidEntryPoint
import ru.android.hikanumaruapp.databinding.FragmentChaptersPageBinding
import ru.android.hikanumaruapp.data.model.Chapter
import ru.android.hikanumaruapp.presentasion.mangapage.MangaPageViewModel
import ru.android.hikanumaruapp.utilits.navigation.Events
import ru.android.hikanumaruapp.utilits.navigation.NavigationFragmentinViewModel
import ru.android.hikanumaruapp.utilits.recyclerviews.snappy.SnappyLinearLayoutManager
import java.lang.reflect.Type

@AndroidEntryPoint
class ChaptersPageFragment(val vm: MangaPageViewModel) : Fragment() {

    private var _binding: FragmentChaptersPageBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<ChaptersPageViewModel>()

    private lateinit var countChapters: String
    private lateinit var nameTitle: String
    private lateinit var url: String
    private var isReversedList: Boolean = false

    private lateinit var llmRecycler: SnappyLinearLayoutManager

    private val navigationEventsObserver = Events.EventObserver { event ->
        when (event) {
            is NavigationFragmentinViewModel.NavigationFrag -> navigateNav(event.navigation, event.bundle)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentChaptersPageBinding.inflate(inflater, container, false)
        val root: View = binding.root

        initUI()

        viewModel.emitter.observe(viewLifecycleOwner, navigationEventsObserver)

        return root
    }

    private fun getArgumentsPage(){
        val str: String? = arguments?.getString("list")
        viewModel.isLoadChapter = arguments?.getBoolean("checkLoadList")!!
        countChapters = arguments?.getString("count").toString()
        nameTitle = arguments?.getString("name").toString()
        url = arguments?.getString("url").toString()
        isReversedList = arguments?.getBoolean("reverse")!!

        val collectionType: Type = object : TypeToken<Collection<Chapter?>?>() {}.type
        val enums: Collection<Chapter> = Gson().fromJson(str, collectionType)

        if (countChapters.toInt()+1 == enums.toMutableList().size){
            binding.rlChapterListTab.visibility = View.VISIBLE
            binding.rlLoaderBlockChapter.visibility = View.GONE
        }

        viewModel.setListChaptersData(enums.toMutableList(),countChapters.toInt())
    }

    private fun initUI() {
        getArgumentsPage()
        binding.tvNumberChapter.text = countChapters

        llmRecycler = SnappyLinearLayoutManager(requireActivity().baseContext,RecyclerView.VERTICAL,false)

        viewModel.initRecyclerView(binding.rlChapterListTab,llmRecycler)

        observerErrorLoad()
        loadChapters()
        initBtn()
    }

    private fun observerErrorLoad(){
        viewModel.errorView.observe(viewLifecycleOwner, Observer { errorView ->
            setViewError(errorView)
        })
    }

    private fun loadChapters(){
        if(!viewModel.listChapters.value.isNullOrEmpty()) {
            if (viewModel.listChapters.value!!.size != (countChapters.toInt()+1))
                // Show frame load...
                setViewError(false)
                //viewModel.reLoadChapter(url)
        }
    }

    private fun setViewError(type:Boolean){
        when(type){
            true ->{
                // Frame error load
                binding.rlChapterListTab.visibility = View.GONE
                binding.rlErrorBlockChapter.visibility = View.VISIBLE
                binding.rlLoaderBlockChapter.visibility = View.GONE
            }
            false ->{
                // Frame load... chapters
                binding.rlChapterListTab.visibility = View.GONE
                binding.rlErrorBlockChapter.visibility = View.GONE
                binding.rlLoaderBlockChapter.visibility = View.VISIBLE
            }
        }
    }

    private fun initBtn(){
        viewModel.btnReverseChapterList(binding.llSortBtn,requireContext(),binding.ivSortImage)
        btnReloadChapterList()
        searchChapter()
    }

    private fun btnReloadChapterList(){
        binding.tvBtnReloadChapter.setOnClickListener {
            setViewError(false)
            //viewModel.reLoadChapter(url)
        }
    }

    private fun searchChapter() {
        binding.etSearchTabNumbers.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.isNotEmpty()) {
                    val pos: Int = s.toString().toInt()
                    if (pos >= 0 && pos <= countChapters.toInt())
                        if (!isReversedList)
                            llmRecycler.scrollToPositionWithOffset(pos, 10)
                        else
                            llmRecycler.scrollToPositionWithOffset((countChapters.toInt() - pos), 10)
                }
            }
        })
    }


    fun navigateNav(navigation: Int, bundle: Bundle?) {
        findNavController().navigate(navigation, bundle)
    }

}