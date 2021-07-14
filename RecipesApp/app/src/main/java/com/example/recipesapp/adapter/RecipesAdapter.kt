package com.example.recipesapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.content.res.AppCompatResources
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.recipesapp.R
import com.example.recipesapp.model.entity.Level
import com.example.recipesapp.model.entity.Recipe
import com.example.recipesapp.utils.Photo
import com.example.recipesapp.utils.RatingSystem
import com.example.recipesapp.utils.TimeConverter
import com.google.android.material.card.MaterialCardView
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textview.MaterialTextView

class RecipesAdapter(
    private val context: Context
) :
    RecyclerView.Adapter<RecipesAdapter.RecipesHolder>() {

    inner class RecipesHolder(view: View) : RecyclerView.ViewHolder(view)

    private val recipesList = ArrayList<Recipe>()

    fun setRecipes(list: List<Recipe>) {
        recipesList.clear()
        recipesList.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipesHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_recipe, parent, false)
        return RecipesHolder(view)
    }

    override fun getItemCount() = recipesList.size

    override fun onBindViewHolder(holder: RecipesHolder, position: Int) {
        val root = holder.itemView.findViewById<MaterialCardView>(R.id.recipe_row_root)
        val name = holder.itemView.findViewById<MaterialTextView>(R.id.name_textView)
        val level = holder.itemView.findViewById<MaterialTextView>(R.id.level_textView)
        val level_imageView = holder.itemView.findViewById<ImageView>(R.id.level_imageView)
        val time = holder.itemView.findViewById<MaterialTextView>(R.id.time_textView)
        val meals = holder.itemView.findViewById<MaterialTextView>(R.id.meals_textView)
        val author = holder.itemView.findViewById<MaterialTextView>(R.id.author_textView)
        val ratingLayout = holder.itemView.findViewById<LinearLayout>(R.id.rating_linearLayout)
        val rating = holder.itemView.findViewById<MaterialTextView>(R.id.rating_textView)
        val imageView = holder.itemView.findViewById<ShapeableImageView>(R.id.imageView_recipe)

        val item = recipesList[position]

        name.text = item.name
        val lvl = Level.values().find { lvl -> lvl.number == item.level }!!
        level.text = context.getString(lvl.text_id)
        level_imageView.setImageDrawable(AppCompatResources.getDrawable(context, lvl.icon_id))
        time.text = TimeConverter.longToString(item.time)
        meals.text = item.meals.toString()
        author.text = item.author
        RatingSystem.displayStars(context, ratingLayout, item.rating)
        rating.text = item.rating.toString()
        Photo.setPhoto(item.image, context, imageView)

        root.setOnClickListener {
            Recipe.setCurrentRecipe(item)
            it.findNavController().navigate(R.id.oneRecipeFragment)
        }
    }
}