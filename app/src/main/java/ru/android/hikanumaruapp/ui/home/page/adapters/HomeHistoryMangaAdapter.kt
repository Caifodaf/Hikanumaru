package ru.android.hikanumaruapp.ui.home.page.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import ru.android.hikanumaruapp.utilits.recyclerviews.RecyclerViewClickListener
import ru.android.hikanumaruapp.R
import ru.android.hikanumaruapp.databinding.MangaMainItemBinding
import ru.android.hikanumaruapp.model.MangaMainModel

class HomeHistoryMangaAdapter(
    private val listenerClick: RecyclerViewClickListener
    ): RecyclerView.Adapter<HomeHistoryMangaAdapter.ViewHolder>()
{
    private val itemViewModels = mutableListOf<MangaMainModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = MangaMainItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.rlBlockMangaMainItem.setOnClickListener {
            listenerClick.onRecyclerViewItemClick(holder.binding.rlBlockMangaMainItem,
                itemViewModels[position])
        }
        holder.bind()
    }

    fun setMain(list: List<MangaMainModel>) {
        itemViewModels.clear()
        itemViewModels.addAll(list)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = itemViewModels.size

    inner class ViewHolder(val binding: MangaMainItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind() {
            Log.d("ListT", "Load in bind holder history - $itemViewModels")
            val model = itemViewModels[absoluteAdapterPosition]

            binding.tvTitleMangaItem.text = model.title
            binding.tvTypeMangaItem.text = model.type

            when(model.state){
                ""->{
                    binding.tvMangaStateMainItem.visibility = GONE
                }
                "Read"->{
                    binding.tvMangaStateMainItem.visibility = VISIBLE
                    binding.tvMangaStateMainItem.text =
                        itemView.context.getString(R.string.status_read_reading_manga_item)
                }
                "In plans"->{
                    binding.tvMangaStateMainItem.visibility = VISIBLE
                    binding.tvMangaStateMainItem.text =
                        itemView.context.getString(R.string.status_read_plans_manga_item)
                }
                "Drop"->{
                    binding.tvMangaStateMainItem.visibility = VISIBLE
                    binding.tvMangaStateMainItem.text =
                        itemView.context.getString(R.string.status_read_dropped_manga_item)
                }
                else->{
                    binding.tvMangaStateMainItem.visibility = GONE
                }
            }

            binding.ivMangaItem.load(model.linkImageView){
                placeholder(R.color.grey)
                //todo add error
                //error(R.drawable.placeholder)
                //size(240,400)
                transformations(RoundedCornersTransformation(7f*4))
            }
        }
    }
}