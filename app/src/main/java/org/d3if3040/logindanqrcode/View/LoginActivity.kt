package org.d3if3040.logindanqrcode.View

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import org.d3if3040.logindanqrcode.R
import org.d3if3040.logindanqrcode.ViewModel.AboutActivity
import org.d3if3040.logindanqrcode.ViewModel.LoginViewModel
import org.d3if3040.logindanqrcode.databinding.LoginLayoutBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: LoginLayoutBinding
    private lateinit var viewModel: LoginViewModel

    //shared pref
    private val SHARED_PREF_NAME = "myPref"
    private lateinit var sharedPreferences: SharedPreferences

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LoginLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        viewModel.initialize(this) // Call the initialize function here

        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE)

        binding.btnRegister.setOnClickListener {
            viewModel.onRegisterButtonClick(this)
        }

        binding.btnLogin.setOnClickListener {
            val getUsername = binding.etUsername.text.toString()
            val getPassword = binding.etPassword.text.toString()

            viewModel.onLoginButtonClick(this, getUsername, getPassword)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                // Tombol Up ditekan, lakukan tindakan yang diinginkan, seperti kembali ke activity sebelumnya
                onBackPressed()
                true
            }
            R.id.action_about -> {
                // Tombol "About" ditekan, buka activity "AboutActivity" atau lakukan tindakan yang diinginkan
                // Ganti AboutActivity dengan activity tujuan Anda
                val intent = Intent(this, AboutActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.action_share -> {
                val shareIntent = Intent(Intent.ACTION_SEND)
                shareIntent.type = "text/plain"
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject Here")
                shareIntent.putExtra(Intent.EXTRA_TEXT, "Share Message Here")
                startActivity(Intent.createChooser(shareIntent, "Share using"))
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        return true
    }
}