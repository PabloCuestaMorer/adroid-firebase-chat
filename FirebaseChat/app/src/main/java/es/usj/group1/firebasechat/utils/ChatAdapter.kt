package es.usj.group1.firebasechat.utils

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import es.usj.group1.firebasechat.beans.ChatMessage
import es.usj.group1.firebasechat.databinding.ItemChatBinding

class ChatAdapter(
    private val userId: String,
    private val onDelete: (ChatMessage) -> Unit
) : ListAdapter<ChatMessage, ChatAdapter.ChatViewHolder>(ChatMessageDiffCallback()) {

    inner class ChatViewHolder(val binding: ItemChatBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemChatBinding.inflate(layoutInflater, parent, false)
        return ChatViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val chatMessage = getItem(position)
        holder.binding.apply {
            userIdTextView.text = chatMessage.userId
            descriptionTextView.text = chatMessage.description
            commentIdTextView.text = chatMessage.commentId
            movieIdTextView.text = chatMessage.movieId
            timestampTextView.text = chatMessage.timestamp.toString()

            if (chatMessage.userId == userId) {
                //deleteButton.visibility = View.VISIBLE
                //deleteButton.setOnClickListener { onDelete(chatMessage) }
            } else {
                //deleteButton.visibility = View.GONE
            }
        }
    }
}

class ChatMessageDiffCallback : DiffUtil.ItemCallback<ChatMessage>() {
    override fun areItemsTheSame(oldItem: ChatMessage, newItem: ChatMessage): Boolean {
        return oldItem.commentId == newItem.commentId
    }

    override fun areContentsTheSame(oldItem: ChatMessage, newItem: ChatMessage): Boolean {
        return oldItem == newItem
    }
}