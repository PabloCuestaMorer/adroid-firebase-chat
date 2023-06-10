package es.usj.group1.firebasechat.beans

import java.util.*

data class ChatMessage(
    val commentId: String? = "",
    val movieId: String? = "",
    val userId: String? = "",
    val description: String? = "",
    val timestamp: Date? = null
) {
}
