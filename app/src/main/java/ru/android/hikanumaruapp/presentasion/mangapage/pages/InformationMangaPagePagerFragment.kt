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
            TVName.text = data.title
            View.VISIBLE
        }else
            View.GONE

        TVAltName.visibility = if(data.title.isNotBlank()){
            TVAltName.text = data.additionalTitle
            if (!data.othersTitle.isNullOrEmpty()){
                data.othersTitle!!.forEachIndexed { index, othersTitle ->
                    TVAltName.text = TVAltName.text.toString() + "\n" + othersTitle
                }
            }
            View.VISIBLE
        }else
            View.GONE

        val crTypes = resources.getStringArray(R.array.creativeworks_types)
        var positionCRTypes = crTypes.indexOf(data.type)
        if (positionCRTypes == -1) positionCRTypes = 0
        TVType.text = resources.getStringArray(R.array.creativeworks_type_texts)[positionCRTypes]

        TVReleaseYear.text = data.releaseYear.toString()

        val crStatus = resources.getStringArray(R.array.creativeworks_status_types)
        var positionCRStatus = crStatus.indexOf(data.publicationStatus)
        if (positionCRStatus == -1) positionCRStatus = 0
        TVStatus.text = resources.getStringArray(R.array.creativeworks_status_type_texts)[positionCRStatus]

        val crTranslate = resources.getStringArray(R.array.creativeworks_translation_status_types)
        var positionCRTranslate = crTranslate.indexOf(data.translationStatus)
        if (positionCRTranslate == -1) positionCRTranslate = 0
        TVStatusTranslate.text = resources.getStringArray(R.array.creativeworks_translation_status_type_texts)[positionCRTranslate]

        //TVAgeRating.text = data.ageRating.toString()
        TVAgeRating.text = "13+"
    }

}