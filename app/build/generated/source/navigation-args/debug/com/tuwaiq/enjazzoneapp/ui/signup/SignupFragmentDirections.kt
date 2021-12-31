package com.tuwaiq.enjazzoneapp.ui.signup

import androidx.navigation.ActionOnlyNavDirections
import androidx.navigation.NavDirections
import com.tuwaiq.enjazzoneapp.R

public class SignupFragmentDirections private constructor() {
  public companion object {
    public fun actionSignupFragmentToLoginFragment(): NavDirections =
        ActionOnlyNavDirections(R.id.action_signupFragment_to_loginFragment)
  }
}
