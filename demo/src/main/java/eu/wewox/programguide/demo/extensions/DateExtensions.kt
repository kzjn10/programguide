package eu.wewox.programguide.demo.extensions

import android.util.Log
import java.util.Calendar

fun Calendar.currentHourInFloat(): Float {
    val hour24hrs: Int = this.get(Calendar.HOUR_OF_DAY)
    val minutes: Int = this.get(Calendar.MINUTE)
    Log.d("Calendar", "hourInFloat: $hour24hrs $minutes")
    val minuteInFloat = ((minutes * 1f / 60) * 100) / 100
    Log.d("Calendar", "minuteInFloat: $minuteInFloat ${hour24hrs + minuteInFloat}")
    return hour24hrs + minuteInFloat
}

fun Calendar.currentHourAndMinute(): String {
    val hour24hrs: Int = this.get(Calendar.HOUR_OF_DAY)
    val minutes: Int = this.get(Calendar.MINUTE)
    return "$hour24hrs:$minutes"
}