package ru.android.hikanumaruapp.api.init

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow

open class BaseViewModel : ViewModel() {
    private var mJob: Job? = null

    protected fun <T> baseRequest(liveData: MutableLiveData<T>, errorHandler: CoroutinesErrorHandler, request: () -> Flow<T>) {
        mJob = viewModelScope.launch(Dispatchers.IO + CoroutineExceptionHandler { _, error ->
            viewModelScope.launch(Dispatchers.Main) {
                errorHandler.onError(error,error.localizedMessage ?: "Error occured! Please try again.")
            }
        }){
            request().collect {
                withContext(Dispatchers.Main) {
                    liveData.value = it
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        mJob?.let {
            if (it.isActive) {
                it.cancel()
            }
        }
    }
}

interface CoroutinesErrorHandler {
    fun onError(cause: Throwable?, message: String)
}