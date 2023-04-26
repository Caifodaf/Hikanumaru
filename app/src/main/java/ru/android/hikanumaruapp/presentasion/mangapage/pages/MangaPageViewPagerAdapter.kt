package ru.android.hikanumaruapp.presentasion.mangapage.pages

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import ru.android.hikanumaruapp.presentasion.mangapage.MangaPageViewModel

private const val NUM_TABS = 3

class MangaPageViewPagerAdapter(
    val vm: MangaPageViewModel,
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int = NUM_TABS

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> GeneralMangaPagePagerFragment(vm)
            1 -> InformationMangaPagePagerFragment(vm)
            2 -> StatisticMangaPagePagerFragment(vm)
            else -> GeneralMangaPagePagerFragment(vm)
        }
    }
}