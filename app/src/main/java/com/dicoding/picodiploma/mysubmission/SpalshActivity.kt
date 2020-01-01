package com.dicoding.picodiploma.mysubmission

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler

class SpalshActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_spalsh)

        val handler = Handler()
        handler.postDelayed({
            val splashIntent = Intent(this@SpalshActivity, MainActivity::class.java)
            startActivity(splashIntent)
        }, 1000)
    }
}
