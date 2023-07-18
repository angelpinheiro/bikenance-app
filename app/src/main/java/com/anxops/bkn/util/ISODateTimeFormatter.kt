package com.anxops.bkn.util

import android.text.format.DateUtils
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAccessor

object DateTimeFormatters {
    val iso8061: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
    val dayMonth: DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM")
    val localTime: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm")
}

fun String.toLocalDateTime(): LocalDateTime {
    return LocalDateTime.parse(this, DateTimeFormatters.iso8061)
}

fun TemporalAccessor.formatAsIso8061(): String {
    return DateTimeFormatters.iso8061.format(this)
}

fun TemporalAccessor.formatAsDayMonth(): String {
    return DateTimeFormatters.dayMonth.format(this)
}

fun LocalDateTime.formatAsRelativeTime(from: Long = System.currentTimeMillis()): String {
    val millis = this.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
    return DateUtils.getRelativeTimeSpanString(millis, from, 0,
        DateUtils.FORMAT_SHOW_YEAR or DateUtils.FORMAT_NO_MONTH_DAY).toString();
}

fun TemporalAccessor.formatAsTime(): String {
    return DateTimeFormatters.localTime.format(this)
}

