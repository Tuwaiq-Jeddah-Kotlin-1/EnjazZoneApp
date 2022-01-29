package com.tuwaiq.enjazzoneapp.ui.tasks_view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tuwaiq.enjazzoneapp.R
import com.tuwaiq.enjazzoneapp.ui.todo.ToDoViewModel


class TasksViewFragment : Fragment() {

    private lateinit var vmViewModel: ToDoViewModel
    private lateinit var tvFragmentHeader: TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var rvLayoutManager:LinearLayoutManager
    //private lateinit var tasksArrayList:ArrayList<TasksDataClass>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_tasks_view, container, false)
        vmViewModel =
            ViewModelProvider(this)[ToDoViewModel::class.java]

        getTasks(view)
        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getTasks(view)
/*        tvFragmentHeader = view.findViewById(R.id.text_calendar_view)
        recyclerView = view.findViewById(R.id.recyclerView)
        rvLayoutManager = LinearLayoutManager(this.context)
        recyclerView.layoutManager = rvLayoutManager

        vmViewModel.getAllTasks().observe(viewLifecycleOwner) { it ->
            //tasksViewViewModel.tasks.observe(viewLifecycleOwner, {
            it.sortByDescending { list -> list.dueDate }

            recyclerView.adapter = TasksViewRecyclerViewAdapter(it, view, vmViewModel)
            *//*{
                *//**//*(recyclerView.layoutManager as LinearLayoutManager).findViewByPosition(
                    positionToStartFrom)*//**//*
                (recyclerView.layoutManager as LinearLayoutManager).scrollToPosition(it)
                println("HEEEEEEEEEEEEEEEEre is Position: $it")
            }
            *//*
            //recyclerView.adapter!!.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY

            tvFragmentHeader.text = "All Tasks (${it.size}) \"due-date\" sorted:"
        }*/
        //recyclerView.adapter = TasksViewRecyclerViewAdapter(tasksArrayList, view, tasksViewViewModel)
/*        recyclerView.adapter = TasksViewRecyclerViewAdapter(tasksArrayList, view) {
            (recyclerView.layoutManager as LinearLayoutManager).findViewByPosition(it)
            println("HEEEEEEEEEEEEEEEEre is Position: $it")
        }*/
    }

    private fun getTasks(view: View) {
        tvFragmentHeader = view.findViewById(R.id.text_calendar_view)
        recyclerView = view.findViewById(R.id.recyclerView)
        rvLayoutManager = LinearLayoutManager(this.context)
        recyclerView.layoutManager = rvLayoutManager
        vmViewModel.getAllTasksSortedByDescendingDueDate().observe(viewLifecycleOwner) {
            //it.sortByDescending { list -> list.dueDate }
            recyclerView.adapter = TasksViewRecyclerViewAdapter(it, view, vmViewModel)
            tvFragmentHeader.text = "All Tasks (${it.size}) \"due-date\" sorted:"
        }
    }
}
