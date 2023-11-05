package com.base.hilt.ui.explore.adapter

import android.content.Context
import com.base.hilt.R
import com.base.hilt.bind.GenericRecyclerViewAdapter
import com.base.hilt.databinding.CardExploreLayoutBinding


class ExploreAdapter(
    context: Context, cardlist: ArrayList<String>,
) : GenericRecyclerViewAdapter<String, CardExploreLayoutBinding>(context, cardlist) {
    override val layoutResId: Int
        get() = R.layout.card_explore_layout

    override fun onItemClick(model: String, position: Int) {

    }

    override fun onBindData(model: String, position: Int, dataBinding: CardExploreLayoutBinding) {

        dataBinding.tvCardName.text = model.toString()

    }
}