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

        binding.loginButton.setOnClickListener {
            // Lakukan aksi login
        }
    }
}
