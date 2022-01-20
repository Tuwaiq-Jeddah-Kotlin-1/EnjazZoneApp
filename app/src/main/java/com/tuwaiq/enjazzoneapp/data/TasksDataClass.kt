package com.tuwaiq.enjazzoneapp.data

import android.os.Parcelable
import com.tuwaiq.enjazzoneapp.ui.todo.threeHoursInMilliSeconds
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class TasksDataClass(
    var taskTitle: String = "",
    var taskId: String = "",
    val nowDate: Long = System.currentTimeMillis(),
    var dueDate: Long = nowDate + threeHoursInMilliSeconds,
    var done: Boolean = false,
    var defDurationInMilliSeconds: Long = 64800000,
    var numberingInList: Int = 0,
    var taskDescription: String = "Task description.",
    var taskStartingHourMillis: Long = System.currentTimeMillis(),
    var taskEndingHourMillis: Long = System.currentTimeMillis()
) : Parcelable