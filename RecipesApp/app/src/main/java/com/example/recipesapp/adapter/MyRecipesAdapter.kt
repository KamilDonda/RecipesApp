package com.example.recipesapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.recipesapp.R
import com.example.recipesapp.model.entity.Recipe
import com.example.recipesapp.utils.Photo
import com.example.recipesapp.utils.TimeConverter
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textview.MaterialTextView

class MyRecipesAdapter(
    private val context: Context
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val VIEW_TYPE_RECIPE = 1
        const val VIEW_TYPE_ADD = 2
    }

    inner class RecipesHolder(view: View) : RecyclerView.ViewHolder(view) {
        val root = itemView.findViewById<MaterialCardView>(R.id.recipe_row_root)
        val name = itemView.findViewById<MaterialTextView>(R.id.name_textView)
        val time = itemView.findViewById<MaterialTextView>(R.id.time_textView)
        val meals = itemView.findViewById<MaterialTextView>(R.id.meals_textView)
        val imageView = itemView.findViewById<ImageView>(R.id.imageView_my_recipe)

        fun bind(position: Int) {
            val item = recipesList[position]

            name.text = item.name
            time.text = TimeConverter.longToString(item.time)
            meals.text = item.meals.toString()
            Photo.setPhoto(item.image, context, imageView)

            root.setOnClickListener {
                Recipe.setCurrentRecipe(item)
                it.findNavController().navigate(R.id.action_home_to_oneRecipeFragment)
            }
        }
    }

    inner class AddRecipeHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind() {
            itemView.setOnClickListener {
                Recipe.resetEditRecipe()
                it.findNavController().navigate(R.id.action_home_to_editRecipeFragment)
            }
        }
    }

    private val recipesList = ArrayList<Recipe>()

    fun setRecipes(list: List<Recipe>) {
        recipesList.clear()
        recipesList.addAll(list)
        recipesList.add(Recipe())
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_RECIPE) {
            RecipesHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_user_recipe, parent, false))
        } else {
            AddRecipeHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_add, parent, false))
        }
    }

    override fun getItemCount() = recipesList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (position != itemCount - 1) {
            (holder as RecipesHolder).bind(position)
        } else {
            (holder as AddRecipeHolder).bind()
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position != itemCount - 1) VIEW_TYPE_RECIPE else VIEW_TYPE_ADD
    }
}