package ru.android.hikanumaruapp.presentasion.settings

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.android.hikanumaruapp.api.models.ErrorResponse
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor() : ViewModel() {

    val error: MutableLiveData<ErrorResponse> by lazy { MutableLiveData<ErrorResponse>() }

}