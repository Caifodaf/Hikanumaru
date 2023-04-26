package ru.android.hikanumaruapp.presentasion.mangapage.pages

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import ru.android.hikanumaruapp.R
import ru.android.hikanumaruapp.databinding.InfoTabMangaPageBinding
import ru.android.hikanumaruapp.presentasion.mangapage.MangaPageViewModel

@SuppressLint("UseCompatLoadingForDrawables", "SetTextI18n")
@AndroidEntryPoint
class InformationMangaPagePagerFragment(private val vm: MangaPageViewModel) : Fragment() {

    private var _binding: InfoTabMangaPageBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = InfoTabMangaPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            updateDataUI()
        }
    }

    private fun InfoTabMangaPageBinding.updateDataUI() {
        val data = vm.listPage.value!!

        TVName.visibility = if(data.title.isNotBlank()){
            TVName.text = data.description
            View.VISIBLE
        }else
            View.GONE

        TVAltName.visibility = if(data.title.isNotBlank()){
            TVAltName.text = data.additionalTitle
            if (!data.othersTitle.isNullOrEmpty()){
                data.othersTitle!!.forEachIndexed { index, othersTitle ->
                    TVAltName.text = TVAltName.text.toString() + "/n" + othersTitle
                }
            }
            View.VISIBLE
        }else
            View.GONE

        TVType.text = when(data.type){
            "manga" -> resources.getString(R.string.manga)
            "manhwa" -> resources.getString(R.string.manhva_text)
            "" -> resources.getString(R.string.manga)
            "" -> resources.getString(R.string.manga)
            "" -> resources.getString(R.string.manga)
            "" -> resources.getString(R.string.manga)
            else -> ""
        }

        TVReleaseYear.text = data.releaseYear.toString()

        TVStatus.text = when(data.publicationStatus){
            "ongoing" -> resources.getString(R.string.ongoing_text)
            "" -> resources.getString(R.string.manhva_text)
            "" -> resources.getString(R.string.manga)
            "" -> resources.getString(R.string.manga)
            "" -> resources.getString(R.string.manga)
            "" -> resources.getString(R.string.manga)
            else -> ""
        }

        TVStatusTranslate.text = when(data.publicationStatus){
            "continues" -> resources.getString(R.string.contines)
            "" -> resources.getString(R.string.manhva_text)
            "" -> resources.getString(R.string.manga)
            "" -> resources.getString(R.string.manga)
            "" -> resources.getString(R.string.manga)
            "" -> resources.getString(R.string.manga)
            else -> ""
        }

        TVAgeRating.text = data.ageRating.toString()
    }

}