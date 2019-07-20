package ru.skillbranch.devintensive.extensions

import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.inputmethod.InputMethodManager
import kotlin.math.roundToInt


fun Activity.hideKeyboard(){
    Log.d("M_Activity", "hideKeyboard")
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
}

fun Activity.isKeyboardOpen():Boolean{
    val visibleBounds = Rect()
    val rootView = findViewById<View>(android.R.id.content)
    rootView.getWindowVisibleDisplayFrame(visibleBounds)
    val heightDiff = rootView.height - visibleBounds.height()
    val marginOfError =
        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50F, this.resources.displayMetrics).roundToInt()
    return heightDiff > marginOfError
}

fun Activity.isKeyboardClosed():Boolean{
    return !isKeyboardOpen()
}