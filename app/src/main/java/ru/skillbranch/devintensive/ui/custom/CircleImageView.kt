package ru.skillbranch.devintensive.ui.custom

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.widget.ImageView
import androidx.annotation.ColorRes
import androidx.annotation.Dimension
import androidx.core.graphics.drawable.toBitmap
import kotlinx.android.synthetic.main.activity_profile.view.*
import ru.skillbranch.devintensive.R
import ru.skillbranch.devintensive.utils.Utils


/*
Реализуй CustomView с названием класса CircleImageView и кастомными xml атрибутами
cv_borderColor (цвет границы (format="color") по умолчанию white) и
cv_borderWidth (ширина границы (format="dimension") по умолчанию 2dp).
CircleImageView должна превращать установленное изображение в круглое изображение с цветной рамкой,
у CircleImageView должны быть реализованы методы
@Dimension getBorderWidth():Int,
setBorderWidth(@Dimension dp:Int),
getBorderColor():Int,
setBorderColor(hex:String),
setBorderColor(@ColorRes colorId: Int).
Используй CircleImageView как ImageView для аватара пользователя (@id/iv_avatar)
 */

class CircleImageView@JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr:Int = 0
): ImageView(context, attrs, defStyleAttr) {
    companion object{
        @SuppressLint("ResourceType")
        @ColorRes private const val DEFAULT_BORDER_COLOR = Color.WHITE
        @Dimension private const val DEFAULT_BORDER_WIDTH = 2
    }

    private var avatarDrawable:Drawable? = null
    private var borderColor = DEFAULT_BORDER_COLOR
    private var borderWidth = DEFAULT_BORDER_WIDTH
    init {
        if(attrs != null){
            val a = context.obtainStyledAttributes(attrs, R.styleable.CircleImageView)
            borderColor = a.getInt(R.styleable.CircleImageView_cv_borderColor, DEFAULT_BORDER_COLOR)
            borderWidth = a.getDimensionPixelSize(R.styleable.CircleImageView_cv_borderWidth, DEFAULT_BORDER_WIDTH)
            clipToOutline = true
            a.recycle()
        }
    }

    fun getBorderWidth():Int = borderWidth
    fun setBorderWidth(@Dimension dp:Int) {
        borderWidth = dp
        invalidate()
    }

    fun getBorderColor():Int = borderColor

    @SuppressLint("ResourceAsColor")
    fun setBorderColor(hex:String){
        borderColor = Color.parseColor(hex)
        invalidate()
    }

    @SuppressLint("ResourceAsColor")
    fun setBorderColor(@ColorRes colorId: Int){
        borderColor = resources.getColor(colorId, null)
        invalidate()
    }

    fun setAvatarDrawable(avatar: Drawable?){
        avatarDrawable = avatar
        invalidate()
    }

    fun getAvatarDrawable():Drawable? = avatarDrawable

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawAvatar(canvas)
        createBorder(canvas)
    }

    private fun drawAvatar(canvas: Canvas) {
        val drawable = if(avatarDrawable != null) avatarDrawable else resources.getDrawable(R.drawable.ic_avatar, null)
        val avatarBitmap = drawable?.toBitmap(iv_avatar.width, iv_avatar.height)
        val padding = Utils.dpToPx(borderWidth, context)/2F
        canvas.drawBitmap(avatarBitmap!!, padding, padding, null)
    }

    @SuppressLint("ResourceAsColor")
    private fun createBorder(canvas: Canvas) {
        if(borderWidth > 0) {
            val dpBorderWidth = Utils.dpToPx(borderWidth, context).toFloat()

            val paint = Paint()
            paint.strokeWidth = dpBorderWidth
            paint.isAntiAlias = true
            paint.isDither = true
            paint.color = borderColor
            paint.style = Paint.Style.STROKE

            val centerX = (layoutParams.width / 2).toFloat()
            val centerY = (layoutParams.height / 2).toFloat()
            val radius = centerX - dpBorderWidth / 2

            canvas.drawCircle(centerX, centerY, radius, paint)
        }
    }
}