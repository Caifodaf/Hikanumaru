package ru.android.hikanumaruapp.presentasion.auth.startpage.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import ru.android.hikanumaruapp.R

class StartPageImagersAdapter(
    private val adsList: Array<Int>
) :
    RecyclerView.Adapter<StartPageImagersAdapter.InfiniteRecyclerViewHolder>() {
    private val newList: Array<Int> =
        arrayOf(adsList.last()) + adsList + arrayOf(adsList.first())

    override fun getItemCount(): Int = newList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InfiniteRecyclerViewHolder {
        return InfiniteRecyclerViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: InfiniteRecyclerViewHolder, position: Int) =
        holder.bind(newList[position])

    class InfiniteRecyclerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(images: Int) {
            val pagerView: ImageView = itemView.findViewById(R.id.Image)
            pagerView.setImageResource(images)
        }

        companion object {
            fun from(parent: ViewGroup) : InfiniteRecyclerViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val itemView = layoutInflater.inflate(R.layout.start_page_imagers_pager,
                    parent, false)
                return InfiniteRecyclerViewHolder(itemView)
            }
        }

    }

}