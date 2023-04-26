package ru.android.hikanumaruapp.presentasion.search.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.android.hikanumaruapp.databinding.SearchMangaItemBinding
import ru.android.hikanumaruapp.model.MangaMainModel
import ru.android.hikanumaruapp.utilits.recyclerviews.RecyclerViewClickListener

class SearchAdapter(
    private val listenerClick: RecyclerViewClickListener
): RecyclerView.Adapter<SearchAdapter.ViewHolder>()
{
    private val itemViewModels = mutableListOf<MangaMainModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = SearchMangaItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.CCSearchItem.setOnClickListener {
            listenerClick.onRecyclerViewItemClick(holder.binding.CCSearchItem,
                itemViewModels[position])
        }
        holder.bind(itemViewModels[position])
    }

    override fun getItemCount(): Int = itemViewModels.size

    fun setMain(list: List<MangaMainModel>) {
        itemViewModels.clear()
        itemViewModels.addAll(list)
        notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: SearchMangaItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(model: MangaMainModel) {
            Log.d("ListT", "Load in bind holder history - $model")

        }
    }
}