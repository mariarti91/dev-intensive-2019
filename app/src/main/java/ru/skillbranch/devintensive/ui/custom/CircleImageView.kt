package ru.skillbranch.devintensive.ui.custom

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.util.AttributeSet
import android.widget.ImageView
import androidx.annotation.ColorRes
import androidx.annotation.Dimension
import androidx.core.graphics.drawable.toBitmap
import kotlinx.android.synthetic.main.activity_profile.view.*
import ru.skillbranch.devintensive.R

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
        @Dimension private const val DEFAULT_BORDER_WIDTH = 2f
    }

    private var borderColor = DEFAULT_BORDER_COLOR
    private var borderWidth = DEFAULT_BORDER_WIDTH

    init {
        if(attrs != null){
            val a = context.obtainStyledAttributes(attrs, R.styleable.CircleImageView)
            borderColor = a.getInt(R.styleable.CircleImageView_cv_borderColor, DEFAULT_BORDER_COLOR)
            borderWidth = a.getDimension(R.styleable.CircleImageView_cv_borderWidth, DEFAULT_BORDER_WIDTH)
            a.recycle()
        }
    }
}