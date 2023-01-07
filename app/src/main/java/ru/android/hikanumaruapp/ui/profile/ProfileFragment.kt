package ru.android.hikanumaruapp.ui.profile

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.SimpleItemAnimator
import coil.load
import dagger.hilt.android.AndroidEntryPoint
import ru.android.hikanumaruapp.BaseFragment
import ru.android.hikanumaruapp.R
import ru.android.hikanumaruapp.databinding.FragmentProfileBinding
import ru.android.hikanumaruapp.model.Manga
import ru.android.hikanumaruapp.model.MangaMainModel
import ru.android.hikanumaruapp.ui.profile.folders.FoldersLibraryAdapter
import ru.android.hikanumaruapp.ui.profile.folders.LibraryAdapter
import ru.android.hikanumaruapp.utilits.navigation.Events
import ru.android.hikanumaruapp.utilits.navigation.NavigationFragmentinViewModel
import ru.android.hikanumaruapp.utilits.recyclerviews.RecyclerViewClickListener
import ru.android.hikanumaruapp.utilits.recyclerviews.SpaceItemDecoration

@AndroidEntryPoint
class ProfileFragment : BaseFragment(), RecyclerViewClickListener {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val vm by viewModels<ProfileViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    private val navigationEventsObserver = Events.EventObserver { event ->
        when (event) {
            is NavigationFragmentinViewModel.NavigationFrag -> navigateNav(event.navigation, event.bundle)
        }
    }

    private lateinit var folderAdapter: FoldersLibraryAdapter
    private lateinit var libraryAdapter: LibraryAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnBack()
        vm.getUser(requireActivity())
        vm.initBDLibrary(this)

        observeList()
        initUI()
        initBtn()
    }

    private fun observeList() {
        vm.user.observe(viewLifecycleOwner, Observer { user ->
            binding.tvLoginName.text = "@"+user.login
            binding.tvNameUserTop.text = user.userName
            if (user.imageCover != "null" ) // todo add default cover
                binding.ivSelectImageAvatar.load(user.imageAvatar)
            if (user.imageCover != "null" ) // todo add default cover
                binding.ivBackUserProfile.load(user.imageCover)
            binding.tvRoleUserTop // todo add other roles
        })

        vm.library.observe(viewLifecycleOwner, Observer { library ->
            Log.e("Eadadadadadadadadadadad", "isBookmark ${library}")
            libraryAdapter.setMain(library)
        })
    }

    private fun initUI() {
       initRecyclerTags()
       initRecyclerLibrary()
    }

    private fun initRecyclerLibrary() {
        libraryAdapter = LibraryAdapter(this,
            binding.rlLibContainer.layoutManager as GridLayoutManager)
        binding.rlLibContainer.apply {
            adapter = libraryAdapter
        }
    }

    private fun initRecyclerTags() {
        val snapManger = LinearSnapHelper()
        folderAdapter = FoldersLibraryAdapter(this)

        snapManger.attachToRecyclerView(binding.rlLibTagsContainer)
        binding.rlLibTagsContainer.apply {
            addItemDecoration(SpaceItemDecoration(16))
            adapter = folderAdapter
            (itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        }

        vm.setDefaultTagsList(folderAdapter)
    }

    private fun initBtn() {
        val lm: GridLayoutManager = binding.rlLibContainer.layoutManager as GridLayoutManager
        binding.ImageViewGroupLine.setOnClickListener {
            if (lm.spanCount != 1) {
                lm.spanCount = 1
                initRecyclerLibrary()
                libraryAdapter.setMain(vm.library.value!!)
                grigViewSwitcher()
            }
        }
        binding.ImageViewGroupBig.setOnClickListener {
            if (lm.spanCount != 2) {
                lm.spanCount = 2
                initRecyclerLibrary()
                libraryAdapter.setMain(vm.library.value!!)
                grigViewSwitcher()
            }
        }
        binding.ImageViewGroupSmall.setOnClickListener {
            if (lm.spanCount != 3) {
                lm.spanCount = 3
                initRecyclerLibrary()
                libraryAdapter.setMain(vm.library.value!!)
                grigViewSwitcher()
            }
        }
    }

    @SuppressLint("UseCompatLoadingForColorStateLists")
    private fun grigViewSwitcher() {
        binding.ImageViewGroupSmall.imageTintList = resources.getColorStateList(R.color.grey_light_alternative_2)
        binding.ImageViewGroupBig.imageTintList = resources.getColorStateList(R.color.grey_light_alternative_2)
        binding.ImageViewGroupLine.imageTintList = resources.getColorStateList(R.color.grey_light_alternative_2)
        when((binding.rlLibContainer.layoutManager as GridLayoutManager).spanCount){
            1->binding.ImageViewGroupLine.imageTintList =
                resources.getColorStateList(R.color.grey_light_alternative_7)
            2->binding.ImageViewGroupBig.imageTintList =
                resources.getColorStateList(R.color.grey_light_alternative_7)
            3->binding.ImageViewGroupSmall.imageTintList =
                resources.getColorStateList(R.color.grey_light_alternative_7)
        }
    }

    fun navigateNav(navigation: Int, bundle: Bundle?) {
        findNavController().navigate(navigation, bundle)
    }

    override fun onRecyclerViewItemClick(view: View, list: Any?) {
        when(view.id){
            R.id.CCMainTagsItem ->{
                // todo tags fun
            }
            R.id.CCMainLibraryItem ->{
                val bundle = Bundle()
                bundle.putString("url", (list as Manga).urlManga)
                findNavController().navigate(R.id.navigation_mangapage, bundle)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}