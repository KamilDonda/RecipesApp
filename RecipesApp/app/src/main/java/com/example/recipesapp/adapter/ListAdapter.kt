package com.example.recipesapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ListAdapter(private val click: () -> Unit) :
    RecyclerView.Adapter<ListAdapter.ListHolder>() {

    inner class ListHolder(view: View) : RecyclerView.ViewHolder(view)

    private val stringList = ArrayList<String>()

    fun setList(list: List<String>) {
        stringList.clear()
        stringList.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(android.R.layout.simple_list_item_1, parent, false)
        return ListHolder(view)
    }

    override fun getItemCount() = stringList.size

    override fun onBindViewHolder(holder: ListHolder, position: Int) {
        val text = holder.itemView.rootView as TextView
        text.text = stringList[position]

        text.setOnClickListener {
            click()
        }
    }
}