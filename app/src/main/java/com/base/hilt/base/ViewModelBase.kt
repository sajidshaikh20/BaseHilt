package com.base.hilt.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


open class ViewModelBase : ViewModel() {

    var snackbarMessage = MutableLiveData<Any>()
    var progressBarDisplay = MutableLiveData<Boolean>()
    var toolBarModel = MutableLiveData<ToolbarModel>()
    private var hideKeyBoardEvent = MutableLiveData<Any>()

    fun getHideKeyBoardEvent(): MutableLiveData<Any> {
        return hideKeyBoardEvent
    }

    /**
     * This method is used to display the Snake Bar Message
     *
     * @param message string object to display.
     */
    fun showSnackbarMessage(message: Any) {
        snackbarMessage.postValue(message)
    }

    fun showProgressBar(display: Boolean) {
        progressBarDisplay.postValue(display)

    }

    fun setToolbarItems(toolbarModel: ToolbarModel) {
        toolBarModel.postValue(toolbarModel)
    }

    fun hideKeyboard() {
        getHideKeyBoardEvent().value = true
    }

}
