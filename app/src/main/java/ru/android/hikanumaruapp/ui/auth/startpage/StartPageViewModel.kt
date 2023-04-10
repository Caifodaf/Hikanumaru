package ru.android.hikanumaruapp.ui.auth.startpage

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.android.hikanumaruapp.api.init.ApiResponse
import ru.android.hikanumaruapp.api.models.TokenJWT
import javax.inject.Inject

@HiltViewModel
class StartPageViewModel @Inject constructor() : ViewModel() {

    val pagerCounter: MutableLiveData<Int> by lazy { MutableLiveData<Int>() }

}