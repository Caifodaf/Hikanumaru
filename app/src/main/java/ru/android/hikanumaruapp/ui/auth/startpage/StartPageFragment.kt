package ru.android.hikanumaruapp.ui.auth.startpage

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import android.widget.ImageSwitcher
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import ru.android.hikanumaruapp.BaseFragment
import ru.android.hikanumaruapp.R
import ru.android.hikanumaruapp.databinding.FragmentStartPageBinding
import ru.android.hikanumaruapp.utilits.UIUtils
import java.util.*

@AndroidEntryPoint
class StartPageFragment : BaseFragment(),UIUtils {

    private var _binding: FragmentStartPageBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<StartPageViewModel>()

    private lateinit var sh : SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentStartPageBinding.inflate(inflater, container, false)
        val root: View = binding.root

        sh = requireActivity().getSharedPreferences("user", Context.MODE_PRIVATE)

        //errorCheck()
        initUI()

        return root
    }

    private fun errorCheck(){
        // todo post refactor
        // todo getstring after error create user
        // error = arguments?.getString("error").toString()
        //errorLayout = requireView().ll_error_block
        //inflater = layoutInflater

        //if(error.isNotBlank() && error != "null")
        //    errorPop(error,errorLayout,inflater,10000)
    }

    private fun initUI(){
        btnBack()

        imageSwitcherLoader()
        initButtons()
        initButtonsServices()
    }

    private fun imageSwitcherLoader(){
        val imageSwitcher: ImageSwitcher = binding.imageSwitcher

        imageSwitcher.setFactory {
            val imgView = ImageView(context)
            imgView.layoutParams = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
            )
            imgView.scaleType = ImageView.ScaleType.CENTER_CROP
            imgView.setPadding(20, 20, 20, 20)
            imgView
        }

        val adsList= arrayOf<Int>(
            R.drawable.ashot,
            R.drawable.dan9,
            R.drawable.any,
            R.drawable.maks,
            R.drawable.andre
        )

        imageSwitcher.setImageResource(adsList[0])

        val imgIn = AnimationUtils.loadAnimation(
            context, android.R.anim.slide_in_left)
        imageSwitcher.inAnimation = imgIn

        val imgOut = AnimationUtils.loadAnimation(
            context, android.R.anim.slide_out_right)
        imageSwitcher.outAnimation = imgOut

        var currentIndex = -1
        try {
            Timer().scheduleAtFixedRate(object : TimerTask() {
                override fun run() {
                    currentIndex++
                    if (currentIndex == adsList.size) currentIndex = 0
                    activity?.runOnUiThread(Runnable {
                        imageSwitcher.setImageResource(adsList[currentIndex])
                    })
                }
            }, 0, 3000)
        }catch (e:Exception){
            Log.d("StartPageError", "imageSwitcher error")
        }finally {
            Log.d("StartPageError", "imageSwitcher error")
        }
    }

    private fun initButtons(){
        binding.tvBtnLoginStart.setOnClickListener {
            timerDoubleBtn(binding.tvBtnLoginStart)
            findNavController().navigate(R.id.navigation_login, null)
        }

        binding.tvBtnRegStart.setOnClickListener {
            timerDoubleBtn(binding.tvBtnRegStart)
            findNavController().navigate(R.id.navigation_registration, null)
        }

        binding.tvBtnLoginGuest.setOnClickListener {
            // Todo DEV guest mode
            timerDoubleBtn(binding.tvBtnLoginGuest)

            sh.edit().putBoolean("guest_mode", true).apply()

            Toast.makeText(requireContext(), "Вы вошли как гость", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.navigation_home, null)
        }
    }

    private fun initButtonsServices(){
        binding.ivBtnGoogle.setOnClickListener{
            // todo add fun
        }
        binding.ivBtnVk.setOnClickListener{
            // todo add fun
        }
        binding.ivBtnFacebook.setOnClickListener{
            // todo add fun
        }
        binding.ivBtnTwitter.setOnClickListener{
            // todo add fun
        }
        binding.ivBtnShikimori.setOnClickListener{
            // todo add fun
        }
    }

}