package com.tuwaiq.enjazzoneapp.data

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class Repository {
    private val currentUserID =FirebaseAuth.getInstance().currentUser!!.uid
    private val db = FirebaseFirestore.getInstance()
    private val tasksCollectionRef = Firebase.firestore.collection("users")

    suspend fun getAllTasksFromDBSortedByDescendingNowDate(): MutableList<TasksDataClass> = withContext(
        Dispatchers.IO) {
        val newTasksList = mutableListOf<TasksDataClass>()
        //newTasksList = arrayListOf()

        db.collection("users").document(currentUserID).collection("tasks")
            .addSnapshotListener(object :
                EventListener<QuerySnapshot> {

                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                    if (error != null) {
                        Log.e("Firestore", error.message.toString())
                        return
                    }
                    for (dc: DocumentChange in value?.documentChanges!!) {
                        if (dc.type == DocumentChange.Type.ADDED)
                            newTasksList.add(dc.document.toObject(TasksDataClass::class.java))
                    }
                    newTasksList.sortByDescending { list -> list.nowDate }
                    //tasksList = tasksArrayList
                }
            })
        return@withContext newTasksList
    }

    suspend fun getAllTasksFromDBSortedByDescendingDueDate(): MutableList<TasksDataClass> = withContext(
        Dispatchers.IO) {
        val newTasksList = mutableListOf<TasksDataClass>()
        //newTasksList = arrayListOf()

        db.collection("users").document(currentUserID).collection("tasks")
            .addSnapshotListener(object :
                EventListener<QuerySnapshot> {

                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                    if (error != null) {
                        Log.e("Firestore", error.message.toString())
                        return
                    }
                    for (dc: DocumentChange in value?.documentChanges!!) {
                        if (dc.type == DocumentChange.Type.ADDED)
                            newTasksList.add(dc.document.toObject(TasksDataClass::class.java))
                    }
                    newTasksList.sortByDescending { list -> list.dueDate }
                    //tasksList = tasksArrayList
                }
            })
        return@withContext newTasksList
    }

    suspend fun createTaskInDB(task:TasksDataClass) = withContext(Dispatchers.IO) {
        db.collection("users").document(currentUserID).collection("tasks").document(task.taskId).set(task)
            .addOnCompleteListener {
            if (it.isSuccessful)
                Log.e("Firestore \"Create Task\", it.isSuccessful", "Successfully added the task! ${it.result}")
             else
                it.exception?.let { exception -> Log.e("Firestore, Add Task Exception:", exception.localizedMessage.toString()) }
        }
            .addOnFailureListener {
                Log.e("Firestore \"Create Task\", addOnFailureListener:", it.localizedMessage)
            }
    }

    suspend fun updateTaskInDB(taskFromAdapter: TasksDataClass, key:String) = withContext(Dispatchers.IO) {
        when(key) {
            "done" -> tasksCollectionRef.document(currentUserID).collection("tasks")
                .document(taskFromAdapter.taskId).update(key, taskFromAdapter.done)
                .addOnCompleteListener {
                    if (it.isSuccessful)
                        Log.e("Firestore \"Update Task\", it.isSuccessful", "Successfully updated the task!")
                    else
                        it.exception?.let { exception -> Log.e("Firestore, updated Task Exception:", exception.localizedMessage.toString()) }
                }
                .addOnFailureListener {
                    Log.e("Firestore \"Update Task\", addOnFailureListener:", it.localizedMessage)
                }
            else -> {}
        }
    }
}