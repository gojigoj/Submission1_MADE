package com.dicoding.picodiploma.mysubmission.view

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.dicoding.picodiploma.mysubmission.R
import com.dicoding.picodiploma.mysubmission.model.db.MovieHelper
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var movieHelper: MovieHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        movieHelper = MovieHelper.getInstance(applicationContext)
        movieHelper.open()

        setSupportActionBar(toolbar)
        supportActionBar?.title = ""

        val navController = findNavController(R.id.nav_host_fragment)
        bottom_navigation.setupWithNavController(navController)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.setting -> {
                val settingIntent = Intent(this@MainActivity, SettingsActivity::class.java)
                startActivity(settingIntent)
//                val mIntent = Intent(Settings.ACTION_LOCALE_SETTINGS)
//                startActivity(mIntent)
            }
            R.id.search -> {
                val searchIntent = Intent(this@MainActivity, SearchActivity::class.java)
                startActivity(searchIntent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        movieHelper.close()
    }
}
