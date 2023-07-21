package com.anxops.bkn.util

import android.text.format.DateUtils
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAccessor

object DateTimeFormatters {
    val iso8061: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
    val dayMonth: DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM")
    val monthYear: DateTimeFormatter = DateTimeFormatter.ofPattern("MM/YYYY")
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

fun TemporalAccessor.formatAsMonthYear(): String {
    return DateTimeFormatters.monthYear.format(this)
}

fun Instant.formatAsRelativeTime(from: Long = System.currentTimeMillis()): String {
    val millis = this.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
    return DateUtils.getRelativeTimeSpanString(
        millis, from, 0, DateUtils.FORMAT_SHOW_YEAR or DateUtils.FORMAT_NO_MONTH_DAY
    ).toString();
}

fun LocalDateTime.formatAsRelativeTime(from: Long = System.currentTimeMillis()): String {
    return this.toInstant(ZoneOffset.of(ZoneId.systemDefault().id)).formatAsRelativeTime(from)
}

fun TemporalAccessor.formatAsTime(): String {
    return DateTimeFormatters.localTime.format(this)
}

