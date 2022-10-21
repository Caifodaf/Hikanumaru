package ru.android.hikanumaruapp.ui.home.page.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import ru.android.hikanumaruapp.R
import ru.android.hikanumaruapp.utilits.RecyclerViewClickListener
import ru.android.hikanumaruapp.databinding.LargePopularMainMangaItemBinding
import ru.android.hikanumaruapp.model.MangaPopularMainModel

class HomePopularMangaAdapter(
    private val listenerClick: RecyclerViewClickListener
    ): RecyclerView.Adapter<HomePopularMangaAdapter.ViewHolder>()
{
    private val itemViewModels = mutableListOf<MangaPopularMainModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = LargePopularMainMangaItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.rlBlockPopularMangaItem.setOnClickListener {
            listenerClick.onRecyclerViewItemClick(holder.binding.rlBlockPopularMangaItem,
                itemViewModels[position])
        }
        holder.bind()
    }

    override fun getItemCount(): Int = itemViewModels.size

    fun setMain(list: List<MangaPopularMainModel>) {
        itemViewModels.clear()
        itemViewModels.addAll(list)
        notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: LargePopularMainMangaItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind() {
            Log.d("ListT", "Load in bind holder history - $itemViewModels")
            val model = itemViewModels[absoluteAdapterPosition]

            binding.tvTitlePopularMangaItem.text = model.title
            binding.tvTypeMangaMainItem.text = model.type
            binding.tvAuthorPopularMangaItem.text = model.author
            binding.tvDescriptionPopularMangaItem.text = model.description

            binding.ivPopularMangaItem.load(model.linkImageView){
                placeholder(R.color.grey)
                //todo add error
                //error(R.drawable.placeholder)
                //size(240,400)
                transformations(RoundedCornersTransformation(28f*4))
            }

            // Auto-size Title\Description on block
            binding.tvTitlePopularMangaItem.post(Runnable {
                when (binding.tvTitlePopularMangaItem.lineCount) {
                    in 2..100 -> {
                        binding.tvTitlePopularMangaItem.maxLines = 2
                        binding.tvDescriptionPopularMangaItem.maxLines = 3
                    }
                    else -> {
                        binding.tvTitlePopularMangaItem.maxLines = 1
                        binding.tvDescriptionPopularMangaItem.maxLines = 4
                    }
                }
            })

            binding.ratingbarItem.numStars = 5
            binding.ratingbarItem.rating = (model.score)?.toFloat() ?: 0.0f
        }

    }
}