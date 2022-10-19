package ru.android.hikanumaruapp.ui.mangapage.moredata.sheetdialog

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.android.hikanumaruapp.R
import ru.android.hikanumaruapp.databinding.BottomSheetMangaPageBinding
import ru.android.hikanumaruapp.model.Manga
import java.lang.reflect.Type


class FragmentMangaPageBottomSheetPanel : BottomSheetDialogFragment() {

    companion object {
    }

    lateinit var binding: BottomSheetMangaPageBinding

    private lateinit var page: MutableList<Manga>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = BottomSheetMangaPageBinding.bind(inflater.inflate(R.layout.bottom_sheet_manga_page, container))
        return binding.root
    }

    override fun onStart() {
        super.onStart()

        binding.rlInfoBottomSheet.visibility = VISIBLE

        loadArgs()

        if (!page.isNullOrEmpty())
            updateDate()
        else
        //TODO error
            super.onDestroy()
    }

    private fun loadArgs(){
        val str: String? = arguments?.getString("list")
        val collectionType: Type = object : TypeToken<Collection<Manga?>?>() {}.type
        val enums: Collection<Manga> = Gson().fromJson(str, collectionType)
        page = enums.toMutableList()
    }

    @SuppressLint("SetTextI18n")
    private fun updateDate() {
        val score = page[0].score

        // Load emoji
        binding.tvScoreSmile.text = (getEmojiByUnicode(0x2B50)) + " $score"
        binding.tvViewsSmile.text = (getEmojiByUnicode(0x1F440))
        binding.tvLikesSmile.text = (getEmojiByUnicode(0x1F496))
        binding.tvCommentsSmile.text = (getEmojiByUnicode(0x1F4AC))

        // Load all counts
        binding.tvCountScore.text = page[0].info[0].scoreCount
        binding.tvCountViews.text = page[0].info[0].viewCount
        binding.tvCountLikes.text = page[0].info[0].likesCount
        binding.tvCountComments.text = page[0].info[0].commentCount

        binding.tvCountLoadLocal.text = "${page[0].chapterCountLoadLocal}/${page[0].chapterCount}"
        binding.tvCountViewsLocal.text = "${page[0].chapterCountViewLocal}/${page[0].chapterCount}"

        binding.tvTypeData.text = when (page[0].status) {
            0 -> requireContext().getString(R.string.manga_text)
            1 -> requireContext().getString(R.string.manhva_text)
            2 -> requireContext().getString(R.string.manhva_text)
            3 -> requireContext().getString(R.string.web_text)
            4 -> requireContext().getString(R.string.comics_text)
            5 -> requireContext().getString(R.string.rumanga_text)
            else -> requireContext().getString(R.string.manga_text)
        }

        binding.tvAuthorData.text = page[0].info[0].author[0]

        if(page[0].info[0].art.isNullOrEmpty())
            binding.tvArtData.text = binding.tvAuthorData.text
        else
            binding.tvArtData.text = page[0].info[0].art!![0]

        if (page[0].info[0].publisher.isNullOrEmpty())
            binding.rlPublisher.visibility = GONE
        else
            binding.tvPublisherData.text = page[0].info[0].publisher!![0].substringBefore(',')

        binding.tvStateMangaData.text =
            when (page[0].status) {
                0 -> requireContext().getString(R.string.anons_text)
                1 -> requireContext().getString(R.string.ongoing_text)
                2 -> requireContext().getString(R.string.paused_text)
                3 -> requireContext().getString(R.string.ended_text)
                else -> requireContext().getString(R.string.ongoing_text)
            }

        if(page[0].status == 0){
            binding.rlStateTranslate.visibility = GONE
            binding.rlTranslators.visibility = GONE
        }else{
            binding.tvStateTranslateData.text =
                when (page[0].statusTranslated) {
                    0 -> requireContext().getString(R.string.anons_text)
                    1 -> requireContext().getString(R.string.ongoing_text)
                    2 -> requireContext().getString(R.string.paused_text)
                    3 -> requireContext().getString(R.string.ended_text)
                    else -> requireContext().getString(R.string.ongoing_text)
                }
            binding.tvTranslatorsData.text = page[0].info[0].translators?.get(0)?.substringBefore(',') ?: "Нет"
        }

        binding.tvDateRelisedData.text = page[0].info[0].dateRelise
    }

    private fun getEmojiByUnicode(unicode: Int): String? {
        return String(Character.toChars(unicode))
    }

}