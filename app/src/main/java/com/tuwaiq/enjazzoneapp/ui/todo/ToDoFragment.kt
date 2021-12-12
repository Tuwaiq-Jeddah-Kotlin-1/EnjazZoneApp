package com.tuwaiq.enjazzoneapp.ui.todo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.tuwaiq.enjazzoneapp.databinding.FragmentToDoBinding

class ToDoFragment : Fragment() {

    private lateinit var toDoViewModel: ToDoViewModel
    private var _binding: FragmentToDoBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        toDoViewModel =
            ViewModelProvider(this).get(ToDoViewModel::class.java)

        _binding = FragmentToDoBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textHome
        toDoViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}