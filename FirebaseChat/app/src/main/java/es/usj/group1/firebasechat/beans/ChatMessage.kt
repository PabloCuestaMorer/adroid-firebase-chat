package es.usj.group1.firebasechat.beans

import java.util.*

data class ChatMessage(
    val commentId: String? = null,
    val movieId: String? = null,
    val userId: String? = null,
    val description: String? = null,
    val timestamp: Date? = null
) {
    // No-argument constructor
    constructor() : this(null, null, null, null, null)
}
