package com.example.myapplication.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import android.content.Context
import android.content.Intent
import com.example.myapplication.DefaultUserScreen.ReplyActivity

class AnswerAdapter(private val context: Context, private val answerList: MutableList<Pair<String, String>>) : RecyclerView.Adapter<AnswerAdapter.AnswerViewHolder>() {

    inner class AnswerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvAnswerNumber: TextView = itemView.findViewById(R.id.tvAnswerNumber)
        val tvProblemTitle: TextView = itemView.findViewById(R.id.tvProblemTitle)
        val tvResponseText: TextView = itemView.findViewById(R.id.tvResponseText)

        init {
            itemView.setOnClickListener {
                val context: Context = itemView.context
                val (problemTitle, responseText) = answerList[adapterPosition]

                // Запускаем новый активити и передаем в него данные о заголовке и содержании ответа
                val intent = Intent(context, ReplyActivity::class.java).apply {
                    putExtra("problemTitle", problemTitle)
                    putExtra("responseText", responseText)
                }
                context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnswerViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_item_answer, parent, false)
        return AnswerViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: AnswerViewHolder, position: Int) {
        val (problemTitle, responseText) = answerList[position]
        val answerNumber = (position + 1).toString()

        val answerNumberText = context.getString(R.string.answer_num, answerNumber)
        val problemTitleText = context.getString(R.string.u_title, problemTitle)

        holder.tvAnswerNumber.text = answerNumberText
        holder.tvProblemTitle.text = problemTitleText
        holder.tvResponseText.text = responseText?.take(20) + if (responseText?.length ?: 0 > 20) "..." else ""

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


