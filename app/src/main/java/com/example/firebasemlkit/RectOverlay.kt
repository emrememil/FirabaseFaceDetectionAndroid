package com.example.firebasemlkit


import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF



class RectOverlay(
    private val graphicOverlay: GraphicOverlay,
    private val rect: Rect,
    private val text:String
) : GraphicOverlay.Graphic(graphicOverlay) {

    private val RECT_COLOR = Color.RED
    private val TEXT_COLOR = Color.WHITE
    private val strokeWidth = 4.0f
    private val rectPaint: Paint = Paint()
    private val textPaint: Paint = Paint()

    init {
        textPaint.textSize=30f
        textPaint.color= TEXT_COLOR
        rectPaint.color = RECT_COLOR
        rectPaint.style = Paint.Style.STROKE
        rectPaint.strokeWidth = strokeWidth
        postInvalidate()
    }

    override fun draw(canvas: Canvas) {
        val rectF = RectF(rect)
        rectF.left = translateX(rectF.left)
        rectF.right = translateX(rectF.right)
        rectF.top = translateY(rectF.top)
        rectF.bottom = translateY(rectF.bottom)
        var numOfChars = textPaint.breakText(text,true,rect.width().toFloat(),null)
        var start = (text.length-numOfChars)/2

        canvas.drawRect(rectF, rectPaint)
        //canvas.drawText(text,start,start+numOfChars, rectF.top,rectF.top,textPaint)
        canvas.drawText(text,start,start+numOfChars, rect.exactCenterX(),rect.exactCenterY(),textPaint)

    }

}