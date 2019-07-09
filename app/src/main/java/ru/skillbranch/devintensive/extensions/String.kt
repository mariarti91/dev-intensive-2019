package ru.skillbranch.devintensive.extensions

fun String.truncate(count: Int = 16):String {
    var res = this.trim()
    return if (res.length <= count) res else "${res.substring(0, count).trim()}..."
}

fun String.stripHtml():String {
    return this
}