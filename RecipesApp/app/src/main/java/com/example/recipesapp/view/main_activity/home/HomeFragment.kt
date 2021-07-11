package com.example.recipesapp.view.main_activity.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.example.recipesapp.R
import com.example.recipesapp.adapter.MostPopularAdapter
import com.example.recipesapp.adapter.MyRecipesAdapter
import com.example.recipesapp.model.entity.User
import com.example.recipesapp.view.main_activity.edit_recipe.EditRecipeViewModel
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var editRecipeViewModel: EditRecipeViewModel

    private lateinit var myRecipesAdapter: MyRecipesAdapter
    private lateinit var mostPopularAdapter: MostPopularAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        homeViewModel = ViewModelProvider(requireActivity()).get(HomeViewModel::class.java)
        editRecipeViewModel = ViewModelProvider(requireActivity()).get(EditRecipeViewModel::class.java)

        myRecipesAdapter = MyRecipesAdapter(requireContext(), editRecipeViewModel)
        mostPopularAdapter = MostPopularAdapter(requireContext())

        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setupRefreshAction()

        User.currentUser.observe(viewLifecycleOwner) {
            homeViewModel.myRecipes(it.recipes)
                .observe(viewLifecycleOwner) { list ->
                    myRecipesAdapter.setRecipes(list)
                }
        }

        homeViewModel.mostPopular.observe(viewLifecycleOwner) {
            mostPopularAdapter.setRecipes(it)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        myRecipes_recyclerView.adapter = myRecipesAdapter
        mostPopular_recyclerView.adapter = mostPopularAdapter

//        // TODO
//        val navBar = activity?.findViewById<BottomNavigationView>(R.id.bottomNavigation)
//        if (navBar != null) {
//            navBar.getOrCreateBadge(R.id.basket).number = 2
//        }
    }

    // Setup refresh
    private fun setupRefreshAction() {
        swipeRefreshLayout.setOnRefreshListener {
            requireActivity().finish()
            requireActivity().overridePendingTransition( 0, 0)
            startActivity(requireActivity().intent)
            requireActivity().overridePendingTransition( 0, 0)
        }
    }
}