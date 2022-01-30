package com.tuwaiq.enjazzoneapp.ui.task_details

import android.os.Bundle
import android.os.Parcelable
import androidx.navigation.NavArgs
import com.tuwaiq.enjazzoneapp.`data`.TasksDataClass
import java.io.Serializable
import java.lang.IllegalArgumentException
import java.lang.UnsupportedOperationException
import kotlin.Suppress
import kotlin.jvm.JvmStatic

public data class TaskDetailsDialogFragmentArgs(
  public val PassedTaskObject: TasksDataClass
) : NavArgs {
  @Suppress("CAST_NEVER_SUCCEEDS")
  public fun toBundle(): Bundle {
    val result = Bundle()
    if (Parcelable::class.java.isAssignableFrom(TasksDataClass::class.java)) {
      result.putParcelable("PassedTaskObject", this.PassedTaskObject as Parcelable)
    } else if (Serializable::class.java.isAssignableFrom(TasksDataClass::class.java)) {
      result.putSerializable("PassedTaskObject", this.PassedTaskObject as Serializable)
    } else {
      throw UnsupportedOperationException(TasksDataClass::class.java.name +
          " must implement Parcelable or Serializable or must be an Enum.")
    }
    return result
  }

  public companion object {
    @JvmStatic
    public fun fromBundle(bundle: Bundle): TaskDetailsDialogFragmentArgs {
      bundle.setClassLoader(TaskDetailsDialogFragmentArgs::class.java.classLoader)
      val __PassedTaskObject : TasksDataClass?
      if (bundle.containsKey("PassedTaskObject")) {
        if (Parcelable::class.java.isAssignableFrom(TasksDataClass::class.java) ||
            Serializable::class.java.isAssignableFrom(TasksDataClass::class.java)) {
          __PassedTaskObject = bundle.get("PassedTaskObject") as TasksDataClass?
        } else {
          throw UnsupportedOperationException(TasksDataClass::class.java.name +
              " must implement Parcelable or Serializable or must be an Enum.")
        }
        if (__PassedTaskObject == null) {
          throw IllegalArgumentException("Argument \"PassedTaskObject\" is marked as non-null but was passed a null value.")
        }
      } else {
        throw IllegalArgumentException("Required argument \"PassedTaskObject\" is missing and does not have an android:defaultValue")
      }
      return TaskDetailsDialogFragmentArgs(__PassedTaskObject)
    }
  }
}
