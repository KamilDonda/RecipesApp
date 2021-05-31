package com.example.recipesapp.model.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
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

    fun createAccount(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
    }

    fun loginAccount(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
    }

    fun googleLoginAccount(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
    }

}