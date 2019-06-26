package id.erwinka.madesubmission4.util

import android.view.View
import java.util.*

var LOG_TAG = "MADE3"

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.invisible() {
    visibility = View.GONE
}

fun getLocale() : String {
    val locale = Locale.getDefault().language
    var language = "language"
    if (locale == "en") {
        language = "en-US"
    } else if (locale == "in") {
        language = "id-IN"
    }
    return language
}