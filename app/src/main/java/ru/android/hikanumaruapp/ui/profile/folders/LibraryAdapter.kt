package ru.android.hikanumaruapp.ui.profile.folders

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import ru.android.hikanumaruapp.R
import ru.android.hikanumaruapp.databinding.BigSnapLibraryMangaItemBinding
import ru.android.hikanumaruapp.databinding.LargeLibraryMangaItemBinding
import ru.android.hikanumaruapp.utilits.recyclerviews.RecyclerViewClickListener
import ru.android.hikanumaruapp.databinding.SmallSnapLibraryMangaItemBinding
import ru.android.hikanumaruapp.model.Manga

class LibraryAdapter(
    private val listenerClick: RecyclerViewClickListener,
    val gridLM: GridLayoutManager
): RecyclerView.Adapter<RecyclerView.ViewHolder>()
{
    companion object {
        const val SMALL = 3
        const val BIG = 2
        const val LARGE = 1
    }

    private val itemViewModels = mutableListOf<Manga>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        Log.e("Eadadadadadadadadadadad", "isBookmark ${gridLM.spanCount}")
        when (gridLM.spanCount) {
            LARGE -> {
                val binding = LargeLibraryMangaItemBinding
                    .inflate(LayoutInflater.from(parent.context), parent, false)
                return LargeViewHolder(binding)
            }
            BIG -> {
                val binding = BigSnapLibraryMangaItemBinding
                    .inflate(LayoutInflater.from(parent.context), parent, false)
                return BigViewHolder(binding)
            }
            SMALL -> {
                val binding = SmallSnapLibraryMangaItemBinding
                    .inflate(LayoutInflater.from(parent.context), parent, false)
                return SmallViewHolder(binding)
            }
            else -> {
                val binding = SmallSnapLibraryMangaItemBinding
                    .inflate(LayoutInflater.from(parent.context), parent, false)
                return SmallViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is SmallViewHolder ->{
                (holder as SmallViewHolder).binding.CCMainLibraryItem.setOnClickListener {
                    listenerClick.onRecyclerViewItemClick(holder.binding.CCMainLibraryItem,
                        itemViewModels[position])
                }
            }
            is BigViewHolder ->{
                (holder as BigViewHolder).binding.CCMainLibraryItem.setOnClickListener {
                    listenerClick.onRecyclerViewItemClick(holder.binding.CCMainLibraryItem,
                        itemViewModels[position])
                }
            }
            is LargeViewHolder ->{
                (holder as LargeViewHolder).binding.CCMainLibraryItem.setOnClickListener {
                    listenerClick.onRecyclerViewItemClick(holder.binding.CCMainLibraryItem,
                        itemViewModels[position])
                }
            }
        }

        when(holder){
            is SmallViewHolder -> holder.bind()
            is BigViewHolder ->  holder.bind()
            is LargeViewHolder -> holder.bind()
        }
    }

    override fun getItemCount(): Int = itemViewModels.size

    fun setMain(list: List<Manga>) {
        itemViewModels.clear()
        itemViewModels.addAll(list)
        notifyDataSetChanged()
    }

    inner class LargeViewHolder(val binding: LargeLibraryMangaItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind() {
            Log.d("ListT", "Load in bind holder - $itemViewModels")
            val model = itemViewModels[absoluteAdapterPosition]

            binding.Image.load(model.image){
                placeholder(R.color.grey)
                transformations(RoundedCornersTransformation(7f*4))
            }
            binding.TVTitleName.text = model.name
            binding.TVTypeManga.text = model.type.toString() // todo add types
            binding.TVStatusManga.text = model.status.toString() // todo add status
            binding.TVCounterChapterManga.text = model.chapterCount.toString() // todo rework
            binding.TVScoreRating.text = "0" // todo rework
            binding.RatingBar.progress = model.score!!.toFloat().toInt()
            binding.TVDescriptionManga.text = model.description
            // todo add check stutes user
        }
    }

    inner class BigViewHolder(val binding: BigSnapLibraryMangaItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind() {
            Log.d("ListT", "Load in bind holder - $itemViewModels")
            val model = itemViewModels[absoluteAdapterPosition]

            binding.Image.load(model.image){
                placeholder(R.color.grey)
                transformations(RoundedCornersTransformation(7f*4))
            }
            binding.TVTitle.text = model.name
            binding.TVType.text = model.type.toString() // todo add types
            // todo add check stutes user
        }
    }

    inner class SmallViewHolder(val binding: SmallSnapLibraryMangaItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind() {
            Log.d("ListT", "Load in bind holder - $itemViewModels")
            val model = itemViewModels[absoluteAdapterPosition]

            binding.Image.load(model.image){
                placeholder(R.color.grey)
                transformations(RoundedCornersTransformation(7f*4))
            }
            binding.TVTitle.text = model.name
            binding.TVType.text = model.type.toString() // todo add types
            // todo add check stutes user
        }
    }
}