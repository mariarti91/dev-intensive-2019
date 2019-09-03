package ru.skillbranch.devintensive.ui.archive

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_archive.*
import kotlinx.android.synthetic.main.activity_group.*
import kotlinx.android.synthetic.main.activity_group.toolbar
import kotlinx.android.synthetic.main.activity_main.*
import ru.skillbranch.devintensive.R
import ru.skillbranch.devintensive.ui.adapters.ChatAdapter
import ru.skillbranch.devintensive.ui.adapters.ChatItemTouchHelperCallback
import ru.skillbranch.devintensive.viewmodels.ArchiveViewModel

class ArchiveActivity : AppCompatActivity() {

    private lateinit var chatAdapter: ChatAdapter
    private lateinit var viewModel: ArchiveViewModel

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
        chatAdapter = ChatAdapter{
            Snackbar.make(rv_archive_list, "Click on ${it.title}", Snackbar.LENGTH_LONG).show()
        }

        val divider = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        val touchCallback = ChatItemTouchHelperCallback(chatAdapter){
            val chatId = it.id
            viewModel.restoreFromArchive(chatId)
            Snackbar.make(rv_chat_list, "Вы точно хотите восстановить ${it.title} из архива?", Snackbar.LENGTH_LONG)
                    .setAction("Отменить") {
                        viewModel.addToArchive(chatId)
                    }
                    .show()
        }

        val touchHelper = ItemTouchHelper(touchCallback)
        touchHelper.attachToRecyclerView(rv_chat_list)

        with(rv_archive_list){
            adapter = chatAdapter
            layoutManager = LinearLayoutManager(this@ArchiveActivity)
            addItemDecoration(divider)
        }
    }

    private fun initViewModel() {
        viewModel = ViewModelProviders.of(this).get(ArchiveViewModel::class.java)
        viewModel.getChatData().observe(this, Observer {
            chatAdapter.updateData(it)
        })
    }
}
