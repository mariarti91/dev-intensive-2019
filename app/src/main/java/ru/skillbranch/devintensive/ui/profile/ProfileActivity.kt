package ru.skillbranch.devintensive.ui.profile

import android.graphics.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.activity_profile.*
import ru.skillbranch.devintensive.R

import ru.skillbranch.devintensive.models.Profile
import ru.skillbranch.devintensive.utils.Utils
import ru.skillbranch.devintensive.viewmodels.ProfileViewModel

class ProfileActivity : AppCompatActivity() {

    companion object{
        const val IS_EDIT_MODE = "IS_EDIT_MODE"
    }

    private var viewModel: ProfileViewModel = ProfileViewModel()
    var isEditMode = false
    lateinit var viewFields : Map<String, TextView>
    private val repositoryTextWatcher = object : TextWatcher{
        override fun afterTextChanged(p0: Editable?) {
            val re = Regex("^((https:\\/\\/)?(www.)?github\\.com\\/(?!(enterprise|features|topics|collections|trending|events|marketplace|pricing|nonprofit|customer-stories|security|login|join))[^\\/]+)?$")
            wr_repository?.error = if(re.containsMatchIn(p0.toString())) null else "Невалидный адрес репозитория"
        }

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        initViews(savedInstanceState)
        initViewModel()

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.d("M_MainActivity", "onSaveInstanceStatus")

        outState.putBoolean(IS_EDIT_MODE, isEditMode)

    }

    private fun initViewModel(){
        viewModel = ViewModelProviders.of(this).get(ProfileViewModel::class.java)
        viewModel.getProfileData().observe(this, Observer { updateUI(it) })
        viewModel.getTheme().observe(this, Observer { updateTheme(it) })
    }

    private fun updateTheme(theme: Int) {
        delegate.setLocalNightMode(theme)
    }

    private fun updateUI(profile: Profile) {
        profile.toMap().also {
            for((k,v) in viewFields){
                v.text = it[k].toString()
            }
        }
        generateAvatar(profile)
    }

    private fun initViews(savedInstanceState: Bundle?) {

        viewFields = mapOf(
                "nickName" to tv_nick_name,
                "rank" to tv_rank,
                "firstName" to et_first_name,
                "lastName" to et_last_name,
                "about" to et_about,
                "repository" to et_repository,
                "rating" to tv_rating,
                "respect" to tv_respect
        )

        isEditMode = savedInstanceState?.getBoolean(IS_EDIT_MODE, false) ?: false
        showCurrentMode(isEditMode)

        btn_edit.setOnClickListener {
            if(isEditMode) saveProfileInfo()
            isEditMode = !isEditMode
            showCurrentMode(isEditMode)
        }

        btn_switch_theme.setOnClickListener {
            viewModel.switchTheme()
        }


    }

    private fun showCurrentMode(isEdit: Boolean) {
        val info = viewFields.filter { setOf("firstName", "lastName", "about", "repository").contains(it.key) }
        for ((_,v) in info){
            v as EditText
            v.isFocusable = isEdit
            v.isFocusableInTouchMode = isEdit
            v.isEnabled = isEdit
            v.background.alpha = if(isEdit) 255 else 0
        }

        ic_eye.visibility = if(isEdit) View.GONE else View.VISIBLE
        wr_about.isCounterEnabled = isEdit

        with(btn_edit) {
            val filter: ColorFilter? = if (isEdit) {
                PorterDuffColorFilter(
                        resources.getColor(R.color.color_accent, theme),
                        PorterDuff.Mode.SRC_IN)
            } else {
                null
            }

            val icon = if (isEdit) {
                resources.getDrawable(R.drawable.ic_save_black_24dp, theme)
            } else {
                resources.getDrawable(R.drawable.ic_edit_black_24dp, theme)
            }

            background.colorFilter = filter
            setImageDrawable(icon)
        }

        if(isEdit){
            et_repository.addTextChangedListener(repositoryTextWatcher)
        }else{
            et_repository.removeTextChangedListener(repositoryTextWatcher)
        }
    }

    private fun saveProfileInfo(){
        Profile(
                firstName = et_first_name.text.toString(),
                lastName = et_last_name.text.toString(),
                about = et_about.text.toString(),
                repository = if (wr_repository.error.isNullOrEmpty()) et_repository.text.toString() else ""
        ).apply {
            viewModel.saveProfileData(this)
        }
    }

    private fun generateAvatar(profile: Profile) {
        val initials = Utils.toInitials(profile.firstName, profile.lastName)
        iv_avatar.generateAvatar(initials, theme)
    }

}




