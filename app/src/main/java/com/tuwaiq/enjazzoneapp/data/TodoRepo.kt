package com.tuwaiq.enjazzoneapp.data

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*

class TodoRepo(context: Context) {
    private val uid=FirebaseAuth.getInstance().currentUser!!.uid

    private val db = FirebaseFirestore.getInstance()

    fun getAllTasksFromDB(tasksArrayList:ArrayList<TasksDataClass>): LiveData<ArrayList<TasksDataClass>> {
        val tasksList = MutableLiveData<ArrayList<TasksDataClass>>()
        tasksList.value= arrayListOf()

        db.collection("users").document(uid).collection("tasks").addSnapshotListener(object :
            EventListener<QuerySnapshot> {

            override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                if (error != null) {
                    Log.e("Firestore", error.message.toString())
                    return
                }
                for (dc: DocumentChange in value?.documentChanges!!) {
                    if (dc.type == DocumentChange.Type.ADDED) {
                        tasksArrayList.add(dc.document.toObject(TasksDataClass::class.java))
                    }
                }
                tasksArrayList.sortBy { list -> list.nowDate }
                tasksList.value= tasksArrayList
            }
        })
        return tasksList
    }

    fun saveTaskInDB(task:TasksDataClass) {
        db.collection("users").document(uid.toString()).collection("tasks").document(task.taskId).set(task).addOnCompleteListener {
            if (it.isSuccessful) {
                Log.e("Firestore", "Successfully added the task!")
            } else Log.e("Firestore", it.exception.toString())
        }
            .addOnFailureListener {
                println("Localized message: \"${it.localizedMessage}\" <------------")
                Log.e("Firestore", it.localizedMessage)
            }
    }
}