package com.example.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AnswerAdapter(private val answerList: List<Pair<String, String>>) : RecyclerView.Adapter<AnswerAdapter.AnswerViewHolder>() {

    inner class AnswerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvResponseText: TextView = itemView.findViewById(R.id.tvResponseText)
        val tvProblemTitle: TextView = itemView.findViewById(R.id.tvProblemTitle)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnswerViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_item_answer, parent, false)
        return AnswerViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: AnswerViewHolder, position: Int) {
        val (problemTitle, responseText) = answerList[position]
        holder.tvProblemTitle.text = problemTitle
        holder.tvResponseText.text = responseText
    }

    override fun getItemCount(): Int {
        return answerList.size
    }
}
