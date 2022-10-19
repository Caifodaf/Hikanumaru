package ru.android.hikanumaruapp.ui.news.page

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import ru.android.hikanumaruapp.utilits.Events
import javax.inject.Inject

@HiltViewModel
class NewsPageViewModel @Inject constructor() : ViewModel() {

    private lateinit var job: Job
    val emitter = Events.Emitter()

}