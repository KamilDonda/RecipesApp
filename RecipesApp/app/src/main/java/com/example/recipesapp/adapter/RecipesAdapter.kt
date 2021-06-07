package com.example.recipesapp.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.lifecycle.LiveData
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.recipesapp.R
import com.example.recipesapp.model.entity.Recipe
import com.example.recipesapp.utils.RatingSystem
import com.example.recipesapp.utils.TimeConverter
import com.example.recipesapp.view_model.RecipesViewModel
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textview.MaterialTextView

class RecipesAdapter(
    private val list: LiveData<List<Recipe>>,
    private val recipesViewModel: RecipesViewModel,
    private val context: Context
) :
    RecyclerView.Adapter<RecipesAdapter.RecipesHolder>() {

    inner class RecipesHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipesHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recipe_row, parent, false)
        return RecipesHolder(view)
    }

    override fun getItemCount() = list.value?.size ?: 0

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecipesHolder, position: Int) {
        val root = holder.itemView.findViewById<MaterialCardView>(R.id.recipe_row_root)
        val name = holder.itemView.findViewById<MaterialTextView>(R.id.name_textView)
        val level = holder.itemView.findViewById<MaterialTextView>(R.id.level_textView)
        val time = holder.itemView.findViewById<MaterialTextView>(R.id.time_textView)
        val meals = holder.itemView.findViewById<MaterialTextView>(R.id.meals_textView)
        val author = holder.itemView.findViewById<MaterialTextView>(R.id.author_textView)
        val ratingLayout = holder.itemView.findViewById<LinearLayout>(R.id.rating_linearLayout)

        val item = list.value?.get(position)!!

        name.text = item.name
        level.text = "${item.level} / 5"
        time.text = TimeConverter().longToString(item.time)
        meals.text = item.meals.toString()
        author.text = item.author
        RatingSystem().displayStars(context, ratingLayout, item.rating)

        root.setOnClickListener {
            recipesViewModel.setCurrentRecipe(item)
            it.findNavController().navigate(R.id.action_recipes_to_oneRecipeFragment)
        }
    }
}