package com.tuwaiq.enjazzoneapp.ui.todo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast

import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.tuwaiq.enjazzoneapp.R
import com.tuwaiq.enjazzoneapp.data.TaskTodo


class TodoRVHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
    val tvTodoRow:TextView = itemView.findViewById(R.id.tvTodoRow)
    val etTodoRow:EditText = itemView.findViewById(R.id.etTodoRow)
    val editIB: ImageButton = itemView.findViewById(R.id.editIB)
    val editCheckIB: ImageButton = itemView.findViewById(R.id.editCheckIB)
    val deleteIB: ImageButton = itemView.findViewById(R.id.deleteIB)
}

class TodoRVListAdapter(private var list: List<TaskTodo>, private val view: View):RecyclerView.Adapter<TodoRVHolder>() {

    private val tasksCollectionRef = Firebase.firestore.collection("users")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoRVHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.todo_r_v_item_row, parent, false)
        return TodoRVHolder(view)
    }

    override fun onBindViewHolder(holder: TodoRVHolder, position: Int) {
        val taskTodoInAdapter:TaskTodo = list[position]
        holder.tvTodoRow.text = taskTodoInAdapter.task
        holder.deleteIB.setOnClickListener {
            tasksCollectionRef.document(taskTodoInAdapter.taskId).delete()
            list -= taskTodoInAdapter
        }
        holder.editIB.setOnClickListener {
            Toast.makeText(view.context, "\"Edit Button\" has been pressed!", Toast.LENGTH_LONG).show()
            holder.tvTodoRow.visibility = View.INVISIBLE
            holder.etTodoRow.visibility = View.VISIBLE
            holder.editIB.visibility = View.GONE
            holder.editCheckIB.visibility = View.VISIBLE
            holder.etTodoRow.requestFocus()
        }
        holder.etTodoRow.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                holder.etTodoRow.setText(holder.tvTodoRow.text, TextView.BufferType.EDITABLE)
                holder.etTodoRow.setSelectAllOnFocus(true)
                holder.etTodoRow.selectAll()
            }
            else todoTextViewState(holder)
        }
        holder.editCheckIB.setOnClickListener {
            val taskNewTitle = holder.etTodoRow.text.toString()
            if (taskNewTitle.isNotEmpty()) {
                taskTodoInAdapter.task = taskNewTitle
                tasksCollectionRef.document(taskTodoInAdapter.taskId).update("task", taskTodoInAdapter.task)
            }
            holder.tvTodoRow.text = taskTodoInAdapter.task
            todoTextViewState(holder)
            //todoRVDraw(todoRecyclerView, view)
        }
    }
    /*override fun onBindViewHolder(holder: TodoRVHolder, position: Int) {

        var taskInAdapter : TaskTodo = list[position]
        //val oldTaskInAdapter = list[position]
        //holder.tvTodoRow.text = taskInAdapterFromClass.task
        //holder.tvTodoRow.text = taskInAdapterFromClass.task
       if (position == 1 && taskInAdapter.task.isEmpty())
            taskInAdapter = list[position-1]

        holder.tvTodoRow.text = taskInAdapter.task
        //setTextViewHeight(holder)

        holder.editIB.setOnClickListener {
            Toast.makeText(view.context, "\"Edit Button\" has been pressed!", Toast.LENGTH_LONG).show()
            holder.tvTodoRow.visibility = View.INVISIBLE
            holder.etTodoRow.visibility = View.VISIBLE
            holder.editIB.visibility = View.GONE
            holder.editCheckIB.visibility = View.VISIBLE
            holder.etTodoRow.requestFocus()
        }
        holder.etTodoRow.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                holder.etTodoRow.setText(holder.tvTodoRow.text, TextView.BufferType.EDITABLE)
                holder.etTodoRow.setSelectAllOnFocus(true)
                holder.etTodoRow.selectAll()
            }
            else todoTextViewState(holder)
        }
        holder.editCheckIB.setOnClickListener {
            val taskNewTitle = holder.etTodoRow.text.toString()
            if (taskNewTitle.isNotEmpty()) {
                taskInAdapter.task = taskNewTitle
                editTask(taskInAdapter)
            }
            holder.tvTodoRow.text = taskInAdapter.task
            todoTextViewState(holder)
            todoRVDraw(todoRecyclerView, view)
        }
        holder.deleteIB.setOnClickListener {
            Toast.makeText(view.context, "\"Delete Button\" has been pressed!\nDeleting $taskInAdapter", Toast.LENGTH_LONG).show()
            if (taskDataList.size <= 1) {
                taskDataList[0] = TaskTodo("")
                todoRVDraw(todoRecyclerView, view)
            }
            else
            taskDataList -= taskInAdapter
            todoRVDraw(todoRecyclerView, view)
            deleteTask(taskInAdapter)
            println("taskDataList $taskDataList <------------- taskDataList")
            println("position $position <------------- POSITION")
            println("taskDataList $taskDataList <------------- taskDataList")
            println("is taskDataList Null Or Empty? ${taskDataList.isNullOrEmpty()} <------------- is taskDataList Null Or Empty?")
            println(" VIEW PARENT: ${view.parent}")
        }
    }*/

    override fun getItemCount(): Int = list.size

    private fun todoTextViewState(holder: TodoRVHolder) {
        holder.etTodoRow.visibility = View.INVISIBLE
        holder.tvTodoRow.visibility = View.VISIBLE
        holder.editCheckIB.visibility = View.INVISIBLE
        holder.editIB.visibility = View.VISIBLE
    }

    private fun getOldTaskData():TaskTodo {
        val taskTitle = view.findViewById<TextView>(R.id.tvTodoRow).text.toString()
        return TaskTodo(taskTitle)
    }

    private fun getNewTaskData():Map<String, Any> {
        val taskTitle = view.findViewById<EditText>(R.id.etTodoRow).text.toString()
        val map = mutableMapOf<String, Any>()
        if (taskTitle.isNotEmpty()) map["task"]=taskTitle
        return map
    }

    private fun retrieveData() {
        tasksCollectionRef.get().addOnCompleteListener {
            if (it.isSuccessful) {
                val stringBuilder = StringBuilder()
                for (document in it.result!!.documents) {
                    val task = document.toObject<TaskTodo>()
                    stringBuilder.append("$task \n")
                }
                println("String Builder: $stringBuilder <------------- SB")
            }
        }
    }

    //private fun editTask(task:TaskTodo)
    //{

