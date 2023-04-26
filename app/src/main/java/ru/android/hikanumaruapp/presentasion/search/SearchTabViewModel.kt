package ru.android.hikanumaruapp.presentasion.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SearchTabViewModel @Inject constructor(): ViewModel() {

    internal val searchText: MutableLiveData<String> by lazy { MutableLiveData<String>() }

    fun randomButton() {
        TODO("Not yet implemented")
    }

    fun filterButton() {
        TODO("Not yet implemented")
    }


}