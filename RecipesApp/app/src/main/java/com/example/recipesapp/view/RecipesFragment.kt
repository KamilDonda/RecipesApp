package com.example.recipesapp.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.recipesapp.R
import com.example.recipesapp.adapter.RecipesAdapter
import com.example.recipesapp.model.entity.Recipe
import com.example.recipesapp.view_model.FirebaseViewModel
import kotlinx.android.synthetic.main.fragment_recipes.*

class RecipesFragment : Fragment() {

    private lateinit var firebaseViewModel: FirebaseViewModel
    private lateinit var recipesAdapter: RecipesAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        firebaseViewModel = ViewModelProvider(requireActivity()).get(FirebaseViewModel::class.java)

        firebaseViewModel.getPublicRecipes().observe(viewLifecycleOwner, Observer {
            recipesAdapter.notifyDataSetChanged()
        })

        recipesAdapter = RecipesAdapter(firebaseViewModel.recipes)

        return inflater.inflate(R.layout.fragment_recipes, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = recipes_recyclerView.apply { adapter = recipesAdapter }
    }
}