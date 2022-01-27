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
import com.tuwaiq.enjazzoneapp.data.TasksDataClass
import android.content.Context.CLIPBOARD_SERVICE
import android.content.Intent
import android.graphics.Rect
import android.text.Editable
import android.text.TextWatcher
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat

import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.tuwaiq.enjazzoneapp.R
import com.tuwaiq.enjazzoneapp.sharedPreferences
import java.lang.Exception
import java.util.*

/*class TodoRVHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

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
}*/

open class TodoRVListAdapter(list: List<TasksDataClass>, private val view: View, private val viewModel: ToDoViewModel):RecyclerView.Adapter<
        //TodoRVHolder
        RecyclerView.ViewHolder?
        >() {
    private var  mList : MutableList<TasksDataClass> = list as MutableList<TasksDataClass>
    private val bodyView = 2
    private val footerView = 1

    private val tasksCollectionRef = Firebase.firestore.collection("users")
    private val currentUserID = FirebaseAuth.getInstance().currentUser?.uid

/*    override fun getItemId(position: Int): Long {
        return position.toLong()
    }*/

    // Define a view holder for Footer view
    inner class FooterViewHolder(itemView: View, val adapter: TodoRVListAdapter) : TodoRVListAdapter.ViewHolder(itemView, adapter) {
        val etEnterTaskET: EditText = itemView.findViewById(R.id.enterATaskET)
        val ibSendIB: ImageButton = itemView.findViewById(R.id.sendIB)
/*        init {
            itemView.setOnClickListener {
                // Do whatever you want on clicking the item
            }
        }*/
        fun bindView(position: Int) {
            etEnterTaskET.setText(sharedPreferences.getString("ENTER_TASK_DRAFT", null))
            ibSendIB.isEnabled = etEnterTaskET.text.isNotBlank()
            sendIBSetTint()
            etEnterTaskET.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {//sendIB.isEnabled = etEnterTaskET.text.isNotBlank()
                }

                override fun afterTextChanged(s: Editable?) {
                    ibSendIB.isEnabled = etEnterTaskET.text.isNotBlank()
                    sharedPreferences.edit().putString("ENTER_TASK_DRAFT", etEnterTaskET.text.toString()).apply()
                    sendIBSetTint()
                }
            })

            ibSendIB.setOnClickListener {
                val taskTitle: String = etEnterTaskET.text.toString()
                val todoTask = TasksDataClass()
                todoTask.taskTitle = taskTitle
                todoTask.taskId = UUID.randomUUID().toString()

                //mList.add(0, todoTask)
                //Handler(Looper.getMainLooper()).post {
                viewModel.createTask(todoTask)
                //}
                /*val list: MutableList<TasksDataClass> = viewModel.tasks.value!!
                list.add(list.size, todoTask)
                viewModel.tasks.value = list*/
                /*if (currentUserID != null) {
                    db.collection("users").document(currentUserID).collection("tasks").document(todoTask.taskId).set(todoTask)
                }*/
                //toDoViewModel.createTask(todoTask)


                //loadTasksList()
                //notifyItemChanged(0)
                //adapter.notifyItemChanged(position)
                //adapter.notifyItemInserted(0)
                //notifyItemInserted(0)
                notifyDataSetChanged()
                etEnterTaskET.text = null
                view.findNavController().navigate(R.id.navigation_ToDo)

                //notifyItemRangeInserted(mList.size, itemCount)
                //notifyItemRangeInserted(itemCount, 0)
                //notifyDataSetChanged()
                //mList.clear()
            }
        }

        private fun sendIBSetTint() {
            if (ibSendIB.isEnabled) view.context?.let { ContextCompat.getColor(it, R.color.primary_blue) }
                ?.let {
                    DrawableCompat.setTint(
                        ibSendIB.drawable,
                        it
                    )
                }
            else view.context?.let { ContextCompat.getColor(it, R.color.grey) }?.let {
                DrawableCompat.setTint(
                    ibSendIB.drawable,
                    it
                )
            }
        }

    }

    // Now define the viewholder for Normal list item
    inner class TodoRVHolder(itemView: View, adapter: TodoRVListAdapter) : TodoRVListAdapter.ViewHolder(itemView, adapter){
        val cvTaskCardView: CardView = itemView.findViewById(R.id.taskCardView)
        val tvTaskTitle: TextView = itemView.findViewById(R.id.tvTaskTitle)
        val etTodoRow: EditText = itemView.findViewById(R.id.etTodoRow)
        val editIB: ImageButton = itemView.findViewById(R.id.editIB)
        val editCheckIB: ImageButton = itemView.findViewById(R.id.editCheckIB)
        val cancelEditIB: ImageButton = itemView.findViewById(R.id.cancelIB)
        val deleteIB: ImageButton = itemView.findViewById(R.id.deleteIB)
        val ibShareTaskTitleIB: ImageButton = itemView.findViewById(R.id.ibShareTaskTitleIB)
        val editCL: ConstraintLayout = itemView.findViewById(R.id.editCL)
        val expandIB: ImageButton = itemView.findViewById(R.id.expandIB)

        fun bindView(position: Int) {

            //cvTaskCardView.removeAllViews()
            // bindView() method to implement actions
            val taskInAdapter:TasksDataClass = mList[position]
            val listNumbering = "${position + 1}. "
            tvTaskTitle.text = listNumbering+taskInAdapter.taskTitle

            expandIB.setOnClickListener {
                if (editCL.visibility == View.GONE) {
                    TransitionManager.beginDelayedTransition(editCL, AutoTransition())
                    editCL.visibility = View.VISIBLE
                    expandIB.setImageResource(R.drawable.baseline_expand_less_24)
                } else {
                    TransitionManager.beginDelayedTransition(editCL, AutoTransition())
                    editCL.visibility = View.GONE
                    expandIB.setImageResource(R.drawable.baseline_expand_more_24)
                }
            }

            cvTaskCardView.setOnClickListener {
                toTaskDetailsDialogFragment(taskInAdapter, position)
            }
            tvTaskTitle.setOnClickListener {
                toTaskDetailsDialogFragment(taskInAdapter, position)
            }
            tvTaskTitle.setOnLongClickListener {
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
                    ClipData.newPlainText("Task Title", tvTaskTitle.text.toString().substringAfter(". ")))
                Toast.makeText(view.context, "Text copied to clipboard", Toast.LENGTH_SHORT).show()
                true
            }

            deleteIB.setOnClickListener {
                val alertDialogBuilder = AlertDialog.Builder(view.context)
                alertDialogBuilder.setMessage("Delete this task? (deletion cannot be undone)")
                    .setTitle("Task Delete Conformation")
                    // if the dialog is cancelable
                    .setCancelable(true)
                    .setPositiveButton("Yes") { dialog, _ ->
                        tasksCollectionRef.document(currentUserID.toString()).collection("tasks")
                            .document(taskInAdapter.taskId).delete()
                        mList.removeAt(position)
                        notifyDataSetChanged()
                        //notifyItemRemoved(position)

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
            editIB.setOnClickListener {
                Toast.makeText(view.context, "\"Edit Button\" has been pressed!", Toast.LENGTH_LONG).show()
                tvTaskTitle.visibility = View.INVISIBLE
                etTodoRow.visibility = View.VISIBLE
                editIB.visibility = View.GONE
                deleteIB.visibility = View.GONE
                ibShareTaskTitleIB.visibility = View.GONE
                expandIB.visibility = View.GONE
                cancelEditIB.visibility = View.VISIBLE
                editCheckIB.visibility = View.VISIBLE
                etTodoRow.requestFocus()
            }
            etTodoRow.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    etTodoRow.setText(tvTaskTitle.text.toString().substringAfter(". "), TextView.BufferType.EDITABLE)
                    etTodoRow.setSelectAllOnFocus(true)
                    etTodoRow.selectAll()
                    etTodoRow.showKeyboard()
                }
                else todoTextViewState()
            }

            ibShareTaskTitleIB.setOnClickListener {
                val shareIntent = Intent()
                shareIntent.action = Intent.ACTION_SEND
                shareIntent.putExtra(Intent.EXTRA_TEXT, "a Task that's in my schedule: ${taskInAdapter.taskTitle}")
                shareIntent.type = "text/plain"
                ContextCompat.startActivity(view.context, shareIntent, null)
            }

            editCheckIB.setOnClickListener {
                val taskNewTitle = etTodoRow.text.toString()
                if (taskNewTitle.isNotEmpty()) {
                    taskInAdapter.taskTitle = taskNewTitle
                    tasksCollectionRef.document(currentUserID.toString()).collection("tasks").document(taskInAdapter.taskId).update("taskTitle", taskInAdapter.taskTitle)

                }
                tvTaskTitle.text = listNumbering+taskInAdapter.taskTitle
                todoTextViewState()
                etTodoRow.hideKeyboard()
            }
            cancelEditIB.setOnClickListener {
                todoTextViewState()
                etTodoRow.hideKeyboard()
            }


            StickyFooterItemDecoration()
        }      // fun bindView

        private fun todoTextViewState() {
            etTodoRow.visibility = View.INVISIBLE
            editCheckIB.visibility = View.INVISIBLE
            cancelEditIB.visibility = View.INVISIBLE

            editIB.visibility = View.VISIBLE
            deleteIB.visibility = View.VISIBLE
            ibShareTaskTitleIB.visibility = View.VISIBLE
            expandIB.visibility = View.VISIBLE
            tvTaskTitle.visibility = View.VISIBLE
        }
