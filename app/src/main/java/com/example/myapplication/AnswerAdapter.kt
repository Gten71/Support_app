package com.example.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AnswerAdapter(private val answerList: MutableList<Pair<String, String>>) : RecyclerView.Adapter<AnswerAdapter.AnswerViewHolder>() {

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

        holder.itemView.setOnLongClickListener {
            showDeleteConfirmationDialog(holder)
            true
        }
    }

    override fun getItemCount(): Int {
        return answerList.size
    }

    private fun showDeleteConfirmationDialog(holder: AnswerViewHolder) {
        val context = holder.tvProblemTitle.context
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Удаление ответа")
            .setMessage("Вы уверены, что хотите удалить этот ответ?")
            .setPositiveButton("Удалить") { _, _ ->
                val position = holder.adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    removeAnswer(position)
                }
            }
            .setNegativeButton("Отмена", null)
            .create()
            .show()
    }

    private fun removeAnswer(position: Int) {
        val (problemTitle, responseText) = answerList[position]

        // Получаем ссылку на базу данных Firebase для узла "responses"
        val databaseRef = FirebaseDatabase.getInstance().reference.child("responses")

        // Ищем ответ, который нужно удалить, по значению problemTitle
        val query = databaseRef.orderByChild("problemTitle").equalTo(problemTitle)
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (responseSnapshot in snapshot.children) {
                    // Проверяем, соответствует ли responseText в базе данных responseText из списка
                    if (responseSnapshot.child("responseText").getValue(String::class.java) == responseText) {
                        // Удаляем найденный ответ
                        responseSnapshot.ref.removeValue()

                        // После удаления из Firebase удаляем элемент из списка
                        answerList.removeAt(position)

                        // Уведомляем адаптер об удалении элемента
                        notifyItemRemoved(position)

                        // Выходим из цикла, так как нашли и удалили нужный ответ
                        break
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Обработка ошибки, если не удалось получить данные из Firebase
            }
        })
    }
}


