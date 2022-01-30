package com.tuwaiq.enjazzoneapp.ui.task_details

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.tuwaiq.enjazzoneapp.R
import android.view.WindowManager
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.tuwaiq.enjazzoneapp.data.TasksDataClass
import java.text.SimpleDateFormat
import java.util.*
import android.view.WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat.recreate
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.card.MaterialCardView
import com.tuwaiq.enjazzoneapp.hasChangesSharedPrefBooleanKey
import com.tuwaiq.enjazzoneapp.sharedPreferences
import com.tuwaiq.enjazzoneapp.ui.todo.ToDoViewModel


class TaskDetailsDialogFragment: DialogFragment() {

    private lateinit var vmViewModel:ToDoViewModel

    private lateinit var tvDialogFragmentHeaderTitleTV:TextView

    private lateinit var tvTaskCreationDateTV:TextView

    private lateinit var etTaskTitleET:EditText

    private lateinit var etTaskDescriptionET:EditText

    private lateinit var mcvSaveChangesMCV:CardView
    private lateinit var tvSaveChangesTV:TextView

    private lateinit var cvPickDueDateCV:CardView
    private lateinit var tvTaskDueDateTV:TextView

    private lateinit var cvStartingHourCV:CardView
    private lateinit var tvTaskStartingHourTV:TextView

    private lateinit var cvEndingHourCV:CardView
    private lateinit var tvTaskEndHourTV:TextView

    private lateinit var mcvDeleteTaskMCV:MaterialCardView

    private var taskObject = arguments?.getParcelable<TasksDataClass>("taskInAdapter")

    private var taskPosition:Int? = null

    private val tasksCollectionRef = Firebase.firestore.collection("users")
    private val currentUserID = FirebaseAuth.getInstance().currentUser?.uid

    override fun onStart() {
        super.onStart()
        dialog?.window?.setSoftInputMode(SOFT_INPUT_ADJUST_RESIZE)

        dialog?.window?.setLayout(1050,1770)
        dialog?.window?.setBackgroundDrawableResource(R.drawable.radius)
        //dialog?.window?.setGravity(Gravity.BOTTOM)
        val params: WindowManager.LayoutParams? = dialog?.window?.attributes
        params!!.verticalMargin = (0.0001).toFloat()
        dialog?.window?.attributes = params
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        vmViewModel =
            ViewModelProvider(this)[ToDoViewModel::class.java]
        return inflater.inflate(R.layout.dialog_fragment_task_details, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
/*        val taskObject = arguments?.getParcelable<TasksDataClass>("taskInAdapter")
        tvTaskDetailsTV = view.findViewById(R.id.tvTaskDetailsTV)*/

        val sharedEdit = sharedPreferences.edit()
        sharedEdit.putBoolean(hasChangesSharedPrefBooleanKey, false).commit()

        val dateAndDaySimpleDateFormat = SimpleDateFormat("EE dd MMM yyyy")
        val hourSimpleDateFormat = SimpleDateFormat("h:mm a")

        taskPosition = arguments?.getInt("position")
        taskObject = arguments?.getParcelable("taskInAdapter")

        val starterTaskTitle = taskObject?.taskTitle.toString()
        val starterTaskDescription = taskObject?.taskDescription.toString()

        tvDialogFragmentHeaderTitleTV = view.findViewById(R.id.tvDialogFragmentHeaderTitleTV)

        tvTaskCreationDateTV = view.findViewById(R.id.tvCreationDateTV)
        etTaskTitleET = view.findViewById(R.id.etTaskTitleET)
        etTaskDescriptionET = view.findViewById(R.id.etTaskDescriptionET)

        mcvSaveChangesMCV = view.findViewById(R.id.mcvSaveChangesMCV)
        tvSaveChangesTV = view.findViewById(R.id.tvSaveChangesTV)

        cvPickDueDateCV = view.findViewById(R.id.cvPickDueDateCV)
        tvTaskDueDateTV = view.findViewById(R.id.tvTaskDueDateTV)

        cvStartingHourCV = view.findViewById(R.id.cvStartingHourCV)
        tvTaskStartingHourTV = view.findViewById(R.id.tvTaskStartingHourTV)

        cvEndingHourCV = view.findViewById(R.id.cvEndingHourCV)
        tvTaskEndHourTV = view.findViewById(R.id.tvTaskEndingHourTV)

        mcvDeleteTaskMCV = view.findViewById(R.id.mcvDeleteTaskMCV)

        if (taskObject != null) {
            tvDialogFragmentHeaderTitleTV.text = if (!taskObject!!.taskTitle.isNullOrBlank()) resources.getString(R.string.task_number)+taskPosition?.plus(1)+resources.getString(R.string.details) else resources.getString(R.string.new_task_in_details)
            tvTaskCreationDateTV.text = resources.getString(R.string.created_in)+dateAndDaySimpleDateFormat.format(Date(taskObject!!.nowDate))
            etTaskTitleET.setText(taskObject!!.taskTitle)
            etTaskDescriptionET.setText(taskObject!!.taskDescription)
            tvTaskDueDateTV.text = dateAndDaySimpleDateFormat.format(Date(taskObject!!.dueDate))

            tvTaskStartingHourTV.text = hourSimpleDateFormat.format(taskObject!!.taskStartingHourMillis)
            tvTaskEndHourTV.text = hourSimpleDateFormat.format(taskObject!!.taskEndingHourMillis)
        }

        val defaultMCVCardElevation = mcvSaveChangesMCV.cardElevation
        mcvSaveChangesMCV.cardElevation = 0F
        var titleCurrentText = etTaskTitleET.text.toString()
        var descriptionCurrentText = etTaskDescriptionET.text.toString()
        mcvSaveChangesMCV.isEnabled = false

        // Title
        etTaskTitleET.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
            override fun afterTextChanged(s: Editable?) {
                when {
                    etTaskTitleET.text.isNotBlank() && s.toString() != titleCurrentText-> {
                        mcvSaveChangesMCV.isEnabled = true //etTaskTitleET.text.isNotBlank()
                        tvSaveChangesTV.setTextColor(resources.getColor(R.color.primary_blue))
                        mcvSaveChangesMCV.cardElevation = defaultMCVCardElevation

                    }
                    s.toString() == titleCurrentText || etTaskTitleET.text.isBlank() -> {
                        mcvSaveChangesMCV.isEnabled = false
                        tvSaveChangesTV.setTextColor(resources.getColor(R.color.grey))
                        mcvSaveChangesMCV.cardElevation = 0F
                    }
                }
            }
        })

