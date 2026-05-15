package com.reshme.nammapride.util

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

object DateTools {
    private val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy")
    fun todayEpochDay(): Long = LocalDate.now().toEpochDay()
    fun millisNow(): Long = System.currentTimeMillis()
    fun epochToText(day: Long): String = LocalDate.ofEpochDay(day).format(formatter)
    fun millisToText(millis: Long): String = Instant.ofEpochMilli(millis).atZone(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("dd MMM, hh:mm a"))
    fun daysBetween(start: Long, end: Long): Long = ChronoUnit.DAYS.between(LocalDate.ofEpochDay(start), LocalDate.ofEpochDay(end))
}
