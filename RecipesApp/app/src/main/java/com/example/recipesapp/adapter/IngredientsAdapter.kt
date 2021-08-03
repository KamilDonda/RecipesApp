package com.example.recipesapp.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.recipesapp.R
import com.example.recipesapp.model.entity.Ingredient
import com.example.recipesapp.model.entity.Unit
import com.example.recipesapp.utils.NumberConverter
import com.example.recipesapp.view.main_activity.one_recipe.OneRecipeViewModel
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textview.MaterialTextView

class IngredientsAdapter(private val context: Context, private val viewModel: OneRecipeViewModel) :
    RecyclerView.Adapter<IngredientsAdapter.ListHolder>() {

    inner class ListHolder(view: View) : RecyclerView.ViewHolder(view)

    private val stringList = ArrayList<Ingredient>()

    fun setList(list: List<Ingredient>) {
        stringList.clear()
        stringList.addAll(list)
        notifyDataSetChanged()

        // Fill list with false
        selectedItems.addAll(stringList.map { false })
        viewModel.setSelectedMode(false)
    }

    private val items = ArrayList<Triple<MaterialCardView, ImageView, MaterialTextView>>()

    private val selectedItems = ArrayList<Boolean>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_ingredient, parent, false)
        return ListHolder(view)
    }

    override fun getItemCount() = stringList.size

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ListHolder, position: Int) {
        val root = holder.itemView.findViewById<MaterialCardView>(R.id.root)
        val number = holder.itemView.findViewById<MaterialTextView>(R.id.number_ingredient_textView)
        val name = holder.itemView.findViewById<MaterialTextView>(R.id.name_textView)
        val amount = holder.itemView.findViewById<MaterialTextView>(R.id.amount_textView)
        val icon = holder.itemView.findViewById<ImageView>(R.id.icon_circle)
        items.add(Triple(root, icon, number))

        root.setOnLongClickListener {
            selectItem(position, root, icon, number)
            true
        }

        root.setOnClickListener {
            if (viewModel.isSelectedMode.value!!) {
                selectItem(position, root, icon, number)
            }
        }

        number.text = (position + 1).toString()

        val item = stringList[position]
        name.text = item.name
        val unit = context.getString(Unit.values().find { it.number == item.unit }!!.abbr_id)
        amount.text = "${NumberConverter.isWhole(item.amount)} $unit"
    }

    private fun selectItem(
        position: Int,
        root: MaterialCardView,
        icon: ImageView,
        number: MaterialTextView
    ) {
        selectedItems[position] = !selectedItems[position]
        val isSelected = selectedItems[position]
        changeSelected(isSelected, root, icon, number)
        if (isSelected) viewModel.setSelectedMode(true)

        if (!selectedItems.contains(true)) {
            viewModel.setSelectedMode(false)
        }
        viewModel.setAllSelected(!selectedItems.contains(false))
    }

    private fun changeSelected(
        isSelected: Boolean,
        root: MaterialCardView,
        icon: ImageView,
        number: MaterialTextView
    ) {
        val color: Int
        if (isSelected) {
            color = ContextCompat.getColor(context, R.color.light_green)
            icon.visibility = View.VISIBLE
            number.visibility = View.INVISIBLE
        } else {
            color = Color.TRANSPARENT
            icon.visibility = View.INVISIBLE
            number.visibility = View.VISIBLE
        }
        root.setCardBackgroundColor(color)
    }

    fun selectAll(unselect: Boolean? = null) {
        val bool = if (unselect != null) false else selectedItems.contains(false)
        viewModel.setAllSelected(bool)
        selectedItems.fill(bool)
        viewModel.setSelectedMode(bool)
        selectedItems.forEachIndexed { index, _ ->
            changeSelected(bool, items[index].first, items[index].second, items[index].third)
        }
    }

    fun getSelectedList(): ArrayList<Ingredient> {
        val arrayList = ArrayList<Ingredient>()
        stringList.forEachIndexed { index, s ->
            if (selectedItems[index]) arrayList.add(s)
        }
        return arrayList
    }
}