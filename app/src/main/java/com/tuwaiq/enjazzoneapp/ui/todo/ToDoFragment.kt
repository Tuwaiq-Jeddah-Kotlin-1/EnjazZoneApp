package com.tuwaiq.enjazzoneapp.ui.todo

import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tuwaiq.enjazzoneapp.*
import com.tuwaiq.enjazzoneapp.data.TasksDataClass
import java.text.DecimalFormat
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.util.*
import kotlin.collections.ArrayList


const val welcomingMessageString = "Plan your day, Achieve more!"
const val aDayInMilliSeconds = 86400000
const val threeHoursInMilliSeconds = 10800000
//var taskDataList = mutableListOf<TaskTodo>(TaskTodo())
//var tasksArrayList:ArrayList<TaskTodo> = mutableListOf<TaskTodo>(TaskTodo())
//lateinit var todoRecyclerView: RecyclerView

class ToDoFragment : Fragment() {

    private lateinit var tvDayTimerHeaderTV: TextView
    private lateinit var tvDayTimerTV: TextView

    private lateinit var tvTodayDateTV: TextView
    private lateinit var tcDigitalTextClock: TextClock

    private lateinit var todoRecyclerView: RecyclerView

    private lateinit var toDoViewModel: ToDoViewModel

    private lateinit var etEnterTaskET: EditText
    private lateinit var sendIB: ImageButton

    lateinit var welcomingMessageTV: TextView

    private lateinit var tasksArrayList:ArrayList<TasksDataClass>
    //lateinit var sleepingHoursTimer: CountDownTimer

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_to_do, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvDayTimerHeaderTV = view.findViewById(R.id.tvTimerHeader)

        tvDayTimerTV = view.findViewById(R.id.tvDayRemainingTimer)
        val sixHoursInMilliSeconds:Long = 21600000
        val eighteenHoursInMilliSeconds:Long = 86400000 - sixHoursInMilliSeconds //64,800,000
        //val counterStarter:Long = 86400000 - sixHoursInMilliSeconds
        val simpleDateFormat = SimpleDateFormat("HH:mm:ss")
        val currentTime24HourFormat = simpleDateFormat.format(System.currentTimeMillis())
        //val currentTimeMillis = System.currentTimeMillis()
        val currentTimeInModTillADay = simpleDateFormat.format((System.currentTimeMillis()%84000000))
        val currentTimeInLongToDateToDateTimeFormat = simpleDateFormat.format(Date(System.currentTimeMillis()))
        val nowDate = OffsetDateTime.now( ZoneOffset.UTC )

        val currentTimeMillis = System.currentTimeMillis()
        val activeHoursCounter:Long = (86400000 - (currentTimeMillis % 86400000)) - 18000000
        val sleepingHoursCounter:Long = (86400000 - (currentTimeMillis % 86400000)) + 10800000

        var counterBooleanSwitch = true
        var longCounterUntilFinished:Long = activeHoursCounter//if(counterBooleanSwitch) activeHoursCounter else sleepingHoursCounter

        val activeHoursTimerHeader = "Enjaz Hours\uD83C\uDFC6 (6am-10pm):"
        val sleepingHoursTimerHeader = "Sleeping Time\uD83D\uDCA4\uD83D\uDCA4 (10pm-6am):"
        tvDayTimerHeaderTV.text = activeHoursTimerHeader

        val activeHoursTimer = object: CountDownTimer(activeHoursCounter, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                //println(millisUntilFinished)
                // Used for formatting digit to be in 2 digits only:
                tvDayTimerTV.text = twoDigitsDecimalFormatTime(millisUntilFinished)
            }
            override fun onFinish() {
                tvDayTimerHeaderTV.text = sleepingHoursTimerHeader
                val sleepingHoursTimer = object: CountDownTimer(sleepingHoursCounter, 1000) {
                    override fun onTick(millisUntilFinished: Long) {
                        // Used for formatting digit to be in 2 digits only:
                        val f: NumberFormat = DecimalFormat("00")
                        val hour = millisUntilFinished / 3600000 % 24
                        val min = millisUntilFinished / 60000 % 60
                        val sec = millisUntilFinished / 1000 % 60
                        tvDayTimerTV.text = "${f.format(hour)}:${f.format(min)}:${f.format(sec)}"
                    }
                    override fun onFinish() {
                        tvDayTimerHeaderTV.text = sleepingHoursTimerHeader

                        //tvDayTimerTV.text = "(HH:mm to HH:mm)"
                    }
                }
                sleepingHoursTimer.start()
            }
        }
        activeHoursTimer.start()

