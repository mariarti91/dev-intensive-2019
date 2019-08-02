package ru.skillbranch.devintensive.ui.profile

import android.graphics.*
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextPaint
import android.text.TextWatcher
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.core.graphics.drawable.toDrawable
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

    private var currentInitials:String? = null
    private var viewModel: ProfileViewModel = ProfileViewModel()
    var isEditMode = false
    lateinit var viewFields : Map<String, TextView>
    private val repositoryTextWatcher = object : TextWatcher{
        val servicePaths = listOf("enterprise", "features", "topics", "collections", "trending", "events", "marketplace", "pricing", "nonprofit", "customer-stories", "security", "login", "join")
        val errorMessage = "Невалидный адрес репозитория"
        override fun afterTextChanged(repoUrl: Editable?) {

            if(repoUrl.isNullOrEmpty()) {
                wr_repository?.error = null
                return
            }

            val re = Regex("^(https://)?(www\\.)?github\\.com/([^/\\s]+)(?<!${servicePaths.joinToString("|")})/?$")
                    //wr_repository?.error = if(re.matches(repoUrl)) null else errorMessage
            val username = re.matchEntire(repoUrl.toString())?.groups?.get(3)?.value
            if(username.isNullOrEmpty()) {
                wr_repository?.error = errorMessage
                return
            }

            wr_repository?.error = if(servicePaths.contains(username)) errorMessage else null
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
        val initials = Utils.toInitials(profile.firstName, profile.lastName)
        updateAvatar(initials)
    }

    private fun updateAvatar(initials: String?) {
        if(initials != currentInitials) {
            currentInitials = initials
            iv_avatar.setAvatarDrawable(generateNewAvatar())
        }
    }

    private fun generateNewAvatar(): Drawable? {
        if(currentInitials.isNullOrEmpty()) return null

        val width = 400
        val height = 400

        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap!!)

        val color = TypedValue()
        theme.resolveAttribute(R.attr.colorAccent, color, true)
        canvas.drawColor(color.data)

        val textPaint = TextPaint(Paint.ANTI_ALIAS_FLAG)
        textPaint.textSize = 50F * resources.displayMetrics.scaledDensity
        textPaint.color = Color.WHITE
        val textWidth = textPaint.measureText(currentInitials) * 0.5F
        val textBaseLineHeight = textPaint.fontMetrics.ascent * -0.35F
        canvas.drawText(currentInitials!!, width/2 - textWidth, height/2 + textBaseLineHeight, textPaint)

        return bitmap.toDrawable(resources)
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
            wr_repository.error = null
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
}