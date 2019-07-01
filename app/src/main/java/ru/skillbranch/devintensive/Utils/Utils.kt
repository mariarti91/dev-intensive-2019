package ru.skillbranch.devintensive.Utils

import java.io.File

object  Utils {
    fun parseFullName(fullName: String?):Pair<String?, String?>{
        val parts: List<String>? = fullName?.split(" ")

        var firstName = parts?.getOrNull(0)
        var lastName = parts?.getOrNull(1)
        if(firstName?.length == 0) firstName = null
        if(lastName?.length == 0) lastName = null
        return firstName to lastName
    }

    fun toInitials(firstName: String?, lastName: String?):String?{
        val f = firstName?.trim()?.getOrNull(0)
        val l = lastName?.trim()?.getOrNull(0)
        val result = listOfNotNull(f, l).joinToString("")
        return if(result == "") null else result.toUpperCase()
    }
}