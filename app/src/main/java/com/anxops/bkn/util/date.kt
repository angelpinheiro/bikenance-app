package com.anxops.bkn.util

import android.annotation.SuppressLint
import android.content.Context
import android.icu.text.RelativeDateTimeFormatter
import android.text.format.DateUtils
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.*

@SuppressLint("SimpleDateFormat")
val yearMonthFormat = SimpleDateFormat("MMMM 'de' yyyy")

@SuppressLint("SimpleDateFormat")
val iso8061DateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")

@SuppressLint("SimpleDateFormat")
val localDateFormat: DateFormat = SimpleDateFormat("d 'de' MMMM 'de' yyyy")

@SuppressLint("SimpleDateFormat")
val localTimeFormat: DateFormat = SimpleDateFormat("'a las' HH:mm")

@SuppressLint("SimpleDateFormat")
val simpleLocalTimeFormat: DateFormat = SimpleDateFormat("HH:mm")

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

fun Date.formatAsRelativeDate(context: Context) : String {
    return DateUtils.getRelativeDateTimeString(
        context,
        this.time,
        DateUtils.MINUTE_IN_MILLIS,
        DateUtils.WEEK_IN_MILLIS,
        DateUtils.FORMAT_ABBREV_ALL or DateUtils.FORMAT_SHOW_WEEKDAY).toString()
}
