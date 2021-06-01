package com.example.recipesapp.model.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.recipesapp.model.entity.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.awaitAll

class FirebaseRepository {

    private val cloud = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private val PATH_USER = "users"

    fun createAccount(email: String, password: String): LiveData<String?> {
        val result = MutableLiveData<String?>()
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    val UID = it.result!!.user!!.uid
                    cloud.collection(PATH_USER).document(UID).set(User(UID))
                    result.value = null
                } else {
                    result.value = it.exception?.message.toString()
                }
            }
        return result
    }

    fun loginAccount() {
    }

    fun loginAccount(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
    }

    fun googleLoginAccount(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
    }

}