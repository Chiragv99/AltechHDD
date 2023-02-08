package com.pickfords.surveyorapp.extentions

import android.app.Activity
import android.content.Context
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import org.json.JSONObject


fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.hide() {
    this.visibility = View.INVISIBLE
}

fun View.invisible() {
    this.visibility = View.GONE
}

fun EditText.getValue(): String {
    return this.text.toString().trim()
}

fun TextView.getValue(): String {
    return this.text.toString().trim()
}

fun EditText.isEmpty(): Boolean {
    return this.text.trim().isEmpty()
}

fun TextView.isEmpty(): Boolean {
    return this.text.trim().isEmpty()
}

fun Context.showToast(msg: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, msg, duration).show()
}

fun Activity.showToast(msg: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, msg, duration).show()
}

fun setBackGroundColor(v: View, colorId: Int) {
    v.setBackgroundColor(ContextCompat.getColor(v.context, colorId))
}

fun setBackGroundResource(v: View, @DrawableRes resId: Int) {
    v.setBackgroundResource(resId)
}

fun setBackgroundTint(v: View, colorId: Int) {
    v.backgroundTintList = ContextCompat.getColorStateList(v.context, colorId)
}

fun setTextColor(v: AppCompatTextView, colorId: Int) {
    v.setTextColor(ContextCompat.getColor(v.context, colorId))
}


fun hideView(vararg views: View) {
    for (view in views) {
        view.visibility = View.GONE
    }
}

fun invisibleView(vararg views: View) {
    for (view in views) {
        view.visibility = View.INVISIBLE
    }
}

fun showView(vararg views: View) {
    for (view in views) {
        view.visibility = View.VISIBLE
    }
}

fun hideShowView(condition: Boolean, vararg views: View) {
    for (view in views) {
        if (condition) showView(view) else hideView(view)
    }
}

/*check api success status*/
fun checkSuccess(jsonObject: JSONObject): Boolean {
    return jsonObject.optString("Success").equals("true", ignoreCase = true)
}