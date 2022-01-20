package com.tuwaiq.enjazzoneapp.ui.todo

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.tuwaiq.enjazzoneapp.R
import com.tuwaiq.enjazzoneapp.data.TasksDataClass
import android.content.Context.CLIPBOARD_SERVICE
import android.content.Intent
import androidx.core.content.ContextCompat

import androidx.core.os.bundleOf
import androidx.navigation.findNavController


class TodoRVHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
    val cvTaskCardView: CardView = itemView.findViewById(R.id.taskCardView)
    val tvTaskTitle:TextView = itemView.findViewById(R.id.tvTaskTitle)
    val etTodoRow: EditText = itemView.findViewById(R.id.etTodoRow)
    val editIB: ImageButton = itemView.findViewById(R.id.editIB)
    val editCheckIB: ImageButton = itemView.findViewById(R.id.editCheckIB)
    val cancelEditIB: ImageButton = itemView.findViewById(R.id.cancelIB)
    val deleteIB: ImageButton = itemView.findViewById(R.id.deleteIB)
    val ibShareTaskTitleIB: ImageButton = itemView.findViewById(R.id.ibShareTaskTitleIB)
    val editCL: ConstraintLayout = itemView.findViewById(R.id.editCL)
    val expandIB: ImageButton = itemView.findViewById(R.id.expandIB)
}

class TodoRVListAdapter(private var mList: MutableList<TasksDataClass>, private val view: View):RecyclerView.Adapter<TodoRVHolder>() {

