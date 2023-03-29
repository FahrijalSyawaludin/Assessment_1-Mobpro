package org.d3if3040.ritdhafood

import android.content.Intent
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
            val email = binding.emailEdittext.text.toString()
            val password = binding.passwordEdittext.text.toString()

            // Validate input
            if (email.isEmpty() || password.isEmpty()) {
                // Show error message
                binding.errorMessage.text = getString(R.string.error_message_empty_fields)
                return@setOnClickListener
            }

            // Authenticate user
            val authenticated = authenticateUser(email, password)

            if (authenticated) {
                // Save user data to SharedPreferences
                val sharedPreferences = getSharedPreferences("userData", MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.putString("email", email)
                editor.putString("password", password)
                editor.apply()

                // Navigate to home screen
                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
                finish() // Prevent the user from going back to the login screen
            } else {
                // Show error message
                binding.errorMessage.text = getString(R.string.error_message_empty_fields)
            }
        }
    }

    private fun authenticateUser(email: String, password: String): Boolean {
        // Replace this with your authentication logic
        return email == "cps@gmail.com" && password == "fahri123"
    }
}