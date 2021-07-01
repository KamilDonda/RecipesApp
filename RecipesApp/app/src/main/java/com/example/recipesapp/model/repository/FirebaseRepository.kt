package com.example.recipesapp.model.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.recipesapp.model.entity.Recipe
import com.example.recipesapp.model.entity.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class FirebaseRepository {

    private val cloud = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()

    private val PATH_USER = "users"
    private val PATH_RECIPES = "recipes"
    private val PATH_IMAGES = "images"
    private val FIELD_ID = "id"
    private val FIELD_UID = "uid"
    private val FIELD_PUBLIC = "public"
    private val FIELD_RATING = "rating"
    private val FIELD_IMAGE = "image"

    // Create new user
    fun createUser(user: User) {
        cloud.collection(PATH_USER)
            .document(user.uid)
            .set(user)
    }

    // Create new user with Google
    fun createUserWithGoogle(user: User) {
        cloud.collection(PATH_USER).whereEqualTo(FIELD_UID, user.uid).get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    if (it.result!!.documents.isEmpty())
                        createUser(user)
                }
            }
    }

    // Get user's recipes
    fun getUserRecipes(list: ArrayList<String>): LiveData<List<Recipe>> {
        val result = MutableLiveData<List<Recipe>>()
        cloud.collection(PATH_RECIPES)
            .whereIn(FIELD_ID, list)
            .get()
            .addOnSuccessListener {
                val recipes = it.toObjects(Recipe::class.java)
                result.postValue(recipes)
            }
        return result
    }

    // Get id list of recipes
    fun getIdOfRecipes(uid: String): LiveData<List<String>> {
        val result = MutableLiveData<List<String>>()
        cloud.collection(PATH_USER)
            .document(uid)
            .addSnapshotListener(EventListener<DocumentSnapshot> { value, e ->
                if (e != null) {
                    result.value = null
                    return@EventListener
                }
                result.value = value!!.toObject(User::class.java)?.recipes
            })
        return result
    }

    // Get most popular recipes
    fun getMostPopular(): LiveData<List<Recipe>> {
        val result = MutableLiveData<List<Recipe>>()
        cloud.collection(PATH_RECIPES)
            .whereEqualTo(FIELD_PUBLIC, true)
            .orderBy(FIELD_RATING, Query.Direction.DESCENDING)
            .limit(10)
            .get()
            .addOnSuccessListener {
                val recipes = it.toObjects(Recipe::class.java)
                result.postValue(recipes)
            }
        return result
    }

    // Get public recipes
    fun getPublicRecipes(): LiveData<List<Recipe>> {
        val result = MutableLiveData<List<Recipe>>()
        cloud.collection(PATH_RECIPES)
            .whereEqualTo(FIELD_PUBLIC, true)
            .get()
            .addOnSuccessListener {
                val recipes = it.toObjects(Recipe::class.java)
                result.postValue(recipes)
            }
        return result
    }

    // Add recipe to firebase or update if it exists
    fun addOrUpdateRecipe(recipe: Recipe) {
        cloud.collection(PATH_RECIPES).document(recipe.id).set(recipe)
    }

    // Upload photo to storage
    fun uploadPhoto(id: String, bytes: ByteArray) {
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
//==============================

    fun updateUser(user: User) {
        cloud.collection(PATH_USER).document(user.uid).set(user)
    }
}