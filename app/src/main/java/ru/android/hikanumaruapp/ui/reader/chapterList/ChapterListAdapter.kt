package ru.android.hikanumaruapp.ui.reader.chapterList

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.android.hikanumaruapp.R
import ru.android.hikanumaruapp.databinding.ChapterReaderMenuItemBinding
import ru.android.hikanumaruapp.model.Chapter
import ru.android.hikanumaruapp.utilits.recyclerviews.RecyclerViewClickListener

class ChapterListAdapter(
private val listenerClick: RecyclerViewClickListener
): RecyclerView.Adapter<ChapterListAdapter.ViewHolder>()
{
    private var itemViewModels = mutableListOf<Chapter>()
    private var idLastChapter = ""
    var posLastChapter = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ChapterReaderMenuItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.ccMainBlockChapterReader.setOnClickListener {
            listenerClick.onRecyclerViewItemClick(
                holder.binding.ccMainBlockChapterReader,
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

    fun setSelect(idChapter: String) {
        if (idLastChapter != idChapter) {
            idLastChapter = idChapter
            posLastChapter = itemViewModels.withIndex()
                .first { idChapter == it.value.id }
                .index
            notifyDataSetChanged()
        }
    }

    fun reversePages() {
        itemViewModels = itemViewModels.reversed().toMutableList()
        posLastChapter = itemViewModels.withIndex()
            .first { idLastChapter == it.value.id }
            .index
        notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: ChapterReaderMenuItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind() {
            Log.d("ListT", "Load in bind holder history - $itemViewModels")
            val model = itemViewModels[absoluteAdapterPosition]

            if (posLastChapter == absoluteAdapterPosition)
                binding.ivBtnCheckReadChapterItem.visibility = View.VISIBLE
            else
                binding.ivBtnCheckReadChapterItem.visibility = View.INVISIBLE

            binding.tvChapterNumberMangaPageItem.text =
                itemView.context.getString(R.string.tom_chapter_manga_page_item) +
                        " " + model.tom + " — " +
                        itemView.context.getString(R.string.chapter_chapter_manga_page_item) +
                        " " + model.numberList

            if (model.description.isNullOrEmpty() || model.description == "")
                binding.tvChapterDescriptionMangaPageItem.text =
                    itemView.context.getString(R.string.description_nan_chapter_manga_page_item)
            else
                binding.tvChapterDescriptionMangaPageItem.text = model.description

            binding.tvDatePublisherChapterMangaPageItem.text = model.datePublished

        }
    }
}