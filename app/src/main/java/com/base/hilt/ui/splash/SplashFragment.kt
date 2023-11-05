package com.base.hilt.ui.splash

import android.os.Bundle
import androidx.navigation.findNavController
import com.base.hilt.R
import com.base.hilt.base.FragmentBase
import com.base.hilt.base.ToolbarModel
import com.base.hilt.base.ViewModelBase
import com.base.hilt.databinding.FragmentSplashBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SplashFragment : FragmentBase<ViewModelBase, FragmentSplashBinding>() {

    override fun getLayoutId(): Int {
        return R.layout.fragment_splash
    }

    override fun setupToolbar() {
        viewModel.setToolbarItems(ToolbarModel(false, isBottomNavVisible = false))
    }

    override fun getViewModelClass(): Class<ViewModelBase> = ViewModelBase::class.java

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun initializeScreenVariables() {

    }

    override fun onResume() {
        super.onResume()
        GlobalScope.launch(context = Dispatchers.Main) {
            delay(3000)
            val action = SplashFragmentDirections.actionSplashFragmentToNavigationHome()
            getDataBinding().main.findNavController().navigate(action)
        }
    }

}
