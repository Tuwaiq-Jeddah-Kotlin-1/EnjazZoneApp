package com.tuwaiq.enjazzoneapp.data

data class TasksDataClass(var taskTitle:String=""
                          , var taskId:String=""
                          , val nowDate: Long = System.currentTimeMillis()
                          , var dueDate: Long = nowDate+System.currentTimeMillis()
                          , var isDone: Boolean = false)