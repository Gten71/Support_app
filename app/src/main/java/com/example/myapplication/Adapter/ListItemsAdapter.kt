package com.example.myapplication.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.Models.ListItem
import com.example.myapplication.R

class ListItemsAdapter(private val items: List<ListItem>, private val itemClickListener: OnItemClickListener) :
    RecyclerView.Adapter<ListItemsAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(item: ListItem)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvItemText: TextView = itemView.findViewById(R.id.tvListItemText)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val item = items[position]
                    itemClickListener.onItemClick(item)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_item_layout, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = items[position]
        holder.tvItemText.text = currentItem.title
    }

    override fun getItemCount() = items.size
}
