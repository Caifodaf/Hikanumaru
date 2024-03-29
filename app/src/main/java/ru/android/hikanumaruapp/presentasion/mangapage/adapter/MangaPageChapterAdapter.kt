package ru.android.hikanumaruapp.presentasion.mangapage.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.android.hikanumaruapp.utilits.recyclerviews.RecyclerViewClickListener
import ru.android.hikanumaruapp.R
import ru.android.hikanumaruapp.databinding.ChapterMangaPageItemBinding
import ru.android.hikanumaruapp.data.model.Chapter

class MangaPageChapterAdapter(
    private val listenerClick: RecyclerViewClickListener
    ): RecyclerView.Adapter<MangaPageChapterAdapter.ViewHolder>()
{
    private val itemViewModels = mutableListOf<Chapter>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ChapterMangaPageItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.CCMainBlockChapter.setOnClickListener {
            listenerClick.onRecyclerViewItemClick(
                holder.binding.CCMainBlockChapter,
                itemViewModels[position]
            )
        }
        holder.bind()
    }

    override fun getItemCount(): Int = itemViewModels.size

    fun setMain(list: List<Chapter>) {
        itemViewModels.clear()
        itemViewModels.addAll(list)
        notifyDataSetChanged()
    }

    fun reverseList() {
        val itemViewModelsReverse = itemViewModels.reversed()
        itemViewModels.clear()
        itemViewModels.addAll(itemViewModelsReverse)
        notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: ChapterMangaPageItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind() {
            val model = itemViewModels[absoluteAdapterPosition]
            binding.TVChapterNumbers.text =
                itemView.context.getString(R.string.tom_chapter_manga_page_item) +
                        " " + model.tom + " — " +
                        itemView.context.getString(R.string.chapter_chapter_manga_page_item) +
                        " " + model.numberList

            if (model.description.isNullOrEmpty() || model.description == "")
                binding.TVCahpterDescription.text =
                    itemView.context.getString(R.string.description_nan_chapter_manga_page_item)
            else
                binding.TVCahpterDescription.text = model.description

            binding.TVChapterDatePublish.text = model.datePublished

        }
    }
}