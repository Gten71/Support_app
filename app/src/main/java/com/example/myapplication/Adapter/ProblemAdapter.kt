package com.example.myapplication.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.Models.Problem
import com.example.myapplication.R

class ProblemAdapter(private val problemList: List<Problem>) : RecyclerView.Adapter<ProblemAdapter.ProblemViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(problem: Problem)
    }

    private var itemClickListener: OnItemClickListener? = null

    // Метод для установки обработчика кликов
    fun setOnItemClickListener(listener: OnItemClickListener) {
        itemClickListener = listener
    }

    inner class ProblemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvProblemNumber: TextView = itemView.findViewById(R.id.tvProblemNumber)
        val tvProblemTitle: TextView = itemView.findViewById(R.id.tvProblemTitle)
        val tvProblemContent: TextView = itemView.findViewById(R.id.tvProblemContent)

        init {
            // Добавляем обработчик клика на элемент списка
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val clickedProblem = problemList[position]
                    itemClickListener?.onItemClick(clickedProblem)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProblemViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_item_problem, parent, false)
        return ProblemViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ProblemViewHolder, position: Int) {
        val currentProblem = problemList[position]
        val problemNumber = (position + 1).toString()

        holder.tvProblemNumber.text = "Problem №$problemNumber"
        holder.tvProblemTitle.text = currentProblem.title
        holder.tvProblemContent.text = currentProblem.content
    }

    override fun getItemCount(): Int {
        return problemList.size
    }
}

