package com.tuwaiq.enjazzoneapp.ui.login

import androidx.navigation.ActionOnlyNavDirections
import androidx.navigation.NavDirections
import com.tuwaiq.enjazzoneapp.R

public class LoginFragmentDirections private constructor() {
  public companion object {
    public fun actionLoginFragmentToNavigationToDo(): NavDirections =
        ActionOnlyNavDirections(R.id.action_loginFragment_to_navigation_ToDo)

    public fun actionLoginFragmentToSignupFragment(): NavDirections =
        ActionOnlyNavDirections(R.id.action_loginFragment_to_signupFragment)
  }
}
