package ru.android.hikanumaruapp.presentasion.profile

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.SimpleItemAnimator
import coil.load
import dagger.hilt.android.AndroidEntryPoint
import ru.android.hikanumaruapp.presentasion.BaseFragment
import ru.android.hikanumaruapp.R
import ru.android.hikanumaruapp.databinding.FragmentProfileBinding
import ru.android.hikanumaruapp.data.local.user.UserDataViewModel
import ru.android.hikanumaruapp.presentasion.profile.folders.FoldersLibraryAdapter
import ru.android.hikanumaruapp.presentasion.profile.folders.LibraryAdapter
import ru.android.hikanumaruapp.utilits.recyclerviews.RecyclerViewClickListener
import ru.android.hikanumaruapp.utilits.recyclerviews.SpaceItemDecoration

@AndroidEntryPoint
class ProfileFragment : BaseFragment(), RecyclerViewClickListener {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val vm by viewModels<ProfileViewModel>()

    private val vmUser by viewModels<UserDataViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
       _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupOnBackPressed()
        //vm.apply {
        //    requireContext().getUser(vmUser)
        //    initBDLibrary(this@ProfileFragment)
        //}
        binding.apply {
            observeList()

            initRecyclerTags()
            initRecyclerLibrary()

            initBtn()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun FragmentProfileBinding.observeList() {
        vmUser.user.observe(viewLifecycleOwner, Observer { user ->
            TVLoginName.text = "@"+user.login
            TVUserName.text = user.username
            //for (i in user.roles){
            //    TVUserRoles.text = TVUserRoles.text.toString() + when(i){
            //        "ROLE_USER" -> getString(R.string.role_user_user)
            //        "ROLE_ADMIN" -> getString(R.string.role_user_user)
            //        "ROLE_REDACTOR" -> getString(R.string.role_user_user)
            //        else -> getString(R.string.role_user_user)
            //    }
            //}
            TVUserRoles.text = getString(R.string.role_user_user)
            val days = getDaysPassed(user.createdAt)
            TVAgeProfile.text = getString(R.string.profile_age_start_text) + " " +
                    when(days){
                        0 -> "$days ${getString(R.string.profile_age_date_zero_days)}"
                        1 -> "$days ${getString(R.string.profile_age_date_ten_days)}"
                        in 2..4 -> "$days ${getString(R.string.profile_age_date_days)}"
                        in 5..30 -> "$days ${getString(R.string.profile_age_date_month)}"
                        in 31..365*4 -> "$days ${getString(R.string.profile_age_date_year)}"
                        in 365*4..365*100 -> "$days ${getString(R.string.profile_age_date_years)}"
                        else -> "бесконечность :3"
                    }
            if (user.imageCover != null)
                ImageAvatar.load(user.imageAvatar)
            if (user.imageCover != null )
                ImageBackAvatar.load(user.imageCover)
        })

        vm.library.observe(viewLifecycleOwner, Observer { library ->
            Log.e("Eadadadadadadadadadadad", "isBookmark ${library}")
            vm.libraryAdapter.setMain(library)
        })
    }

    private fun FragmentProfileBinding.initRecyclerLibrary() {
        vm.libraryAdapter = LibraryAdapter(this@ProfileFragment,
            RVLibraryContainer.layoutManager as GridLayoutManager)
        RVLibraryContainer.apply {
            adapter = vm.libraryAdapter
        }
    }

    private fun FragmentProfileBinding.initRecyclerTags() {
        val snapManger = LinearSnapHelper()
        vm.folderAdapter = FoldersLibraryAdapter(this@ProfileFragment)

        snapManger.attachToRecyclerView(RVLibraryTags)
        RVLibraryTags.apply {
            addItemDecoration(SpaceItemDecoration(16))
            adapter = vm.folderAdapter
            (itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        }

        vm.setDefaultTagsList(vm.folderAdapter)
    }

    private fun FragmentProfileBinding.initBtn() {
        val lm: GridLayoutManager = RVLibraryContainer.layoutManager as GridLayoutManager
        val spanCountOne = 1
        val spanCountTwo = 2
        val spanCountThree = 3

        ImageViewGroupLine.setOnClickListener {
            if (lm.spanCount != spanCountOne) {
                lm.spanCount = spanCountOne
                initRecyclerLibrary()
                vm.libraryAdapter.setMain(vm.library.value!!)
                grigViewSwitcher()
            }
        }
        ImageViewGroupBig.setOnClickListener {
            if (lm.spanCount != spanCountTwo) {
                lm.spanCount = spanCountTwo
                initRecyclerLibrary()
                vm.libraryAdapter.setMain(vm.library.value!!)
                grigViewSwitcher()
            }
        }
        ImageViewGroupSmall.setOnClickListener {
            if (lm.spanCount != spanCountThree) {
                lm.spanCount = spanCountThree
                initRecyclerLibrary()
                vm.libraryAdapter.setMain(vm.library.value!!)
                grigViewSwitcher()
            }
        }
    }

    @SuppressLint("UseCompatLoadingForColorStateLists")
    private fun FragmentProfileBinding.grigViewSwitcher() {
        ImageViewGroupSmall.imageTintList = resources.getColorStateList(R.color.light_grey_4)
        ImageViewGroupBig.imageTintList = resources.getColorStateList(R.color.light_grey_4)
        ImageViewGroupLine.imageTintList = resources.getColorStateList(R.color.light_grey_4)
        when((RVLibraryContainer.layoutManager as GridLayoutManager).spanCount){
            1->ImageViewGroupLine.imageTintList =
                resources.getColorStateList(R.color.grey_700)
            2->ImageViewGroupBig.imageTintList =
                resources.getColorStateList(R.color.grey_700)
            3->ImageViewGroupSmall.imageTintList =
                resources.getColorStateList(R.color.grey_700)
        }
    }

    override fun onRecyclerViewItemClick(view: View, list: Any?) {
        when(view.id){
            R.id.CCMainTagsItem ->{
                // todo tags fun
            }
            R.id.CCMainLibraryItem ->{
                val bundle = Bundle()
                //bundle.putString("url", (list as Manga).urlManga)
                findNavController().navigate(R.id.action_navigation_profile_to_navigation_mangapage, bundle)
            }
        }
    }
}