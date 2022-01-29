package com.tuwaiq.enjazzoneapp.ui.signup

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.tuwaiq.enjazzoneapp.*
import com.tuwaiq.enjazzoneapp.data.UsersDataClass

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
        val userName = view?.findViewById<EditText>(R.id.etUsername)?.text.toString()
        val signupFragmentContext = this@SignupFragment.context
        val sharedEditor = sharedPreferences.edit()
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

                                sharedEditor.putBoolean(rememberMeKeyInSharedPref, remember.isChecked)
                                sharedEditor.putString(emailKeyInSharedPref, email)
                                sharedEditor.putString(passwordKeyInSharedPref, password)
                                sharedEditor.putString(usernameKeyInSharedPref, userName)
                                sharedEditor.apply()
                            }
                        //}
                        dataUsers(userName, email)
                    }
                }

                .addOnFailureListener {
                    Toast.makeText(signupFragmentContext, resources.getString(R.string.signup_failure)+it.localizedMessage, Toast.LENGTH_LONG).show()
                }
                .addOnCanceledListener {
                    Toast.makeText(signupFragmentContext, resources.getString(R.string.signup_cancelled), Toast.LENGTH_LONG).show()
                }
                .addOnSuccessListener {
                    Toast.makeText(signupFragmentContext, resources.getString(R.string.signup_success), Toast.LENGTH_LONG).show()
                }
        }else Toast.makeText(signupFragmentContext, resources.getString(R.string.username_and_password_cannot_be_empty), Toast.LENGTH_LONG).show()

    }

    private fun dataUsers(userName:String, email:String) {
        val userData = UsersDataClass()
        userData.email =email
        userData.userID= FirebaseAuth.getInstance().currentUser!!.uid
        userData.username=userName
        addUserDataToFirestore(userData)
    }

    private fun addUserDataToFirestore(userData: UsersDataClass) {
        val db =FirebaseFirestore.getInstance()
        db.collection("users").document(userData.userID).set(userData).addOnCompleteListener {
            if (it.isSuccessful) {
                Log.e("Firestore", "Successfully added the task!")
                findNavController().navigate(R.id.navigation_login)
            } else Log.e("Firestore", it.exception.toString())
        }
            .addOnFailureListener {
                println("Localized message: \"${it.localizedMessage}\" <------------")
                Log.e("Firestore", it.localizedMessage)
            }
    }



}