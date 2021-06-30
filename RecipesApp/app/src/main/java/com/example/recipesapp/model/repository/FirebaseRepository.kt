package com.example.recipesapp.model.repository

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.recipesapp.model.entity.Recipe
import com.example.recipesapp.model.entity.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class FirebaseRepository {

    private val cloud = FirebaseFirestore.getInstance()

    private val PATH_USER = "users"
    private val PATH_RECIPES = "recipes"
    private val PATH_IMAGES = "images"
    private val FIELD_ID = "id"
    private val FIELD_UID = "uid"
    private val FIELD_PUBLIC = "public"
    private val FIELD_RATING = "rating"
    private val FIELD_IMAGE = "image"

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

    // Get most popular recipes
    fun getMostPopular(): Query {
        return cloud.collection(PATH_RECIPES)
            .whereEqualTo(FIELD_PUBLIC, true)
            .orderBy(FIELD_RATING, Query.Direction.DESCENDING)
            .limit(10)
    }

    // Add recipe to firebase or update if it exists
    fun addOrUpdateRecipe(recipe: Recipe) {
        cloud.collection(PATH_RECIPES).document(recipe.id).set(recipe)
    }

    fun updateUser(user: User) {
        cloud.collection(PATH_USER).document(user.uid).set(user)
    }

    fun uploadPhoto(storage: FirebaseStorage, id: String, bytes: ByteArray) {
        storage.getReference(PATH_IMAGES)
            .child(id)
            .putBytes(bytes)
            .addOnSuccessListener {
                getPhotoUrl(it.storage, id)
            }
    }

    private fun getPhotoUrl(storage: StorageReference, id: String) {
        storage.downloadUrl
            .addOnSuccessListener {
                updatePhoto(it.toString(), id)
            }
    }

    private fun updatePhoto(url: String?, id: String) {
        cloud.collection(PATH_RECIPES).document(id).update(FIELD_IMAGE, url)
    }
}