package com.tuwaiq.enjazzoneapp.ui.todo

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.tuwaiq.enjazzoneapp.R
import com.tuwaiq.enjazzoneapp.data.TasksDataClass
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

const val welcomingMessageString = "Plan your day, Achieve more!"
//var taskDataList = mutableListOf<TaskTodo>(TaskTodo())
//var tasksArrayList:ArrayList<TaskTodo> = mutableListOf<TaskTodo>(TaskTodo())
//lateinit var todoRecyclerView: RecyclerView

class ToDoFragment : Fragment() {
    private lateinit var todaysDateTV: TextView
    private lateinit var digitalTextClock: TextClock
    private lateinit var todoRecyclerView: RecyclerView
    private lateinit var toDoViewModel: ToDoViewModel //by viewModels()
    private lateinit var enterTaskET: EditText
    private lateinit var sendIB: ImageButton
    lateinit var welcomingMessageTV: TextView
    private val tasksCollectionRef = Firebase.firestore.collection("users")
    private lateinit var db:FirebaseFirestore
    private lateinit var tasksArrayList:ArrayList<TasksDataClass>
    //private lateinit var todoRVListAdapter:TodoRVListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_to_do, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        todaysDateTV = view.findViewById(R.id.tvTodaysDetailedDate)
        todaysDateTV.text = getTodayDetailedDate()
//        todaysDateTV.text = Calendar.getInstance().time.toString() //Sun Jan 09 11:18:34 GMT+03:00 2022

        digitalTextClock = view.findViewById(R.id.tvDigitalClock)
/*        digitalTextClock.format24Hour = HH:mm
        digitalTextClock.format12Hour = h:mm a*/
        digitalTextClock.setOnClickListener {
            println("format24Hour ${digitalTextClock.format24Hour}\nformat12Hour ${digitalTextClock.format12Hour}")
            if (digitalTextClock.format24Hour == "HH:mm" || digitalTextClock.format12Hour == "h:mm") {
                digitalTextClock.format24Hour = "h:mm"
                digitalTextClock.format12Hour = "HH:mm"
            }else {
                digitalTextClock.format24Hour = "HH:mm"
                digitalTextClock.format12Hour = "h:mm a"
            }
        }

        //todaysDateTV.text = Date.getCurrentDateTime().toString("yyyy/MM/dd HH:mm:ss")
        todoRecyclerView = view.findViewById(R.id.rvTodo)
        //todoRecyclerView.setHasFixedSize(true)
        tasksArrayList = arrayListOf()
        todoRecyclerView.adapter = TodoRVListAdapter(tasksArrayList, view)
        todoRecyclerView.layoutManager = LinearLayoutManager(this.context)
        toDoViewModel = ViewModelProvider(this)[ToDoViewModel::class.java]

        //getAllTasksInDB()
        toDoViewModel.getAllTasks(tasksArrayList,viewLifecycleOwner).observe(viewLifecycleOwner,{
            todoRecyclerView.adapter = TodoRVListAdapter(it, view)
        })

        enterTaskET = view.findViewById(R.id.enterATaskET)
        sendIB = view.findViewById(R.id.sendIB)
        sendIB.isEnabled = false
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

        //todoRecyclerView.adapter = TodoRVListAdapter(taskDataList, view)

        enterTaskET.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                sendIB.isEnabled = enterTaskET.text.isNotBlank()
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                sendIB.isEnabled = enterTaskET.text.isNotBlank()
            }

            override fun afterTextChanged(s: Editable?) {
                sendIB.isEnabled = enterTaskET.text.isNotBlank()
            }
        })

        sendIB.setOnClickListener {
            val taskTitle: String = enterTaskET.text.toString()
            val todoTask = TasksDataClass()
            todoTask.taskTitle = taskTitle
            todoTask.taskId = UUID.randomUUID().toString()
            //saveTaskInDB(todoTask)

            toDoViewModel.saveTask(todoTask)

            welcomingMessageTV.visibility = View.GONE
            todoRecyclerView.adapter = TodoRVListAdapter(tasksArrayList, view)
            todoRecyclerView.layoutManager = LinearLayoutManager(this.context)
            //todoRVListAdapter.notifyItemInserted(todoRVListAdapter.itemCount)
            enterTaskET.text = null
        }
    } // onViewCreated END

/*    private fun saveTaskInDB(task:TasksDataClass) {
        tasksCollectionRef.document(task.taskId).set(task).addOnCompleteListener {
            if (it.isSuccessful) {
                Toast.makeText(this@ToDoFragment.context, "Successfully added the task! Way to go 🤩", Toast.LENGTH_SHORT).show()
            } else Toast.makeText(this@ToDoFragment.context, "Error 😣😫😭😭😭", Toast.LENGTH_SHORT).show()
        }
            .addOnFailureListener {
                println("Localized message: \"${it.localizedMessage}\" <------------")
            }
    }*/
/*    private fun getAllTasksInDB() {
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
                            tasksArrayList.add(documentChange.document.toObject(TasksDataClass::class.java))
                        }
                    }
                    tasksArrayList.sortBy { list -> list.nowDate }
                    todoRecyclerView.adapter?.notifyDataSetChanged()
                }
            })
    }*/
}

/*fun todoRVDraw(todoRecyclerView:RecyclerView ,view:View) {
    todoRecyclerView.adapter = TodoRVListAdapter(tasksArrayLis, view)
    todoRecyclerView.layoutManager = LinearLayoutManager(view.context)
}*/

fun welcomeText(taskList: List<TasksDataClass>, tvWelcomeText: TextView) {
    if (taskList.isEmpty()) tvWelcomeText.visibility = View.VISIBLE
    else tvWelcomeText.visibility = View.GONE
}

fun getTodayDetailedDate (time:Date = Calendar.getInstance().time) :String {
    val formatter = SimpleDateFormat("EEEE: D MMM YYYY")
    return formatter.format(time)
}
