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

class ChatAdapter(val context: Context, private val userName: String) :
    ListAdapter<ChatMessage, ChatAdapter.ChatViewHolder>(ChatDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val binding = ItemChatBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ChatViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val chatMessage = getItem(position)
        holder.bind(chatMessage)
    }

    @Suppress("DEPRECATION")
    inner class ChatViewHolder(private val binding: ItemChatBinding) :
        RecyclerView.ViewHolder(binding.root) {

        // onLongClick for delete message
        init {
            binding.root.setOnLongClickListener {
                //if nickname == ...
                val position = adapterPosition
                val builder = AlertDialog.Builder(binding.root.context)
                builder.setTitle("Long Click Dialog")
                builder.setMessage("Would You Like to delete: '${getItem(position).description}'")
                builder.setPositiveButton("Yes") { dialog, _ ->
                }
                builder.setNegativeButton("No") { _, _ ->
                }
                builder.show()
                true
            }
        }

        fun bind(chatMessage: ChatMessage) {
            binding.apply {
                commentIdTextView.text = "Comment ID: ${chatMessage.commentId}"
                movieIdTextView.text = "Movie ID: ${chatMessage.movieId}"
                // Display nickname instead of userID
                userIdTextView.text =
                    "Nickname: ${if (chatMessage.userId == userName) "You" else chatMessage.userId}"
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
