package ru.skillbranch.devintensive.utils

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


    private fun cyrilicToLatin(c:Char):String{
        return when (c) {
            'а' -> "a"
            'б' -> "b"
            'в' -> "v"
            'г' -> "g"
            'д' -> "d"
            'е' -> "e"
            'ё' -> "e"
            'ж' -> "zh"
            'з' -> "z"
            'и' -> "i"
            'й' -> "i"
            'к' -> "k"
            'л' -> "l"
            'м' -> "m"
            'н' -> "n"
            'о' -> "o"
            'п' -> "p"
            'р' -> "r"
            'с' -> "s"
            'т' -> "t"
            'у' -> "u"
            'ф' -> "f"
            'х' -> "h"
            'ц' -> "c"
            'ч' -> "ch"
            'ш' -> "sh"
            'щ' -> "sh'"
            'ъ' -> ""
            'ы' -> "i"
            'ь' -> ""
            'э' -> "e"
            'ю' -> "yu"
            'я' -> "ya"
            'А' -> "A"
            'Б' -> "B"
            'В' -> "V"
            'Г' -> "G"
            'Д' -> "D"
            'Е' -> "E"
            'Ё' -> "E"
            'Ж' -> "Zh"
            'З' -> "Z"
            'И' -> "I"
            'Й' -> "I"
            'К' -> "K"
            'Л' -> "L"
            'М' -> "M"
            'Н' -> "N"
            'О' -> "O"
            'П' -> "P"
            'Р' -> "R"
            'С' -> "S"
            'Т' -> "T"
            'У' -> "U"
            'Ф' -> "F"
            'Х' -> "H"
            'Ц' -> "C"
            'Ч' -> "Ch"
            'Ш' -> "Sh"
            'Щ' -> "Sh"
            'Ъ' -> ""
            'Ы' -> "I"
            'Ь' -> ""
            'Э' -> "E"
            'Ю' -> "Yu"
            'Я' -> "Ya"
            else -> c.toString()
        }
    }

    fun transliteration(payload: String?, divider :String = " "):String?{
        val sb = StringBuilder()
        payload?.forEach()
        {
            sb.append(if(it == ' ') divider else cyrilicToLatin(it))
        }
        return sb.toString()
    }
}