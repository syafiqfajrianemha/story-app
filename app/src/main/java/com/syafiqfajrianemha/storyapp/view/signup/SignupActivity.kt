package com.syafiqfajrianemha.storyapp.view.signup

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import com.syafiqfajrianemha.storyapp.databinding.ActivitySignupBinding
import com.syafiqfajrianemha.storyapp.view.ViewModelFactory
import com.syafiqfajrianemha.storyapp.view.login.LoginActivity

class SignupActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding

    private val signupViewModel by viewModels<SignupViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.edSignupName.addTextChangedListener(signupTextWatcher)
        binding.edSignupEmail.addTextChangedListener(signupTextWatcher)
        binding.edSignupPassword.addTextChangedListener(signupTextWatcher)

        setupView()
        playAnimation()
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()

        signupViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        signupViewModel.signupResponse.observe(this) { signupResponse ->
            if (signupResponse.error) {
                showToast(signupResponse.message)
            } else {
                showToast(signupResponse.message)
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }

        binding.signupButton.setOnClickListener {
            signupViewModel.signup(
                binding.edSignupName.text.toString(),
                binding.edSignupEmail.text.toString(),
                binding.edSignupPassword.text.toString()
            )
        }
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val title = ObjectAnimator.ofFloat(binding.titleTextView, View.ALPHA, 1f).setDuration(200)

        val name = ObjectAnimator.ofFloat(binding.nameTextView, View.ALPHA, 1f).setDuration(200)
        val nameLayout =
            ObjectAnimator.ofFloat(binding.nameEditTextLayout, View.ALPHA, 1f).setDuration(200)

        val email = ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 1f).setDuration(200)
        val emailLayout =
            ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1f).setDuration(200)

        val password =
            ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 1f).setDuration(200)
        val passwordLayout =
            ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f).setDuration(200)

        val signup = ObjectAnimator.ofFloat(binding.signupButton, View.ALPHA, 1f).setDuration(200)

        AnimatorSet().apply {
            playSequentially(
                title,
                name,
                nameLayout,
                email,
                emailLayout,
                password,
                passwordLayout,
                signup
            )
            start()
        }
    }

    private val signupTextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            // Do nothing.
        }

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            val edSignupName = binding.edSignupName.text.toString().trim()
            val edSignupEmail = binding.edSignupEmail.text.toString().trim()
            val edSignupPassword = binding.edSignupPassword.text.toString().trim()

            binding.signupButton.isEnabled =
                edSignupName.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(edSignupEmail)
                    .matches() && edSignupPassword.length >= 8
        }

        override fun afterTextChanged(s: Editable) {
            // Do nothing.
        }

    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}