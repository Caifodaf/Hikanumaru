package ru.android.hikanumaruapp.ui.auth.startpage

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import ru.android.hikanumaruapp.BaseFragment
import ru.android.hikanumaruapp.R
import ru.android.hikanumaruapp.databinding.FragmentStartPageBinding
import java.util.*

@AndroidEntryPoint
class StartPageFragment : BaseFragment() {

    private var _binding: FragmentStartPageBinding? = null
    private val binding get() = _binding!!
    private val vm by viewModels<StartPageViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentStartPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    private lateinit var timer: Timer
    private lateinit var handler: Handler

    private val adsList = arrayOf(
        R.drawable.ashot, R.drawable.dan9, R.drawable.any,
        R.drawable.maks, R.drawable.andre
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().findViewById<ConstraintLayout>(R.id.CCSearchTab).visibility = View.GONE
        requireActivity().findViewById<BottomNavigationView>(R.id.nav_view).visibility = View.GONE

        binding.apply {
            setupOnBackPressed()

            imageSwitcherLoader()
            initButtons()
            initButtonsServices()
        }
    }

    private fun FragmentStartPageBinding.errorCheck(){
        // todo post refactor
        // todo getstring after error create user
        // error = arguments?.getString("error").toString()
        //errorLayout = requireView().ll_error_block
        //inflater = layoutInflater

        //if(error.isNotBlank() && error != "null")
        //    errorPop(error,errorLayout,inflater,10000)
    }

    private fun FragmentStartPageBinding.imageSwitcherLoader() {
        val imgIn = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left)
        val imgOut = AnimationUtils.loadAnimation(context, android.R.anim.slide_out_right)

        ImageSwitcher.apply{
            setFactory {
                ImageView(context).apply {
                    layoutParams = FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.MATCH_PARENT,
                        FrameLayout.LayoutParams.MATCH_PARENT
                    )
                    scaleType = ImageView.ScaleType.CENTER_CROP
                    setPadding(20, 20, 20, 20)
                }
            }

            setImageResource(adsList[0])
            inAnimation = imgIn
            outAnimation = imgOut
        }

        var currentIndex = 0
        timer = Timer()
        timer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                currentIndex = (currentIndex + 1) % adsList.size
                requireActivity().runOnUiThread {
                    ImageSwitcher.setImageResource(adsList[currentIndex])
                }
            }
        }, 0, 3000)
    }

    private fun FragmentStartPageBinding.initButtons(){
        TVBtnLoginStart.setOnClickListener {
            TVBtnLoginStart.timerDoubleBtn()
            findNavController().navigate(R.id.action_navigation_start_page_to_navigation_login, null)
        }

        TVBtnRegStart.setOnClickListener {
            TVBtnRegStart.timerDoubleBtn()
            findNavController().navigate(R.id.action_navigation_start_page_to_navigation_registration, null)
        }

        TVBtnLogInGuest.setOnClickListener {
            // Todo DEV guest mode
            TVBtnLogInGuest.timerDoubleBtn()

            val sp = requireActivity().getPreferences(Context.MODE_PRIVATE)
            sp.edit().putBoolean("guest_mode", true).apply()

            Toast.makeText(requireContext(), "Вы вошли как гость", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_navigation_start_page_to_navigation_home, null)
        }
    }

    private fun FragmentStartPageBinding.initButtonsServices(){
        ImageBtnGoogle.setOnClickListener{
            // todo add fun
        }
        ImageBtnVK.setOnClickListener{
            // todo add fun
        }
        ImageBtnFacebook.setOnClickListener{
            // todo add fun
        }
        ImageBtnTwitter.setOnClickListener{
            // todo add fun
        }
        ImageBtnShikimori.setOnClickListener{
            // todo add fun
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (::timer.isInitialized)
            timer.cancel()
        if (::handler.isInitialized)
            handler.removeCallbacksAndMessages(null)
    }

    fun View.timerDoubleBtn(time: Long = 2000) {
        isClickable = false
        this@StartPageFragment.handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            isClickable = true
        }, time)
    }

}