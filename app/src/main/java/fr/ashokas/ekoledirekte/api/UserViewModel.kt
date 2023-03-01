package fr.ashokas.ekoledirekte.api

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class UserViewModel: ViewModel() {
    private val accountLiveData = MutableLiveData<Map<String, Any>>()

    fun setAccountData(data: Map<String, Any>) {
        accountLiveData.value = data
    }

    fun getAccountData(): Map<String, Any>? {
        return accountLiveData.value
    }
}