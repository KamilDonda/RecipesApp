package com.example.recipesapp.view.main_activity.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
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

        val recipe = Recipe()
        recipe.apply {
            this.name = "Jakaś nazwa przepisu"
            this.rating = 4.76f
            this.author = "Author"
        }
        Photo.setPhoto(recipe.image, requireContext(), todayRecipe_imageView)
        todayRecipe_name_textView.text = recipe.name
        todayRecipe_author_textView.text = recipe.author
        val lvl = Level.values().find { lvl -> lvl.number == recipe.level }!!
        todayRecipe_level_textView.text = getString(lvl.text_id)
        todayRecipe_level_imageView.setImageDrawable(
            AppCompatResources.getDrawable(requireContext(), lvl.icon_id)
        )
        todayRecipe_time_textView.text = TimeConverter.longToString(recipe.time)
        todayRecipe_meals_textView.text = recipe.meals.toString()
        RatingSystem.displayStars(requireContext(), todayRecipe_rating_linearLayout, recipe.rating, 25f)
        todayRecipe_rating_textView.text = recipe.rating.toString()
    }
}