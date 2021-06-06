package com.example.recipesapp.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.recipesapp.R
import com.example.recipesapp.adapter.MostPopularAdapter
import com.example.recipesapp.adapter.MyRecipesAdapter
import com.example.recipesapp.view_model.FirebaseViewModel
import com.example.recipesapp.view_model.RecipesViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment() {

    private lateinit var recipesViewModel: RecipesViewModel
    private lateinit var firebaseViewModel: FirebaseViewModel

    private lateinit var myRecipesAdapter: MyRecipesAdapter
    private lateinit var mostPopularAdapter: MostPopularAdapter

    private lateinit var myRecipesRecyclerView: RecyclerView
    private lateinit var mostPopularRecyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        // Set bottom navigation as visible after logging in
        val bottomNavigation = activity?.findViewById<BottomNavigationView>(R.id.bottomNavigation)!!
        bottomNavigation.visibility = View.VISIBLE

        recipesViewModel = ViewModelProvider(requireActivity()).get(RecipesViewModel::class.java)
        firebaseViewModel = ViewModelProvider(requireActivity()).get(FirebaseViewModel::class.java)

        // 1: Get current user
        // 2: Get list of Strings with id of user's recipes
        // 3: Get user's recipes from Firebase
        // 'myRecipesAdapter.itemCount == 0' prevents multiple data downloads
        firebaseViewModel.getCurrentUser().observe(viewLifecycleOwner, Observer {
            if (it != null && it.recipes.isNotEmpty() && myRecipesAdapter.itemCount == 0)
                recipesViewModel.getMyRecipes(it.recipes)
                    .observe(viewLifecycleOwner, Observer {
                        myRecipesAdapter.notifyDataSetChanged()
                    })
        })
        myRecipesAdapter = MyRecipesAdapter(recipesViewModel.my_recipes, recipesViewModel)

        // Get most popular recipes
        recipesViewModel.getMostPopular().observe(viewLifecycleOwner, Observer {
            mostPopularAdapter.notifyDataSetChanged()
        })
        mostPopularAdapter = MostPopularAdapter(recipesViewModel.most_popular, recipesViewModel, requireContext())

        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        myRecipesRecyclerView = myRecipes_recyclerView.apply {
            adapter = myRecipesAdapter
        }

        mostPopularRecyclerView = mostPopular_recyclerView.apply {
            adapter = mostPopularAdapter
        }
    }
}