/*        val startCalendar = Calendar.getInstance()
        val endCalendar = Calendar.getInstance()

        val startMillis: Long = startCalendar.timeInMillis //get the start time in milliseconds
        endCalendar.set(2022, 0, 14) // 10 = November, month start at 0 = January
        endCalendar.timeInMillis // 10 = November, month start at 0 = January
        val endMillis: Long = endCalendar.timeInMillis //get the end time in milliseconds

        //val totalMillis = endMillis - startMillis //total time in milliseconds
        val totalMillis = System.currentTimeMillis() //total time in milliseconds*/


/*
            println(activeHoursCounter)
            println(twoDigitsDecimalFormatTime(activeHoursCounter))
            println(System.currentTimeMillis())
            println(twoDigitsDecimalFormatTime(System.currentTimeMillis()))
            println(nowDate)
*/

        //val countDownTimer:CountDownTimer = object: CountDownTimer((84000000 - (System.currentTimeMillis() % 84000000)), 1000) {
/*        val countDownTimer:CountDownTimer = object: CountDownTimer(84000000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                //println(millisUntilFinished)
                // Used for formatting digit to be in 2 digits only:
                //tvDayTimerTV.text = twoDigitsDecimalFormatTime(millisUntilFinished)
                var millisUntilFinished = millisUntilFinished
                val days: Long = TimeUnit.MILLISECONDS.toDays(millisUntilFinished)
                millisUntilFinished -= TimeUnit.DAYS.toMillis(days)
                val hours: Long = TimeUnit.MILLISECONDS.toHours(millisUntilFinished)
                millisUntilFinished -= TimeUnit.HOURS.toMillis(hours)
                val minutes: Long = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)
                millisUntilFinished -= TimeUnit.MINUTES.toMillis(minutes)
                val seconds: Long = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished)
                tvDayTimerTV.text = "$days:$hours:$minutes:$seconds" //You can compute the millisUntilFinished on hours/minutes/seconds
            }
            override fun onFinish() {
                tvDayTimerHeaderTV.text = sleepingHoursTimerHeader
            }
        }
        countDownTimer.start()*/

