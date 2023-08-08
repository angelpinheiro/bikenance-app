package com.anxops.bkn.util

import android.text.format.DateUtils
import java.time.Duration
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAccessor

object DateTimeFormatters {
    val iso8061: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
    val dayMonth: DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM")
    val monthYear: DateTimeFormatter = DateTimeFormatter.ofPattern("MM/yyyy")
    val date: DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
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

fun TemporalAccessor.formatAsDate(): String {
    return DateTimeFormatters.date.format(this)
}

fun Instant.formatAsRelativeTime(from: Long = System.currentTimeMillis()): String {
    return this.toEpochMilli().formatAsRelativeTime()
}

fun LocalDateTime.formatAsRelativeTime(
    from: Long = System.currentTimeMillis(),
    showDay: Boolean = false
): String {
    return (this.atZone(ZoneId.systemDefault()).toEpochSecond() * 1000).formatAsRelativeTime(
        from,
        showDay
    )
}

fun Long.formatAsRelativeTime(
    from: Long = System.currentTimeMillis(),
    showDay: Boolean = false
): String {
    val flags = if (showDay) {
        DateUtils.FORMAT_SHOW_YEAR
    } else {
        DateUtils.FORMAT_SHOW_YEAR or DateUtils.FORMAT_NO_MONTH_DAY
    }

    return DateUtils.getRelativeTimeSpanString(
        this,
        from,
        0,
        flags
    ).toString()
}

fun LocalDateTime.formatElapsedTimeUntilNow(now: LocalDateTime = LocalDateTime.now()): String {
    val duration = Duration.between(this, now)
    val days = duration.toDays()
    return "$days days"
}
