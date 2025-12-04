package com.example.filmcatalogapp.ui

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.filmcatalog.R
import com.example.filmcatalogapp.data.Film
import com.example.filmcatalogapp.data.FilmDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FilmDetailActivity : AppCompatActivity() {
    private lateinit var film: Film
    private lateinit var btnFavorite: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_film_detail)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val filmId = intent.getIntExtra("film_id", -1)

        if (filmId == -1) {
            finish()
            return
        }

        btnFavorite = findViewById(R.id.btn_favorite)

        loadFilmDetails(filmId)
        setupFavoriteButton()
    }

    private fun loadFilmDetails(filmId: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            val database = FilmDatabase.getDatabase(this@FilmDetailActivity)
            val loadedFilm = database.filmDao().getFilmById(filmId)

            withContext(Dispatchers.Main) {
                loadedFilm?.let {
                    film = it
                    displayFilmDetails()
                } ?: run {
                    finish()
                }
            }
        }
    }

    private fun displayFilmDetails() {
        supportActionBar?.title = film.title

        val ivPoster: ImageView = findViewById(R.id.iv_film_poster)
        val tvTitle: TextView = findViewById(R.id.tv_film_title)
        val tvYear: TextView = findViewById(R.id.tv_film_year)
        val tvGenre: TextView = findViewById(R.id.tv_film_genre)
        val tvDuration: TextView = findViewById(R.id.tv_film_duration)
        val tvDescription: TextView = findViewById(R.id.tv_film_description)

        // CHARGER L'IMAGE ICI
        ivPoster.setImageResource(film.imageResId)  // ← SIMPLE!

        tvTitle.text = film.title
        tvYear.text = "Année: ${film.year}"
        tvGenre.text = "Genre: ${film.genre}"
        tvDuration.text = "Durée: ${film.duration}"
        tvDescription.text = film.description

        updateFavoriteButtonUI()
    }

    private fun setupFavoriteButton() {
        btnFavorite.setOnClickListener {
            toggleFavoriteStatus()
        }
    }

    private fun toggleFavoriteStatus() {
        film.isFavorite == !film.isFavorite

        CoroutineScope(Dispatchers.IO).launch {
            val database = FilmDatabase.getDatabase(this@FilmDetailActivity)
            database.filmDao().updateFilm(film)

            withContext(Dispatchers.Main) {
                updateFavoriteButtonUI()
                showFavoriteMessage(film.isFavorite)
            }
        }
    }

    private fun updateFavoriteButtonUI() {
        if (film.isFavorite) {
            btnFavorite.text = "Retirer des Favoris"
            btnFavorite.setBackgroundColor(ContextCompat.getColor(this, R.color.red))
        } else {
            btnFavorite.text = "Ajouter aux Favoris"
            btnFavorite.setBackgroundColor(ContextCompat.getColor(this, R.color.dark_gray))
        }
    }

    private fun showFavoriteMessage(isFavorite: Boolean) {
        val message = if (isFavorite) {
            "Ajouté aux favoris"
        } else {
            "Retiré des favoris"
        }
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}