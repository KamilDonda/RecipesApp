package com.example.recipesapp.view.login_activity.register

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.recipesapp.model.entity.User
import com.example.recipesapp.model.repository.FirebaseRepository

class RegisterViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = FirebaseRepository()

    fun createUser(user: User) {
        repository.createUser(user)
    }
}