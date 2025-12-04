package com.example.filmcatalogapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.filmcatalog.R
import com.example.filmcatalogapp.data.Film

class FilmAdapter(
    private var films: List<Film>,
    private val onItemClick: (Film) -> Unit
) : RecyclerView.Adapter<FilmAdapter.FilmViewHolder>() {

    class FilmViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.iv_film_poster)
        val titleTextView: TextView = itemView.findViewById(R.id.tv_film_title)
        val yearTextView: TextView = itemView.findViewById(R.id.tv_film_year)
        val favoriteIcon: ImageView = itemView.findViewById(R.id.iv_favorite)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilmViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_film, parent, false)
        return FilmViewHolder(view)
    }

    override fun onBindViewHolder(holder: FilmViewHolder, position: Int) {
        val film = films[position]

        // CHARGER L'IMAGE ICI
        holder.imageView.setImageResource(film.imageResId)  // ← SIMPLE!

        holder.titleTextView.text = film.title
        holder.yearTextView.text = film.year.toString()

        // Afficher l'icône de favori
        if (film.isFavorite) {
            holder.favoriteIcon.setImageResource(R.drawable.ic_favorite_filled)
            holder.favoriteIcon.visibility = View.VISIBLE
        } else {
            holder.favoriteIcon.visibility = View.GONE
        }

        holder.itemView.setOnClickListener {
            onItemClick(film)
        }
    }

    override fun getItemCount() = films.size

    fun updateFilms(newFilms: List<Film>) {
        films = newFilms
        notifyDataSetChanged()
    }
}