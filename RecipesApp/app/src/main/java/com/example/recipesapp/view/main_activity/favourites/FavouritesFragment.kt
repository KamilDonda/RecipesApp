package com.example.recipesapp.view.main_activity.favourites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.example.recipesapp.R
import com.example.recipesapp.adapter.RecipesAdapter
import com.example.recipesapp.model.entity.User
import kotlinx.android.synthetic.main.fragment_favourites.*

class FavouritesFragment : Fragment() {

    private lateinit var favouritesViewModel: FavouritesViewModel
    private lateinit var recipesAdapter: RecipesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        favouritesViewModel = ViewModelProvider(requireActivity()).get(FavouritesViewModel::class.java)

        recipesAdapter = RecipesAdapter(requireContext())

        return inflater.inflate(R.layout.fragment_favourites, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        User.currentUser.observe(viewLifecycleOwner) {
            favouritesViewModel.favourites(it.favourites)
                .observe(viewLifecycleOwner) { list ->
                    recipesAdapter.setRecipes(list)
                }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        favourites_recyclerView.adapter = recipesAdapter
    }
}