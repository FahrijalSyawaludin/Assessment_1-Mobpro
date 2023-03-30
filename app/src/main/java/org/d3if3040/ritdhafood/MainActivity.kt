package org.d3if3040.ritdhafood

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import org.d3if3040.ritdhafood.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var db: DataBaseHeleperLogin
    private lateinit var sharedPreferences: SharedPreferences

    companion object {
        const val SHARED_PREF_NAME = "myPref"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = DataBaseHeleperLogin(this)
        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)

        val checkSession = db.checkSession("ada")
        if (!checkSession) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        binding.btnKeluar.setOnClickListener {
            val updateSession = db.upgradeSession("kosong", 1)
            if (updateSession) {
                Toast.makeText(this, "Berhasil Keluar", Toast.LENGTH_LONG).show()

                val editor = sharedPreferences.edit()
                editor.putBoolean("masuk", false)
                editor.apply()

                val keluar = Intent(this, LoginActivity::class.java)
                startActivity(keluar)
                finish()
            }
        }
    }
}