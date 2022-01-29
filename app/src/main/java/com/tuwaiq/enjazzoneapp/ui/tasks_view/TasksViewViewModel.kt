package com.tuwaiq.enjazzoneapp.ui.tasks_view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TasksViewViewModel : ViewModel() {


    private val _text = MutableLiveData<String>().apply {
        //value = "This is Calendar (Appointments & Later Tasks) Fragment"
    }
    val text: LiveData<String> = _text

}
