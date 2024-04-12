package com.example.satisfactionsurvey.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import org.springframework.security.crypto.bcrypt.BCrypt

class AuthenticationViewModel : ViewModel() {
    private val tempPassword = BCrypt.hashpw("rødspætte", BCrypt.gensalt())

    var passwordAttempt by mutableStateOf("")
        private set

    var passwordVisible by mutableStateOf(false)
        private set

    fun updatePasswordAttempt(input: String) {
        passwordAttempt = input
    }

    fun togglePasswordVisible() {
        passwordVisible = !passwordVisible
    }

    fun resetTextField() {
        passwordAttempt = ""
        passwordVisible = false
    }

    fun validatePassword(password: String): Boolean {
        return BCrypt.checkpw(password, tempPassword)
    }
}