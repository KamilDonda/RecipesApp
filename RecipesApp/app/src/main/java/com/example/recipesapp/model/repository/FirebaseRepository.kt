package com.example.recipesapp.model.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.recipesapp.model.entity.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class FirebaseRepository {

    private val cloud = FirebaseFirestore.getInstance()

    private val PATH_USER = "users"
    private val PATH_RECIPES = "recipes"

    // Create an account in firebase and returns a communicate
    fun createAccount(auth: FirebaseAuth, email: String, password: String): LiveData<String?> {
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

    // Login with email and password
    fun loginAccount(auth: FirebaseAuth, email: String, password: String): LiveData<String?> {
        val result = MutableLiveData<String?>()
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    result.value = null
                } else {
                    result.value = it.exception?.message.toString()
                }
            }
        return result
    }

    // Login with Google
    fun googleLoginAccount(auth: FirebaseAuth, idToken: String): LiveData<String?> {
        val result = MutableLiveData<String?>()
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    result.value = null
                } else {
                    result.value = it.exception?.message.toString()
                }
            }
        return result
    }

    // Get public recipes
    fun getPublicRecipes(): Query {
        return cloud.collection(PATH_RECIPES).whereEqualTo("public", true)
    }
}