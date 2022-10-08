package com.example.kmmfirstdemo.android.util

import java.text.SimpleDateFormat
import java.util.*


object DateTimeUtil {

    private const val DATE_TIME_FORMAT = "HH:mm dd/MM/yyyy"

    fun getCurrentDateTime(): String {
        val dateFormat = SimpleDateFormat(DATE_TIME_FORMAT, Locale.getDefault())
        val date = Date()
        return dateFormat.format(date)
    }
}
