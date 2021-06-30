package com.example.recipesapp.model.repository

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

    fun createUser(user: User) {
        cloud.collection(PATH_USER)
            .document(user.uid)
            .set(user)
    }

    fun createUserWithGoogle(user: User) {
        cloud.collection(PATH_USER).whereEqualTo(FIELD_UID, user.uid).get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    if (it.result!!.documents.isEmpty())
                        createUser(user)
                }
            }
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