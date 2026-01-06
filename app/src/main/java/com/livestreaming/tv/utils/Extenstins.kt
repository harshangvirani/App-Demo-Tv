package com.livestreaming.tv.utils

import android.content.Context
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment

//For Log
private const val CALL_STACK_INDEX = 1

fun Any.log(message: Any?) {
    val stackTrace = Throwable().stackTrace
    val tag = if (stackTrace.size <= CALL_STACK_INDEX) {
        this.javaClass.simpleName
    } else {
        val lineNumber = stackTrace[CALL_STACK_INDEX].lineNumber
        "${this.javaClass.simpleName}: $lineNumber"
    }
    Log.d(tag, message.toString())
}

fun log(message: Any?, tag: String) {
    Log.d(tag, message.toString())
}

//For Toast
fun Context.toast(@StringRes message: Int, time: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, time).show()
}

fun Context.toast(message: String, time: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, time).show()
}

fun Fragment.toast(@StringRes message: Int, time: Int = Toast.LENGTH_SHORT) {
    context?.toast(message, time)
}

fun Fragment.toast(message: String, time: Int = Toast.LENGTH_SHORT) {
    context?.toast(message, time)
}

//For Visible
fun View.visible() = run { visibility = View.VISIBLE }

fun View.gone() = run { visibility = View.GONE }

fun View.invisible() = run { visibility = View.INVISIBLE }

infix fun View.visibleIf(condition: Boolean) =
    run { visibility = if (condition) View.VISIBLE else View.GONE }

infix fun View.goneIf(condition: Boolean) =
    run { visibility = if (condition) View.GONE else View.VISIBLE }