/*        init {
            itemView.setOnClickListener {
                // Do whatever you want on clicking the normal items
            }
        }*/

    }

    class StickyFooterItemDecoration : ItemDecoration() {
        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            val adapterItemCount = parent.adapter!!.itemCount
            if (isFooter(parent, view, adapterItemCount)) {
                //For the first time, each view doesn't contain any parameters related to its size,
                //hence we can't calculate the appropriate offset.
                //In this case, set a big top offset and notify adapter to update footer one more time.
                //Also, we shouldn't do it if footer became visible after scrolling.
                if (view.height == 0 && state.didStructureChange()) {
                    hideFooterAndUpdate(outRect, view, parent)
                } else {
                    outRect.set(0, calculateTopOffset(parent, view, adapterItemCount), 0, 0)
                }
            }
        }

        private fun hideFooterAndUpdate(outRect: Rect, footerView: View, parent: RecyclerView) {
            outRect.set(0, OFF_SCREEN_OFFSET, 0, 0)
            footerView.post { parent.adapter!!.notifyDataSetChanged() }
        }

        private fun calculateTopOffset(
            parent: RecyclerView,
            footerView: View,
            itemCount: Int
        ): Int {
            val topOffset =
                parent.height - visibleChildsHeightWithFooter(parent, footerView, itemCount)
            return if (topOffset < 0) 0 else topOffset
        }

        private fun visibleChildsHeightWithFooter(
            parent: RecyclerView,
            footerView: View,
            itemCount: Int
        ): Int {
            var totalHeight = 0
            //In the case of dynamic content when adding or removing are possible itemCount from the adapter is reliable,
            //but when the screen can fit fewer items than in adapter, getChildCount() from RecyclerView should be used.
            val onScreenItemCount = Math.min(parent.childCount, itemCount)
            for (i in 0 until onScreenItemCount - 1) {
                totalHeight += parent.getChildAt(i).height
            }
            return totalHeight + footerView.height
        }

        private fun isFooter(parent: RecyclerView, view: View, itemCount: Int): Boolean {
            return parent.getChildAdapterPosition(view) == itemCount
        }

        companion object {
            /**
             * Top offset to completely hide footer from the screen and therefore avoid noticeable blink during changing position of the footer.
             */
            private const val OFF_SCREEN_OFFSET = 5000
        }
    }

    // And now in onCreateViewHolder you have to pass the correct view
    // while populating the list item.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val v: View
        //return
        return if (viewType == footerView) {
            v = LayoutInflater.from(parent.context)
                .inflate(R.layout.task_r_v_row_footer, parent, false)
            FooterViewHolder(v, this)
        }
        else {//if (viewType == bodyView){
            v = LayoutInflater.from(parent.context)
                .inflate(R.layout.task_r_v_row, parent, false)
            TodoRVHolder(v, this)
        }
    }
    // Now the critical part. You have return the exact item count of your list
    // I've only one footer. So I returned data.size() + 1
    // If you've multiple headers and footers, you've to return total count
    // like, headers.size() + data.size() + footers.size()
    override fun getItemCount(): Int {
//        return if (mList == null) 0
//        else if (mList.size === 0) 1

        //Return 1 here to show nothing
        return if (mList.size == 0) 1
        else mList.size +1
        // Add extra view to show the footer view
    }

    // Now define getItemViewType of your own.
