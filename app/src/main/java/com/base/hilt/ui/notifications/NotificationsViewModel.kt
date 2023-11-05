package com.base.hilt.ui.notifications

import com.base.hilt.base.ViewModelBase
import com.base.hilt.utils.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NotificationsViewModel @Inject constructor() : ViewModelBase() {

    val chnageLanguageClick = SingleLiveEvent<Boolean>()

    fun changeLangClick() {
        chnageLanguageClick.call()
    }
}