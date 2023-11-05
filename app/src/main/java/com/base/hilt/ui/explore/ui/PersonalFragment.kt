package com.base.hilt.ui.explore.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.base.hilt.R
import com.base.hilt.base.FragmentBase
import com.base.hilt.base.ToolbarModel
import com.base.hilt.base.ViewModelBase
import com.base.hilt.databinding.FragmentPersonalBinding
import com.base.hilt.ui.explore.adapter.ExploreAdapter


class PersonalFragment : FragmentBase<ViewModelBase, FragmentPersonalBinding>() {
    override fun getLayoutId(): Int {
        return R.layout.fragment_personal
    }

    override fun setupToolbar() {
        viewModel.setToolbarItems(ToolbarModel(true, true))
    }

    override fun initializeScreenVariables() {
        addNotificationAdapter()
    }

    override fun getViewModelClass(): Class<ViewModelBase> = ViewModelBase::class.java


    fun addNotificationAdapter() {

        val arrayList = ArrayList<String>()

        //for demo
        arrayList.add("Sajid Shaikh")
        arrayList.add("Name1")
        arrayList.add("Name2")
        arrayList.add("Name3")
        arrayList.add("Name4")
        arrayList.add("name5")
        arrayList.add("Name6")
        arrayList.add("Name7")
        arrayList.add("Name8")
        arrayList.add("Name9")

        getDataBinding().rcvcard.adapter =
            ExploreAdapter(requireContext(), arrayList)

    }
}