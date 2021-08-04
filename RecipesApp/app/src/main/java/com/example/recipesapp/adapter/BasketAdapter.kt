package com.example.recipesapp.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.recipesapp.R
import com.example.recipesapp.model.entity.Ingredient
import com.example.recipesapp.utils.NumberConverter
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textview.MaterialTextView

class BasketAdapter(private val context: Context) :
    RecyclerView.Adapter<BasketAdapter.BasketHolder>() {

    inner class BasketHolder(view: View) : RecyclerView.ViewHolder(view)

    private val ingredients = ArrayList<Ingredient>()

    fun setList(list: List<Ingredient>) {
        ingredients.clear()
        ingredients.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BasketHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_ingredient_basket, parent, false)
        return BasketHolder(view)
    }

    override fun getItemCount() = ingredients.size

    override fun getItemId(position: Int) = position.toLong()

    override fun getItemViewType(position: Int) = position

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: BasketHolder, position: Int) {
        val root = holder.itemView.findViewById<MaterialCardView>(R.id.root)
        val number = holder.itemView.findViewById<MaterialTextView>(R.id.number_ingredient_textView)
        val name = holder.itemView.findViewById<MaterialTextView>(R.id.name_textView)
        val amount = holder.itemView.findViewById<MaterialTextView>(R.id.amount_textView)
        val icon = holder.itemView.findViewById<ImageView>(R.id.icon_circle)

        number.text = (position + 1).toString()

        val item = ingredients[position]
        name.text = item.name
        val unit = context.getString(
            com.example.recipesapp.model.entity.Unit.values()
                .find { it.number == item.unit }!!.abbr_id
        )
        amount.text = "${NumberConverter.isWhole(item.amount)} $unit"

        root.setOnClickListener {
            item.isSelected = !item.isSelected
            changeSelected(item.isSelected, root, icon)
        }

        if (item.isSelected) {
            changeSelected(true, root, icon)
        }
    }

    private fun changeSelected(
        isSelected: Boolean,
        root: MaterialCardView,
        icon: ImageView
    ) {
        val color: Int
        if (isSelected) {
            color = ContextCompat.getColor(context, R.color.light_green)
            icon.visibility = View.VISIBLE
        } else {
            color = Color.TRANSPARENT
            icon.visibility = View.INVISIBLE
        }
        root.setCardBackgroundColor(color)
    }
}