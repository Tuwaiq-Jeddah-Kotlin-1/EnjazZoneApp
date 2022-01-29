package com.tuwaiq.enjazzoneapp.ui.tasks_view

import android.graphics.Paint
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.RecyclerView
import com.tuwaiq.enjazzoneapp.R
import com.tuwaiq.enjazzoneapp.data.TasksDataClass
import com.tuwaiq.enjazzoneapp.ui.todo.ToDoViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

const val aDayInMilliSeconds = 86400000
const val threeHoursInMilliSeconds = 10800000
const val sixHoursInMilliSeconds = 21600000

class TasksViewRecyclerViewHolder(itemView: View,
    //                              private val clickListener: (position: Int) -> Unit)
):  RecyclerView.ViewHolder(itemView){
    val cvTaskCardView: CardView = itemView.findViewById(R.id.taskCardView)
    val clTaskViewCL: ConstraintLayout = itemView.findViewById(R.id.clTaskViewCL)
    val tvTaskTitle: TextView = itemView.findViewById(R.id.tvTaskTitleTV)
    val chbIsDoneCheckBox: CheckBox = itemView.findViewById(R.id.chbIsDoneCheckBox)
    val tvTaskDescriptionTV: TextView = itemView.findViewById(R.id.tvTaskDescriptionTV)
    val tvTaskStartingHourTV: TextView = itemView.findViewById(R.id.tvTaskStartingHourTV)
    val tvTaskEndingHourTV: TextView = itemView.findViewById(R.id.tvTaskEndingHourTV)
    val tvTaskDueDateTV: TextView = itemView.findViewById(R.id.tvTaskDueDateTV)
/*    init {
        initClickListeners()
    }*/

//And pass data here with invoke
/*

    private fun initClickListeners() {
        itemView.setOnClickListener { clickListener.invoke(bindingAdapterPosition) }
    }
*/
}

