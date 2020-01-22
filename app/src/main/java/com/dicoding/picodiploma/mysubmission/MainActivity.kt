package com.dicoding.picodiploma.mysubmission

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadLocale()
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)
        supportActionBar?.title = ""

        val sectionPagerAdapter = SectionPagerAdapter(this, supportFragmentManager)
        view_pager.adapter = sectionPagerAdapter
        tabs.setupWithViewPager(view_pager)


        supportActionBar?.elevation = 0f
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_languange, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.languange -> {
                showChangeLang()
                return true
            }
            else -> return true
        }
    }

    private fun showChangeLang() {
        val listLang = arrayOf("English", "Indonesia")

        val mBuilder = AlertDialog.Builder(this@MainActivity)
        mBuilder.setTitle(resources.getString(R.string.choose_lang))
        mBuilder.setSingleChoiceItems(listLang, -1) { dialog, which ->
            if (which == 0) {
                setLocate("en")
                recreate()
            } else if (which == 1) {
                setLocate("in")
                recreate()
            }

            dialog.dismiss()
        }

        val mDialog = mBuilder.create()

        mDialog.show()
    }

    private fun setLocate(Lang: String?) {
        val locale = Locale(Lang)
        Locale.setDefault(locale)
        val config = Configuration()
        config.locale = locale
        baseContext.resources.updateConfiguration(config, baseContext.resources.displayMetrics)

        val editor = getSharedPreferences("Settings", Context.MODE_PRIVATE).edit()
        editor.putString("My_Lang", Lang)
        editor.apply()
    }

    private fun loadLocale() {
        val sharedPreferences = getSharedPreferences("Settings", Activity.MODE_PRIVATE)
        val languange = sharedPreferences.getString("My_Lang", "")
        setLocate(languange)
    }


}
