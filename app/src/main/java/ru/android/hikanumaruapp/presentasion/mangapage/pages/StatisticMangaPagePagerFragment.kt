package ru.android.hikanumaruapp.presentasion.mangapage.pages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.android.hikanumaruapp.databinding.StatisticTabMangaPageBinding
import ru.android.hikanumaruapp.presentasion.mangapage.MangaPageViewModel


class StatisticMangaPagePagerFragment(private val vm: MangaPageViewModel) : Fragment() {

    private var _binding: StatisticTabMangaPageBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = StatisticTabMangaPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //binding.TVNameManga.text = "fhjlkavhlkjhdklhlcl"
    }



}