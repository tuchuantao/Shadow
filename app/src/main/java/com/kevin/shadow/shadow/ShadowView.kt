package com.kuaiest.ui.shadow

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import com.kevin.shadow.R


class ShadowView @JvmOverloads constructor(context: Context?, attributeSet: AttributeSet? = null, defStyleInt: Int = 0) :
    View(context, attributeSet, defStyleInt) {

    private var SIZE_UNSET = -1
    private var SIZE_DEFAULT = 0

    private val bgPaint = Paint()
    private var shadowColor: Int = 0
    private var backgroundClr: Int = 0
    private var shadowRadius = 0f
        get() {
            return if (field > getShadowMarginMax() && getShadowMarginMax() != 0f) {
                getShadowMarginMax()
            } else {
                field
            }
        }
    private var shadowDx = 0f
    private var shadowDy = 0f
    private var cornerRadiusTL: Float
    private var cornerRadiusTR: Float
    private var cornerRadiusBL: Float
    private var cornerRadiusBR: Float
    private var shadowMarginTop: Int = 0
    private var shadowMarginLeft: Int = 0
    private var shadowMarginRight: Int = 0
    private var shadowMarginBottom: Int = 0
    private var openCache: Boolean = false
    private var sizeChanged = false
    private var cacheBitmap: Bitmap? = null

    init {
        val a = getContext().obtainStyledAttributes(
            attributeSet, R.styleable.ShadowView,
            defStyleInt, 0
        )
        shadowColor = a.getColor(
            R.styleable.ShadowView_shadowColor
            , ContextCompat.getColor(context!!, R.color.colorPrimary)
        )
        openCache = a.getBoolean(R.styleable.ShadowView_openCache, false)
        backgroundClr = a.getColor(R.styleable.ShadowView_backgroundColor, Color.WHITE)
        shadowDx = a.getDimensionPixelSize(R.styleable.ShadowView_shadowDx, 0).toFloat()
        shadowDy = a.getDimensionPixelSize(R.styleable.ShadowView_shadowDy, 0).toFloat()
        shadowRadius = a.getDimensionPixelSize(R.styleable.ShadowView_shadowRadius, SIZE_DEFAULT).toFloat()
        val shadowMargin = a.getDimensionPixelSize(R.styleable.ShadowView_shadowMargin, SIZE_UNSET)
        if (shadowMargin >= 0) {
            shadowMarginTop = shadowMargin
            shadowMarginLeft = shadowMargin
            shadowMarginRight = shadowMargin
            shadowMarginBottom = shadowMargin
        } else {
            shadowMarginTop = a.getDimensionPixelSize(R.styleable.ShadowView_shadowMarginTop, SIZE_DEFAULT)
            shadowMarginLeft = a.getDimensionPixelSize(R.styleable.ShadowView_shadowMarginLeft, SIZE_DEFAULT)
            shadowMarginRight = a.getDimensionPixelSize(R.styleable.ShadowView_shadowMarginRight, SIZE_DEFAULT)
            shadowMarginBottom = a.getDimensionPixelSize(R.styleable.ShadowView_shadowMarginBottom, SIZE_DEFAULT)
        }

        val cornerRadius = a.getDimensionPixelSize(R.styleable.ShadowView_cornerRadius, SIZE_UNSET).toFloat()
        if (cornerRadius >= 0) {
            cornerRadiusTL = cornerRadius
            cornerRadiusTR = cornerRadius
            cornerRadiusBL = cornerRadius
            cornerRadiusBR = cornerRadius
        } else {
            cornerRadiusTL = a.getDimensionPixelSize(R.styleable.ShadowView_cornerRadiusTL, SIZE_DEFAULT).toFloat()
            cornerRadiusTR = a.getDimensionPixelSize(R.styleable.ShadowView_cornerRadiusTR, SIZE_DEFAULT).toFloat()
            cornerRadiusBL = a.getDimensionPixelSize(R.styleable.ShadowView_cornerRadiusBL, SIZE_DEFAULT).toFloat()
            cornerRadiusBR = a.getDimensionPixelSize(R.styleable.ShadowView_cornerRadiusBR, SIZE_DEFAULT).toFloat()
        }
        a.recycle()
        bgPaint.color = backgroundClr
        bgPaint.isAntiAlias = true
        bgPaint.style = Paint.Style.FILL
        bgPaint.setShadowLayer(shadowRadius, shadowDx, shadowDy, shadowColor)
        setLayerType(LAYER_TYPE_SOFTWARE, null)
        background = null
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (openCache) {
            if (sizeChanged) {
                sizeChanged = false
                try {
                    // Bitmap.Config.ALPHA_8只保留透明度，因为现在的阴影是黑色，所以可以使用，用以降低Bitmap的大小
                    cacheBitmap = Bitmap.createBitmap(measuredWidth, measuredHeight, Bitmap.Config.ALPHA_8)
                    drawShadow(Canvas(cacheBitmap))
                } catch (e: OutOfMemoryError) {
                    Log.e(ShadowView::class.java.simpleName, "OutOfMemoryError: " + e.message)
                }
            }
            cacheBitmap?.let {
                canvas?.drawBitmap(cacheBitmap, 0F, 0F, Paint())
            }
        } else {
            drawShadow(canvas)
        }
    }

    /**
     * 绘制阴影
     */
    private fun drawShadow(canvas: Canvas?) {
        canvas?.let {
            val w = measuredWidth
            val h = measuredHeight
            val path = ShapeUtils.roundedRect(
                shadowMarginLeft.toFloat(), shadowMarginTop.toFloat(), (w - shadowMarginRight).toFloat()
                , (h - shadowMarginBottom).toFloat()
                , cornerRadiusTL
                , cornerRadiusTR
                , cornerRadiusBR
                , cornerRadiusBL
            )
            it.drawPath(path, bgPaint)
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        sizeChanged = true
    }

    private fun getShadowMarginMax() = intArrayOf(shadowMarginLeft, shadowMarginTop, shadowMarginRight, shadowMarginBottom).max()?.toFloat() ?: 0f
}
