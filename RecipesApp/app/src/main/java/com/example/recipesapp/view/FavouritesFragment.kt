package com.example.recipesapp.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.recipesapp.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class FavouritesFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        // Set bottom navigation as visible after logging in
        val bottomNavigation = activity?.findViewById<BottomNavigationView>(R.id.bottomNavigation)!!
        bottomNavigation.visibility = View.VISIBLE

        return inflater.inflate(R.layout.fragment_favourites, container, false)
    }
}