package ru.android.hikanumaruapp.presentasion.search.adapter

import android.content.Context
import android.media.Image
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.core.content.res.ResourcesCompat.getColor
import coil.load
import coil.transform.RoundedCornersTransformation
import ru.android.hikanumaruapp.R
import ru.android.hikanumaruapp.databinding.SearchMangaItemBinding
import ru.android.hikanumaruapp.data.model.MangaMainModel
import ru.android.hikanumaruapp.utilits.UIUtils
import ru.android.hikanumaruapp.utilits.recyclerviews.RecyclerViewClickListener
import java.lang.System.load

class SearchAdapter(private val context: Context,
    private val listenerClick: RecyclerViewClickListener
): RecyclerView.Adapter<SearchAdapter.ViewHolder>(), UIUtils
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
            val readmangaUrlImage = "https://staticrm.rmr.rocks"
            binding.apply {

                Image.load(readmangaUrlImage + model.linkImageView) {
                    crossfade(true)
                    crossfade(500)
                    transformations(RoundedCornersTransformation(itemView.context.pixelToDP(8f)
                        .toFloat()))
                }

                TVTitleName.text = model.title

                TVTypeManga.text = model.type
                TVTypeManga.setTextColor(when(model.type){
                    "Ongoing" -> getColor(context.resources, R.color.blue, null)
                    "" -> getColor(context.resources, R.color.black, null)
                    else -> getColor(context.resources, R.color.black, null)
                })
            }

        }
    }
}