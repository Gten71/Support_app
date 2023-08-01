package com.example.myapplication.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.Models.Problem
import com.example.myapplication.R
import com.google.firebase.storage.FirebaseStorage
import com.bumptech.glide.Glide

class ProblemAdapter(private val problemList: List<Problem>) : RecyclerView.Adapter<ProblemAdapter.ProblemViewHolder>() {

    // Интерфейс для обработки кликов
    interface OnItemClickListener {
        fun onItemClick(problem: Problem)
    }

    // Переменная для хранения слушателя
    private var onItemClickListener: OnItemClickListener? = null

    // Метод для установки слушателя
    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.onItemClickListener = listener
    }

    inner class ProblemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvProblemNumber: TextView = itemView.findViewById(R.id.tvProblemNumber)
        val tvProblemTitle: TextView = itemView.findViewById(R.id.tvProblemTitle)
        val tvProblemContent: TextView = itemView.findViewById(R.id.tvProblemContent)
        val ivProblemPhoto: ImageView = itemView.findViewById(R.id.ivProblemPhoto) // New ImageView for the photo

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProblemViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_item_problem, parent, false)
        return ProblemViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ProblemViewHolder, position: Int) {
        val currentProblem = problemList[position]

        holder.tvProblemNumber.text = "Problem № ${position + 1}"
        holder.tvProblemTitle.text = currentProblem.title
        holder.tvProblemContent.text = currentProblem.content

        // Check if photoUrl is not null and not empty
        val photoUrl = currentProblem.photoUrl
        if (photoUrl != null && photoUrl.isNotEmpty()) {
            Glide.with(holder.itemView)
                .load(photoUrl)
                .placeholder(R.drawable.placeholder_image) // Optional placeholder while loading
                .error(R.drawable.error_image) // Optional error placeholder if loading fails
                .into(holder.ivProblemPhoto)

            // Make sure to set the ImageView visibility to VISIBLE if a photo is attached
            holder.ivProblemPhoto.visibility = View.VISIBLE
        } else {
            // Hide the ImageView if no photo is attached (Optional)
            holder.ivProblemPhoto.visibility = View.GONE
        }

        // Set the click listener for the item view (if needed)
        holder.itemView.setOnClickListener {
            onItemClick(currentProblem)
        }
        val maxTextLength = 20
        val truncatedContent = currentProblem.content?.take(maxTextLength) + if (currentProblem.content?.length ?: 0 > maxTextLength) "..." else ""
        holder.tvProblemContent.text = truncatedContent



    }
    private fun onItemClick(problem: Problem) {
        onItemClickListener?.onItemClick(problem)
    }


    override fun getItemCount(): Int {
        return problemList.size
    }
}


