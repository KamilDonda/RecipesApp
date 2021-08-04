package com.example.recipesapp.view.main_activity.basket

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import com.example.recipesapp.R
import com.example.recipesapp.adapter.BasketAdapter
import com.example.recipesapp.model.entity.User
import kotlinx.android.synthetic.main.fragment_basket.*

class BasketFragment : Fragment() {

    private lateinit var basketAdapter: BasketAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        basketAdapter = BasketAdapter(requireContext())

        return inflater.inflate(R.layout.fragment_basket, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        User.currentUser.observe(viewLifecycleOwner) {
            basketAdapter.setList(it.basket)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        basket_recyclerView.adapter = basketAdapter
    }
}