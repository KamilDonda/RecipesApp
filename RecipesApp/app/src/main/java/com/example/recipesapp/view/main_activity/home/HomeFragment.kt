package com.example.recipesapp.view.main_activity.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.example.recipesapp.R
import com.example.recipesapp.adapter.MostPopularAdapter
import com.example.recipesapp.adapter.MyRecipesAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel

    private lateinit var myRecipesAdapter: MyRecipesAdapter
    private lateinit var mostPopularAdapter: MostPopularAdapter

    private val auth = FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        homeViewModel = ViewModelProvider(requireActivity()).get(HomeViewModel::class.java)

        myRecipesAdapter = MyRecipesAdapter(requireContext())
        mostPopularAdapter = MostPopularAdapter(requireContext())

        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // 1: Get list of Strings with id of user's recipes
        // 2: Get user's recipes from Firebase
        homeViewModel.getIdOfRecipes(auth.currentUser!!.uid).observe(viewLifecycleOwner, Observer {
            if (!it.isNullOrEmpty() &&
                (myRecipesAdapter.itemCount == 0 || myRecipesAdapter.itemCount != it.size)
            )
                homeViewModel.myRecipes(it as ArrayList<String>)
                    .observe(viewLifecycleOwner) { list ->
                        myRecipesAdapter.setRecipes(list)
                    }
        })

        homeViewModel.mostPopular.observe(viewLifecycleOwner) {
            mostPopularAdapter.setRecipes(it)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        myRecipes_recyclerView.adapter = myRecipesAdapter
        mostPopular_recyclerView.adapter = mostPopularAdapter

        // TODO
        val navBar = activity?.findViewById<BottomNavigationView>(R.id.bottomNavigation)
        if (navBar != null) {
            navBar.getOrCreateBadge(R.id.basket).number = 2
        }
    }
}