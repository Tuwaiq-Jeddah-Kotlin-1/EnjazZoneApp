package com.tuwaiq.enjazzoneapp

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.*
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    //private lateinit var appBarConfiguration:AppBarConfiguration
    private lateinit var navController: NavController
    private lateinit var bottomNavView: BottomNavigationView
    private lateinit var navView: NavigationView
    lateinit var toggle: ActionBarDrawerToggle
    private lateinit var drawerLayout:DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        drawerLayout = findViewById(R.id.activity_main_container)
        navView = findViewById(R.id.nav_view)
        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

/*        navView.setNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.btnLogout -> {
                    Log.e("Inside when of Nav View", "${it.itemId} BEEN PRESSED")
                    Toast.makeText(this, "Logout has been pressed", Toast.LENGTH_LONG).show()
                    FirebaseAuth.getInstance().signOut()
                    navController.navigate(R.id.navigation_login)
                    println("HELLO !!! \nLogout been clicked.")
                    val sharedEditor = sharedPreferences.edit()
                    sharedEditor.putBoolean(keepMeSignedInKeyInSharedPref, false)
                    sharedEditor.apply()
                }else -> Log.e("else", "ELSE NAV VIEW")
            }
            true
        }*/
        bottomNavView = findViewById(R.id.bottom_nav_view)
        navController = findNavController(R.id.nav_host_fragment_activity_main)
/*        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_ToDo, R.id.navigation_calender_view, R.id.navigation_enjaz_zone
            )
        )*/
        //setupActionBarWithNavController(navController, appBarConfiguration)
        //setupActionBarWithNavController(navController, drawerLayout)
        bottomNavView.setupWithNavController(navController)
        //navView.setupWithNavController(navController)

/*        when(navController.currentDestination?.id) {
            R.id.navigation_ToDo -> bottomNavView.visibility = View.GONE
        }*/

        //navController.removeOnDestinationChangedListener { controller, destination, arguments ->  }
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when(destination.id) {
                R.id.navigation_login-> {
                    bottomNavView.visibility = View.GONE
                    supportActionBar?.setDisplayHomeAsUpEnabled(false)
                    drawerLayout.close()
                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
                }
                R.id.navigation_signup-> {
                    bottomNavView.visibility = View.GONE
                    supportActionBar?.setDisplayHomeAsUpEnabled(false)
                    drawerLayout.close()
                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
                }
                else -> {
                    bottomNavView.visibility = View.VISIBLE
                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
                    supportActionBar?.setDisplayHomeAsUpEnabled(true)
                }
            }
            //navController.saveState()
        }
        //navView.lis

        //onBackPressed()
        //navController.graph.startDestination

    } // fun onCreate()

    override fun onBackPressed() {
        when(navController.currentDestination?.id) {
            R.id.navigation_ToDo -> finish()
            R.id.navigation_tasks_view -> navController.navigate(R.id.navigation_ToDo)
            R.id.navigation_enjaz_zone -> navController.navigate(R.id.navigation_ToDo)
            R.id.navigation_login -> finish()
            R.id.navigation_signup -> finish()
/*            R.id.taskDetailsFragment -> {
                finish()
            }*/
            else -> super.onBackPressed()
        } // when() {}
    } //fun onBackPressed()

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) return true
        return super.onOptionsItemSelected(item)
    }
}