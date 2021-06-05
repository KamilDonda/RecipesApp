package com.example.recipesapp.view_model

import android.app.Activity
import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.recipesapp.R
import com.example.recipesapp.model.entity.Recipe
import com.example.recipesapp.model.entity.User
import com.example.recipesapp.model.repository.FirebaseRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.QuerySnapshot

class FirebaseViewModel(application: Application) : AndroidViewModel(application) {
    private val firebaseRepository = FirebaseRepository()
    val auth = FirebaseAuth.getInstance()

    // Create an account in firebase and returns a communicate
    fun createAccount(
        email: String?,
        password: String?,
        repeatPassword: String?,
        activity: Activity
    ): LiveData<String?> {
        val message = MutableLiveData<String>()
        when {
            email.isNullOrEmpty() -> message.value = activity.getString(R.string.email_is_empty)
            password.isNullOrEmpty() -> message.value =
                activity.getString(R.string.password_is_empty)
            password != repeatPassword -> message.value =
                activity.getString(R.string.different_passwords)
            else -> return firebaseRepository.createAccount(auth, email, password)
        }
        return message
    }

    // Login with email and password
    fun login(
        email: String?,
        password: String?,
        activity: Activity
    ): LiveData<String?> {
        val message = MutableLiveData<String>()
        when {
            email.isNullOrEmpty() -> message.value = activity.getString(R.string.email_is_empty)
            password.isNullOrEmpty() -> message.value =
                activity.getString(R.string.password_is_empty)
            else -> return firebaseRepository.loginAccount(auth, email, password)
        }
        return message
    }

    // Login with Google
    fun login(idToken: String): LiveData<String?> {
        return firebaseRepository.googleLoginAccount(auth, idToken)
    }

    fun logout(activity: Activity): LiveData<String?> {
        val message = MutableLiveData<String>()
        auth.signOut()
        if (auth.currentUser == null)
            message.value = activity.getString(R.string.logout_successfully)
        else
            message.value = activity.getString(R.string.logout_not_successfully)
        return message

    }

    var currentUser: MutableLiveData<User> = MutableLiveData()

    // Get current user
    fun getCurrentUser(): LiveData<User> {
        firebaseRepository.getCurrentUser(auth.uid!!)
            .addSnapshotListener(EventListener<DocumentSnapshot> { value, e ->
                if (e != null) {
                    currentUser.value = null
                    return@EventListener
                }
                currentUser.value = value!!.toObject(User::class.java)
            })
        return currentUser
    }
}