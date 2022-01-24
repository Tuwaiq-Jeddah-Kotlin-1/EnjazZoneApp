package com.tuwaiq.enjazzoneapp.ui.tasks_view

import android.graphics.Paint
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.tuwaiq.enjazzoneapp.R
import com.tuwaiq.enjazzoneapp.data.TasksDataClass
import com.tuwaiq.enjazzoneapp.ui.todo.ToDoViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*


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

class TasksViewRecyclerViewAdapter(private var tasksMutableList: MutableList<TasksDataClass>,
                                   private val view: View,
                                   private val vm:ToDoViewModel,
                                   //private val llm: LinearLayoutManager,
                                   //private val clickListener: (position: Int) -> Unit
)
    : RecyclerView.Adapter<TasksViewRecyclerViewHolder>() {
    private val tasksCollectionRef = Firebase.firestore.collection("users")
    private val currentUserID = FirebaseAuth.getInstance().currentUser?.uid
    //private val mHandler = object :
    //private val vm

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
    : TasksViewRecyclerViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.tasks_view_recycler_view_row, parent, false)
        return TasksViewRecyclerViewHolder(view,
        //    clickListener
        )
    }

    override fun onBindViewHolder(
        holder: TasksViewRecyclerViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if(payloads.isNotEmpty()) {
        } else
            super.onBindViewHolder(holder,position, payloads)
    }

    override fun onBindViewHolder(holder: TasksViewRecyclerViewHolder, position: Int) {
        val taskInAdapter = tasksMutableList[position]

        val listNumbering = "${position + 1}. "
        holder.tvTaskTitle.text = listNumbering+taskInAdapter.taskTitle
        holder.chbIsDoneCheckBox.isChecked = taskInAdapter.done
        holder.tvTaskDescriptionTV.text = taskInAdapter.taskDescription
        holder.tvTaskStartingHourTV.text = SimpleDateFormat("h:mm a", Locale.getDefault()).format(Date(taskInAdapter.taskStartingHourMillis)).lowercase()+" - "
        holder.tvTaskEndingHourTV.text = SimpleDateFormat("h:mm a", Locale.getDefault()).format(Date(taskInAdapter.taskEndingHourMillis))
            .lowercase()
        holder.tvTaskDueDateTV.text = SimpleDateFormat("EEE dd MMM yyyy", Locale.getDefault()).format(Date(taskInAdapter.dueDate))

        val defaultCardElevation = 22.0F
        Log.d("CardElevationDefValue", holder.cvTaskCardView.cardElevation.toString())
        val defaultConstraintLayoutBackgroundColor = ContextCompat.getColor(view.context, R.color.lighter_blue_transparent)
        //Log.d("CL Background Color defValue", .toString())
        //holder.clTaskViewCL.ba
        val defaultTitleTextColor = holder.tvTaskTitle.currentTextColor
        val taskCalendar = Calendar.getInstance(Locale.getDefault())
        val calendar = Calendar.getInstance(Locale.getDefault())

        taskCalendar.timeInMillis = taskInAdapter.dueDate
        taskCalendar.get(Calendar.DAY_OF_WEEK)

        if (taskCalendar.time >= calendar.time) holder.tvTaskDueDateTV.setTextColor(ContextCompat.getColor(view.context, R.color.primary_blue))
        else if (taskCalendar.get(Calendar.HOUR_OF_DAY) == calendar.get(Calendar.HOUR_OF_DAY) && taskCalendar.get(Calendar.DAY_OF_MONTH) == calendar.get(Calendar.DAY_OF_MONTH)) holder.tvTaskDueDateTV.setTextColor(ContextCompat.getColor(view.context, R.color.green))

        //holder.tvTaskDueDateTV.setTextColor(if(taskCalendar.get(Calendar.HOUR_OF_DAY) == calendar.get(Calendar.HOUR_OF_DAY)) ContextCompat.getColor(view.context, R.color.green) else ContextCompat.getColor(view.context, R.color.green))

        if (holder.chbIsDoneCheckBox.isChecked) {
            holder.cvTaskCardView.cardElevation = 0F
            holder.clTaskViewCL.setBackgroundColor(ContextCompat.getColor(view.context, R.color.lighter_gray))
            holder.tvTaskTitle.setTextColor(ContextCompat.getColor(view.context, R.color.black))
            holder.tvTaskTitle.paintFlags = holder.tvTaskTitle.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        } else if (!holder.chbIsDoneCheckBox.isChecked) {
            holder.cvTaskCardView.cardElevation = defaultCardElevation
            holder.clTaskViewCL.setBackgroundColor(defaultConstraintLayoutBackgroundColor)
            holder.tvTaskTitle.setTextColor(defaultTitleTextColor)
            //if (holder.tvTaskTitle.paintFlags == holder.tvTaskTitle.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG)
                holder.tvTaskTitle.paintFlags = holder.tvTaskTitle.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
        }

        if (taskInAdapter.dueDate < Calendar.getInstance(Locale.getDefault()).timeInMillis) {
            holder.clTaskViewCL.setBackgroundColor(
                ContextCompat.getColor(
                    view.context,
                    R.color.lighter_gray
                )
            )
            positionToStartFrom++
        }

        //if (Date(taskInAdapter.dueDate) >= Date(Calendar.getInstance().timeInMillis)) positionToStartFrom = position

        //if (positionToStartFrom != 0)

        holder.chbIsDoneCheckBox.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                taskInAdapter.done = true
                vm.viewModelScope.launch {
                    vm.updateTask(taskInAdapter, "done")
                }
                //tasksCollectionRef.document(currentUserID.toString()).collection("tasks").document(taskInAdapter.taskId).update("done", taskInAdapter.done)
                //llm.scrollToPosition(position)
                Handler(Looper.myLooper()!!).post {
                    notifyItemChanged(position)
                }
//                notifyItemChanged(position)

            }
            else if (!isChecked){ //not checked
                taskInAdapter.done = false

                vm.viewModelScope.launch {
                    vm.updateTask(taskInAdapter, "done")
                }
                //tasksCollectionRef.document(currentUserID.toString()).collection("tasks").document(taskInAdapter.taskId).update("done", taskInAdapter.done)
                //llm.scrollToPosition(position)
  //              notifyItemChanged(position)

                Handler(Looper.myLooper()!!).post {
                    notifyItemChanged(position)
                }
            }
        }
        Log.d("RV position", "Hellooo, this is RV position: $position value")
        Log.d("RV positionToStartFrom", "value $positionToStartFrom of positionToStartFrom")
        println("Hello")
    }

    override fun getItemCount(): Int = tasksMutableList.size
}
