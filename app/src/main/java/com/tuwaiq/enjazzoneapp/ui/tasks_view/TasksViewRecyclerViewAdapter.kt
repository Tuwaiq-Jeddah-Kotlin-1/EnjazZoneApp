package com.tuwaiq.enjazzoneapp.ui.tasks_view

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.tuwaiq.enjazzoneapp.R
import com.tuwaiq.enjazzoneapp.data.TasksDataClass
import java.text.SimpleDateFormat
import java.util.*


class TasksViewRecyclerViewHolder(itemView: View):  RecyclerView.ViewHolder(itemView){
    val cvTaskCardView: CardView = itemView.findViewById(R.id.taskCardView)
    val clTaskViewCL: ConstraintLayout = itemView.findViewById(R.id.clTaskViewCL)
    val tvTaskTitle: TextView = itemView.findViewById(R.id.tvTaskTitleTV)
    val chbIsDoneCheckBox: CheckBox = itemView.findViewById(R.id.chbIsDoneCheckBox)
    val tvTaskDescriptionTV: TextView = itemView.findViewById(R.id.tvTaskDescriptionTV)
    val tvTaskStartingHourTV: TextView = itemView.findViewById(R.id.tvTaskStartingHourTV)
    val tvTaskEndingHourTV: TextView = itemView.findViewById(R.id.tvTaskEndingHourTV)
    val tvTaskDueDateTV: TextView = itemView.findViewById(R.id.tvTaskDueDateTV)
}

class TasksViewRecyclerViewAdapter(private var tasksMutableList: MutableList<TasksDataClass>, private val view: View) : RecyclerView.Adapter<TasksViewRecyclerViewHolder>() {
    private val tasksCollectionRef = Firebase.firestore.collection("users")
    private val currentUserID = FirebaseAuth.getInstance().currentUser?.uid

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TasksViewRecyclerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.tasks_view_recycler_view_row, parent, false)
        return TasksViewRecyclerViewHolder(view)
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

        val defaultCardElevation = holder.cvTaskCardView.cardElevation
        val defaultConstraintLayoutBackgroundColor = ContextCompat.getColor(view.context, R.color.lighter_blue_transparent)
        val defaultTitleTextColor = holder.tvTaskTitle.currentTextColor

        if (holder.chbIsDoneCheckBox.isChecked) {
            holder.cvTaskCardView.cardElevation = 0F
            holder.clTaskViewCL.setBackgroundColor(ContextCompat.getColor(view.context, R.color.grey))
            holder.tvTaskTitle.setTextColor(ContextCompat.getColor(view.context, R.color.black))
            holder.tvTaskTitle.paintFlags = holder.tvTaskTitle.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        } else if (!holder.chbIsDoneCheckBox.isChecked) {
            holder.cvTaskCardView.cardElevation = defaultCardElevation
            holder.clTaskViewCL.setBackgroundColor(defaultConstraintLayoutBackgroundColor)
            holder.tvTaskTitle.setTextColor(defaultTitleTextColor)
            if (holder.tvTaskTitle.paintFlags == holder.tvTaskTitle.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG)
                holder.tvTaskTitle.paintFlags = holder.tvTaskTitle.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
        }

        if (taskInAdapter.dueDate < Calendar.getInstance().timeInMillis) {
            holder.clTaskViewCL.setBackgroundColor(
                ContextCompat.getColor(
                    view.context,
                    R.color.grey
                )
            )
            positionToStartFrom++
        }

        //if (Date(taskInAdapter.dueDate) >= Date(Calendar.getInstance().timeInMillis)) positionToStartFrom = position

        //if (positionToStartFrom != 0)

        holder.chbIsDoneCheckBox.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                taskInAdapter.done = true
                tasksCollectionRef.document(currentUserID.toString()).collection("tasks").document(taskInAdapter.taskId).update("done", taskInAdapter.done)
                notifyItemChanged(position)
            }
            else if (!isChecked){ //not checked
                taskInAdapter.done = false
                tasksCollectionRef.document(currentUserID.toString()).collection("tasks").document(taskInAdapter.taskId).update("done", taskInAdapter.done)
                notifyItemChanged(position)
            }
        }
    }

    override fun getItemCount(): Int = tasksMutableList.size
}
