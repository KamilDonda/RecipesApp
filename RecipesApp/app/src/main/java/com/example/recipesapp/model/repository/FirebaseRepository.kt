package com.example.recipesapp.model.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.recipesapp.model.entity.Ingredient
import com.example.recipesapp.model.entity.Recipe
import com.example.recipesapp.model.entity.User
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
    private val PATH_TODAY_RECIPE = "today_recipe"

    private val FIELD_ID = "id"
    private val FIELD_UID = "uid"
    private val FIELD_PUBLIC = "public"
    private val FIELD_RATING = "rating"
    private val FIELD_IMAGE = "image"
    private val FIELD_RECIPES = "recipes"
    private val FIELD_FAVOURITES = "favourites"
    private val FIELD_NAME = "name"
    private val FIELD_BASKET = "basket"

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

    // Get user's recipes or favourites recipes
    fun getRecipesByIDs(list: ArrayList<String>): LiveData<ArrayList<Recipe>> {
        val result = MutableLiveData<ArrayList<Recipe>>()
        if (list.isNotEmpty()) {
            cloud.collection(PATH_RECIPES)
                .whereIn(FIELD_ID, list)
                .orderBy(FIELD_NAME)
                .get()
                .addOnSuccessListener {
                    val recipes = it.toObjects(Recipe::class.java) as ArrayList<Recipe>?
                    result.postValue(recipes)
                }
        } else {
            result.postValue(ArrayList())
        }
        return result
    }

    // Get most popular recipes
    fun getMostPopular(): LiveData<ArrayList<Recipe>> {
        val result = MutableLiveData<ArrayList<Recipe>>()
        cloud.collection(PATH_RECIPES)
            .whereEqualTo(FIELD_PUBLIC, true)
            .orderBy(FIELD_RATING, Query.Direction.DESCENDING)
            .limit(10)
            .get()
            .addOnSuccessListener {
                val recipes = it.toObjects(Recipe::class.java) as ArrayList<Recipe>?
                result.postValue(recipes)
            }
        return result
    }

    // Get public recipes
    fun getPublicRecipes(): LiveData<ArrayList<Recipe>> {
        val result = MutableLiveData<ArrayList<Recipe>>()
        cloud.collection(PATH_RECIPES)
            .whereEqualTo(FIELD_PUBLIC, true)
            .get()
            .addOnSuccessListener {
                val recipes = it.toObjects(Recipe::class.java) as ArrayList<Recipe>?
                result.postValue(recipes)
            }
        return result
    }

    // Get today's recipe
    fun getTodayRecipe(): LiveData<Recipe> {
        val result = MutableLiveData<Recipe>()
        cloud.collection(PATH_TODAY_RECIPE)
            .document(PATH_TODAY_RECIPE)
            .get()
            .addOnSuccessListener {
                cloud.collection(PATH_RECIPES)
                    .whereEqualTo(FIELD_ID, it.data?.get(FIELD_ID))
                    .get()
                    .addOnSuccessListener {
                        result.postValue((it.toObjects(Recipe::class.java) as ArrayList<Recipe>)[0])
                    }
            }
        return result
    }

    // Add recipe to firebase or update if it exists
    fun addOrUpdateRecipe(recipe: Recipe) {
        cloud.collection(PATH_RECIPES).document(recipe.id).set(recipe)
    }

    fun deleteRecipe(recipe: Recipe) {
        cloud.collection(PATH_RECIPES).document(recipe.id).delete()
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

    // Delete photo from storage
    fun deletePhoto(id: String) {
        storage.getReference(PATH_IMAGES)
            .child(id)
            .delete()
    }

    // Get photo from storage
    private fun getPhotoUrl(storage: StorageReference, id: String) {
        storage.downloadUrl
            .addOnSuccessListener {
                updatePhoto(it.toString(), id)
            }
    }

    // Update field "image" in User
    private fun updatePhoto(url: String?, id: String) {
        cloud.collection(PATH_RECIPES).document(id).update(FIELD_IMAGE, url)
    }

    fun updateUser(user: User) {
        cloud.collection(PATH_USER).document(user.uid).set(user)
    }

    // Update field "recipes" in User
    fun updateUserRecipes(uid: String, idsOfRecipes: ArrayList<String>) {
        cloud.collection(PATH_USER).document(uid).update(FIELD_RECIPES, idsOfRecipes)
    }

    // Update field "favourites" in User
    fun updateUserFavourites(uid: String, idsOfFavourites: ArrayList<String>) {
        cloud.collection(PATH_USER).document(uid).update(FIELD_FAVOURITES, idsOfFavourites)
    }

    // Get User's data
    fun getUser(uid: String): LiveData<User?> {
        val currentUser: MutableLiveData<User> = MutableLiveData()
        cloud.collection(PATH_USER).document(uid)
            .addSnapshotListener(EventListener<DocumentSnapshot> { value, e ->
                if (e != null) {
                    currentUser.value = null
                    return@EventListener
                }
                currentUser.value = value?.toObject(User::class.java)
            })
        return currentUser
    }

    fun updateUserBasket(uid: String, items: ArrayList<Ingredient>) {
        cloud.collection(PATH_USER).document(uid).update(FIELD_BASKET, items)
    }
}