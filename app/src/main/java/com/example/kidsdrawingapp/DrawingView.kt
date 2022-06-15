package com.example.kidsdrawingapp

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View


class DrawingView(context :Context,attrs :AttributeSet) : View(context,attrs){
    private var mdrawPath : CustomPath? =null
    private var mbitmap : Bitmap? =null
    private var mdrawPaint :Paint? =null
    private var mcanvasPaint :Paint? =null
    private var mbrushSize :Float = 0.toFloat()
    private var color =Color.BLACK
    private var canvas :Canvas? =null

    private var mpaths = ArrayList<CustomPath>()
    private var mUndoPaths = ArrayList<CustomPath>()

    init {
        setUpDrawing()
    }
    fun onClickUndo(){
        if(mpaths.size >0){
            mUndoPaths.add(mpaths.removeAt(mpaths.size-1))
            invalidate()
        }
    }
    fun onclickRedo(){
        if(mUndoPaths.size > 0) {
            mpaths.add(mUndoPaths.removeAt(mUndoPaths.size - 1))
            invalidate()
        }
    }

    private fun setUpDrawing() {
        mdrawPaint =Paint()
        mdrawPath =CustomPath(color,mbrushSize)
        mdrawPaint!!.color =color
        mdrawPaint!!.style =Paint.Style.STROKE
        mdrawPaint!!.strokeJoin =Paint.Join.ROUND
        mdrawPaint!!.strokeCap = Paint.Cap.ROUND
        mcanvasPaint = Paint(Paint.DITHER_FLAG)
        //mbrushSize =20.toFloat()

    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {      //overriding member of View class
        super.onSizeChanged(w, h, oldw, oldh)
        mbitmap = Bitmap.createBitmap(w,h,Bitmap.Config.ARGB_8888)
        canvas = Canvas(mbitmap!!)
    }

    //change Canvas to canvas? if fails
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawBitmap(mbitmap!!,0f,0f,mcanvasPaint)
        for (path in mpaths){
            mdrawPaint!!.strokeWidth = path.brushThickness
            mdrawPaint!!.color = path.color
            canvas.drawPath(path, mdrawPaint!!)
        }

        if(!mdrawPath!!.isEmpty) {
            mdrawPaint!!.strokeWidth = mdrawPath!!.brushThickness
            mdrawPaint!!.color = mdrawPath!!.color
            canvas.drawPath(mdrawPath!!, mdrawPaint!!)
        }
    }
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val touchX =event?.x
        val touchY =event?.y

        when(event?.action){
            MotionEvent.ACTION_DOWN ->{
               mdrawPath!!.color =color
               mdrawPath!!.brushThickness =mbrushSize

               mdrawPath!!.reset()
                if (touchX != null) {
                    if (touchY != null) {
                        mdrawPath!!.moveTo(touchX,touchY)
                    }
                }
            }
            MotionEvent.ACTION_MOVE ->{
                if (touchX != null) {
                    if (touchY != null) {
                        mdrawPath!!.lineTo(touchX,touchY)
                    }
                }
            }
            MotionEvent.ACTION_UP ->{
                mpaths.add(mdrawPath!!)
                mdrawPath = CustomPath(color,mbrushSize)
            }
            else -> return false
            }
        invalidate()
        return true
        }

    fun setsizeforBrush(newSize :Float){
        mbrushSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,newSize,resources.displayMetrics)
        mdrawPaint!!.strokeWidth = mbrushSize
    }

    fun setColor(newColor:String){
        color =Color.parseColor(newColor)
        mdrawPaint!!.color = color
    }
    internal inner class CustomPath(var color :Int, var brushThickness :Float) : Path(){

    }
}