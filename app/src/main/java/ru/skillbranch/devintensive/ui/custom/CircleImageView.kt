package ru.skillbranch.devintensive.ui.custom

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.text.TextPaint
import android.util.AttributeSet
import android.util.TypedValue
import android.widget.ImageView
import androidx.annotation.ColorRes
import androidx.annotation.Dimension
import androidx.core.graphics.drawable.toDrawable
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

    private var borderColor = DEFAULT_BORDER_COLOR
    private var borderWidth = DEFAULT_BORDER_WIDTH
    init {
        if(attrs != null){
            val a = context.obtainStyledAttributes(attrs, R.styleable.CircleImageView)
            borderColor = a.getInt(R.styleable.CircleImageView_cv_borderColor, DEFAULT_BORDER_COLOR)
            borderWidth = a.getDimensionPixelSize(R.styleable.CircleImageView_cv_borderWidth, DEFAULT_BORDER_WIDTH)
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

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        createBorder(canvas)
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

    @SuppressLint("ResourceAsColor")
    fun generateAvatar(initials: String?, theme: Resources.Theme){
//        if(initials.isNullOrEmpty()){
//            avatarBitmap = null
//            return
//        }
//
//        avatarBitmap = Bitmap.createBitmap(layoutParams.width, layoutParams.height, Bitmap.Config.ARGB_8888)
//        val canvas = Canvas(avatarBitmap!!)
//
//        val color = TypedValue()
//        theme.resolveAttribute(R.attr.colorAccent, color, true)
//
//        var paint = Paint()
//        paint.color = color.data
//        paint.strokeWidth = borderWidth.toFloat()
//        paint.isAntiAlias = true
//        paint.isDither = true
//
//        var centerX = (layoutParams.width/2).toFloat()
//        var centerY = (layoutParams.height/2).toFloat()
//        var radius = centerX
//
//        canvas.drawCircle(centerX, centerY, radius, paint)
//
//        paint.color = borderColor
//        paint.style = Paint.Style.STROKE
//        radius = centerX - borderWidth/2
//        canvas.drawCircle(centerX, centerY, radius, paint)
//
//        val textPaint = TextPaint(Paint.ANTI_ALIAS_FLAG)
//        textPaint.textSize = 40F * resources.displayMetrics.scaledDensity
//        textPaint.color = Color.WHITE
//
//        val textWidth = textPaint.measureText(initials) * 0.5F
//        val textBaseLineHeight = textPaint.fontMetrics.ascent * -0.4F
//
//        canvas.drawText(initials, centerX - textWidth, centerY + textBaseLineHeight, textPaint)
//        invalidate()
    }
}