package com.tuwaiq.enjazzoneapp.ui.todo

import android.app.Application
import androidx.lifecycle.*
import com.tuwaiq.enjazzoneapp.data.TasksDataClass
import com.tuwaiq.enjazzoneapp.data.Repository
import kotlinx.coroutines.launch

class ToDoViewModel(context: Application) : AndroidViewModel(context) {
    private val repo = Repository()

    fun getAllTasksSortedByDescendingNowDate() : LiveData<MutableList<TasksDataClass>>{
        val tasks = MutableLiveData<MutableList<TasksDataClass>>()
        //tasks.value?.clear()
        viewModelScope.launch {
                tasks.postValue(repo.getAllTasksFromDBSortedByDescendingNowDate())
        }
        return tasks
    }
    fun getAllTasksSortedByDescendingDueDate() : LiveData<MutableList<TasksDataClass>>{
        val tasks = MutableLiveData<MutableList<TasksDataClass>>()
        //tasks.value?.clear()
        viewModelScope.launch {
                tasks.postValue(repo.getAllTasksFromDBSortedByDescendingDueDate())
        }
        return tasks
    }

    fun createTask(task:TasksDataClass) = viewModelScope.launch { repo.createTaskInDB(task) }

    fun updateTask(task:TasksDataClass, key:String) = viewModelScope.launch { repo.updateTaskInDB (task, key) }
}