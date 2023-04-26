package ru.android.hikanumaruapp.presentasion.home.page.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.android.hikanumaruapp.utilits.recyclerviews.RecyclerViewClickListener
import ru.android.hikanumaruapp.databinding.GenresMainItemBinding
import ru.android.hikanumaruapp.model.GenresMainModel

class HomeGenresMangaAdapter(
    private val listenerClick: RecyclerViewClickListener
    ): RecyclerView.Adapter<HomeGenresMangaAdapter.ViewHolder>()
{
    private val itemViewModels = mutableListOf<GenresMainModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = GenresMainItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.rlBackGenresItem.setOnClickListener {
            listenerClick.onRecyclerViewItemClick(holder.binding.rlBackGenresItem,
                itemViewModels[position])
        }
        holder.bind()
    }

    fun setMain(list: List<GenresMainModel>) {
        itemViewModels.clear()
        itemViewModels.addAll(list)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = itemViewModels.size

    inner class ViewHolder(val binding: GenresMainItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind() {
            Log.d("ListT", "Load in bind holder history - ${itemViewModels[absoluteAdapterPosition]}")
            val model = itemViewModels[absoluteAdapterPosition]

            binding.tvTitleGenresItem.text = model.title

            //binding.rlBackGenresItem.background = when (model.typeColor) {
            //    "1" -> (ContextCompat.getDrawable(itemView.context, R.drawable.gradient_blue))
            //    "2" -> (ContextCompat.getDrawable(itemView.context, R.drawable.gradient_green))
            //    "3" -> (ContextCompat.getDrawable(itemView.context, R.drawable.gradient_orange))
            //    "4" -> (ContextCompat.getDrawable(itemView.context, R.drawable.gradient_red))
            //    "5" -> (ContextCompat.getDrawable(itemView.context, R.drawable.gradient_pink))
            //    "6" -> (ContextCompat.getDrawable(itemView.context, R.drawable.gradient_yellow))
            //    else -> (ContextCompat.getDrawable(itemView.context, R.drawable.gradient_blue))
            //}
        }
    }
}