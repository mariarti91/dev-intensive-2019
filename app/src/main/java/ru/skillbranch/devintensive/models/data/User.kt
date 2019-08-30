package ru.skillbranch.devintensive.models.data

import ru.skillbranch.devintensive.extensions.humanizeDiff
import ru.skillbranch.devintensive.utils.Utils
import java.util.*

data class User(
    val id:String,
    var firstName:String?,
    var lastName: String?,
    var avatar: String?,
    var rating:Int = 0,
    var respect: Int = 0,
    val lastVisit:Date? = Date(),
    val isOnline:Boolean = false
) {
    constructor(id: String, firstName: String?, lastName: String?) : this(
        id = id,
        firstName = firstName,
        lastName = lastName,
        avatar = null
    )

    constructor(id: String) : this(id, "John", "Doe")

    init{
        println("It's Alive!!!\n" +
                "${if(lastName == "Doe") "His name id $firstName $lastName" else "And his name is $firstName $lastName!!!"}\n"
        )
    }

    fun printMe() = println("""
            id: $id
            firstName: $firstName
            lastName: $lastName
            avatar: $avatar
            rating: $rating
            respect: $respect
            lastVisit: $lastVisit
            isOnline: $isOnline
        """.trimIndent())

    fun toUserItem(): UserItem {
        val lastActivity = when{
            lastVisit == null -> "Еще ни разу не заходил"
            isOnline -> "online"
            else -> "Последний раз был ${lastVisit.humanizeDiff()}"
        }

        return UserItem(
                id,
                "${firstName.orEmpty()} ${lastName.orEmpty()}".trim(),
                Utils.toInitials(firstName, lastName),
                avatar,
                lastActivity,
                false,
                isOnline
        )
    }

    companion object Factory{
        private var lastId : Int = -1
        fun makeUser(fullName: String?) : User {
            lastId++
            val (firstName, lastName) = Utils.parseFullName(fullName)

            return User(id = "$lastId", firstName = firstName, lastName = lastName)
        }
    }

    class Builder(){
        var id:String = "-1"
        var firstName:String? = null
        var lastName: String? = null
        var avatar: String? = null
        var rating:Int = 0
        var respect: Int = 0
        var lastVisit:Date = Date()
        var isOnline:Boolean = false

        fun id(s:String): Builder {
            id = s
            return this
        }

        fun firstName(s:String?): Builder {
            firstName = s
            return this
        }
        fun lastName(s:String?): Builder {
            lastName = s
            return this
        }
        fun avatar(s:String?): Builder {
            avatar = s
            return this
        }
        fun rating(n:Int): Builder {
            rating = n
            return this
        }
        fun respect(n:Int): Builder {
            respect = n
            return this
        }
        fun lastVisit(d:Date): Builder {
            lastVisit = d
            return this
        }
        fun isOnline(b:Boolean): Builder {
            isOnline = b
            return this
        }
        fun build(): User {
            lastId++
            return User(id, firstName, lastName, avatar, rating, respect, lastVisit, isOnline)
        }
    }
}