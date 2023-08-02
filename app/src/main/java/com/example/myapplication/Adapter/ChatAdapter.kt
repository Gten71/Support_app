package com.example.myapplication.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.Models.Message
import com.example.myapplication.R

class ChatAdapter(private val messageList: List<Message>, private val userId: String) :
    RecyclerView.Adapter<ChatAdapter.MessageViewHolder>() {

    inner class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvSenderName: TextView = itemView.findViewById(R.id.tvSenderName)
        val tvMessageText: TextView = itemView.findViewById(R.id.tvMessageText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_item_message, parent, false)
        return MessageViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val currentMessage = messageList[position]

        // Определяем, отправителем сообщения является текущий пользователь или нет
        val isCurrentUserMessage = currentMessage.senderId == userId

        // Устанавливаем текст и отображаем или скрываем имя отправителя в зависимости от условия
        holder.tvSenderName.visibility = if (isCurrentUserMessage) View.GONE else View.VISIBLE
        holder.tvSenderName.text = if (!isCurrentUserMessage) currentMessage.senderName else ""
        holder.tvMessageText.text = currentMessage.messageText

        // Выравниваем текстовое сообщение в зависимости от отправителя
        holder.tvMessageText.textAlignment =
            if (isCurrentUserMessage) View.TEXT_ALIGNMENT_TEXT_END else View.TEXT_ALIGNMENT_TEXT_START
    }

    override fun getItemCount(): Int {
        return messageList.size
    }
}
