package com.example.recipesapp.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.recipesapp.R
import com.example.recipesapp.model.entity.Ingredient
import com.example.recipesapp.utils.NumberConverter
import com.example.recipesapp.view.main_activity.edit_recipe.EditRecipeViewModel
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textview.MaterialTextView

class EditIngredientAdapter(
    private val context: Context,
    private val viewModel: EditRecipeViewModel
) :
    RecyclerView.Adapter<EditIngredientAdapter.EditIngredientHolder>() {

    inner class EditIngredientHolder(view: View) : RecyclerView.ViewHolder(view)

    private val stringList = ArrayList<Ingredient>()

    fun setList(list: List<Ingredient>) {
        stringList.clear()
        stringList.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EditIngredientHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_ingredient_edit, parent, false)
        return EditIngredientHolder(view)
    }

    override fun getItemCount() = stringList.size

    override fun getItemId(position: Int) = position.toLong()

    override fun getItemViewType(position: Int) = position

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: EditIngredientHolder, position: Int) {
        val root = holder.itemView.findViewById<MaterialCardView>(R.id.root)
        val number = holder.itemView.findViewById<MaterialTextView>(R.id.number_ingredient_textView)
        val name = holder.itemView.findViewById<MaterialTextView>(R.id.name_textView)
        val amount = holder.itemView.findViewById<MaterialTextView>(R.id.amount_textView)
        val deleteButton = holder.itemView.findViewById<ImageButton>(R.id.delete_button)

        number.text = (position + 1).toString()

        val item = stringList[position]
        name.text = item.name
        val unit = context.getString(
            com.example.recipesapp.model.entity.Unit.values()
                .find { it.number == item.unit }!!.abbr_id
        )
        amount.text = "${NumberConverter.isWhole(item.amount)} $unit"

        root.setOnClickListener {
            viewModel.setCurrentIngredient(item, position)
            it.findNavController()
                .navigate(R.id.action_editRecipeFragment_to_ingredientDialogFragment)
        }

        deleteButton.setOnClickListener {
            viewModel.deleteIngredient(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, stringList.size)
        }
    }
}