/*    override fun getItemViewType(position: Int): Int {
        return if (position == mList.size) {
            // This is where we'll add footer.
            FOOTER_VIEW
        } else super.getItemViewType(position)
    }*/
    override fun getItemViewType(position: Int): Int {
        //val item = NumberList[position]
        //val item = mList[position]
        return if(position == mList.size)
            footerView
        /*else if(item == "VIEW_TYPE_FOOTER") {
                return TYPE_FOOTER
            }*/
        else
            bodyView
    }

    // So you're done with adding a footer and its action on onClick.
    // Now set the default ViewHolder for NormalViewHolder

    open class ViewHolder(itemView: View?, adapter: TodoRVListAdapter) : RecyclerView.ViewHolder(itemView!!) {
        // Define elements of a row here
/*        open fun ViewHolder(itemView: View?) {
            // ----> super(itemView)
            // Find view by ID and initialize here

        }*/
    }

    companion object {
        private const val FOOTER_VIEW = 1
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        //holder.setIsRecyclable(false)
        //mList.clear()
        try {
            if (holder is TodoRVHolder) {
                val vh: TodoRVHolder = holder// as TodoRVHolder
                holder.bindView(position)

            } else if (holder is FooterViewHolder) {
                val vh = holder as FooterViewHolder
                holder.bindView(position)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        /*if (taskInAdapter.numberingInList==0) taskInAdapter.numberingInList = position+1
        val listNumbering = "${taskInAdapter.numberingInList}. "
        holder.tvTaskTitle.text = if (taskInAdapter.taskTitle.startsWith(listNumbering)) taskInAdapter.taskTitle else listNumbering+taskInAdapter.taskTitle*/

    }

    private fun toTaskDetailsDialogFragment(taskInAdapter: TasksDataClass, position: Int) {
        val bundle = bundleOf(
            "taskInAdapter" to taskInAdapter,
            "position" to position
        )
        view.findNavController().navigate(R.id.taskDetailsFragment, bundle)
    }


}

fun View.showKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
}
fun View.hideKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}
