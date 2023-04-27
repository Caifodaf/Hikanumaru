package ru.android.hikanumaruapp.presentasion.home.page.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import ru.android.hikanumaruapp.utilits.recyclerviews.RecyclerViewClickListener
import ru.android.hikanumaruapp.R
import ru.android.hikanumaruapp.databinding.JournalsMainItemBinding
import ru.android.hikanumaruapp.data.model.JournalMainModel

class HomeJournalMangaAdapter(
    private val listenerClick: RecyclerViewClickListener
    ): RecyclerView.Adapter<HomeJournalMangaAdapter.ViewHolder>()
{
    private val itemViewModels = mutableListOf<JournalMainModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = JournalsMainItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.rlBackJournalItem.setOnClickListener {
            listenerClick.onRecyclerViewItemClick(holder.binding.rlBackJournalItem,
                itemViewModels[position])
        }
        holder.bind()
    }

    fun setMain(list: List<JournalMainModel>) {
        itemViewModels.clear()
        itemViewModels.addAll(list)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = itemViewModels.size

    inner class ViewHolder(val binding: JournalsMainItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind() {
            Log.d("ListT", "Load in bind holder history - $itemViewModels")
            val model = itemViewModels[absoluteAdapterPosition]

            binding.tvTitleJournalItem.text = model.title
            binding.rlBackJournalItem.background = when (model.typeColor) {
                "1" -> (ContextCompat.getDrawable(itemView.context, R.drawable.gradient_blue))
                "2" -> (ContextCompat.getDrawable(itemView.context, R.drawable.gradient_green))
                "3" -> (ContextCompat.getDrawable(itemView.context, R.drawable.gradient_orange))
                "4" -> (ContextCompat.getDrawable(itemView.context, R.drawable.gradient_red))
                "5" -> (ContextCompat.getDrawable(itemView.context, R.drawable.gradient_pink))
                "6" -> (ContextCompat.getDrawable(itemView.context, R.drawable.gradient_yellow))
                else -> (ContextCompat.getDrawable(itemView.context, R.drawable.gradient_blue))
            }
        }
    }
}