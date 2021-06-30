package com.example.recipesapp.view.login_activity.login

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.recipesapp.model.entity.User
import com.example.recipesapp.model.repository.FirebaseRepository

class LoginViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = FirebaseRepository()

    fun createUserWithGoogle(user: User) {
        repository.createUserWithGoogle(user)
    }
}