package ru.skillbranch.devintensive.extensions

import java.lang.IllegalStateException
import java.text.SimpleDateFormat
import java.util.*

const val SECOND = 1000L
const val MINUTE = 60 * SECOND
const val HOUR = 60 * MINUTE
const val DAY = 24 * HOUR

enum class TimeUnits(val one:String, val twofour:String, val many:String){
    SECOND("секунду", "секунды", "секунд"),
    MINUTE("минуту","минуты","минут"),
    HOUR("час","часа","часов"),
    DAY("день","дня","дней");
    fun plural(value: Int):String{
        val n = value % 100
        return when(if(n > 20) n % 10 else n)
        {
            1 -> "$value $one"
            in 2..4 -> "$value $twofour"
            0, in 5..20 -> "$value $many"
            else -> "так быть не должно"
        }
    }
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

    val isPast = diff >= 0
    if(!isPast) diff *= -1

//    if(diff <  2 * SECOND) return "только что"
//    if(diff <  45 * SECOND)
//    {
//        return if(isPast) "несколько секунд назад" else "через несколько секунд"
//    }
    return when{
        Math.round(diff.toDouble() / SECOND) <= 1L -> "только что"
        Math.round(diff.toDouble() / SECOND) <= 45 -> if(isPast) "несколько секунд назад" else "через несколько секунд"
        Math.round(diff.toDouble() / SECOND) <= 75 -> if(isPast) "минуту назад" else "через минуту"
        Math.round(diff.toDouble() / MINUTE) <= 45 -> {
            val n = Math.round(diff.toDouble() / MINUTE)
            val m = getCorrectMetric(n, TimeUnits.MINUTE)
            if (isPast) "$n $m назад" else "через $n $m"
        }
        Math.round(diff.toDouble() / MINUTE) <= 75 -> if(isPast) "час назад" else "через час"
        Math.round(diff.toDouble() / HOUR) <= 22 -> {
            val n = Math.round(diff.toDouble() / HOUR)
            val m = getCorrectMetric(n, TimeUnits.HOUR)
            if(isPast) "$n $m назад" else "через $n $m"
        }
        Math.round(diff.toDouble() / HOUR) <= 26 -> if(isPast) "день назад" else "через день"
        Math.round(diff.toDouble() / DAY) <= 360 -> {
            val n = Math.round(diff.toDouble() / DAY)
            val m = getCorrectMetric(n, TimeUnits.DAY)
            if(isPast) "$n $m назад" else "через $n $m"
        }
        else -> if(isPast) "более года назад" else "более чем через год"
    }
}

fun getCorrectMetric(N:Long = 1, unit:TimeUnits = TimeUnits.MINUTE):String{
    return when(N)
    {
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
        0L, in 5..20 -> when(unit){
            TimeUnits.SECOND -> "секунд"
            TimeUnits.MINUTE -> "минут"
            TimeUnits.HOUR -> "часов"
            TimeUnits.DAY -> "дней"
        }
        else -> getCorrectMetric(N%10, unit)
    }
}