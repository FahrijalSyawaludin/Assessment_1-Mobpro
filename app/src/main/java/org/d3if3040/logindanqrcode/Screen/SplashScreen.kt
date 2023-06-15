package org.d3if3040.logindanqrcode.Screen

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import org.d3if3040.logindanqrcode.MainActivity
import org.d3if3040.logindanqrcode.View.LoginActivity

import org.d3if3040.logindanqrcode.databinding.ActivitySplashScreenBinding

class SplashScreen : AppCompatActivity() {

    private lateinit var binding: ActivitySplashScreenBinding

    // Shared preference name
    private val SHARED_PREF_NAME = "myPref"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN

        Handler().postDelayed({
            val sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, 0)
            val logged = sharedPreferences.getBoolean("masuk", false)

            if (logged) {
                startActivity(Intent(applicationContext, MainActivity::class.java))
                finish()
            } else {
                startActivity(Intent(applicationContext, LoginActivity::class.java))
                finish()
            }
        }, 3000)
    }
}