/*        val cdt: CountDownTimer = object : CountDownTimer(totalMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                var millisUntilFinished = millisUntilFinished
                val days: Long = TimeUnit.MILLISECONDS.toDays(millisUntilFinished)
                millisUntilFinished -= TimeUnit.DAYS.toMillis(days)
                val hours: Long = TimeUnit.MILLISECONDS.toHours(millisUntilFinished)
                millisUntilFinished -= TimeUnit.HOURS.toMillis(hours)
                val minutes: Long = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)
                millisUntilFinished -= TimeUnit.MINUTES.toMillis(minutes)
                val seconds: Long = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished)
                tvDayTimerTV.text = "$days:$hours:$minutes:$seconds" //You can compute the millisUntilFinished on hours/minutes/seconds
            }

            override fun onFinish() {
                tvDayTimerTV.text ="Finish!"
            }
        }
        cdt.start()*/



        tvDayTimerTV.setOnLongClickListener {
            Toast.makeText(this.context, "Time remaining until sleeping hours\uD83D\uDCA4\uD83D\uDCA4", Toast.LENGTH_LONG).show()
            true
        }

        tvTodayDateTV = view.findViewById(R.id.tvTodayDetailedDate)
        tvTodayDateTV.text = getTodayDetailedDate()

        tcDigitalTextClock = view.findViewById(R.id.tvDigitalTextClock)
        tcDigitalTextClock.format24Hour = "h:mm a"

        tcDigitalTextClock.setOnClickListener {
            if (tcDigitalTextClock.format24Hour == "HH:mm" || tcDigitalTextClock.format12Hour == "h:mm") {
                tcDigitalTextClock.format24Hour = "h:mm a"
                tcDigitalTextClock.format12Hour = "HH:mm"
            }else {
                tcDigitalTextClock.format24Hour = "HH:mm"
                tcDigitalTextClock.format12Hour = "h:mm a"
            }
        }


        todoRecyclerView = view.findViewById(R.id.rvTodoRecyclerView)
        //todoRecyclerView.setHasFixedSize(true)
        tasksArrayList = arrayListOf()
        todoRecyclerView.adapter = TodoRVListAdapter(tasksArrayList, view)
        todoRecyclerView.layoutManager = LinearLayoutManager(this.context)
        toDoViewModel = ViewModelProvider(this)[ToDoViewModel::class.java]

        //getAllTasksInDB()
        toDoViewModel.getAllTasks(tasksArrayList,viewLifecycleOwner).observe(viewLifecycleOwner,{
            todoRecyclerView.adapter = TodoRVListAdapter(it, view)
        })

        welcomingMessageTV = view.findViewById(R.id.tvWelcomingMessage)
        welcomingMessageTV.text = welcomingMessageString
        welcomingMessageTV.visibility = View.GONE

        sendIB = view.findViewById(R.id.sendIB)

        etEnterTaskET = view.findViewById(R.id.enterATaskET)
        etEnterTaskET.setText(sharedPreferences.getString("ENTER_TASK_DRAFT", null))
        sendIB.isEnabled = etEnterTaskET.text.isNotBlank()
        sendIBSetTint()
        etEnterTaskET.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {//sendIB.isEnabled = etEnterTaskET.text.isNotBlank()
            }

            override fun afterTextChanged(s: Editable?) {
                sendIB.isEnabled = etEnterTaskET.text.isNotBlank()
                sharedPreferences.edit().putString("ENTER_TASK_DRAFT", etEnterTaskET.text.toString()).apply()
                sendIBSetTint()
            }
        })

        sendIB.setOnClickListener {
            val taskTitle: String = etEnterTaskET.text.toString()
            val todoTask = TasksDataClass()
            todoTask.taskTitle = taskTitle
            todoTask.taskId = UUID.randomUUID().toString()

            toDoViewModel.saveTask(todoTask)

            //welcomingMessageTV.visibility = View.GONE
            todoRecyclerView.adapter = TodoRVListAdapter(tasksArrayList, view)
            todoRecyclerView.layoutManager = LinearLayoutManager(this.context)
            //todoRVListAdapter.notifyItemInserted(todoRVListAdapter.itemCount)
            etEnterTaskET.text = null
        }
    } // onViewCreated END

    private fun sendIBSetTint() {
        if (sendIB.isEnabled) context?.let { ContextCompat.getColor(it, R.color.primary_blue) }
            ?.let {
                DrawableCompat.setTint(
                    sendIB.drawable,
                    it
                )
            }
        else context?.let { ContextCompat.getColor(it, R.color.grey) }?.let {
            DrawableCompat.setTint(
                sendIB.drawable,
                it
            )
        }
    }

    private fun twoDigitsDecimalFormatTime(millisUntilFinished: Long): String {
        val f: NumberFormat = DecimalFormat("00")
        val hour = millisUntilFinished / 3600000 % 24
        val min = millisUntilFinished / 60000 % 60
        val sec = millisUntilFinished / 1000 % 60
        return "${f.format(hour)}:${f.format(min)}:${f.format(sec)} ⏳"
    }

    private fun editEditTextHeight() {
        if (etEnterTaskET.lineCount == etEnterTaskET.maxLines) {
            etEnterTaskET.maxLines = etEnterTaskET.lineCount+1
        }
    }
}

fun welcomeText(taskList: List<TasksDataClass>, tvWelcomeText: TextView) {
    if (taskList.isEmpty()) tvWelcomeText.visibility = View.VISIBLE
    else tvWelcomeText.visibility = View.GONE
}

fun getTodayDetailedDate (time:Date = Calendar.getInstance().time) :String {
    val formatter = SimpleDateFormat("EEEE: D MMM yyyy")
    return formatter.format(time)
}