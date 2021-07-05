package com.example.recipesapp.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.recipesapp.R
import com.example.recipesapp.view.main_activity.one_recipe.OneRecipeViewModel
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textview.MaterialTextView

class IngredientsAdapter(private val context: Context, private val viewModel: OneRecipeViewModel) :
    RecyclerView.Adapter<IngredientsAdapter.ListHolder>() {

    inner class ListHolder(view: View) : RecyclerView.ViewHolder(view)

    private val stringList = ArrayList<String>()

    fun setList(list: List<String>) {
        stringList.clear()
        stringList.addAll(list)
        notifyDataSetChanged()

        selectedItems.addAll(stringList.map { false })
        viewModel.setSelectedMode(false)
    }

    private val selectedItems = ArrayList<Boolean>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_ingredient, parent, false)
        return ListHolder(view)
    }

    override fun getItemCount() = stringList.size

    override fun onBindViewHolder(holder: ListHolder, position: Int) {
        val root = holder.itemView.findViewById<MaterialCardView>(R.id.root)
        val text = holder.itemView.findViewById<MaterialTextView>(R.id.materialTextView)
        val icon = holder.itemView.findViewById<ImageView>(R.id.icon_circle)

        root.setOnLongClickListener {
            selectItem(position, root, icon)
            true
        }

        root.setOnClickListener {
            if (viewModel.isSelectedMode)
                selectItem(position, root, icon)
        }

        text.text = stringList[position]
    }

    private fun selectItem(position: Int, root: MaterialCardView, icon: ImageView) {
        selectedItems[position] = !selectedItems[position]

        if (selectedItems[position]) {
            val color = ContextCompat.getColor(context, R.color.light_green)
            root.setCardBackgroundColor(color)
            icon.visibility = View.VISIBLE

            viewModel.setSelectedMode(true)
        } else {
            root.setCardBackgroundColor(Color.TRANSPARENT)
            icon.visibility = View.INVISIBLE
        }

        if (!selectedItems.contains(true)) {
            viewModel.setSelectedMode(false)
        }
    }
}