package org.d3if3040.ritdhafood

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import org.d3if3040.ritdhafood.databinding.ActivityMainBinding
import org.d3if3040.ritdhafood.databinding.LoginLayoutBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonLogin.setOnClickListener {
            val loginBinding = LoginLayoutBinding.inflate(layoutInflater)
            setContentView(loginBinding.root)
        }
    }
}