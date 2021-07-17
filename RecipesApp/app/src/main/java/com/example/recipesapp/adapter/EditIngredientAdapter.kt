package com.example.recipesapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.RecyclerView
import com.example.recipesapp.R
import com.example.recipesapp.model.entity.Ingredient
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textview.MaterialTextView

class EditIngredientAdapter(
    private val updateItem: (Int, Ingredient) -> Unit,
    private val deleteItem: (Int) -> Unit
) :
    RecyclerView.Adapter<EditIngredientAdapter.EditIngredientHolder>() {

    inner class EditIngredientHolder(view: View) : RecyclerView.ViewHolder(view)

     private val stringList = ArrayList<Ingredient>()

    fun setList(list: List<Ingredient>) {
        stringList.clear()
        stringList.addAll(list)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EditIngredientHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_edit_text, parent, false)
        return EditIngredientHolder(view)
    }

    override fun getItemCount() = stringList.size

    override fun getItemId(position: Int) = position.toLong()

    override fun getItemViewType(position: Int) = position

    override fun onBindViewHolder(holder: EditIngredientHolder, position: Int) {
        val text = holder.itemView.findViewById<TextInputEditText>(R.id.textInput)
        val number = holder.itemView.findViewById<MaterialTextView>(R.id.number_textView)
        val deleteButton = holder.itemView.findViewById<ImageButton>(R.id.delete_button)

        number.text = (position + 1).toString()
        text.setText(stringList[position].name)

        text.doAfterTextChanged {
            updateItem(position, Ingredient(it.toString()))
        }

        deleteButton.setOnClickListener {
            deleteItem(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, stringList.size)
        }
    }
}