package com.tuwaiq.enjazzoneapp.data

import com.tuwaiq.enjazzoneapp.ui.todo.threeHoursInMilliSeconds

data class TasksDataClass(var taskTitle:String=""
                          , var taskId:String=""
                          , val nowDate: Long = System.currentTimeMillis() //1641735702419
                          , var dueDate: Long = nowDate+threeHoursInMilliSeconds
                          , var isDone: Boolean = false
                          , var defDurationInMilliSeconds: Long = 64800000
                          , var numberingInList: Int = 0
)