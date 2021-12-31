package com.tuwaiq.enjazzoneapp.data

data class TaskTodo(var task:String="",
                    var taskId:String="",
                    val nowDate: Long = System.currentTimeMillis(),
                    var dueDate: Long = nowDate+System.currentTimeMillis())