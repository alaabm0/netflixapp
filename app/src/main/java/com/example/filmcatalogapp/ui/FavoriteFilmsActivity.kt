package com.example.filmcatalogapp.ui
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.filmcatalog.R
import com.example.filmcatalogapp.adapter.FilmAdapter
import com.example.filmcatalogapp.data.Film
import com.example.filmcatalogapp.data.FilmDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FavoriteFilmsActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var tvEmpty: TextView
    private lateinit var adapter: FilmAdapter
    private val favoriteFilms = mutableListOf<Film>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite_films)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Mes Favoris"

        recyclerView = findViewById(R.id.rv_favorites)
        tvEmpty = findViewById(R.id.tv_empty_favorites)

        setupRecyclerView()
        loadFavoriteFilms()
    }

    private fun setupRecyclerView() {
        recyclerView.layoutManager = GridLayoutManager(this, 2)
        adapter = FilmAdapter(favoriteFilms) { film ->
            val intent = Intent(this, FilmDetailActivity::class.java)
            intent.putExtra("film_id", film.id)
            startActivity(intent)
        }
        recyclerView.adapter = adapter
    }

    private fun loadFavoriteFilms() {
        CoroutineScope(Dispatchers.IO).launch {
            val database = FilmDatabase.getDatabase(this@FavoriteFilmsActivity)
            val films = database.filmDao().getFavoriteFilms()

            withContext(Dispatchers.Main) {
                favoriteFilms.clear()
                favoriteFilms.addAll(films)
                adapter.notifyDataSetChanged()

                // Afficher message si pas de favoris
                if (favoriteFilms.isEmpty()) {
                    tvEmpty.visibility = TextView.VISIBLE
                    recyclerView.visibility = RecyclerView.GONE
                } else {
                    tvEmpty.visibility = TextView.GONE
                    recyclerView.visibility = RecyclerView.VISIBLE
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        loadFavoriteFilms()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}