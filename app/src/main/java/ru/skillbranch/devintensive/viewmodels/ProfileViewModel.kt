package ru.skillbranch.devintensive.viewmodels

import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.skillbranch.devintensive.models.Profile
import ru.skillbranch.devintensive.repositories.PreferencesRepository

class ProfileViewModel : ViewModel(){
    private val repository:PreferencesRepository = PreferencesRepository
    private val profileData = MutableLiveData<Profile>()
    private val appTheme = MutableLiveData<Int>()
    private val isRepositoryInvalidate = MutableLiveData<Boolean>()

    private val repositoryServicePaths = listOf("enterprise", "features", "topics", "collections", "trending", "events", "marketplace", "pricing", "nonprofit", "customer-stories", "security", "login", "join")


    init{
        Log.d("M_ProfileViewModel", "init view model")
        profileData.value = repository.getProfile()
        appTheme.value = repository.getAppTheme()
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("M_ProfileViewModel", "onCleared")
    }

    fun getProfileData():LiveData<Profile> = profileData

    fun getTheme():LiveData<Int> = appTheme

    fun saveProfileData(profile: Profile){
        repository.saveProfile(profile)
        profileData.value = profile
    }

    fun getRepositoryError():LiveData<Boolean> = isRepositoryInvalidate

    fun switchTheme() {
        if(appTheme.value == AppCompatDelegate.MODE_NIGHT_YES){
            appTheme.value = AppCompatDelegate.MODE_NIGHT_NO
        }else{
            appTheme.value = AppCompatDelegate.MODE_NIGHT_YES
        }
        repository.saveAppTheme(appTheme.value!!)
    }

    fun onRepositoryChange(repository: String) {
        isRepositoryInvalidate.value = isRepositoryInvalid(repository)
    }

    private fun isRepositoryInvalid(repository: String): Boolean? {
        if(repository.isEmpty()) {
            return false
        }

        val re = Regex(pattern = "^(https://)?(www\\.)?github\\.com/([\\d\\w\\-_]+)/?$")
        val username = re.matchEntire(repository)?.groups?.get(3)?.value
        if(username.isNullOrEmpty()) {
            return true
        }

        return (repositoryServicePaths.contains(username))
    }
}