    private val tasksCollectionRef = Firebase.firestore.collection("users")
    private val currentUserID = FirebaseAuth.getInstance().currentUser?.uid

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoRVHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.task_r_v_row, parent, false)
        return TodoRVHolder(view)
    }

    override fun onBindViewHolder(holder: TodoRVHolder, position: Int) {
        val taskInAdapter:TasksDataClass = mList[position]
        /*if (taskInAdapter.numberingInList==0) taskInAdapter.numberingInList = position+1
        val listNumbering = "${taskInAdapter.numberingInList}. "
        holder.tvTaskTitle.text = if (taskInAdapter.taskTitle.startsWith(listNumbering)) taskInAdapter.taskTitle else listNumbering+taskInAdapter.taskTitle*/
        val listNumbering = "${position + 1}. "
        holder.tvTaskTitle.text = listNumbering+taskInAdapter.taskTitle

        holder.expandIB.setOnClickListener {
            if (holder.editCL.visibility == View.GONE) {
                TransitionManager.beginDelayedTransition(holder.editCL, AutoTransition())
                holder.editCL.visibility = View.VISIBLE
                holder.expandIB.setImageResource(R.drawable.baseline_expand_less_24)
            } else {
                TransitionManager.beginDelayedTransition(holder.editCL, AutoTransition())
                holder.editCL.visibility = View.GONE
                holder.expandIB.setImageResource(R.drawable.baseline_expand_more_24)
            }
        }

        holder.cvTaskCardView.setOnClickListener {
            toTaskDetailsDialogFragment(taskInAdapter, position)
        }
        holder.tvTaskTitle.setOnClickListener {
            toTaskDetailsDialogFragment(taskInAdapter, position)
        }
        holder.tvTaskTitle.setOnLongClickListener {
/*            val params = it.layoutParams
            params.height+= 10
            it.layoutParams = params*/
/*            holder.editCL.visibility = if (holder.editIB.visibility != View.VISIBLE) View.VISIBLE else View.GONE
            holder.editIB.visibility = if (holder.editIB.visibility != View.VISIBLE) View.VISIBLE else View.GONE
            holder.deleteIB.visibility = if (holder.deleteIB.visibility != View.VISIBLE) View.VISIBLE else View.GONE
            holder.editInDetailsIB.visibility = if (holder.editInDetailsIB.visibility != View.VISIBLE) View.VISIBLE else View.GONE*/

/*            if (holder.tvTaskTitle.paintFlags == holder.tvTaskTitle.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG) {
                holder.tvTaskTitle.paintFlags = holder.tvTaskTitle.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                taskInAdapter.isDone = false
                tasksCollectionRef.document(taskInAdapter.taskId).update("isDone", taskInAdapter.taskTitle)
                Toast.makeText(view.context, "Task completion has been set to ${taskInAdapter.isDone}", Toast.LENGTH_SHORT).show()
            }
            else {
                holder.tvTaskTitle.paintFlags = holder.tvTaskTitle.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                Toast.makeText(view.context, "\"${taskInAdapter.taskTitle}\" has been completed, Horrraaaaaaay !! \n(👏👏👏👏 🙌🙌🙌)", Toast.LENGTH_SHORT).show()
                taskInAdapter.isDone = true
                tasksCollectionRef.document(taskInAdapter.taskId).update("isDone", taskInAdapter.taskTitle)
            }*/
            (view.context.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager?)?.setPrimaryClip(
                ClipData.newPlainText("Task Title", holder.tvTaskTitle.text.toString().substringAfter(". ")))
            Toast.makeText(view.context, "Text copied to clipboard", Toast.LENGTH_SHORT).show()
            true
        }

        holder.deleteIB.setOnClickListener {
            val alertDialogBuilder = AlertDialog.Builder(view.context)
            alertDialogBuilder.setMessage("Delete this task? (deletion cannot be undone)")
                .setTitle("Task Delete Conformation")
                // if the dialog is cancelable
                .setCancelable(true)
                .setPositiveButton("Yes") { dialog, _ ->
                    tasksCollectionRef.document(currentUserID.toString()).collection("tasks")
                        .document(taskInAdapter.taskId).delete()
                    mList.removeAt(position)
                    notifyItemRemoved(position)
                    dialog.dismiss()
                }
                .setNegativeButton("No") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
/*            val alert = alertDialogBuilder.create()
            alert.setTitle("Task Delete Conformation")
            alert.setMessage("Delete this task?")
            alert.show()*/
        }
        holder.editIB.setOnClickListener {
            Toast.makeText(view.context, "\"Edit Button\" has been pressed!", Toast.LENGTH_LONG).show()
            holder.tvTaskTitle.visibility = View.INVISIBLE
            holder.etTodoRow.visibility = View.VISIBLE
            holder.editIB.visibility = View.GONE
            holder.deleteIB.visibility = View.GONE
            holder.ibShareTaskTitleIB.visibility = View.GONE
            holder.expandIB.visibility = View.GONE
            holder.cancelEditIB.visibility = View.VISIBLE
            holder.editCheckIB.visibility = View.VISIBLE
            holder.etTodoRow.requestFocus()
        }
        holder.etTodoRow.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                holder.etTodoRow.setText(holder.tvTaskTitle.text.toString().substringAfter(". "), TextView.BufferType.EDITABLE)
                holder.etTodoRow.setSelectAllOnFocus(true)
                holder.etTodoRow.selectAll()
                holder.etTodoRow.showKeyboard()
            }
            else todoTextViewState(holder)
        }

        holder.ibShareTaskTitleIB.setOnClickListener {
            val shareIntent = Intent()
            shareIntent.action = Intent.ACTION_SEND
            shareIntent.putExtra(Intent.EXTRA_TEXT, "a Task that's in my schedule: ${taskInAdapter.taskTitle}")
            shareIntent.type = "text/plain"
            ContextCompat.startActivity(view.context, shareIntent, null)
        }

        holder.editCheckIB.setOnClickListener {
            val taskNewTitle = holder.etTodoRow.text.toString()
            if (taskNewTitle.isNotEmpty()) {
                taskInAdapter.taskTitle = taskNewTitle
                tasksCollectionRef.document(currentUserID.toString()).collection("tasks").document(taskInAdapter.taskId).update("taskTitle", taskInAdapter.taskTitle)

            }
            holder.tvTaskTitle.text = listNumbering+taskInAdapter.taskTitle
            todoTextViewState(holder)
            holder.etTodoRow.hideKeyboard()
        }
        holder.cancelEditIB.setOnClickListener {
            todoTextViewState(holder)
            holder.etTodoRow.hideKeyboard()
        }
    }

    override fun getItemCount(): Int = mList.size

    private fun toTaskDetailsDialogFragment(taskInAdapter: TasksDataClass, position: Int) {
        val bundle = bundleOf(
            "taskInAdapter" to taskInAdapter,
            "position" to position
        )
        view.findNavController().navigate(R.id.taskDetailsFragment, bundle)
    }

    private fun View.showKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
    }
    private fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }

    private fun todoTextViewState(holder: TodoRVHolder) {
        holder.etTodoRow.visibility = View.INVISIBLE
        holder.editCheckIB.visibility = View.INVISIBLE
        holder.cancelEditIB.visibility = View.INVISIBLE

        holder.editIB.visibility = View.VISIBLE
        holder.deleteIB.visibility = View.VISIBLE
        holder.ibShareTaskTitleIB.visibility = View.VISIBLE
        holder.expandIB.visibility = View.VISIBLE
        holder.tvTaskTitle.visibility = View.VISIBLE
    }
}
