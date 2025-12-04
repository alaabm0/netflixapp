package com.example.filmcatalogapp.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.filmcatalog.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.title = "Film Catalog"

        val btnCatalog: Button = findViewById(R.id.btn_catalog)
        val btnFavorites: Button = findViewById(R.id.btn_favorites)

        btnCatalog.setOnClickListener {
            val intent = Intent(this, FilmListActivity::class.java)
            startActivity(intent)
        }

        btnFavorites.setOnClickListener {
            val intent = Intent(this, FavoriteFilmsActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_catalog -> {
                startActivity(Intent(this, FilmListActivity::class.java))
                true
            }
            R.id.menu_favorites -> {
                startActivity(Intent(this, FavoriteFilmsActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}