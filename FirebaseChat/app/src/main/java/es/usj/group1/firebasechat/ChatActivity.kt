package es.usj.group1.firebasechat

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import es.usj.group1.firebasechat.databinding.ActivityChatBinding
import es.usj.group1.firebasechat.utils.ChatAdapter
import es.usj.group1.firebasechat.utils.ChatMessage
import java.util.*

class ChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatBinding
    private val db = FirebaseFirestore.getInstance()
    private lateinit var chatAdapter: ChatAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val selectedMovieId = intent.getStringExtra("selected_movie_id")

        // Check if user ID exists
        if (SplashActivity.userId != "0") {
            binding.userIdEditText.setText(SplashActivity.userId)
            binding.userIdEditText.isEnabled = false // Disables editing if user ID exists
        }

        // Setup RecyclerView
        chatAdapter = ChatAdapter()
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = chatAdapter

        // Listen for incoming chat messages
        if (selectedMovieId != null) {
            db.collection("chats").document(selectedMovieId).collection("messages")
                .addSnapshotListener { snapshots, e ->
                    if (e != null) {
                        Toast.makeText(this, "Error reading messages", Toast.LENGTH_SHORT).show()
                        return@addSnapshotListener
                    }
                    snapshots?.let {
                        val messages = it.toObjects(ChatMessage::class.java)
                        chatAdapter.submitList(messages)
                    }
                }
        }

        // Send button action
        binding.sendButton.setOnClickListener {
            val userId = SplashActivity.userId
            val message = binding.messageEditText.text.toString()

            if (userId == "0" || message.isBlank()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Generate a unique comment ID
            generateCommentId(selectedMovieId) { commentId ->
                if (commentId != null) {
                    // Create ChatMessage and send to Firebase
                    val timestamp = Date() // Get the current timestamp
                    val chatMessage =
                        ChatMessage(commentId, selectedMovieId, userId, message, timestamp)

                    if (selectedMovieId != null) {
                        db.collection("chats").document(selectedMovieId).collection("messages").add(chatMessage)
                            .addOnSuccessListener {
                                binding.messageEditText.setText("")
                            }
                            .addOnFailureListener {
                                Toast.makeText(this, "Error sending message", Toast.LENGTH_SHORT).show()
                            }
                    }
                } else {
                    Toast.makeText(this, "Error generating comment ID", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // Back button action
        binding.backButton.setOnClickListener {
            val intent = Intent(this@ChatActivity, MovieListActivity::class.java)
            startActivity(intent)
        }
    }

    private fun generateCommentId(movieId: String?, callback: (String?) -> Unit) {
        if (movieId != null) {
            val messagesRef = db.collection("chats").document(movieId).collection("messages")
            messagesRef.orderBy("commentId", Query.Direction.DESCENDING).limit(1).get().addOnSuccessListener { documents ->
                val lastCommentId = documents?.documents?.firstOrNull()?.getString("commentId")?.toIntOrNull() ?: 0
                val newCommentId = (lastCommentId + 1).toString()
                callback(newCommentId)
            }.addOnFailureListener {
                callback(null)
            }
        } else {
            callback(null)
        }
    }
}