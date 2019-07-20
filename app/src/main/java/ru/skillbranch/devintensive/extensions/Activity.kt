package ru.skillbranch.devintensive.extensions

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.inputmethod.InputMethodManager





fun Activity.hideKeyboard(){
    Log.d("M_Activity", "hideKeyboard")
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
}