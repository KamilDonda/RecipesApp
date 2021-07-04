package com.example.recipesapp.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.RecyclerView
import com.example.recipesapp.R
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textview.MaterialTextView

class EditTextAdapter(
    private val updateItem: (Int, String) -> Unit,
    private val deleteItem: (Int) -> Unit
) :
    RecyclerView.Adapter<EditTextAdapter.EditTextHolder>() {

    inner class EditTextHolder(view: View) : RecyclerView.ViewHolder(view)

     private val stringList = ArrayList<String>()

    fun setList(list: List<String>) {
        stringList.clear()
        stringList.addAll(list)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EditTextHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_edit_text, parent, false)
        return EditTextHolder(view)
    }

    override fun getItemCount() = stringList.size

    override fun getItemId(position: Int) = position.toLong()

    override fun getItemViewType(position: Int) = position

    override fun onBindViewHolder(holder: EditTextHolder, position: Int) {
        val text = holder.itemView.findViewById<TextInputEditText>(R.id.textInput)
        val number = holder.itemView.findViewById<MaterialTextView>(R.id.number_textView)
        val deleteButton = holder.itemView.findViewById<ImageButton>(R.id.delete_button)

        number.text = (position + 1).toString()
        text.setText(stringList[position])

        text.doAfterTextChanged {
            updateItem(position, it.toString())
        }

        deleteButton.setOnClickListener {
            Log.v("testt", "position: $position")
            deleteItem(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, stringList.size)
        }
    }
}