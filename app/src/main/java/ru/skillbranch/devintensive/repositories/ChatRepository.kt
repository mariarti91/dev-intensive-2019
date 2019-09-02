package ru.skillbranch.devintensive.repositories

import androidx.lifecycle.MutableLiveData
import ru.skillbranch.devintensive.data.managers.CacheManager
import ru.skillbranch.devintensive.models.data.Chat

object ChatRepository {
    private val chats = CacheManager.loadChats()
    private const val archiveId = "-1"

    fun loadChats(): MutableLiveData<List<Chat>> {
        return chats
    }

    fun update(chat: Chat) {
        val copy = chats.value!!.toMutableList()
        val ind = chats.value!!.indexOfFirst { it.id == chat.id }
        if(ind == -1) return
        copy[ind] = chat
        chats.value = copy
    }

    private fun getArchiveItem(): Chat {
        var archive = find(archiveId)

        if (archive == null) {
            archive = Chat(
                    id = archiveId,
                    title = "Архив чатов",
                    members = listOf(),
                    messages = mutableListOf(),
                    isArchived = false
            )
            val copy = chats.value!!.toMutableList()
            copy.add(0, archive)
            chats.value = copy
        }

        return archive
    }

    fun find(chatId: String): Chat? {
        val ind = chats.value!!.indexOfFirst { it.id == chatId }
        return chats.value!!.getOrNull(ind)
    }

    fun addToArchive(chatId: String) {
        val chat = find(chatId)
        chat ?: return
        update(chat.copy(isArchived = true))
        val archive = getArchiveItem()
        archive.messages.addAll(chat.messages)
        update(archive)
    }

    fun restoreFromArchive(chatId: String) {
        val chat = find(chatId)
        chat ?: return
        update(chat.copy(isArchived = false))
        val archive = getArchiveItem()
        archive.messages.removeAll(chat.messages)

        if(archive.messages.isEmpty()){
            remove(archive)

        } else {
            update(archive)
        }

    }

    private fun remove(chat: Chat) {
        val copy = chats.value!!.toMutableList()
        copy.remove(chat)
        chats.value = copy
    }
}