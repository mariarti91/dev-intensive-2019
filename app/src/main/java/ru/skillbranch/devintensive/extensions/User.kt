package ru.skillbranch.devintensive.extensions

import ru.skillbranch.devintensive.models.data.User
import ru.skillbranch.devintensive.models.UserView

fun User.toUserView(): UserView{

    val nickName = ""
    val initials = ""
    val status = if (lastVisit == null) "Ни разу не был" else if (isOnline) "online" else "Последний раз был ${humanizeDiff()}"

    return UserView(
        id,
        fullName = "$firstName $lastName",
        nickName = nickName,
        initials = initials,
        avatar = avatar,
        status = status
    )
}

fun humanizeDiff(): String {
    return ""
}
