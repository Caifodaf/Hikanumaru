package ru.android.hikanumaruapp.ui.reader

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.fragment.app.viewModels
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import ru.android.hikanumaruapp.R
import ru.android.hikanumaruapp.databinding.FragmentReaderBinding
import ru.android.hikanumaruapp.databinding.FragmentSettingsBinding
import ru.android.hikanumaruapp.ui.settings.SettingsViewModel

@AndroidEntryPoint
class ReaderFragment : Fragment() {

    private var _binding: FragmentReaderBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<ReaderViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentReaderBinding.inflate(inflater, container, false)
        val root: View = binding.root

        requireActivity().findViewById<BottomNavigationView>(R.id.nav_view).visibility = View.GONE

        initUI()

        //toggleMenu()

        return root
    }

    private fun initUI(){


        menuOcClick()
    }

    private fun toggleMenu() {
        val menu = binding.readerMenu

        // todo add anim select item menu
        //ShowAnim().collapse(rl_read_mode_menu_layout)
        //ShowAnim().collapse(rl_translators_menu_layout)

        // todo close all menu layout
        //if(rl_read_mode_menu_layout.visibility == View.VISIBLE)
        //    rl_read_mode_menu_layout.visibility = View.GONE
        //if(rl_translators_menu_layout.visibility == View.VISIBLE)
        //    rl_translators_menu_layout.visibility = View.GONE

        when (menu.visibility) {
            View.VISIBLE ->menu.visibility = View.GONE
            View.GONE -> menu.visibility = View.VISIBLE
            else -> //Todo: Error
                Log.e("Error", "123")
        }
    }

    private fun menuOcClick(){
        binding.ivBtnSettingsMenu.setOnClickListener{
            menuVisibilitySwitch(binding.scrollLlReaderMenuSettings)
        }
        binding.ivBtnDownloaderMenu.setOnClickListener{
           // todo download image
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

    private fun menuVisibilitySwitch(layout:LinearLayout){
        layout.visibility = when(binding.scrollLlReaderMenuSettings.visibility){
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