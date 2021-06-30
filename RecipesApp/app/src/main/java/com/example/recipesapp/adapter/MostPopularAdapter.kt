package com.example.recipesapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.recipesapp.R
import com.example.recipesapp.model.entity.Recipe
import com.example.recipesapp.utils.Photo
import com.example.recipesapp.utils.RatingSystem
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textview.MaterialTextView

class MostPopularAdapter(
    private val context: Context
) : RecyclerView.Adapter<MostPopularAdapter.RecipesHolder>() {

    inner class RecipesHolder(view: View) : RecyclerView.ViewHolder(view)

    private val recipesList = ArrayList<Recipe>()

    fun setRecipes(list: List<Recipe>) {
        recipesList.clear()
        recipesList.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipesHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.most_popular_row, parent, false)
        return RecipesHolder(view)
    }

    override fun getItemCount() = recipesList.size

    override fun onBindViewHolder(holder: RecipesHolder, position: Int) {
        val root = holder.itemView.findViewById<MaterialCardView>(R.id.recipe_row_root)
        val name = holder.itemView.findViewById<MaterialTextView>(R.id.name_textView)
        val ratingLayout = holder.itemView.findViewById<LinearLayout>(R.id.rating_linearLayout)
        val imageView = holder.itemView.findViewById<ImageView>(R.id.imageView_most_popular)

        val item = recipesList[position]

        name.text = item.name
        RatingSystem().displayStars(context, ratingLayout, item.rating)
        Photo().setPhoto(item.image, context, imageView)

        root.setOnClickListener {
            Recipe.setCurrentRecipe(item)
            it.findNavController().navigate(R.id.action_home_to_oneRecipeFragment)
        }
    }
}