package ru.android.hikanumaruapp.presentasion

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import ru.android.hikanumaruapp.R
import ru.android.hikanumaruapp.databinding.ActivityMainBinding

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    //private val vmSearch by viewModels<SearchTabViewModel>()

    //private val HomeFragment = HomeFragment()
    //private val RadioFragment = RadioFragment()
    //private val NewsFragment = NewsFragment()
    //private val ProfileFragment = ProfileFragment()
    //private val SettingsFragment = SettingsFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        navView.setupWithNavController(navController)

        binding.apply {
            //searchTabInit()
        }

//        navView.setOnNavigationItemSelectedListener(object :
//            BottomNavigationView.OnNavigationItemSelectedListener {
//            override fun onNavigationItemSelected(item: MenuItem): Boolean {
//                when(item.itemId){
//                    R.id.navigation_home -> loadFragment(HomeFragment)
//                    R.id.navigation_radio -> loadFragment(RadioFragment)
//                    R.id.navigation_news -> loadFragment(NewsFragment)
//                    R.id.navigation_profile -> loadFragment(ProfileFragment)
//                    R.id.navigation_settings -> loadFragment(SettingsFragment)
//                }
//                return true
//            }
//        })



    }

    //private fun ActivityMainBinding.searchTabInit() {
    //    vmSearch.initSearchTab(ETSearchBar, ImageFilters, ImageRandom)
//
    //    ImageFilters.setOnClickListener { vmSearch.randomButton() }
    //    ImageRandom.setOnClickListener { vmSearch.filterButton() }
//
    //    ETSearchBar.doOnTextChanged { text, start, count, after ->
    //        vmSearch.apply{
    //            applicationContext.addListenerET(text,start,count,after)
    //        }
    //    }
    //}

    //private fun loadFragment(fragment: Fragment) {
    //    val transaction: FragmentTransaction = supportFragmentManager.beginTransaction();
    //    transaction.replace(R.id.nav_host_fragment_activity_main, fragment).commit();
    //}
}