package com.example.recipesapp.view.main_activity.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.findNavController
import com.example.recipesapp.R
import com.example.recipesapp.adapter.MostPopularAdapter
import com.example.recipesapp.adapter.MyRecipesAdapter
import com.example.recipesapp.model.entity.Level
import com.example.recipesapp.model.entity.Recipe
import com.example.recipesapp.model.entity.User
import com.example.recipesapp.utils.Photo
import com.example.recipesapp.utils.RatingSystem
import com.example.recipesapp.utils.TimeConverter
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel

    private lateinit var myRecipesAdapter: MyRecipesAdapter
    private lateinit var mostPopularAdapter: MostPopularAdapter

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

        setupRefreshAction()

        User.currentUser.observe(viewLifecycleOwner) {
            homeViewModel.userRecipes(it.recipes)
                .observe(viewLifecycleOwner) { list ->
                    myRecipesAdapter.setRecipes(list)
                    userRecipes_indicator.visibility = View.INVISIBLE
                }
        }

        homeViewModel.mostPopular.observe(viewLifecycleOwner) {
            mostPopularAdapter.setRecipes(it)
            mostPopular_indicator.visibility = View.INVISIBLE
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupData()
        setupRecipeClick()

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

    private fun setupData() {
        userRecipes_recyclerView.adapter = myRecipesAdapter
        mostPopular_recyclerView.adapter = mostPopularAdapter

        homeViewModel.todayRecipe.observe(viewLifecycleOwner) {
            Photo.setPhoto(it.image, requireContext(), todayRecipe_imageView)
            todayRecipe_name_textView.text = it.name
            todayRecipe_author_textView.text = it.author
            val lvl = Level.values().find { lvl -> lvl.number == it.level }!!
            todayRecipe_level_textView.text = getString(lvl.text_id)
            todayRecipe_level_imageView.setImageDrawable(
                AppCompatResources.getDrawable(requireContext(), lvl.icon_id)
            )
            todayRecipe_time_textView.text = TimeConverter.longToString(it.time)
            todayRecipe_meals_textView.text = it.meals.toString()
            RatingSystem.displayStars(requireContext(), todayRecipe_rating_linearLayout, it.rating, 25f)
            todayRecipe_rating_textView.text = it.rating.toString()

            todayRecipe_cardView.visibility = View.VISIBLE
            todayRecipe_indicator.visibility = View.INVISIBLE
        }
    }

    private fun setupRecipeClick() {
        todayRecipe_cardView.setOnClickListener {
            Recipe.setCurrentRecipe(homeViewModel.todayRecipe.value!!)
            it.findNavController().navigate(R.id.action_home_to_oneRecipeFragment)
        }
    }
}