package com.tuwaiq.enjazzoneapp.ui.todo

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.tuwaiq.enjazzoneapp.R
import com.tuwaiq.enjazzoneapp.data.TaskTodo
import java.util.*
import kotlin.collections.ArrayList

const val welcomingMessageString = "Plan your day, Achieve more!"
//var taskDataList = mutableListOf<TaskTodo>(TaskTodo())
//var tasksArrayList:ArrayList<TaskTodo> = mutableListOf<TaskTodo>(TaskTodo())
//lateinit var todoRecyclerView: RecyclerView

class ToDoFragment : Fragment() {
    private lateinit var todoRecyclerView: RecyclerView
    private lateinit var toDoViewModel: ToDoViewModel //by viewModels()
    private lateinit var enterTaskET: EditText
    private lateinit var sendIB: ImageButton
    lateinit var welcomingMessageTV: TextView
    private val tasksCollectionRef = Firebase.firestore.collection("users")
    private lateinit var db:FirebaseFirestore
    private lateinit var tasksArrayList:ArrayList<TaskTodo>
    //private lateinit var todoRVListAdapter:TodoRVListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_to_do, container, false)
        toDoViewModel =
            ViewModelProvider(this)[ToDoViewModel::class.java]
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        todoRecyclerView = view.findViewById(R.id.rvTodo)
        //todoRecyclerView.setHasFixedSize(true)

        tasksArrayList = arrayListOf()

        todoRecyclerView.adapter = TodoRVListAdapter(tasksArrayList, view)
        todoRecyclerView.layoutManager = LinearLayoutManager(this.context)
        getAllTasksInDB()
        enterTaskET = view.findViewById(R.id.enterATaskET)
        sendIB = view.findViewById(R.id.sendIB)
        welcomingMessageTV = view.findViewById(R.id.tvWelcomingMessage)
        welcomingMessageTV.text = welcomingMessageString

        welcomingMessageTV.visibility = View.GONE
/*        if (db.collection("users").get().result?.isEmpty == true)
            welcomingMessageTV.visibility = View.VISIBLE
        else welcomingMessageTV.visibility = View.GONE*/

/*        if ((taskDataList.size == 1 && taskDataList[0].task == "") || taskDataList.isNullOrEmpty()) {
            welcomingMessageTV.visibility = View.VISIBLE
            todoRecyclerView.visibility = View.INVISIBLE
        }
        else {
            welcomingMessageTV.visibility = View.GONE
            todoRecyclerView.visibility = View.VISIBLE
        }*/
        sendIB.isEnabled = false

        //todoRecyclerView.adapter = TodoRVListAdapter(taskDataList, view)

        enterTaskET.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //if(enterTaskET.text.isNotBlank())sendIB.isEnabled = true
                /*if(enterTaskET.text.isNotBlank())sendIB.isEnabled = true
                else sendIB.isEnabled = false*/
                sendIB.isEnabled = enterTaskET.text.isNotBlank()
                //if (enterTaskET.text.isNotBlank()) toDoViewModel.vmTaskTitle = enterTaskET.text.toString()
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                //if(enterTaskET.text.isNotBlank())sendIB.isEnabled = true
                sendIB.isEnabled = enterTaskET.text.isNotBlank()
                //if (enterTaskET.text.isNotBlank()) toDoViewModel.vmTaskTitle = enterTaskET.text.toString()
            }

            override fun afterTextChanged(s: Editable?) {
                //if(enterTaskET.text.isNotBlank())sendIB.isEnabled = true
                sendIB.isEnabled = enterTaskET.text.isNotBlank()
                //if (enterTaskET.text.isNotBlank()) toDoViewModel.vmTaskTitle = enterTaskET.text.toString()
            }
        })

        sendIB.setOnClickListener {
            val taskTitle: String = enterTaskET.text.toString()
            val todoTask = TaskTodo()
            todoTask.task = taskTitle
            todoTask.taskId = UUID.randomUUID().toString()
            saveTaskInDB(todoTask)
            welcomingMessageTV.visibility = View.GONE
            todoRecyclerView.adapter = TodoRVListAdapter(tasksArrayList, view)
            todoRecyclerView.layoutManager = LinearLayoutManager(this.context)
            //todoRVListAdapter.notifyItemInserted(todoRVListAdapter.itemCount)
            enterTaskET.text = null
        }
    } // onViewCreated END

    private fun saveTaskInDB(task:TaskTodo) {
        tasksCollectionRef.document(task.taskId).set(task).addOnCompleteListener {
            if (it.isSuccessful) {
                Toast.makeText(this@ToDoFragment.context, "Successfully added the task! Way to go 🤩", Toast.LENGTH_SHORT).show()
            } else Toast.makeText(this@ToDoFragment.context, "Error 😣😫😭😭😭", Toast.LENGTH_SHORT).show()
        }
            .addOnFailureListener {
                println("Localized message: \"${it.localizedMessage}\" <------------")
            }
    }
    private fun getAllTasksInDB() {
        db = FirebaseFirestore.getInstance()
        db.collection("users")
            .addSnapshotListener(object : EventListener<QuerySnapshot> {
                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                    if (error != null) {
                        Log.e("Firestore Error:", "")
                        return
                    }
                    for (documentChange:DocumentChange in value?.documentChanges!!) {
                        if (documentChange.type == DocumentChange.Type.ADDED) {
                            tasksArrayList.add(documentChange.document.toObject(TaskTodo::class.java))
                        }
                    }
                    tasksArrayList.sortBy { list -> list.nowDate }
                    todoRecyclerView.adapter?.notifyDataSetChanged()
                }


            })
    }
}

/*fun todoRVDraw(todoRecyclerView:RecyclerView ,view:View) {
    todoRecyclerView.adapter = TodoRVListAdapter(tasksArrayLis, view)
    todoRecyclerView.layoutManager = LinearLayoutManager(view.context)
}*/

fun welcomeText(taskList: List<TaskTodo>, tvWelcomeText: TextView) {
    if (taskList.isEmpty()) tvWelcomeText.visibility = View.VISIBLE
    else tvWelcomeText.visibility = View.GONE
}