package ru.skillbranch.devintensive.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.skillbranch.devintensive.ui.profile.ProfileActivity

class SplashActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val intent = Intent(this, ProfileActivity::class.java)
        startActivity(intent)
        finish()
    }
}