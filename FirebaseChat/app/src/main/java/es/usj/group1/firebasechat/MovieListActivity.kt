package es.usj.group1.firebasechat

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import es.usj.group1.firebasechat.databinding.ActivityMovieListBinding
import es.usj.group1.firebasechat.databinding.MovieListItemBinding
import es.usj.group1.firebasechat.beans.Movie

class MovieListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMovieListBinding
    private lateinit var movieList: List<Movie>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Load movie list from SharedPreferences
        val sharedPref = getSharedPreferences("MoviePrefs", Context.MODE_PRIVATE)
        val moviesJson = sharedPref.getString("movies", "")
        val gson = Gson()
        movieList = gson.fromJson(moviesJson, object : TypeToken<List<Movie>>() {}.type)

        // Create a MovieAdapter to handle the RecyclerView items
        binding.movieRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.movieRecyclerView.adapter = MovieAdapter(movieList).apply {
            onItemClick = { movie ->
                // Start CommentsActivity, passing the ID of the selected movie to the new activity
                val intent = Intent(this@MovieListActivity, ChatActivity::class.java).apply {
                    putExtra("selected_movie_id", movie.id)
                }
                startActivity(intent)
            }
        }
    }
}

class MovieAdapter(private val movieList: List<Movie>) : RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    var onItemClick: ((Movie) -> Unit)? = null

    inner class MovieViewHolder(private val binding: MovieListItemBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val movie = movieList[position]
                    onItemClick?.invoke(movie)
                }
            }
        }

        fun bind(movie: Movie) {
            binding.movieTitle.text = movie.title
            binding.movieYear.text = movie.year.toString()
            binding.movieRating.text = movie.rating.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding = MovieListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val currentMovie = movieList[position]
        holder.bind(currentMovie)
    }

    override fun getItemCount() = movieList.size
}
