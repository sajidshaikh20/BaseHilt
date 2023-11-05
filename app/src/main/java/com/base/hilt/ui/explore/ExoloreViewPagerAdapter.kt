package com.base.hilt.ui.explore

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.base.hilt.ui.explore.ui.BusinessFragment
import com.base.hilt.ui.explore.ui.MerchantFragment
import com.base.hilt.ui.explore.ui.PersonalFragment


class ExoloreViewPagerAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    ) : FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> PersonalFragment()
            1 -> BusinessFragment()
            2 -> MerchantFragment()
            else -> PersonalFragment()
        }
    }


}