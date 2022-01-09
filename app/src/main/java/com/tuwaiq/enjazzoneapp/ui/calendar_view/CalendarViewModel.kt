package com.tuwaiq.enjazzoneapp.ui.calendar_view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CalendarViewModel : ViewModel() {


    private val _text = MutableLiveData<String>().apply {
        value = "This is Calendar (Appointments & Later Tasks) Fragment"
    }
    val text: LiveData<String> = _text

}
