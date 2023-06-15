package org.d3if3040.logindanqrcode.ViewModel

import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.AndroidViewModel
import org.d3if3040.logindanqrcode.MainActivity.Companion.SHARED_PREF_NAME
import org.d3if3040.logindanqrcode.Model.LoginRepository
import org.d3if3040.logindanqrcode.View.RegisterActivity

class LoginViewModel(application: Application) : AndroidViewModel(application) {
    private lateinit var repository: LoginRepository
    private lateinit var sharedPreferences: SharedPreferences

    fun initialize(context: Context) {
        repository = LoginRepository(context)
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
    }

    fun onRegisterButtonClick(context: Context) {
        RegisterActivity.newInstance().show((context as AppCompatActivity).supportFragmentManager, RegisterActivity.TAG)
    }

    fun onLoginButtonClick(context: Context, username: String, password: String) {
        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(
                context.applicationContext,
                "Username atau password salah!",
                Toast.LENGTH_LONG
            ).show()
        } else {
            val loggedIn = repository.checkLogin(username, password)
            if (loggedIn) {
                val updateSession = repository.upgradeSession("ada", 1)
                if (updateSession) {
                    Toast.makeText(
                        context.applicationContext,
                        "Berhasil Masuk",
                        Toast.LENGTH_LONG
                    ).show()
                    val editor = sharedPreferences.edit()
                    editor.putBoolean("masuk", true)
                    editor.apply()
                    val dashboard = Intent(context.applicationContext, ViewDataActivity::class.java)
                    context.startActivity(dashboard)
                    (context as AppCompatActivity).finish()
                }
            } else {
                Toast.makeText(
                    context.applicationContext,
                    "Gagal Masuk",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}
