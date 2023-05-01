package ru.android.hikanumaruapp.presentasion.mangapage.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.core.view.marginEnd
import androidx.recyclerview.widget.RecyclerView
import ru.android.hikanumaruapp.R
import ru.android.hikanumaruapp.data.model.mangapage.TagsMangaPageId
import ru.android.hikanumaruapp.data.model.mangapage.TagsMangaPageModel
import ru.android.hikanumaruapp.databinding.TagsMangaPageFlexItemBinding
import ru.android.hikanumaruapp.utilits.UIUtils
import ru.android.hikanumaruapp.utilits.recyclerviews.RecyclerViewClickListener


class MangaPageFlexTagsAdapter(
    private val listenerClick: RecyclerViewClickListener,
): RecyclerView.Adapter<MangaPageFlexTagsAdapter.ViewHolder>(),UIUtils {

    private val itemViewModels = mutableListOf<TagsMangaPageModel>()

    override fun getItemCount(): Int = itemViewModels.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MangaPageFlexTagsAdapter.ViewHolder {
        val binding = TagsMangaPageFlexItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MangaPageFlexTagsAdapter.ViewHolder, position: Int) {
        holder.binding.apply {
            CCMainTagItem.setOnClickListener {
                listenerClick.onRecyclerViewItemClick(CCMainTagItem, itemViewModels[position])
            }
        }

        holder.bind(itemViewModels[position])
    }

    fun setMain(list: List<TagsMangaPageModel>) {
        itemViewModels.clear()
        itemViewModels.addAll(list)
        notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: TagsMangaPageFlexItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(model: TagsMangaPageModel) {
            binding.apply {
                when (model.type) {
                    TagsMangaPageId.DIVIDE -> {
                        TVTag.visibility = View.GONE
                        Image.visibility = View.VISIBLE
                        TVTag.text = model.text

                        val drawable = Image.drawable
                        drawable.setColorFilter(ContextCompat.getColor(itemView.context, R.color.light_grey_4)
                            , PorterDuff.Mode.SRC_IN)
                        Image.setImageDrawable(drawable)
                    }
                    TagsMangaPageId.GENRE_TAG -> TVTag.text = model.text
                    TagsMangaPageId.RATING_TAG -> {
                        Image.visibility = View.VISIBLE
                        TVTag.text = model.text
                        Image.layoutParams.apply {
                            height = itemView.context.pixelToDP(10f)
                            width = itemView.context.pixelToDP(10f)
                        }
                        Image.requestLayout()
                        Image.setImageResource(R.drawable.ic_star_yellow)
                    }
                    TagsMangaPageId.YEAR_TAG -> TVTag.text = model.text
                    TagsMangaPageId.TYPE_WORK_TAG -> TVTag.text = model.text
                    TagsMangaPageId.AGE_RATING_TAG -> TVTag.text = model.text
                    TagsMangaPageId.PUBLISH_STATUS_TAG -> {
                        val crTypes = itemView.context.resources.getStringArray(R.array.creativeworks_status_types)
                        var positionCRTypes = crTypes.indexOf(model.text)
                        if (positionCRTypes == -1) positionCRTypes = 0
                        TVTag.text = itemView.context.resources.getStringArray(R.array.creativeworks_status_type_texts)[positionCRTypes]
                        TVTag.setTextColor(when(positionCRTypes){
                            0 -> ContextCompat.getColor(itemView.context, R.color.blue) // ongoing
                            1 -> ContextCompat.getColor(itemView.context, R.color.yellow) // hiatus
                            2 -> ContextCompat.getColor(itemView.context, R.color.green) // completed
                            3 -> ContextCompat.getColor(itemView.context, R.color.yellow) // planned
                            4 -> ContextCompat.getColor(itemView.context, R.color.red) // cancelled
                            else -> ContextCompat.getColor(itemView.context, R.color.black)
                        })
                    }
                }
            }
        }
    }
}