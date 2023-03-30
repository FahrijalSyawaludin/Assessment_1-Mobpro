package org.d3if3040.ritdhafood

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import org.d3if3040.ritdhafood.databinding.LoginLayoutBinding


class LoginActivity : AppCompatActivity() {

    companion object {
        const val SHARED_PREF_NAME = "myPref"
    }

    private lateinit var binding: LoginLayoutBinding
    private lateinit var db: DataBaseHeleperLogin

    //shared pref
    private val SHARED_PREF_NAME = "myPref"
    private lateinit var sharedPreferences: SharedPreferences

    @SuppressLint("MissingInflatedId")
    @Override

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LoginLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = DataBaseHeleperLogin(this)

        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE)

        binding.tvRegister.setOnClickListener {
            Register.newInstance().show(supportFragmentManager, Register.TAG)
        }

        binding.btnLogin.setOnClickListener {
            val getUsername = binding.etUsername.text.toString()
            val getPassword = binding.etPassword.text.toString()

            if (getPassword.isEmpty() && getPassword.isEmpty()) {
                Toast.makeText(
                    applicationContext,
                    "Username atau password salah!",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                val masuk = db.checkLogin(getUsername, getPassword)
                if (masuk) {
                    val updateSession = db.upgradeSession("ada", 1)
                    if (updateSession) {
                        Toast.makeText(
                            applicationContext,
                            "Berhasil Masuk",
                            Toast.LENGTH_LONG
                        ).show()
                        val editor = sharedPreferences.edit()
                        editor.putBoolean("masuk", true)
                        editor.apply()
                        val dashbord = Intent(applicationContext, MainActivity::class.java)
                        startActivity(dashbord)
                        finish()
                    }
                } else {
                    Toast.makeText(
                        applicationContext,
                        "Gagal Masuk",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }
}