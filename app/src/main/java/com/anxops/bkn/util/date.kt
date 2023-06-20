package com.anxops.bkn.util

import android.annotation.SuppressLint
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SimpleDateFormat")
val yearMonthFormat = SimpleDateFormat("MMMM 'de' yyyy")

@SuppressLint("SimpleDateFormat")
val iso8061DateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")

@SuppressLint("SimpleDateFormat")
val localDateFormat: DateFormat = SimpleDateFormat("d 'de' MMMM 'de' yyyy")

@SuppressLint("SimpleDateFormat")
val localTimeFormat: DateFormat = SimpleDateFormat("'a las' HH:mm")


fun String?.toDate(): Date? {
    return if (this != null) iso8061DateFormat.parse(this) else null
}

fun Date.formatAsIso8061(): String? {
    return iso8061DateFormat.format(this)
}

fun Date.formatAsYearMonth(): String? {
    return yearMonthFormat.format(this)
}


fun Date.formatAsLocalDate(): String? {
    return localDateFormat.format(this) + ", " + localTimeFormat.format(this)
}
