package com.base.hilt.ui.refine

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.base.hilt.R
import com.base.hilt.base.FragmentBase
import com.base.hilt.base.ToolbarModel
import com.base.hilt.base.ViewModelBase
import com.base.hilt.databinding.FragmentRefineBinding
import com.base.hilt.databinding.FragmentSplashBinding


class RefineFragment : FragmentBase<ViewModelBase, FragmentRefineBinding>() {
    override fun getLayoutId(): Int {
        return R.layout.fragment_refine
    }

    override fun setupToolbar() {
        viewModel.setToolbarItems(ToolbarModel(true, true, backbtn = true, title = "Refine"))
    }

    override fun initializeScreenVariables() {
        setSpinner()

    }

    override fun getViewModelClass(): Class<ViewModelBase> = ViewModelBase::class.java


    private fun setSpinner() {
        var speciality =
            arrayOf(
                "Available | Hey Let Us Connect",
                "Available | Stay  Us Watch",
                "Available | Hey Let Us Connect",
                "Available | Hey Let Us Connect",
            )

        var specialityAdapter: ArrayAdapter<String> =
            ArrayAdapter<String>(
                requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                speciality
            )
        getDataBinding().actvavailiblity.setAdapter(specialityAdapter)
    }
}