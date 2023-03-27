package org.d3if3040.ritdhafood

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import org.d3if3040.ritdhafood.databinding.LoginLayoutBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: LoginLayoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = LoginLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar) // set the Toolbar as the ActionBar
        supportActionBar?.setDisplayHomeAsUpEnabled(true) // enable the Up button

        // set the click listener for the Up button
        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        binding.loginButton.setOnClickListener {
            // Lakukan aksi login
            binding.emailEdittext.text.toString()
            binding.passwordEdittext.text.toString()
        }
    }
}
