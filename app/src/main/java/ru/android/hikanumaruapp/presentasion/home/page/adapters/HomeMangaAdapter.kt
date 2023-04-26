package ru.android.hikanumaruapp.presentasion.home.page.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.ui.geometry.CornerRadius
import androidx.core.content.res.ResourcesCompat.getColor
import androidx.recyclerview.widget.RecyclerView
import coil.load
import ru.android.hikanumaruapp.R
import ru.android.hikanumaruapp.databinding.MangaMainItemBinding
import ru.android.hikanumaruapp.databinding.MangaMainLastedItemBinding
import ru.android.hikanumaruapp.model.MangaList
import ru.android.hikanumaruapp.utilits.recyclerviews.RecyclerViewClickListener

class HomeMangaAdapter(private val context: Context,
    private val listenerClick: RecyclerViewClickListener,
): RecyclerView.Adapter<RecyclerView.ViewHolder>()
{
    private val itemViewModels = mutableListOf<MangaList>()

    companion object{
        const val MANGA_TYPE = 0
        const val SEE_MORE_TYPE = 1
    }

    override fun getItemViewType(position: Int): Int {
        return when(position){
            itemViewModels.size-1 -> MANGA_TYPE
            itemViewModels.size -> SEE_MORE_TYPE
            else -> MANGA_TYPE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when(viewType){
            MANGA_TYPE -> {
                val binding = MangaMainItemBinding
                    .inflate(LayoutInflater.from(parent.context), parent, false)
                return MangaViewHolder(binding)
            }
            SEE_MORE_TYPE -> {
                val binding = MangaMainLastedItemBinding
                    .inflate(LayoutInflater.from(parent.context), parent, false)
                return MoreViewHolder(binding)
            }
            else -> {
                val binding = MangaMainItemBinding
                    .inflate(LayoutInflater.from(parent.context), parent, false)
                return MangaViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder.itemViewType){
            MANGA_TYPE -> {
                val mangaViewHolder: MangaViewHolder = holder as MangaViewHolder
                mangaViewHolder.binding.CCMainMangaBlock.setOnClickListener {
                    listenerClick.onRecyclerViewItemClick(mangaViewHolder.binding.CCMainMangaBlock,
                        itemViewModels[position])
                }
                mangaViewHolder.bind(itemViewModels[position])
            }
            SEE_MORE_TYPE -> {
                val moreViewHolder: MoreViewHolder = holder as MoreViewHolder
                moreViewHolder.binding.CCMainMangaLastedBlock.setOnClickListener {
                    listenerClick.onRecyclerViewItemClick(moreViewHolder.binding.CCMainMangaLastedBlock,
                        itemViewModels[position])
                }
                moreViewHolder.bind()
            }
            else -> {
                val mangaViewHolder: MangaViewHolder = holder as MangaViewHolder
                mangaViewHolder.binding.CCMainMangaBlock.setOnClickListener {
                    listenerClick.onRecyclerViewItemClick(mangaViewHolder.binding.CCMainMangaBlock,
                        itemViewModels[position])
                }
                mangaViewHolder.bind(itemViewModels[position])
            }
        }
    }

    fun setMain(list: List<MangaList>) {
        itemViewModels.clear()
        itemViewModels.addAll(list)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = itemViewModels.size

    inner class MangaViewHolder(val binding: MangaMainItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(model: MangaList) {
            Log.d("ListT", "Load in bind holder history - $itemViewModels")
            binding.apply {
                ImageManga.load(model.coverLinksLast){
                    CornerRadius(8f)
                }

               // TVMangaUserStatus // todo
               //when(model.state){
               //    ""->{
               //        binding.tvMangaStateMainItem.visibility = View.GONE
               //    }
               //    "Read"->{
               //        binding.tvMangaStateMainItem.visibility = View.VISIBLE
               //        binding.tvMangaStateMainItem.text =
               //            itemView.context.getString(R.string.status_read_reading_manga_item)
               //    }
               //    "In plans"->{
               //        binding.tvMangaStateMainItem.visibility = View.VISIBLE
               //        binding.tvMangaStateMainItem.text =
               //            itemView.context.getString(R.string.status_read_plans_manga_item)
               //    }
               //    "Drop"->{
               //        binding.tvMangaStateMainItem.visibility = View.VISIBLE
               //        binding.tvMangaStateMainItem.text =
               //            itemView.context.getString(R.string.status_read_dropped_manga_item)
               //    }
               //    else->{
               //        binding.tvMangaStateMainItem.visibility = View.GONE
               //    }
               //}

                TVMangaTitle.text = model.title

                TVTypeManga.text = model.type
                TVTypeManga.setTextColor(when(model.type){
                    "Ongoing" -> getColor(context.resources, R.color.blue,null)
                    "" -> getColor(context.resources, R.color.black,null)
                    else -> getColor(context.resources, R.color.black,null)
                })
            }
        }
    }
    inner class MoreViewHolder(val binding: MangaMainLastedItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind() {}
    }


}