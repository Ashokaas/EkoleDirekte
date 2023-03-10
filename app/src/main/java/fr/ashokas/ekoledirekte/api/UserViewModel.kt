package fr.ashokas.ekoledirekte.api

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class UserViewModel: ViewModel() {
    private val accountLiveData = MutableLiveData<Map<String, Any>>()
    private val notesLiveData = MutableLiveData<List<Any>>()
    private val matieresLiveData = MutableLiveData<Array<Array<Any>>>()
    private val notesAndMatieresLiveData = MutableLiveData<Pair<List<Any>,Array<Array<Any>>>>()


    var accountData: LiveData<Map<String, Any>>
        get() = accountLiveData
        set(value) {accountLiveData.value = value.value}

    var notes: LiveData<List<Any>>
        get() = notesLiveData
        set(value) {notesLiveData.value = value.value}

    var matieres: LiveData<Array<Array<Any>>>
        get() = matieresLiveData
        set(value) {matieresLiveData.value = value.value}

    var notesAndMatieres: LiveData<Pair<List<Any>,Array<Array<Any>>>>
        get() = notesAndMatieresLiveData
        set(value) {notesAndMatieresLiveData.value = value.value}
}