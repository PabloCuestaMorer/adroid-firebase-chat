package es.usj.group1.firebasechat.beans

data class Movie(
    val id: Int,
    val title: String,
    val description: String,
    val year: Int,
    val runtime: Int,
    val rating: Double,
    val votes: Int,
    val revenue: Double,
    val director: String,
    val genres: List<Int>,
    val actors: List<Int>
)