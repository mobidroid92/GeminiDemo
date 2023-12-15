package com.bdour.gemini.common

import java.text.SimpleDateFormat
import java.util.*

fun Long.toTime() : String {
    return SimpleDateFormat("hh:mm").format(Date(this))
}