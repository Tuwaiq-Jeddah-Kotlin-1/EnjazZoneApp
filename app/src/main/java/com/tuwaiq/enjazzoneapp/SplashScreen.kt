package com.tuwaiq.enjazzoneapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity

lateinit var sharedPreferences: SharedPreferences
// Keys:
const val sharedPrefFile = "SHARED_PREF"
const val emailKeyInSharedPref = "EMAIL"
const val passwordKeyInSharedPref = "PASSWORD"
const val rememberMeKeyInSharedPref = "REMEMBER-ME-CHECKBOX"
const val keepMeSignedInKeyInSharedPref = "KEEP-SIGNED-IN-CHECKBOX"
const val usernameKeyInSharedPref = "USERNAME"
const val getInBedSharedPrefStringKey = "GET_IN_BED_STRING"
const val getInBedSharedPrefLongKey = "GET_IN_BED_LONG"
const val wakeupSharedPrefStringKey = "WAKE_UP_STRING"
const val wakeupSharedPrefLongKey = "WAKE_UP_LONG"
const val milliSecondsInDay:Long = 86400000

@Suppress("DEPRECATION")
class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        sharedPreferences = getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)
        val delayMillis:Long = if (sharedPreferences.getBoolean(keepMeSignedInKeyInSharedPref, false)
            || sharedPreferences.getBoolean(rememberMeKeyInSharedPref,false)) 200
        else 2500
        // This is used to hide the status bar and make
        // the splash screen as a full screen activity.
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        // we used the postDelayed(Runnable, time) method
        // to send a message with a delayed time.
        Handler().postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, delayMillis) // 3000 is the delayed time in milliseconds.
    }
}