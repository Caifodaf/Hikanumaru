package ru.android.hikanumaruapp.presentasion.mangapage.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.android.hikanumaruapp.utilits.recyclerviews.RecyclerViewClickListener
import ru.android.hikanumaruapp.R
import ru.android.hikanumaruapp.databinding.MangaPageTextItemBinding
import ru.android.hikanumaruapp.data.model.MangaPageTextDate

class MangaPageTextAdapter(
    private val listenerClick: RecyclerViewClickListener
    ): RecyclerView.Adapter<MangaPageTextAdapter.ViewHolder>()
{
    private val itemViewModels = mutableListOf<MangaPageTextDate>()

    private var set:Int = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = MangaPageTextItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.clBlockTestItem.setOnClickListener {
            listenerClick.onRecyclerViewItemClick(
                holder.binding.clBlockTestItem,
                itemViewModels[position]
            )
        }
        holder.bind(set)
        set++
    }

    fun setMain(list: List<MangaPageTextDate>) {
        itemViewModels.clear()
        itemViewModels.addAll(list)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = 5

    inner class ViewHolder(val binding: MangaPageTextItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(set: Int) {
            Log.d("ListT", "Load in bind holder history - $itemViewModels")
            val model = itemViewModels[0]

            when(set){
                0->{ // Type
                    binding.tvTextItem.text = when (model.type) {
                        0 -> itemView.context.getString(R.string.type_manga_text_item)
                        1 -> itemView.context.getString(R.string.type_manhya_text_item)
                        2 -> itemView.context.getString(R.string.type_manhva_text_item)
                        3 -> itemView.context.getString(R.string.type_web_text_item)
                        4 -> itemView.context.getString(R.string.type_comics_text_item)
                        5 -> itemView.context.getString(R.string.type_rumanga_text_item)
                        else -> itemView.context.getString(R.string.type_manga_text_item)
                    }
                }
                1->{ // Status
                    binding.tvTextItem.text = when (model.status) {
                        0 -> itemView.context.getString(R.string.status_anons_text_item)
                        1 -> itemView.context.getString(R.string.status_ongoing_text_item)
                        2 -> itemView.context.getString(R.string.status_paused_text_item)
                        3 -> itemView.context.getString(R.string.status_ended_text_item)
                        else -> itemView.context.getString(R.string.status_ongoing_text_item)
                    }
                }
                3->{ // Status Translated
                    binding.tvTextItem.text = when (model.status) {
                        0 -> itemView.context.getString(R.string.status_translate_anons_text_item)
                        1 -> itemView.context.getString(R.string.status_translate_ongoing_text_item)
                        2 -> itemView.context.getString(R.string.status_translate_paused_text_item)
                        3 -> itemView.context.getString(R.string.status_translate_ended_text_item)
                        else -> itemView.context.getString(R.string.status_translate_ongoing_text_item)
                    }
                }
                2->{ // Year
                    binding.tvTextItem.text = itemView.context.getString(R.string.year_text_item) + " ${model.year}"
                }
                4->{ // Author
                    if (!model.author.isNullOrEmpty())
                        binding.tvTextItem.text = itemView.context.getString(R.string.author_text_item) + " ${model.author}"
                }
            }
        }
    }
}