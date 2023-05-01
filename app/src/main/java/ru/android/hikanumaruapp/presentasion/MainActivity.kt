package ru.android.hikanumaruapp.presentasion

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import ru.android.hikanumaruapp.R
import ru.android.hikanumaruapp.databinding.ActivityMainBinding
import ru.android.hikanumaruapp.presentasion.home.page.HomeFragment
import ru.android.hikanumaruapp.presentasion.news.NewsFragment
import ru.android.hikanumaruapp.presentasion.profile.ProfileFragment
import ru.android.hikanumaruapp.presentasion.radio.RadioFragment
import ru.android.hikanumaruapp.presentasion.settings.SettingsFragment

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    //private val homeFragment = HomeFragment()
    //private val radioFragment = RadioFragment()
    //private val newsFragment = NewsFragment()
    //private val profileFragment = ProfileFragment()
    //private var settingsFragment = SettingsFragment()
//
    //private val fragmentManager = supportFragmentManager
    //private var activeFragment: Fragment = HomeFragment()

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHostFragment = supportFragmentManager.findFragmentById(
            R.id.nav_host_fragment_activity_main
        ) as NavHostFragment
        navController = navHostFragment.navController

        // Setup the bottom navigation view with navController
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.nav_view)
        bottomNavigationView.setupWithNavController(navController)

        //val navView: BottomNavigationView = findViewById(R.id.nav_view)
        //val navController= findNavController(R.id.nav_host_fragment_activity_main)
        //navView.setupWithNavController(navController)
    }
}












//fragmentManager.beginTransaction().apply {
//    add(R.id.container, newsFragment, getString(R.string.title_home)).hide(newsFragment)
//    add(R.id.container, radioFragment, getString(R.string.title_radio)).hide(radioFragment)
//    add(R.id.container, profileFragment, getString(R.string.title_profile)).hide(profileFragment)
//    add(R.id.container, settingsFragment, getString(R.string.title_settings)).hide(settingsFragment)
//    add(R.id.container, homeFragment, getString(R.string.title_news))
//}.commit()
//
//navView.setOnNavigationItemSelectedListener { menuItem ->
//    when (menuItem.itemId) {
//        R.id.navigation_home -> {
//            fragmentManager.beginTransaction().hide(activeFragment).show(homeFragment).commit()
//            activeFragment = homeFragment
//            true
//        }
//        R.id.navigation_radio -> {
//            fragmentManager.beginTransaction().hide(activeFragment).show(radioFragment).commit()
//            activeFragment = radioFragment
//            true
//        }
//        R.id.navigation_news -> {
//            fragmentManager.beginTransaction().hide(activeFragment).show(newsFragment).commit()
//            activeFragment = newsFragment
//            true
//        }
//        R.id.navigation_profile -> {
//            fragmentManager.beginTransaction().hide(activeFragment).show(profileFragment).commit()
//            activeFragment = profileFragment
//            true
//        }
//        R.id.navigation_settings -> {
//            fragmentManager.beginTransaction().hide(activeFragment).show(settingsFragment).commit()
//            activeFragment = settingsFragment
//            true
//        }
//        else -> false
//    }
//}


//private lateinit var binding: ActivityMainBinding

//binding = ActivityMainBinding.inflate(layoutInflater)
//setContentView(binding.root)
//
//val navView: BottomNavigationView = binding.navView
//
//val navController = findNavController(R.id.nav_host_fragment_activity_main)
//navView.setupWithNavController(navController)
//
//binding.apply {
//    //searchTabInit()
//}

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