package com.tuwaiq.enjazzoneapp.ui.user_profile_and_settings

import android.app.TimePickerDialog
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import com.tuwaiq.enjazzoneapp.*
import com.tuwaiq.enjazzoneapp.R
import java.text.SimpleDateFormat
import java.util.*


class UserProfileAndSettingsFragment : Fragment() {

    private lateinit var userProfileAndSettingsViewModel: UserProfileAndSettingsViewModel

    private lateinit var tvUsernameTV: TextView
    private lateinit var tvEmailTV: TextView

    private lateinit var btnGetInBed:Button

    private lateinit var btnWakeup:Button

    private lateinit var btnLogout:Button

    //private val tasksCollectionRef = Firebase.firestore.collection("users")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_user_profile_and_settings, container, false)
        userProfileAndSettingsViewModel =
            ViewModelProvider(this)[UserProfileAndSettingsViewModel::class.java]
        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val userID = FirebaseAuth.getInstance().currentUser!!.uid
        val userDisplayName = FirebaseAuth.getInstance().currentUser
        val userEmail = FirebaseAuth.getInstance().currentUser!!.email

        val myFirestore = FirebaseFirestore.getInstance()

        tvUsernameTV = view.findViewById(R.id.tvUsernameTV)
        tvEmailTV = view.findViewById(R.id.tvEmailTV)

        btnGetInBed = view.findViewById(R.id.btnGetInBedBTN)
        btnWakeup = view.findViewById(R.id.btnWakeUpBTN)

        btnLogout = view.findViewById(R.id.btnLogoutBTN)


        myFirestore.collection("users").document(userID).get()
            .addOnSuccessListener { documentSnapshot ->
                tvUsernameTV.text = documentSnapshot?.getString("username")
                tvEmailTV.text = documentSnapshot?.getString("email")
            }
        //tvUsernameTV.text = FirebaseAuth.getInstance().currentUser!!.email


        val sdf12HoursSDF = SimpleDateFormat("h:mm a", Locale.getDefault())
        //val sdf24HoursSDF = SimpleDateFormat("H:mm", Locale.getDefault())

        btnGetInBed.text = sharedPreferences.getString(getInBedSharedPrefStringKey, "22:00")

        btnGetInBed.setOnClickListener {
            TimePickerDialog(requireActivity(), android.R.style.Theme_Material_Dialog_Alert, TimePickerDialog.OnTimeSetListener
            { _, hour, minute ->
                val pickedDateTime = Calendar.getInstance()
                pickedDateTime.set(Calendar.HOUR_OF_DAY, hour)
                pickedDateTime.set(Calendar.MINUTE, minute)


                val sharedEditor: SharedPreferences.Editor = sharedPreferences.edit()
                btnGetInBed.text = sdf12HoursSDF.format(pickedDateTime.time)
                sharedEditor.putString(getInBedSharedPrefStringKey, sdf12HoursSDF.format(pickedDateTime.time))

                val toDateToLong = pickedDateTime.timeInMillis
                sharedEditor.putLong(getInBedSharedPrefLongKey, toDateToLong)
                sharedEditor.apply()

                Toast.makeText(view.context,"Selected Starting Hour is: ${btnGetInBed.text}",
                    Toast.LENGTH_LONG).show()
                Toast.makeText(view.context,"Selected Starting Hour in Millis: $toDateToLong",
                    Toast.LENGTH_LONG).show()
                println(toDateToLong)


                Toast.makeText(view.context, "Edit saved ✔", Toast.LENGTH_SHORT).show()
            }, Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), false).show()
            //btnGetInBed.text
        }

        btnWakeup.text = sharedPreferences.getString(wakeupSharedPrefStringKey, "06:00")
        btnWakeup.setOnClickListener {
            TimePickerDialog(requireActivity(), android.R.style.Theme_Material_Dialog_Alert, TimePickerDialog.OnTimeSetListener
            { _, hour, minute ->
                val pickedDateTime = Calendar.getInstance()
                pickedDateTime.set(Calendar.HOUR_OF_DAY, hour)
                pickedDateTime.set(Calendar.MINUTE, minute)
                println("hour $hour")
                println("minute $minute")
                val sharedEditor: SharedPreferences.Editor = sharedPreferences.edit()
                btnWakeup.text = sdf12HoursSDF.format(pickedDateTime.time)
                sharedEditor.putString(wakeupSharedPrefStringKey, sdf12HoursSDF.format(pickedDateTime.time))

                val toDateToLong = pickedDateTime.timeInMillis
                sharedEditor.putLong(wakeupSharedPrefLongKey, toDateToLong)
                sharedEditor.apply()

                Toast.makeText(view.context,"Selected Starting Hour is: ${btnWakeup.text}",
                    Toast.LENGTH_LONG).show()
                Toast.makeText(view.context,"Selected Starting Hour in Millis: $toDateToLong",
                    Toast.LENGTH_LONG).show()
                println(toDateToLong)

                Toast.makeText(view.context, "Edit saved ✔", Toast.LENGTH_SHORT).show()
            }, Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), false).show()

            Toast.makeText(view.context,"pickedTimeInMillis: ${sharedPreferences.getLong(wakeupSharedPrefLongKey,10800000)}",Toast.LENGTH_LONG).show()

        }

        tvUsernameTV.setOnClickListener {
            println(sharedPreferences.getLong(getInBedSharedPrefLongKey,10800000))
            println(sharedPreferences.getLong(wakeupSharedPrefLongKey,10800000))
        }

        btnLogout.setOnClickListener {
            val alertDialogBuilder = AlertDialog.Builder(view.context)
            alertDialogBuilder.setMessage("ِAre you sure you want to logout?")
                .setTitle("Logout Conformation")
                // if the dialog is cancelable
                .setCancelable(true)
                .setPositiveButton("Yes") { confirmDialog, _ ->
                    FirebaseAuth.getInstance().signOut()
                    confirmDialog.dismiss()
                    findNavController().navigate(R.id.navigation_login)
                    val sharedEditor = sharedPreferences.edit()
                    sharedEditor.putBoolean(keepMeSignedInKeyInSharedPref, false)
                    sharedEditor.apply()
                }
                .setNegativeButton("No") { confirmDialog, _ ->
                    confirmDialog.dismiss()
                }
                .show()
        }

    }
}