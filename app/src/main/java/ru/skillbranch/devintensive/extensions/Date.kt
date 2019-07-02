package ru.skillbranch.devintensive.extensions

import java.lang.IllegalStateException
import java.text.SimpleDateFormat
import java.util.*

const val SECOND = 1000L
const val MINUTE = 60 * SECOND
const val HOUR = 60 * MINUTE
const val DAY = 24 * HOUR

enum class TimeUnits{
    SECOND,
    MINUTE,
    HOUR,
    DAY
}

fun Date.format(pattern: String = "HH:mm:ss dd.MM.yy"):String {
    val dateFormat = SimpleDateFormat(pattern, Locale("ru"))
    return dateFormat.format(this)
}

fun Date.add(value: Int, units: TimeUnits): Date{
    var time = this.time;

    time += when(units){
        TimeUnits.SECOND -> value * SECOND
        TimeUnits.MINUTE -> value * MINUTE
        TimeUnits.HOUR -> value * HOUR
        TimeUnits.DAY -> value * DAY
    }
    this.time = time
    return this
}

fun Date.humanizeDiff(date:Date = Date()):String{
    var diff = (Date().time - this.time)
    var metrics = TimeUnits.SECOND

    val isPast = diff > 0
    if(!isPast) diff *= -1

    if(diff <= 1) return "только что"
    if(diff < 45)
    {
        return if(isPast) "несколько секунд назад" else "через несколько секунд"
    }
    return when(diff){
        in 45..75 -> if(isPast) "минуту назад" else "через минуту"
        in 75..45*MINUTE -> {
            val n = diff / MINUTE
            val m = getCorrectMetric(n, TimeUnits.MINUTE)
            if (isPast) "$n $m назад" else "через $n $m"
        }
        in 45*MINUTE..75*MINUTE -> if(isPast) "час назад" else "через час"
        in 75*MINUTE..22*HOUR -> {
            val n = diff/HOUR
            val m = getCorrectMetric(n, TimeUnits.HOUR)
            if(isPast) "$n $m назад" else "через $n $m"
        }
        in 22*HOUR..26*HOUR -> if(isPast) "день назад" else "через день"
        in 26*HOUR..360*DAY -> {
            val n = diff/DAY
            val m = getCorrectMetric(n, TimeUnits.DAY)
            if(isPast) "$n $m назад" else "через $n $m"
        }
        else -> if(isPast) "более года назад" else "более чем через год"
    }
}

fun getCorrectMetric(N:Long = 1, unit:TimeUnits = TimeUnits.MINUTE):String{
    return when(N)
    {
        0L -> when(unit) {
            TimeUnits.SECOND -> "секунд"
            TimeUnits.MINUTE -> "минут"
            TimeUnits.HOUR -> "часов"
            TimeUnits.DAY -> "дней"
        }
        1L -> when(unit){
            TimeUnits.SECOND -> "секунду"
            TimeUnits.MINUTE -> "минуту"
            TimeUnits.HOUR -> "час"
            TimeUnits.DAY -> "день"
        }
        in 2..4 -> when(unit){
            TimeUnits.SECOND -> "секунды"
            TimeUnits.MINUTE -> "минуты"
            TimeUnits.HOUR -> "часа"
            TimeUnits.DAY -> "дня"
        }
        in 5..20 -> when(unit){
            TimeUnits.SECOND -> "секунд"
            TimeUnits.MINUTE -> "минут"
            TimeUnits.HOUR -> "часов"
            TimeUnits.DAY -> "дней"
        }
        else -> getCorrectMetric(N%10, unit)
    }
}