class TasksViewRecyclerViewAdapter(private var tasksMutableList: List<TasksDataClass>,
                                   private val view: View,
                                   private val vm:ToDoViewModel,
)
    : RecyclerView.Adapter<TasksViewRecyclerViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
    : TasksViewRecyclerViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.tasks_view_recycler_view_row, parent, false)
        return TasksViewRecyclerViewHolder(view,
        )
    }

    override fun onBindViewHolder(holder: TasksViewRecyclerViewHolder, position: Int) {
        val taskInAdapter = tasksMutableList[position]

        val listNumbering = "${position + 1}. "
        holder.tvTaskTitle.text = listNumbering+taskInAdapter.taskTitle
        holder.chbIsDoneCheckBox.isChecked = taskInAdapter.done
        holder.tvTaskDescriptionTV.text = taskInAdapter.taskDescription
        holder.tvTaskStartingHourTV.text = SimpleDateFormat("h:mm a", Locale.getDefault()).format(Date(taskInAdapter.taskStartingHourMillis)).lowercase() //+" - "
        holder.tvTaskEndingHourTV.text = SimpleDateFormat("h:mm a", Locale.getDefault()).format(Date(taskInAdapter.taskEndingHourMillis)).lowercase()
        holder.tvTaskDueDateTV.text = SimpleDateFormat("EEE dd MMM yyyy", Locale.getDefault()).format(Date(taskInAdapter.dueDate))

        val defaultCardElevation = 22.0F
        val calendar = Calendar.getInstance(Locale.getDefault())
        val taskCalendar = Calendar.getInstance(Locale.getDefault())
        taskCalendar.timeInMillis = taskInAdapter.dueDate

        val defaultTaskDescription = "Task description."
        val defaultTaskStartingHourText = SimpleDateFormat("h:mm a", Locale.getDefault()).format(Date(taskInAdapter.taskStartingHourMillis)).lowercase()+" - "
        val defaultTaskEndingHourText = SimpleDateFormat("h:mm a", Locale.getDefault()).format(Date(taskInAdapter.taskEndingHourMillis)).lowercase()
        val defaultTaskDuaDateText = SimpleDateFormat("EEE dd MMM yyyy", Locale.getDefault()).format(Date(taskInAdapter.dueDate))

        val colorPrimaryBlue = ContextCompat.getColor(view.context, R.color.primary_blue)
        val colorBlack = ContextCompat.getColor(view.context, R.color.black)
        val colorGreen = ContextCompat.getColor(view.context, R.color.green)
        val colorYellow = ContextCompat.getColor(view.context, R.color.yellow)
        val colorRed = ContextCompat.getColor(view.context, R.color.bright_red)
        val colorLighterGrey = ContextCompat.getColor(view.context, R.color.lighter_gray)
        val colorLighterBlueTransparent = ContextCompat.getColor(view.context, R.color.lighter_blue_transparent)

        // Task
        holder.clTaskViewCL.setBackgroundColor(
            when {
                holder.chbIsDoneCheckBox.isChecked || taskInAdapter.dueDate < Calendar.getInstance(Locale.getDefault()).timeInMillis -> colorLighterGrey
                //taskInAdapter.dueDate < Calendar.getInstance(Locale.getDefault()).timeInMillis -> ContextCompat.getColor(view.context, R.color.lighter_gray)
                //!holder.chbIsDoneCheckBox.isChecked -> ContextCompat.getColor(view.context, R.color.lighter_blue_transparent)
                //taskInAdapter.dueDate >= Calendar.getInstance(Locale.getDefault()).timeInMillis -> ContextCompat.getColor(view.context, R.color.lighter_blue_transparent)
                else -> colorLighterBlueTransparent
            }
        )
        // Card Elevation
        holder.cvTaskCardView.cardElevation =
            when {
                holder.chbIsDoneCheckBox.isChecked -> 0F
                taskDueDateIsThisWeek(taskCalendar, calendar) -> defaultCardElevation+4F
                taskDueDateIsToday(taskCalendar, calendar) -> defaultCardElevation+8F
                taskDueDateIsThisHour(taskCalendar, calendar) -> defaultCardElevation+defaultCardElevation
                //!holder.chbIsDoneCheckBox.isChecked -> defaultCardElevation
                else -> defaultCardElevation
            }

        // Title
        holder.tvTaskTitle.setTextColor(
            when {
                holder.chbIsDoneCheckBox.isChecked || taskInAdapter.dueDate < Calendar.getInstance(Locale.getDefault()).timeInMillis
                -> colorBlack
                //taskCalendar.time >= calendar.time || !holder.chbIsDoneCheckBox.isChecked -> ContextCompat.getColor(view.context, R.color.primary_blue)
                else -> colorPrimaryBlue
            }
        )
        holder.tvTaskTitle.paintFlags =
            when {
            holder.chbIsDoneCheckBox.isChecked -> holder.tvTaskTitle.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            // !holder.chbIsDoneCheckBox.isChecked -> holder.tvTaskTitle.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            else -> holder.tvTaskTitle.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
        }

        // Description
        holder.tvTaskDescriptionTV.setTextColor(
            when {
                holder.tvTaskDescriptionTV.text != defaultTaskDescription -> colorBlack
                else -> colorLighterGrey
            }
        )
        holder.tvTaskDescriptionTV.paintFlags =
            when {
                holder.chbIsDoneCheckBox.isChecked -> holder.tvTaskDescriptionTV.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                // !holder.chbIsDoneCheckBox.isChecked -> holder.tvTaskTitle.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                else -> holder.tvTaskTitle.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            }

        // Starting Hour
        holder.tvTaskStartingHourTV.setTextColor(
            when {
                holder.tvTaskStartingHourTV.text == SimpleDateFormat("h:mm a", Locale.getDefault()).format(Date(System.currentTimeMillis())).lowercase()+" - " -> colorLighterGrey
                else -> colorBlack
            }
        )
        holder.tvTaskStartingHourTV.text =
            when (holder.tvTaskStartingHourTV.text) {
                defaultTaskStartingHourText -> "$defaultTaskEndingHourText - "
                else -> "$defaultTaskEndingHourText - " //==
            }
        
        // Ending Hour
        holder.tvTaskEndingHourTV.setTextColor(
            when {
                holder.tvTaskEndingHourTV.text == SimpleDateFormat("h:mm a", Locale.getDefault()).format(Date(System.currentTimeMillis()+sixHoursInMilliSeconds)).lowercase()+" - " -> colorLighterGrey
                else -> colorBlack
            }
        )
        holder.tvTaskEndingHourTV.text = "${holder.tvTaskEndingHourTV.text}"
        //holder.tvTaskEndingHourTV.text = "${holder.tvTaskEndingHourTV.text} (Duration: ${SimpleDateFormat("h:mm a", Locale.getDefault()).format(Date((taskInAdapter.taskEndingHourMillis - taskInAdapter.taskStartingHourMillis)))})"

        // Due date
        holder.tvTaskDueDateTV.setTextColor(
            when {
                taskCalendar.time < calendar.time || holder.chbIsDoneCheckBox.isChecked -> colorBlack
                taskDueDateIsThisHour(taskCalendar, calendar) -> colorRed
                taskDueDateIsToday(taskCalendar, calendar) -> colorYellow
                taskDueDateIsThisWeek(taskCalendar, calendar) -> colorGreen //colorGreen
                //taskCalendar.time >= calendar.time -> ContextCompat.getColor(view.context, R.color.primary_blue)
                else -> colorPrimaryBlue
            }
        )
        holder.tvTaskDueDateTV.text =
            when {
                taskDueDateIsThisHour(taskCalendar, calendar) -> "$defaultTaskDuaDateText (This Hour)"
                taskDueDateIsToday(taskCalendar, calendar) -> "$defaultTaskDuaDateText (Today)"
                taskDueDateIsTomorrow(taskCalendar, calendar) -> "$defaultTaskDuaDateText (Tomorrow)"
                taskDueDateIsThisWeek(taskCalendar, calendar) -> "$defaultTaskDuaDateText (This Week)"
                taskDueDateIsMoreThanWeek(taskCalendar, calendar) -> "$defaultTaskDuaDateText (Upcoming week+)"
                taskInAdapter.dueDate < Calendar.getInstance(Locale.getDefault()).timeInMillis -> "$defaultTaskDuaDateText (Past due date)"
                else -> holder.tvTaskDueDateTV.text
            }
        holder.tvTaskDueDateTV.paintFlags =
            when {
                holder.chbIsDoneCheckBox.isChecked || taskInAdapter.dueDate < Calendar.getInstance(Locale.getDefault()).timeInMillis -> holder.tvTaskDueDateTV.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                // !holder.chbIsDoneCheckBox.isChecked -> holder.tvTaskTitle.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                else -> holder.tvTaskTitle.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            }

        holder.chbIsDoneCheckBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                taskInAdapter.done = true
                //vm.viewModelScope.launch {
                    vm.updateTask(taskInAdapter, "done")
                //}
                Handler(Looper.myLooper()!!).post {
                    notifyItemChanged(position)
                }
                //Add animation Here when user scratches/striking-Through the task, indicating that, the user, have finished the task
            }
            else if (!isChecked){ //not checked
                taskInAdapter.done = false
                //vm.viewModelScope.launch {
                    vm.updateTask(taskInAdapter, "done")
                //}
                Handler(Looper.myLooper()!!).post {
                    notifyItemChanged(position)
                }
                //Add animation Here when user un-scratches/un-striking-Through the task, indicating that, the user, haven't finished the task
            }
        }

/*        Log.d("RV position", "Hellooo, this is RV position: $position value")
        Log.d("RV positionToStartFrom", "value $positionToStartFrom of positionToStartFrom")
        println("Hello")*/
    }

    private fun taskDueDateIsThisHour(
        taskCalendar: Calendar,
        calendar: Calendar
    ) = (taskCalendar.get(Calendar.HOUR_OF_DAY) == calendar.get(Calendar.HOUR_OF_DAY)
            && taskCalendar.get(Calendar.DAY_OF_WEEK_IN_MONTH) == calendar.get(Calendar.DAY_OF_YEAR)
            && taskCalendar.get(Calendar.DAY_OF_WEEK) == calendar.get(Calendar.DAY_OF_WEEK)
            && taskCalendar.get(Calendar.DAY_OF_MONTH) == calendar.get(Calendar.DAY_OF_MONTH)
            && taskCalendar.get(Calendar.DAY_OF_YEAR) == calendar.get(Calendar.DAY_OF_YEAR)
            )

    private fun taskDueDateIsTomorrow(
        taskCalendar: Calendar,
        calendar: Calendar
    ) = (taskCalendar.get(Calendar.DAY_OF_WEEK_IN_MONTH+1) == calendar.get(Calendar.DAY_OF_YEAR+1)
            && taskCalendar.get(Calendar.DAY_OF_WEEK+1) == calendar.get(Calendar.DAY_OF_WEEK+1)
            && taskCalendar.get(Calendar.DAY_OF_MONTH+1) == calendar.get(Calendar.DAY_OF_MONTH+1)
            && taskCalendar.get(Calendar.DAY_OF_YEAR+1) == calendar.get(Calendar.DAY_OF_YEAR+1)
            )


    private fun taskDueDateIsToday(
        taskCalendar: Calendar,
        calendar: Calendar
    ) = (taskCalendar.get(Calendar.DAY_OF_WEEK_IN_MONTH) == calendar.get(Calendar.DAY_OF_YEAR)
            && taskCalendar.get(Calendar.DAY_OF_WEEK) == calendar.get(Calendar.DAY_OF_WEEK)
            && taskCalendar.get(Calendar.DAY_OF_MONTH) == calendar.get(Calendar.DAY_OF_MONTH)
            && taskCalendar.get(Calendar.DAY_OF_YEAR) == calendar.get(Calendar.DAY_OF_YEAR)
            )

    private fun taskDueDateIsThisWeek(
        taskCalendar: Calendar,
        calendar: Calendar
    ) = (taskCalendar.get(Calendar.WEEK_OF_MONTH) == calendar.get(Calendar.WEEK_OF_MONTH)
            && taskCalendar.get(Calendar.WEEK_OF_YEAR) == calendar.get(Calendar.WEEK_OF_YEAR)
            && taskCalendar.get(Calendar.DAY_OF_WEEK_IN_MONTH) == calendar.get(Calendar.DAY_OF_YEAR)
            && taskCalendar.get(Calendar.DAY_OF_WEEK) == calendar.get(Calendar.DAY_OF_WEEK)
            && taskCalendar.get(Calendar.DAY_OF_MONTH) == calendar.get(Calendar.DAY_OF_MONTH)
            && taskCalendar.get(Calendar.DAY_OF_YEAR) == calendar.get(Calendar.DAY_OF_YEAR)
            )

    private fun taskDueDateIsMoreThanWeek(
        taskCalendar: Calendar,
        calendar: Calendar
    ) = (taskCalendar.get(Calendar.WEEK_OF_MONTH) > calendar.get(Calendar.WEEK_OF_MONTH)
            && taskCalendar.get(Calendar.WEEK_OF_YEAR) > calendar.get(Calendar.WEEK_OF_YEAR)
            && taskCalendar.get(Calendar.DAY_OF_WEEK_IN_MONTH) != calendar.get(Calendar.DAY_OF_YEAR)
            && taskCalendar.get(Calendar.DAY_OF_WEEK) != calendar.get(Calendar.DAY_OF_WEEK)
            && taskCalendar.get(Calendar.DAY_OF_MONTH) != calendar.get(Calendar.DAY_OF_MONTH)
            && taskCalendar.get(Calendar.DAY_OF_YEAR) != calendar.get(Calendar.DAY_OF_YEAR)
            )

    override fun getItemCount(): Int = tasksMutableList.size
}
