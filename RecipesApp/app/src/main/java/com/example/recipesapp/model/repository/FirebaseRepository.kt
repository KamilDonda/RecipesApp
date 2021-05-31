package com.example.recipesapp.model.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class FirebaseRepository {

    private val cloud = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    fun getTest(){
        val ref = cloud.collection("test").document("SjgVDrWEcfs0VMh80IjE")
        ref.addSnapshotListener { value, _ ->
            Log.v("TEST", "value: $value")
        }
    }
}