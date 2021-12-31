package com.tuwaiq.enjazzoneapp.ui.todo

import androidx.navigation.ActionOnlyNavDirections
import androidx.navigation.NavDirections
import com.tuwaiq.enjazzoneapp.R

public class ToDoFragmentDirections private constructor() {
  public companion object {
    public fun actionNavigationToDoToNavigationLogin(): NavDirections =
        ActionOnlyNavDirections(R.id.action_navigation_ToDo_to_navigation_login)
  }
}
