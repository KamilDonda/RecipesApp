package com.example.recipesapp.view_model

import android.app.Activity
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.recipesapp.R
import com.example.recipesapp.model.repository.FirebaseRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class FirebaseViewModel(application: Application) : AndroidViewModel(application) {
    private val firebaseRepository = FirebaseRepository()
    val auth = FirebaseAuth.getInstance()

    // create an account in firebase and returns a communicate
    fun createAccount(
        email: String?,
        password: String?,
        repeatPassword: String?,
        activity: Activity
    ): LiveData<String?> {
        val message = MutableLiveData<String>()
        when {
            email.isNullOrEmpty() -> message.value = activity.getString(R.string.email_is_empty)
            password.isNullOrEmpty() -> message.value = activity.getString(R.string.password_is_empty)
            password != repeatPassword -> message.value = activity.getString(R.string.different_passwords)
            else -> return firebaseRepository.createAccount(auth, email, password)
        }
        return message
    }

    fun login(
        email: String?,
        password: String?,
        activity: Activity
    ): LiveData<String?> {
        val message = MutableLiveData<String>()
        when {
            email.isNullOrEmpty() -> message.value = activity.getString(R.string.email_is_empty)
            password.isNullOrEmpty() -> message.value = activity.getString(R.string.password_is_empty)
            else -> return firebaseRepository.loginAccount(auth, email, password)
        }
        return message
    }
}