package es.usj.group1.firebasechat

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView

class ChatActivity : AppCompatActivity() {

    private lateinit var usernameInput: EditText
    private lateinit var commentInput: EditText
    private lateinit var sendButton: Button
    private lateinit var messagesRecyclerView: RecyclerView
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        usernameInput = findViewById(R.id.username_input)
        commentInput = findViewById(R.id.comment_input)
        sendButton = findViewById(R.id.button_send)
        messagesRecyclerView = findViewById(R.id.message_recycler_view)

        sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE)

        val savedUsername = sharedPreferences.getString("username", "")

        if (!savedUsername.isNullOrEmpty()) {
            usernameInput.setText(savedUsername)
        }

        sendButton.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val username = usernameInput.text.toString()
                val comment = commentInput.text.toString()

                if (username.isEmpty() || comment.isEmpty()) {
                    // show error that neither username nor comment can be empty
                } else {
                    saveUsername(username)
                    sendMessage(username, comment)
                }
            }
        })

        setupRecyclerView()
        fetchMessages()
    }

    private fun saveUsername(username: String) {
        val myEdit = sharedPreferences.edit()
        myEdit.putString("username", username)
        myEdit.apply()
    }

    private fun setupRecyclerView() {
        // set up RecyclerView with an adapter and layout manager
    }

    private fun fetchMessages() {
        // fetch messages from Firebase and update RecyclerView
    }

    private fun sendMessage(username: String, comment: String) {
        // send message to Firebase
    }
}
