package com.livestreaming.tv.utils

import android.content.Context
import android.graphics.Color
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.widget.NestedScrollView
import com.google.android.material.textview.MaterialTextView
import com.livestreaming.tv.R
import kotlin.math.abs

fun View.hideKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE)
            as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}

fun View.showKeyboard() {
    post {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE)
                as InputMethodManager
        imm.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
    }
}

fun Context.dp(value: Int): Int {
    return (value * resources.displayMetrics.density).toInt()
}

// --- UTILITY FUNCTION: FORMAT TIME IN hh:mm:ss or mm:ss ---
fun formatTime(milliseconds: Long): String {
    val safeMillis = abs(milliseconds)

    val totalSeconds = safeMillis / 1000
    val seconds = totalSeconds % 60
    val minutes = (totalSeconds / 60) % 60
    val hours = totalSeconds / 3600

    return if (hours > 0) {
        String.format("%02d:%02d:%02d", hours, minutes, seconds)
    } else {
        String.format("%02d:%02d", minutes, seconds)
    }
}

//Media Player
object MediaPlayerConstants {

    // Playback Speed
    const val SPEED_0_5 = "0.5"
    const val SPEED_1_0 = "1.0"
    const val SPEED_1_25 = "1.25"
    const val SPEED_1_5 = "1.5"

    // Video Quality
    const val QUALITY_AUTO = "auto"
    const val QUALITY_1080P = "1080p"
    const val QUALITY_720P = "720p"
    const val QUALITY_DATA = "data"
}

fun createCircularList(list: List<Any>): List<Any> {
    if (list.size < 2) return list

    val circularList = mutableListOf<Any>()
    circularList.add(list.last())     // fake first
    circularList.addAll(list)
    circularList.add(list.first())    // fake last
    return circularList
}

fun getFocusHighlightListener(): View.OnFocusChangeListener {
    return View.OnFocusChangeListener { view, hasFocus ->
        val textView = view as MaterialTextView
        if (hasFocus) {
            // Highlight focused item
            textView.setBackgroundResource(R.drawable.bg_focused)
        } else {
            // Remove background when not focused
            textView.setBackgroundColor(Color.TRANSPARENT)
        }
    }
}

fun scrollToTop(
    scrollView: NestedScrollView,
) {
    scrollView.post {
        scrollView.smoothScrollTo(0, 0)
    }
}