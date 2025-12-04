package com.example.filmcatalogapp.data

import androidx.room.*

@Dao
interface FilmDao {
    @Query("SELECT * FROM films")
    suspend fun getAllFilms(): List<Film>

    @Query("SELECT * FROM films WHERE id = :filmId")
    suspend fun getFilmById(filmId: Int): Film?

    @Query("SELECT * FROM films WHERE isFavorite = 1")
    suspend fun getFavoriteFilms(): List<Film>

    @Query("SELECT * FROM films WHERE title LIKE '%' || :query || '%'")
    suspend fun searchFilms(query: String): List<Film>

    @Update
    suspend fun updateFilm(film: Film)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFilm(film: Film)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(films: List<Film>)
}