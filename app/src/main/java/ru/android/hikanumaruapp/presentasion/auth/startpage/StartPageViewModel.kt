package ru.android.hikanumaruapp.presentasion.auth.startpage

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class StartPageViewModel @Inject constructor() : ViewModel() {

    val pagerCounter: MutableLiveData<Int> by lazy { MutableLiveData<Int>() }

}