/*
        tasksCollectionRef
            .whereEqualTo("task", task.task)
            .get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    if (it.result!!.documents.isNotEmpty()) {
                        for (document in it.result!!.documents) {
                            tasksCollectionRef.document(document.id).set(
                                newTaskMap, SetOptions.merge()
                            )
                        }
                    }else {
                        Toast.makeText(view.context, "No matching document", Toast.LENGTH_LONG).show()
                    }
                }
            }.addOnFailureListener {
                Toast.makeText(view.context, "Add on Failure Listener: ${it.localizedMessage}", Toast.LENGTH_LONG).show()
            }
*/
    //}
    private fun deleteTask(task:TaskTodo) {

/*        tasksCollectionRef
            .whereEqualTo("task", task.task)
            .get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    if (it.result!!.documents.isNotEmpty()) {
                        for (document in it.result!!.documents) {
                            tasksCollectionRef.document(document.id).delete()
                        }
                    }else {
                        Toast.makeText(view.context, "No matching document", Toast.LENGTH_LONG).show()
                    }
                }
            }.addOnFailureListener {
                Toast.makeText(view.context, "Add on Failure Listener: ${it.localizedMessage}", Toast.LENGTH_LONG).show()
            }*/
    }

    private fun setTextViewHeight(holder: TodoRVHolder) {
        val heightInPixels = holder.tvTodoRow.lineCount*holder.tvTodoRow.lineHeight
        holder.tvTodoRow.height = heightInPixels
        println("Height In Pixels: $heightInPixels <--------------------- !!!!!!!!!!!!!!!!")
    }

}