        // Description
        etTaskDescriptionET.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                etTaskDescriptionET.setSelectAllOnFocus(true)
                etTaskDescriptionET.selectAll()
            }
            //else
        }
        etTaskDescriptionET.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
            override fun afterTextChanged(s: Editable?) {
                when {
                    etTaskDescriptionET.text.isNotBlank() && s.toString() != descriptionCurrentText-> {
                        mcvSaveChangesMCV.isEnabled = true
                        tvSaveChangesTV.setTextColor(resources.getColor(R.color.primary_blue))
                        mcvSaveChangesMCV.cardElevation = defaultMCVCardElevation

                    }
                    s.toString() == descriptionCurrentText || etTaskDescriptionET.text.isBlank() -> {
                        mcvSaveChangesMCV.isEnabled = false
                        tvSaveChangesTV.setTextColor(resources.getColor(R.color.grey))
                        mcvSaveChangesMCV.cardElevation = 0F

                    }
                }
            }
        })

        // Save
        mcvSaveChangesMCV.setOnClickListener {
            taskObject?.taskTitle = etTaskTitleET.text.toString()
            taskObject?.taskDescription = etTaskDescriptionET.text.toString()

            if (taskObject?.taskTitle != titleCurrentText) {
                taskObject?.let { it1 ->
                    tasksCollectionRef.document(currentUserID.toString()).collection("tasks")
                        .document(
                            it1.taskId
                        ).update("taskTitle", it1.taskTitle)
                }
                //sharedEdit.putBoolean(hasChangedSharedPrefBooleanKey, true).commit()
                titleCurrentText = taskObject?.taskTitle.toString()
                Toast.makeText(view.context, resources.getString(R.string.task_title_changes_saved), Toast.LENGTH_LONG).show()
            }

            if (taskObject?.taskDescription != descriptionCurrentText) {
                taskObject?.let { it1 ->
                    tasksCollectionRef.document(currentUserID.toString()).collection("tasks")
                        .document(
                            it1.taskId
                        ).update("taskDescription", it1.taskDescription)
                }
                //sharedEdit.putBoolean(hasChangedSharedPrefBooleanKey, true).commit()
                descriptionCurrentText = taskObject?.taskDescription.toString()
                Toast.makeText(view.context, resources.getString(R.string.task_description_changes_saved), Toast.LENGTH_LONG).show()
            }
            Toast.makeText(view.context, resources.getString(R.string.edit_saved_check), Toast.LENGTH_SHORT).show()
            sharedEdit.putBoolean(hasChangesSharedPrefBooleanKey, true).commit()
            mcvSaveChangesMCV.isEnabled = false
            tvSaveChangesTV.setTextColor(resources.getColor(R.color.grey))
            mcvSaveChangesMCV.cardElevation = 0F
        }

        // DUE-DATE
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val hourOfTheDay = calendar.get(Calendar.HOUR_OF_DAY)
        val minuteOfTheDay = calendar.get(Calendar.MINUTE)
        calendar.set(year, month, day, hourOfTheDay, minuteOfTheDay)
        cvPickDueDateCV.setOnClickListener{
            DatePickerDialog(
                requireActivity(),
                android.R.style.Theme_Material_Dialog_Alert, DatePickerDialog.OnDateSetListener
                { view, year, monthOfYear, dayOfMonth ->
                    calendar.set(Calendar.YEAR, year)
                    calendar.set(Calendar.MONTH, monthOfYear)
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                    //val myFormat = "EEE dd MMM yyyy" // mention the format you need
                    val sdf = SimpleDateFormat("EEE dd MMM yyyy", Locale.getDefault())
                    tvTaskDueDateTV.text = sdf.format(calendar.time)
                    val toDateToLong = calendar.timeInMillis
                    Toast.makeText(view.context, resources.getString(R.string.selected_due_date)+tvTaskDueDateTV.text, Toast.LENGTH_LONG).show()
                    //Toast.makeText(view.context, "Selected Date in Millis: $toDateToLong", Toast.LENGTH_LONG).show()
                    //println(toDateToLong)
                    taskObject?.dueDate = calendar.timeInMillis
                    taskObject?.let { it1 ->
                        tasksCollectionRef.document(currentUserID.toString()).collection("tasks").document(
                            it1.taskId).update("dueDate", it1.dueDate)
                    }
                    //view.setBackgroundColor(resources.getColor(R.color.primary_blue))
                    // Display Selected date in TextView
                    //tvTaskDueDateTV.text = "$dayOfMonth $monthOfYear $year"
                    Toast.makeText(view.context, resources.getString(R.string.edit_saved_check), Toast.LENGTH_SHORT).show()
                }, year, month, day
            ).show()
        }

        // STARTING HOUR
        val startHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        val startMinute = Calendar.getInstance().get(Calendar.MINUTE)
        cvStartingHourCV.setOnClickListener {
            TimePickerDialog(requireActivity(), android.R.style.Theme_Material_Dialog_Alert, TimePickerDialog.OnTimeSetListener
            { _, hour, minute ->
                val pickedDateTime = Calendar.getInstance()
                pickedDateTime.set(Calendar.HOUR_OF_DAY, hour)
                pickedDateTime.set(Calendar.MINUTE, minute)

                val sdf = SimpleDateFormat("h:mm a", Locale.getDefault())
                tvTaskStartingHourTV.text = sdf.format(pickedDateTime.time)
                val toDateToLong = pickedDateTime.timeInMillis
                Toast.makeText(view.context,resources.getString(R.string.selected_starting_hour)+tvTaskStartingHourTV.text,Toast.LENGTH_LONG).show()
                //Toast.makeText(view.context,"Selected Starting Hour in Millis: $toDateToLong",Toast.LENGTH_LONG).show()
                //println(toDateToLong)
                taskObject?.taskStartingHourMillis = pickedDateTime.timeInMillis
                taskObject?.let { it1 ->
                    tasksCollectionRef.document(currentUserID.toString()).collection("tasks")
                        .document(it1.taskId).update("taskStartingHourMillis", it1.taskStartingHourMillis)
                }
                Toast.makeText(view.context, resources.getString(R.string.edit_saved_check), Toast.LENGTH_SHORT).show()
            }, startHour, startMinute, false).show()
        } //cvStartingHourCV.setOnClickListener

        // ENDING HOUR
        val endHour = calendar.get(Calendar.HOUR_OF_DAY)
        val endMinute = calendar.get(Calendar.MINUTE)
        cvEndingHourCV.setOnClickListener {
            TimePickerDialog(requireContext(), android.R.style.Theme_Material_Dialog_Alert,
                { _, hour, minute ->
                    calendar.set(Calendar.HOUR_OF_DAY, hour)
                    calendar.set(Calendar.MINUTE, minute)

                    val sdf = SimpleDateFormat("h:mm a", Locale.getDefault())
                    tvTaskEndHourTV.text = sdf.format(calendar.time)
                    val toDateToLong = calendar.timeInMillis
                    Toast.makeText(view.context,resources.getString(R.string.selected_ending_hour)+tvTaskEndHourTV.text,Toast.LENGTH_LONG).show()
                    //Toast.makeText(view.context,"Selected Ending Hour in Millis: $toDateToLong",Toast.LENGTH_LONG).show()
                    println(toDateToLong)
                    taskObject?.taskEndingHourMillis = calendar.timeInMillis
                    taskObject?.let { it1 ->
                        tasksCollectionRef.document(currentUserID.toString()).collection("tasks")
                            .document(
                                it1.taskId
                            ).update("taskEndingHourMillis", it1.taskEndingHourMillis)
                    }
                    Toast.makeText(view.context, resources.getString(R.string.edit_saved_check), Toast.LENGTH_SHORT).show()
                }, endHour, endMinute, false).show()
        } //cvEndingHourCV.setOnClickListener

        // DELETE
        mcvDeleteTaskMCV.setOnClickListener {
            val alertDialogBuilder = AlertDialog.Builder(view.context)
            alertDialogBuilder.setMessage(resources.getString(R.string.alert_dialog_message))
                .setTitle(resources.getString(R.string.alert_dialog_title))
                // if the dialog is cancelable
                .setCancelable(true)
                .setPositiveButton(resources.getString(R.string.alert_dialog_yes)) { confirmDialog, _ ->
                    taskObject?.let { it1 ->
                        tasksCollectionRef.document(currentUserID.toString()).collection("tasks")
                            .document(it1.taskId).delete()
                    }
                    confirmDialog.dismiss()
                    //view.findNavController().navigate(R.id.navigation_ToDo)
                    dismiss()
                    //sharedEdit.putBoolean(hasChangesSharedPrefBooleanKey, true).commit()

                    recreate(context as Activity)
                    /*activity?.supportFragmentManager?.beginTransaction()
                        ?.replace(R.id.activity_main_container, ToDoFragment.newInstance())?.commit()*/
                    /*.removeAt(position)
                    notifyItemRemoved(position)*/
                }
                .setNegativeButton(resources.getString(R.string.alert_dialog_no)) { confirmDialog, _ ->
                    confirmDialog.dismiss()
                }
                .show()
        }

    } //override fun onViewCreated

    override fun onResume() {
        super.onResume()
/*
        dialog?.window?.addFlags(
            WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
        )
        dialog?.window?.decorView?.setOnTouchListener { v, event ->
            v.performClick()
            val dialogBounds = Rect()
            v.getHitRect(dialogBounds)
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    if (!dialogBounds.contains(event.x.toInt(), event.y.toInt())) {
                        // You have clicked the grey area
                        if (taskObject != startingTaskObject)
                            Toast.makeText(view?.context, "Clicked outside dialog fragment", Toast.LENGTH_LONG).show()
                        false // stop activity closing
                    }
                    Toast.makeText(view?.context, "Touch DOWN", Toast.LENGTH_SHORT).show()
                }
                MotionEvent.ACTION_UP -> Toast.makeText(view?.context, "Touch UP", Toast.LENGTH_SHORT).show()
                MotionEvent.ACTION_OUTSIDE -> Toast.makeText(view?.context, "Touch OUTSIDE", Toast.LENGTH_SHORT).show()
                MotionEvent.ACTION_CANCEL -> Toast.makeText(view?.context, "Touch CANCEL", Toast.LENGTH_SHORT).show()
                MotionEvent.BUTTON_BACK -> Toast.makeText(view?.context, "Touch BACK", Toast.LENGTH_SHORT).show()
            }
            true
        }*/
    }

    override fun onCancel(dialog: DialogInterface) { // equivalent to onBackPressed()
/*        Toast.makeText(view?.context, "Cancelling Task Details Dialog Fragment", Toast.LENGTH_SHORT)
            .show()*/

        super.onCancel(dialog)
    }

    override fun onDismiss(dialog: DialogInterface) {
        if (sharedPreferences.getBoolean(hasChangesSharedPrefBooleanKey, false)) {
//            view?.findNavController()?.navigate(R.id.navigation_ToDo)
            sharedPreferences.edit().putBoolean(hasChangesSharedPrefBooleanKey, false).apply()
            recreate(context as Activity)
        }
        else
        super.onDismiss(dialog)
/*        Toast.makeText(view?.context, "Any changes made are directly saved", Toast.LENGTH_SHORT)
            .show()*/

    }
}