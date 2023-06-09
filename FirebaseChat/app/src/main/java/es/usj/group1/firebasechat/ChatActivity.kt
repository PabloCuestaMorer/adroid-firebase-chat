package es.usj.group1.firebasechat

import android.content.ContentValues.TAG
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.*
import es.usj.group1.firebasechat.databinding.ActivityChatBinding
import es.usj.group1.firebasechat.utils.ChatAdapter
import es.usj.group1.firebasechat.beans.ChatMessage
import java.util.*

class ChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatBinding
    private lateinit var database: DatabaseReference
    private var userName: String = ""
    private var movieId: String = ""
    private var movieTitle: String = ""
    private val chatMessages = mutableListOf<ChatMessage>()
    private val adapter by lazy {
        ChatAdapter(userName) { chatMessage ->
            // When delete button is clicked, remove the message from Firebase
            database.child("messages").child(chatMessage.commentId!!).removeValue()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        movieId = intent.getIntExtra("selected_movie_id", 0).toString()
        movieTitle = intent.getStringExtra("selected_movie_title").toString()
        binding.tvMovieTitle.text = movieTitle

        fetchUserName()

        if (userName.isBlank()) {
            showNicknameDialog()
        } else {
            initChat()
        }
    }

    private fun fetchUserName() {
        val sharedPreferences = getSharedPreferences("MoviePrefs", Context.MODE_PRIVATE)
        userName = sharedPreferences.getString("user_name", "") ?: ""
    }

    private fun showNicknameDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Enter Nickname")

        val input = EditText(this)
        builder.setView(input)

        builder.setPositiveButton("OK") { _, _ ->
            val nickname = input.text.toString()
            if (nickname.isNotBlank()) {
                saveNickname(nickname)
                userName = nickname
                initChat()
            }
        }

        builder.setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }
        builder.show()
    }

    private fun saveNickname(nickname: String) {
        val sharedPreferences = getSharedPreferences("MoviePrefs", Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putString("user_name", nickname)
            apply()
        }
    }

    private fun initChat() {
        binding.userIdEditText.setText(userName)
        setupRecyclerView()
        setupDatabase()
        setupSendButton()
        setupBackButton()
        fetchChatMessages()
    }

    private fun setupRecyclerView() {
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter
    }

    private fun setupDatabase() {
        database =
            FirebaseDatabase.getInstance("https://adroid-firebase-chat-default-rtdb.europe-west1.firebasedatabase.app/").reference

    }

    private fun setupSendButton() {
        binding.sendButton.setOnClickListener {
            val description = binding.messageEditText.text.toString()
            if (description.isNotBlank()) {
                sendMessage(description)
                binding.messageEditText.text.clear()
            }
        }
    }

    private fun setupBackButton() {
        binding.backButton.setOnClickListener {
            finish()
        }
    }

    private fun sendMessage(description: String) {
        // Create a new chat message
        val newCommentId = database.child("messages").push().key
        if (newCommentId != null) {
            val chatMessage = ChatMessage(
                newCommentId, movieId, userName, description, Date()
            )
            database.child("messages").child(newCommentId).setValue(chatMessage)
        }
    }

    private fun fetchChatMessages() {
        database.child("messages").orderByChild("movieId").equalTo(movieId)
            .addChildEventListener(object : ChildEventListener {
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    val chatMessage = snapshot.getValue(ChatMessage::class.java)
                    chatMessage?.let {
                        chatMessages.add(it)
                        adapter.submitList(chatMessages.sortedBy { it.timestamp })
                        binding.recyclerView.scrollToPosition(chatMessages.size - 1)
                    }
                }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}

                override fun onChildRemoved(snapshot: DataSnapshot) {
                    val chatMessage = snapshot.getValue(ChatMessage::class.java)
                    chatMessage?.let {
                        chatMessages.remove(it)
                        adapter.submitList(chatMessages.sortedBy { it.timestamp })
                    }
                }

                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}

                override fun onCancelled(databaseError: DatabaseError) {
                    Log.w(TAG, "Failed to read comments.", databaseError.toException())
                }
            })
    }
}