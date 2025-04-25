package com.example.trackwheel.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.trackwheel.MainActivity
import com.example.trackwheel.data.preferences.UserPreferences
import com.example.trackwheel.databinding.ActivityLoginBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    
    @Inject
    lateinit var userPreferences: UserPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupClickListeners()
    }

    private fun setupClickListeners() {
        binding.loginButton.setOnClickListener {
            if (validateInputs()) {
                performLogin()
            }
        }

        binding.registerText.setOnClickListener {
            // In a real app, navigate to registration screen
            Toast.makeText(this, "Registration feature coming soon!", Toast.LENGTH_SHORT).show()
        }

        binding.forgotPasswordText.setOnClickListener {
            // In a real app, navigate to forgot password screen
            Toast.makeText(this, "Password reset feature coming soon!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun validateInputs(): Boolean {
        var isValid = true

        val email = binding.emailEditText.text.toString().trim()
        val password = binding.passwordEditText.text.toString().trim()

        if (email.isEmpty()) {
            binding.emailLayout.error = "Email is required"
            isValid = false
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.emailLayout.error = "Enter a valid email address"
            isValid = false
        } else {
            binding.emailLayout.error = null
        }

        if (password.isEmpty()) {
            binding.passwordLayout.error = "Password is required"
            isValid = false
        } else if (password.length < 6) {
            binding.passwordLayout.error = "Password must be at least 6 characters"
            isValid = false
        } else {
            binding.passwordLayout.error = null
        }

        return isValid
    }

    private fun performLogin() {
        // Show progress
        binding.progressBar.visibility = View.VISIBLE
        binding.loginButton.isEnabled = false

        // For demo purposes, we'll simulate a network delay
        // In a real app, you would make an API call to authenticate
        lifecycleScope.launch {
            // Simulate network delay
            kotlinx.coroutines.delay(1500)

            // For demo, we'll accept any valid email/password
            // In a real app, you would validate credentials against a backend
            val email = binding.emailEditText.text.toString()
            
            // Save login state and user info
            userPreferences.saveLoginState(true)
            userPreferences.saveUserEmail(email)
            
            // Navigate to main activity
            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
            finish()
            
            // Hide progress
            binding.progressBar.visibility = View.GONE
            binding.loginButton.isEnabled = true
        }
    }
}
