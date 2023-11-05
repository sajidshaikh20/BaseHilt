package com.base.hilt.ui.notifications

import android.content.Intent
import com.base.hilt.MainActivity
import com.base.hilt.R
import com.base.hilt.base.FragmentBase
import com.base.hilt.base.LocaleManager
import com.base.hilt.base.ToolbarModel
import com.base.hilt.databinding.FragmentNotificationsBinding
import com.base.hilt.utils.MyPreference
import com.base.hilt.utils.PrefKey
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class NotificationsFragment : FragmentBase<NotificationsViewModel, FragmentNotificationsBinding>() {


    @Inject
    lateinit var mPref: MyPreference
    override fun getLayoutId(): Int {
        return R.layout.fragment_notifications
    }

    override fun setupToolbar() {
        viewModel.setToolbarItems(ToolbarModel(true, true, ))
    }

    override fun initializeScreenVariables() {


    }


    override fun getViewModelClass(): Class<NotificationsViewModel> =
        NotificationsViewModel::class.java

}