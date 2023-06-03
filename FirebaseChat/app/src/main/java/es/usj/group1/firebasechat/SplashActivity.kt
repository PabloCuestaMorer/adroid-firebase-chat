package es.usj.group1.firebasechat

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.content.Context
import android.util.Log
import com.google.gson.Gson
import es.usj.group1.firebasechat.utils.MovieApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlinx.coroutines.*

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // Check if data already exists in SharedPreferences
        val sharedPreferences = getSharedPreferences("MoviePrefs", Context.MODE_PRIVATE)
        val moviesData = sharedPreferences.getString("movies", null)
        if (moviesData != null) {
            // Start the next activity
            val intent = Intent(this@SplashActivity, MovieListActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            // If data does not exist in SharedPreferences, get data from API
            coroutineScope.launch {
                try {
                    val retrofit = Retrofit.Builder()
                        .baseUrl("http://52.18.54.217:8080/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()

                    val moviesAPI = retrofit.create(MovieApi::class.java)
                    val response = moviesAPI.getMovies()

                    if (response.isSuccessful) {
                        val movieList = response.body()

                        // Save the list of movies to SharedPreferences
                        with(sharedPreferences.edit()) {
                            putString("movies", Gson().toJson(movieList))
                            apply()
                        }

                        // Start the next activity
                        val intent = Intent(this@SplashActivity, MovieListActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                } catch (e: Exception) {
                    Log.e("SplashScreenActivity", "Error: ${e.message}")
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        coroutineScope.cancel()  // Cancel any remaining coroutine when the activity is destroyed
    }
}
