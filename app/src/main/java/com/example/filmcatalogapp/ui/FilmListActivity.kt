package com.example.filmcatalogapp.ui

import android.content.Intent
import android.os.Bundle
import android.widget.SearchView
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

class FilmListActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchView: SearchView
    private lateinit var adapter: FilmAdapter
    private val filmList = mutableListOf<Film>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_film_list)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Catalogue de Films"

        recyclerView = findViewById(R.id.rv_films)
        searchView = findViewById(R.id.sv_search)

        setupRecyclerView()
        setupSearchView()
        loadFilms()
    }

    private fun setupRecyclerView() {
        recyclerView.layoutManager = GridLayoutManager(this, 2)
        adapter = FilmAdapter(filmList) { film ->
            val intent = Intent(this, FilmDetailActivity::class.java)
            intent.putExtra("film_id", film.id)
            startActivity(intent)
        }
        recyclerView.adapter = adapter
    }

    private fun setupSearchView() {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterFilms(newText ?: "")
                return true
            }
        })
    }

    private fun loadFilms() {
        CoroutineScope(Dispatchers.IO).launch {
            val database = FilmDatabase.getDatabase(this@FilmListActivity)

            // Vérifier si la base de données est vide
            val existingFilms = database.filmDao().getAllFilms()

            if (existingFilms.isEmpty()) {
                // Insérer des films de démonstration
                val sampleFilms = createSampleFilms()
                database.filmDao().insertAll(sampleFilms)
            }

            val films = database.filmDao().getAllFilms()

            withContext(Dispatchers.Main) {
                filmList.clear()
                filmList.addAll(films)
                adapter.notifyDataSetChanged()
            }
        }
    }

    private fun filterFilms(query: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val database = FilmDatabase.getDatabase(this@FilmListActivity)
            val filteredFilms = if (query.isEmpty()) {
                database.filmDao().getAllFilms()
            } else {
                database.filmDao().searchFilms(query)
            }

            withContext(Dispatchers.Main) {
                filmList.clear()
                filmList.addAll(filteredFilms)
                adapter.notifyDataSetChanged()
            }
        }
    }

    private fun createSampleFilms(): List<Film> {
        return listOf(
            Film(
                title = "Inception",
                year = 2010,
                description = "Un voleur qui s'infiltre dans les rêves pour voler des secrets.",
                genre = "Science-Fiction",
                duration = "148 min",
                imageResId = R.drawable.film_inception  // ← ICI
            ),
            Film(
                title = "The Dark Knight",
                year = 2008,
                description = "Batman affronte le Joker, un criminel psychotique.",
                genre = "Action",
                duration = "152 min",
                imageResId = R.drawable.film_dark_knight  // ← ICI
            ),
            Film(
                title = "Pulp Fiction",
                year = 1994,
                description = "Histoires entrelacées de criminels à Los Angeles.",
                genre = "Crime",
                duration = "154 min",
                imageResId = R.drawable.film_pulp_fiction  // ← ICI
            ),
            Film(
                title = "Forrest Gump",
                year = 1994,
                description = "La vie extraordinaire d'un homme au QI limité.",
                genre = "Drame",
                duration = "142 min",
                imageResId = R.drawable.film_forrest_gump  // ← ICI
            ),
            Film(
                title = "The Matrix",
                year = 1999,
                description = "Un hacker découvre la réalité virtuelle qui contrôle le monde.",
                genre = "Science-Fiction",
                duration = "136 min",
                imageResId = R.drawable.film_matrix  // ← ICI
            ),
            Film(
                title = "Titanic",
                year = 1997,
                description = "Histoire d'amour sur le paquebot Titanic.",
                genre = "Romance",
                duration = "195 min",
                imageResId = R.drawable.film_titanic  // ← ICI
            ),
            Film(
                title = "Avengers: Endgame",
                year = 2019,
                description = "Les Avengers tentent de restaurer l'univers.",
                genre = "Action",
                duration = "181 min",
                imageResId = R.drawable.film_placeholder  // ← Placeholder si pas d'image
            ),
            Film(
                title = "The Shawshank Redemption",
                year = 1994,
                description = "Un banquier est emprisonné à vie pour un crime qu'il n'a pas commis.",
                genre = "Drame",
                duration = "142 min",
                imageResId = R.drawable.film_placeholder  // ← Placeholder si pas d'image
            )
        )
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}