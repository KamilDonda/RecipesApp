package com.example.recipesapp.view.main_activity.account

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.recipesapp.model.repository.FirebaseRepository

class AccountViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = FirebaseRepository()

}