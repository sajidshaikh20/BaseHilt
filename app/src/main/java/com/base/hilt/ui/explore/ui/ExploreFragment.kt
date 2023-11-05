package com.base.hilt.ui.explore.ui

import com.base.hilt.R
import com.base.hilt.base.FragmentBase
import com.base.hilt.base.ToolbarModel
import com.base.hilt.databinding.FragmentExploreBinding
import com.base.hilt.ui.explore.ExoloreViewPagerAdapter
import com.base.hilt.ui.explore.HomeViewModel
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ExploreFragment : FragmentBase<HomeViewModel, FragmentExploreBinding>() {

    override fun getLayoutId(): Int {
        return R.layout.fragment_explore
    }

    override fun setupToolbar() {
        viewModel.setToolbarItems(ToolbarModel(true, true,))
    }

    override fun getViewModelClass(): Class<HomeViewModel> = HomeViewModel::class.java

    override fun initializeScreenVariables() {
        setupOverViewDetailsPager()
    }


    //tablayout_page_setup
    fun setupOverViewDetailsPager() {

        getDataBinding().vpexplore.isUserInputEnabled = false

        getDataBinding().vpexplore.adapter =
            ExoloreViewPagerAdapter(childFragmentManager, lifecycle)

        TabLayoutMediator(
            getDataBinding().tlexplore,
            getDataBinding().vpexplore
        )
        { tab, position ->
            when (position) {
                0 -> tab.text = "Personal"
                1 -> tab.text = "Business"
                2 -> tab.text = "Merchant"
            }
        }.attach()
    }


}