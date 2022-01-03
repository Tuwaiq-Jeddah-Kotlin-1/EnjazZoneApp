package com.tuwaiq.enjazzoneapp.ui.login

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.tuwaiq.enjazzoneapp.*

class LoginFragment : Fragment() {

    //private var sharedPreferences: SharedPreferences = this.requireActivity().getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)
    //private val emailET = view?.findViewById<EditText>(R.id.etEmailLogin)
    private lateinit var emailET:EditText// = view?.findViewById<EditText>(R.id.etEmailLogin)
    private lateinit var passwordET:EditText// = view?.findViewById<EditText>(R.id.etEmailLogin)
    private lateinit var rememberChB:CheckBox
    //private val passwordET = view?.findViewById<EditText>(R.id.etPasswordLogin)

    private val isRemembered = sharedPreferences.getBoolean("CHECKBOX", false)
    private val emailInSharedPref = sharedPreferences.getString("EMAIL", null)
    private val passwordInSharedPref = sharedPreferences.getString("PASSWORD", null)
    private val sharedEditor: SharedPreferences.Editor = sharedPreferences.edit()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_login, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(sharedPreferences.getBoolean("KEEP-SIGNED-IN-CHECKBOX", false))
            findNavController().navigate(R.id.navigation_ToDo)

        emailET = view.findViewById(R.id.etEmailLogin)
        passwordET = view.findViewById(R.id.etPasswordLogin)
        rememberChB = view.findViewById(R.id.chbRememberMe)
        //if (isRemembered && emailInSharedPref!=null && passwordInSharedPref !=null) {
            //Toast.makeText(this.context, "REMEMBER ME IS APPLIED", Toast.LENGTH_LONG).show()
            emailET.setText(emailInSharedPref)
            passwordET.setText(passwordInSharedPref)
            rememberChB.isChecked = isRemembered
        //}
        val btnLogin = view.findViewById<Button>(R.id.btnLogin)

        // create instance of navigate button
        val navigateTVSignupLink: TextView = view.findViewById(R.id.tvSignupLink)

        btnLogin.setOnClickListener {
            loginUser()
        }

        navigateTVSignupLink.setOnClickListener {
            val actionNavigateToSignupFragment = LoginFragmentDirections.actionLoginFragmentToSignupFragment()
            findNavController().navigate(actionNavigateToSignupFragment)
        }
    }

    private fun loginUser() {

        val email = view?.findViewById<EditText>(R.id.etEmailLogin)?.text.toString()
        val password = view?.findViewById<EditText>(R.id.etPasswordLogin)?.text.toString()
        val loginFragmentContext = this@LoginFragment.context
        val keepSignIn = view?.findViewById<CheckBox>(R.id.chbKeepMeSignedIn)

        if (email.isNotEmpty() && password.isNotEmpty()) {
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->

                    if (task.isSuccessful) {
                        val firebaseUser: FirebaseUser = task.result!!.user!!
                        Toast.makeText(loginFragmentContext, "Logged in", Toast.LENGTH_LONG).show()
                        //val intent = Intent(this, SecondActivity::class.java) //this line is replaced by:
                        val actionNavigateToToDoFragment = LoginFragmentDirections.actionLoginFragmentToNavigationToDo()
                        //remember!!.setOnClickListener {
                        if (rememberChB.isChecked){
                            sharedEditor.putBoolean("CHECKBOX", rememberChB.isChecked)
                            sharedEditor.putString("EMAIL", email)
                            sharedEditor.putString("PASSWORD", password)
                            sharedEditor.apply()
                        }else{
                            sharedEditor.putBoolean("CHECKBOX", false)
                            sharedEditor.putString("EMAIL", null)
                            sharedEditor.putString("PASSWORD", null)
                            sharedEditor.apply()
                        }
                        if (keepSignIn!!.isChecked){
                            sharedEditor.putBoolean("KEEP-SIGNED-IN-CHECKBOX", keepSignIn.isChecked)
                            sharedEditor.putString("EMAIL", email)
                            sharedEditor.apply()
                        }
                        else {
                            sharedEditor.putBoolean("KEEP-SIGNED-IN-CHECKBOX", false)
                            //sharedEditor.putString("EMAIL", null)
                            sharedEditor.apply()
                        }
                        //}
                        findNavController().navigate(actionNavigateToToDoFragment)
                        /* intent.putExtra("user_id", firebaseUser.uid)
                        intent.putExtra("email_id", email)
                        startActivity(intent)
                        finish()*/
                    }//else Toast.makeText(this@LoginActivity, "Error: Task is not successful!", Toast.LENGTH_LONG).show() //commented; replaced by ".addOnFailureListener"
                }
                .addOnFailureListener {
                    Toast.makeText(loginFragmentContext, "Error: ${it.localizedMessage}", Toast.LENGTH_LONG).show()
                }
                .addOnCanceledListener {
                    Toast.makeText(loginFragmentContext, "Login Error: Login cancelled", Toast.LENGTH_LONG).show()
                }
                .addOnSuccessListener {
                    Toast.makeText(loginFragmentContext, "Login result: $it", Toast.LENGTH_LONG).show()
                }
        }else Toast.makeText(loginFragmentContext, "Error: Email or Password can't be empty", Toast.LENGTH_LONG).show()
    }

}