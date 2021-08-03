package com.example.recipesapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.RecyclerView
import com.example.recipesapp.R
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textview.MaterialTextView

class EditPreparationAdapter(
    private val updateItem: (Int, String) -> Unit,
    private val deleteItem: (Int) -> Unit
) :
    RecyclerView.Adapter<EditPreparationAdapter.EditPrepHolder>() {

    inner class EditPrepHolder(view: View) : RecyclerView.ViewHolder(view)

     private val stringList = ArrayList<String>()

    fun setList(list: List<String>) {
        stringList.clear()
        stringList.addAll(list)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EditPrepHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_preparation_edit, parent, false)
        return EditPrepHolder(view)
    }

    override fun getItemCount() = stringList.size

    override fun getItemId(position: Int) = position.toLong()

    override fun getItemViewType(position: Int) = position

    override fun onBindViewHolder(holder: EditPrepHolder, position: Int) {
        val text = holder.itemView.findViewById<TextInputEditText>(R.id.textInput)
        val number = holder.itemView.findViewById<MaterialTextView>(R.id.number_preparation_textView)
        val deleteButton = holder.itemView.findViewById<ImageButton>(R.id.delete_button)

        number.text = (position + 1).toString()
        text.setText(stringList[position])

        text.doAfterTextChanged {
            updateItem(position, it.toString())
        }

        deleteButton.setOnClickListener {
            deleteItem(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, stringList.size)
        }
    }
}