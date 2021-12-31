package com.tuwaiq.enjazzoneapp.ui.signup

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.tuwaiq.enjazzoneapp.R
import com.tuwaiq.enjazzoneapp.sharedPreferences

class SignupFragment : Fragment() {

    //private lateinit var sharedPreferences: SharedPreferences
    //private val sharedEditor: SharedPreferences.Editor = sharedPreferences.edit()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_signup, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnSignup = view.findViewById<Button>(R.id.btnSignup)
        val tvLoginLink = view.findViewById<TextView>(R.id.tvLoginLink)

        btnSignup.setOnClickListener {
            signupUser()
        }
        tvLoginLink.setOnClickListener {
            val actionNavigateToLoginFragment = SignupFragmentDirections.actionSignupFragmentToLoginFragment()
            findNavController().navigate(actionNavigateToLoginFragment)
        }
    }

    private fun signupUser() {
        val email = view?.findViewById<EditText>(R.id.etEmailSignup)?.text.toString()
        val password = view?.findViewById<EditText>(R.id.etPasswordSignup)?.text.toString()
        val signupFragmentContext = this@SignupFragment.context
        val remember = view?.findViewById<CheckBox>(R.id.chbSignupRememberMe)
        //sharedPreferences = this.requireActivity().getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)

        if (email.isNotEmpty() && password.isNotEmpty()) {
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val firebaseUser: FirebaseUser = task.result!!.user!!
                        Toast.makeText(view?.context, "Sign up is complete", Toast.LENGTH_LONG).show()
                        val actionNavigateToSignupFragment = SignupFragmentDirections.actionSignupFragmentToLoginFragment()
                        //remember!!.setOnClickListener {
                            if (remember!!.isChecked){
                                //Toast.makeText(signupFragmentContext, "REMEMBER ME IS SUCCESSFUL", Toast.LENGTH_LONG).show()
                                val sharedEditor = sharedPreferences.edit()
                                sharedEditor.putBoolean("CHECKBOX", remember.isChecked)
                                sharedEditor.putString("EMAIL", email)
                                sharedEditor.putString("PASSWORD", password)
                                sharedEditor.apply()
                            }
                        //}
                        findNavController().navigate(actionNavigateToSignupFragment)
                    }
                }

                .addOnFailureListener {
                    Toast.makeText(signupFragmentContext, "Error: ${it.localizedMessage}", Toast.LENGTH_LONG).show()
                }
                .addOnCanceledListener {
                    Toast.makeText(signupFragmentContext, "Login Error: Login cancelled", Toast.LENGTH_LONG).show()
                }
                .addOnSuccessListener {
                    Toast.makeText(signupFragmentContext, "Navigating to: Login Page", Toast.LENGTH_LONG).show()
                }
        }else Toast.makeText(signupFragmentContext, "Error: Email or Password can't be empty", Toast.LENGTH_LONG).show()

    }

}