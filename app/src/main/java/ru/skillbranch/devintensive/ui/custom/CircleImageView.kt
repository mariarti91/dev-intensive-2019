package ru.skillbranch.devintensive.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import ru.skillbranch.devintensive.R

class CircleImageView@JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr:Int = 0
): ImageView(context, attrs, defStyleAttr) {
    companion object{
        private const val DEFAULT_DIAMETER = 112
    }

    private var diameter = DEFAULT_DIAMETER

    init {
        if(attrs != null){
            val a = context.obtainStyledAttributes(attrs, R.styleable.CircleImageView)
            diameter = a.getInt(R.styleable.CircleImageView_diameter, DEFAULT_DIAMETER)
            setMeasuredDimension(diameter, diameter)
            a.recycle()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(diameter, diameter)
    }
}