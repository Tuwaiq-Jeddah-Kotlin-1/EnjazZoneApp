package com.tuwaiq.enjazzoneapp.ui.todo

import android.app.Application
import androidx.lifecycle.*
import com.tuwaiq.enjazzoneapp.data.TasksDataClass
import com.tuwaiq.enjazzoneapp.data.Repository
import kotlinx.coroutines.launch

class ToDoViewModel(context: Application) : AndroidViewModel(context) {
    private val repo = Repository()

    fun getAllTasks(newList:ArrayList<TasksDataClass>, viewLifecycleOwner: LifecycleOwner): LiveData<ArrayList<TasksDataClass>> {
        val tasks = MutableLiveData<ArrayList<TasksDataClass>>()
        viewModelScope.launch {
            repo.getAllTasksFromDB(newList).observe(viewLifecycleOwner,{
                tasks.postValue(it)
            })
        }
        return tasks
    }

    suspend fun createTask(task:TasksDataClass) = viewModelScope.launch { repo.createTaskInDB(task) }

    suspend fun updateTask(task:TasksDataClass, key:String) = viewModelScope.launch { repo.updateTaskInDB (task, key) }
}