// Yurick Tchakountio (UID: 117991758) Jocelyn Choo (UID: 117965773)
package com.example.project6

import android.content.Context
import android.util.Log
import android.view.View
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Point
import android.graphics.Rect

class GameView : View {

    private var width : Int = 0
    private var height : Int = 0
    private var paint : Paint
    private var pong : Pong

    constructor( context: Context, width: Int, height: Int) : super( context ){
        this.width = width
        this.height = height

        paint = Paint( )
        paint.color = Color.BLACK
        paint.isAntiAlias = true
        paint.strokeWidth = 20.0f
        paint.textSize = 60f

        pong = Pong( context, Point(width/2, 50), Point(width/2, (height.toFloat() - 75.0f).toInt()),
            25, .00008f)
        pong.setBallSpeed( width * .0006f )
        pong.setPongRect( Rect( 0,0, width, height ) )
        Log.w("GameView", "In constructor")

    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        Log.w("MainActivity", "Inside onDraw")
        var rect = Rect((pong.getLineCenter().x - 100.0f).toInt(), (height.toFloat() - 75.0f).toInt(),
            (pong.getLineCenter().x + 100.0f).toInt(), (height.toFloat() - 55.0f).toInt())
        canvas.drawCircle(pong.getBallCenter().x.toFloat(), pong.getBallCenter().y.toFloat(), 25.0f, paint)
        canvas.drawRect(rect, paint)
        //score display
        if (pong.getGameOver()) {
            if (pong.getNewBestScore()) {
                canvas.drawText(
                    "NEW BEST SCORE: " + pong.getScore(),
                    width / 2 - paint.measureText("NEW BEST SCORE:  ") / 2f,
                    height / 2f,
                    paint
                )
            } else {
                canvas.drawText(
                    "Score: " + pong.getScore(),
                    width / 2 - paint.measureText("Score:  ") / 2f,
                    height / 2f,
                    paint
                )
                canvas.drawText(
                    "Best Score: " + pong.getBestScore(),
                    width / 2f - paint.measureText("Best Score:  ") / 2f,
                    height / 2f + paint.textSize + 20f,
                    paint
                )
            }
        }
    }

    fun getPong(): Pong{
        return pong
    }

    companion object {
        const val DELTA_TIME : Int  = 50
    }
}