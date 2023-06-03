package es.usj.group1.firebasechat.utils

import retrofit2.Response
import retrofit2.http.GET

interface MovieApi {
    @GET("movies")
    suspend fun getMovies(): Response<List<Movie>>
}