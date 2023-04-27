package ru.android.hikanumaruapp.presentasion.profile.folders

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import ru.android.hikanumaruapp.R
import ru.android.hikanumaruapp.data.model.library.FolderLibraryModel
import ru.android.hikanumaruapp.databinding.FolderTagsLibraryProfileItemBinding
import ru.android.hikanumaruapp.utilits.recyclerviews.RecyclerViewClickListener

class FoldersLibraryAdapter(
    private val listenerClick: RecyclerViewClickListener
    ): RecyclerView.Adapter<FoldersLibraryAdapter.ViewHolder>()
{
    private var selectedItemPos = 0
    private var lastItemSelectedPos = 0

    private val itemViewModels = mutableListOf<FolderLibraryModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = FolderTagsLibraryProfileItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        if(position == selectedItemPos)
            holder.selectedBg()
        else
            holder.defaultBg()

        holder.binding.CCMainTagsItem.setOnClickListener {
            selectedItemPos = position

            notifyItemChanged(lastItemSelectedPos)
            lastItemSelectedPos = selectedItemPos

            notifyItemChanged(selectedItemPos)

            // Default OnClickListener
            listenerClick.onRecyclerViewItemClick(holder.binding.CCMainTagsItem,
                itemViewModels[position])
        }
        holder.bind()
    }

    fun setMain(list: List<FolderLibraryModel>) {
        itemViewModels.clear()
        itemViewModels.addAll(list)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = itemViewModels.size

    @SuppressLint("UseCompatLoadingForColorStateLists")
    inner class ViewHolder (val binding: FolderTagsLibraryProfileItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind() {
            Log.d("ListT", "Load in bind FoldersLibraryViewHolder - $itemViewModels")
            val model = itemViewModels[absoluteAdapterPosition]

            binding.TVTitleItem.text = model.idName
            binding.TVCounterItem
        }


        fun defaultBg() {
            binding.TVTitleItem.setTextColor(ContextCompat.getColor(itemView.context, R.color.grey_light_alternative_6))
            binding.CCMainTagsItem.backgroundTintList = (itemView.context).resources.getColorStateList(R.color.grey_light_alternative_1)
        }

        fun selectedBg() {
            binding.TVTitleItem.setTextColor(ContextCompat.getColor(itemView.context, R.color.white))
            binding.CCMainTagsItem.backgroundTintList = (itemView.context).resources.getColorStateList(R.color.black)
        }
    }
}