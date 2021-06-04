package com.example.recipesapp.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.recipesapp.R
import com.example.recipesapp.adapter.ListAdapter
import com.example.recipesapp.view_model.RecipesViewModel
import kotlinx.android.synthetic.main.fragment_one_recipe.*

class OneRecipeFragment : Fragment() {

    private lateinit var recipesViewModel: RecipesViewModel
    private lateinit var ingredientsListAdapter: ListAdapter
    private lateinit var ingredientsRecyclerView: RecyclerView

    private lateinit var preparationListAdapter: ListAdapter
    private lateinit var preparationRecyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        recipesViewModel = ViewModelProvider(requireActivity()).get(RecipesViewModel::class.java)

        recipesViewModel.getIngredients().observe(viewLifecycleOwner, Observer {
            ingredientsListAdapter.notifyDataSetChanged()
        })

        recipesViewModel.getPreparation().observe(viewLifecycleOwner, Observer {
            preparationListAdapter.notifyDataSetChanged()
        })

        ingredientsListAdapter = ListAdapter(recipesViewModel.ingredients)
        preparationListAdapter = ListAdapter(recipesViewModel.preparation)

        return inflater.inflate(R.layout.fragment_one_recipe, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ingredientsRecyclerView = ingredients_recyclerView.apply { adapter = ingredientsListAdapter }
        preparationRecyclerView = preparation_recyclerView.apply { adapter = preparationListAdapter }
    }
}