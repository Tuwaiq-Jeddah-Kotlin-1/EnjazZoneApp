package com.tuwaiq.enjazzoneapp.ui.todo

import android.app.Application
import androidx.lifecycle.*
import com.tuwaiq.enjazzoneapp.data.TasksDataClass
import com.tuwaiq.enjazzoneapp.data.TodoRepo

class ToDoViewModel(context: Application) : AndroidViewModel(context) {
    private val repo = TodoRepo(context)

    fun getAllTasks(newList:ArrayList<TasksDataClass>, viewLifecycleOwner: LifecycleOwner): LiveData<ArrayList<TasksDataClass>> {
        val tasks = MutableLiveData<ArrayList<TasksDataClass>>()
        repo.getAllTasksFromDB(newList).observe(viewLifecycleOwner,{
            tasks.postValue(it)
        })
        return tasks
    }

    fun saveTask(task:TasksDataClass):String = repo.saveTaskInDB(task)
}