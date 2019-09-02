package ru.skillbranch.devintensive.ui.archive

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_group.*
import ru.skillbranch.devintensive.R

class ArchiveActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_archive)

        initToolBar()
        initViews()
        initViewModel()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home){
            finish()
            overridePendingTransition(R.anim.idle, R.anim.bottom_down)
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initToolBar() {
        toolbar.title = "Архив чатов"
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun initViews() {

    }

    private fun initViewModel() {

    }
}
