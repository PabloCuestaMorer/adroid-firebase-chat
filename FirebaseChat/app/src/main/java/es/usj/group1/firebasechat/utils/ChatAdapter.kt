package es.usj.group1.firebasechat.utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import es.usj.group1.firebasechat.databinding.ItemChatBinding
import es.usj.group1.firebasechat.utils.ChatMessage

class ChatAdapter : ListAdapter<ChatMessage, ChatAdapter.ChatViewHolder>(ChatDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val binding = ItemChatBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ChatViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val chatMessage = getItem(position)
        holder.bind(chatMessage)
    }

    inner class ChatViewHolder(private val binding: ItemChatBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(chatMessage: ChatMessage) {
            binding.apply {
                commentIdTextView.text = "Comment ID: ${chatMessage.commentId}"
                movieIdTextView.text = "Movie ID: ${chatMessage.movieId}"
                userIdTextView.text = "User ID: ${chatMessage.userId}"
                descriptionTextView.text = chatMessage.description
                timestampTextView.text = chatMessage.timestamp.toString()
            }
        }
    }

    private class ChatDiffCallback : DiffUtil.ItemCallback<ChatMessage>() {
        override fun areItemsTheSame(oldItem: ChatMessage, newItem: ChatMessage): Boolean {
            return oldItem.commentId == newItem.commentId
        }

        override fun areContentsTheSame(oldItem: ChatMessage, newItem: ChatMessage): Boolean {
            return oldItem == newItem
        }
    }
}
