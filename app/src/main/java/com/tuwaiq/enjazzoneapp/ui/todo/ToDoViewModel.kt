package com.tuwaiq.enjazzoneapp.ui.todo

import android.app.Application
import androidx.lifecycle.*
import com.tuwaiq.enjazzoneapp.data.TasksDataClass
import com.tuwaiq.enjazzoneapp.data.Repository
import kotlinx.coroutines.launch

class ToDoViewModel(context: Application) : AndroidViewModel(context) {
    private val repo = Repository()
    val tasks = MutableLiveData<MutableList<TasksDataClass>>()

    fun getAllTasks() : LiveData<MutableList<TasksDataClass>>{
        //tasks.value?.clear()
        viewModelScope.launch {
                tasks.postValue(repo.getAllTasksFromDB())
        }
        return tasks
    }

    fun createTask(task:TasksDataClass) = viewModelScope.launch { repo.createTaskInDB(task) }

    fun updateTask(task:TasksDataClass, key:String) = viewModelScope.launch { repo.updateTaskInDB (task, key) }
}