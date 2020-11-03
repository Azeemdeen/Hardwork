@file:Suppress("UNREACHABLE_CODE")

package com.azeem.hardwork

import android.content.Context
import android.content.Intent
import android.os.Bundle
import java.util.regex.Pattern

fun String.isEmailValid(): Boolean {
    val expression = "^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})"
    val pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE)
    val matcher = pattern.matcher(this)
    return matcher.matches()
}


fun String.isPassWordValid(): Boolean {
    val expression = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@\$%^&*-]).{6,}\$"

    //val expression = "/^(?=.*\\d)(?=.*[A-Z])([@\$%&#])[0-9a-zA-Z]{4,}$/"
    val pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE)
    val matcher = pattern.matcher(this)
    return matcher.matches()
}

    fun String.isPassWordlengthValid(): Boolean {
        val expression = "^(?=.{5,20}\$) "

        //val expression = "/^(?=.*\\d)(?=.*[A-Z])([@\$%&#])[0-9a-zA-Z]{4,}$/"
        val pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE)
        val matcher = pattern.matcher(this)
        return matcher.matches()
}
fun String.isNamevalid(): Boolean {
    val expression = "^[A-Za-z](.*)"
    val pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE)
    val matcher = pattern.matcher(this)
    return matcher.matches()
}


//intent calling extension
fun <T> Context.openActivity(it: Class<T>, extras: Bundle.() -> Unit = {}) {
    val intent = Intent(this, it)
    intent.putExtras(Bundle().apply(extras))
    startActivity(intent)
}


