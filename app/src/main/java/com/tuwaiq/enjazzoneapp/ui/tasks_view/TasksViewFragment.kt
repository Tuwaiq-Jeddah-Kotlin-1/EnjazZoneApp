package com.tuwaiq.enjazzoneapp.ui.tasks_view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.app.ActivityCompat.recreate
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tuwaiq.enjazzoneapp.R
import com.tuwaiq.enjazzoneapp.data.TasksDataClass
import com.tuwaiq.enjazzoneapp.ui.todo.ToDoViewModel
var positionToStartFrom = 0

class TasksViewFragment : Fragment() {

    private lateinit var tasksViewViewModel: ToDoViewModel
    private lateinit var tvFragmentHeader: TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var tasksArrayList:ArrayList<TasksDataClass>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_tasks_view, container, false)
        tasksViewViewModel =
            ViewModelProvider(this)[ToDoViewModel::class.java]
        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tvFragmentHeader = view.findViewById(R.id.text_calendar_view)
        recyclerView = view.findViewById(R.id.recyclerView)
        tasksArrayList = arrayListOf()
        recyclerView.adapter = TasksViewRecyclerViewAdapter(tasksArrayList, view)

        recyclerView.layoutManager = LinearLayoutManager(this.context)

        //var listCount = 0
        tasksViewViewModel.getAllTasks(tasksArrayList, viewLifecycleOwner).observe(viewLifecycleOwner, { it ->
            it.sortBy { list -> list.dueDate }
            recyclerView.adapter = TasksViewRecyclerViewAdapter(it, view)
            //listCount = it.size
            tvFragmentHeader.text = "All Tasks (${it.size}) \"due-date\" sorted:"
/*            var positionToStart = 0
            it.forEach { taskInAdapter ->
                taskInAdapter.dueDate
            }
            LinearLayoutManager(this.context).scrollToPositionWithOffset(positionToStart, 0)*/
            recyclerView.smoothScrollToPosition(positionToStartFrom)
            //LinearLayoutManager(view.context).startSmoothScroll(positionToStartFrom)
        })

    }
}
