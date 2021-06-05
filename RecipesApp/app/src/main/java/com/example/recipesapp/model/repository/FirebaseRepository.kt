package com.example.recipesapp.model.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.recipesapp.model.entity.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class FirebaseRepository {

    private val cloud = FirebaseFirestore.getInstance()

    private val PATH_USER = "users"
    private val PATH_RECIPES = "recipes"
    private val FIELD_ID = "id"
    private val FIELD_UID = "uid"
    private val FIELD_PUBLIC = "public"

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
                    val UID = it.result!!.user!!.uid
                    // If user is not exist, then his account is created
                    cloud.collection(PATH_USER).whereEqualTo(FIELD_UID, UID).get()
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                if (it.result!!.documents.isEmpty())
                                    cloud.collection(PATH_USER).document(UID).set(User(UID))
                            }
                        }
                } else {
                    result.value = it.exception?.message.toString()
                }
            }
        return result
    }

    // Get current user
    fun getCurrentUser(uid: String): DocumentReference {
        return cloud.collection(PATH_USER).document(uid)
    }

    // Get public recipes
    fun getPublicRecipes(): Query {
        return cloud.collection(PATH_RECIPES).whereEqualTo(FIELD_PUBLIC, true)
    }

    // Get user's recipes
    fun getMyRecipes(list: ArrayList<String>): Query {
        return cloud.collection(PATH_RECIPES).whereIn(FIELD_ID, list)
    }
}