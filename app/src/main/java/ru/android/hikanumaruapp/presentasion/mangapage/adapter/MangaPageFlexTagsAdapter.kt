package ru.android.hikanumaruapp.presentasion.mangapage.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import ru.android.hikanumaruapp.R
import ru.android.hikanumaruapp.databinding.TagsMangaPageFlexItemBinding
import ru.android.hikanumaruapp.presentasion.mangapage.adapter.model.TagsMangaPageModel
import ru.android.hikanumaruapp.presentasion.mangapage.adapter.model.TagsMangaPageId
import ru.android.hikanumaruapp.utilits.recyclerviews.RecyclerViewClickListener

class MangaPageFlexTagsAdapter(
    private val tagsItemModels: List<TagsMangaPageModel>,
    private val listenerClick: RecyclerViewClickListener,
    private val context: Context
    ): RecyclerView.Adapter<MangaPageFlexTagsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MangaPageFlexTagsAdapter.ViewHolder {
        val binding = TagsMangaPageFlexItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MangaPageFlexTagsAdapter.ViewHolder, position: Int) {
        holder.binding.apply {
            CCMainTagItem.setOnClickListener {
                listenerClick.onRecyclerViewItemClick(CCMainTagItem, tagsItemModels[position])
            }
        }

        holder.bind(tagsItemModels[position])
    }

    override fun getItemCount(): Int = tagsItemModels.size

    inner class ViewHolder(val binding: TagsMangaPageFlexItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(model: TagsMangaPageModel) {
            Log.d("ListT", "Load in bind holder history - $tagsItemModels")

            binding.apply {
                when (model.type) {
                    TagsMangaPageId.DIVIDE -> {
                        TVTag.visibility = View.GONE
                        Image.visibility = View.VISIBLE
                        //Image.setImageResource(R.drawable.circle)
                    }
                    TagsMangaPageId.GENRE_TAG -> TVTag.text = model.text
                    TagsMangaPageId.RATING_TAG -> {
                        Image.visibility = View.VISIBLE
                        TVTag.text = model.text
                    }
                    TagsMangaPageId.YEAR_TAG -> TVTag.text = model.text
                    TagsMangaPageId.TYPE_WORK_TAG -> TVTag.text = model.text
                    TagsMangaPageId.AGE_RATING_TAG -> TVTag.text = model.text
                    TagsMangaPageId.PUBLISH_STATUS_TAG -> {
                        TVTag.text = model.text
                        TVTag.setTextColor(when(model.text){
                            "Ongoing" -> ContextCompat.getColor(context, R.color.blue)
                            "" -> ContextCompat.getColor(context, R.color.black)
                            else -> ContextCompat.getColor(context, R.color.black)
                        })
                    }
                }
            }
        }
    }
}