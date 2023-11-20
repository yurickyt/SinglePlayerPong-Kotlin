// Yurick Tchakountio (UID: 117991758) Jocelyn Choo (UID: 117965773)
package com.example.project6

import android.content.res.Resources
import android.media.SoundPool
import android.os.Bundle
import android.view.GestureDetector
import android.view.MotionEvent
import androidx.activity.ComponentActivity
import java.util.Timer

class MainActivity : ComponentActivity() {
    private lateinit var gameView : GameView
    lateinit var pong : Pong
    private lateinit var detector : GestureDetector
    private lateinit var gameTimer: Timer
    private lateinit var gameTimerTask: GameTimerTask
    private var context = this
    private var start: Boolean = true
    private var width = 0
    private var height = 0
    private var statusBarId = 0
    private var statusBarHeight = 0
    lateinit var pool: SoundPool
    private var hitId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        width = Resources.getSystem().displayMetrics.widthPixels
        height = Resources.getSystem().displayMetrics.heightPixels

        statusBarId = resources.getIdentifier("status_bar_height", "dimen", "android")
        statusBarHeight = resources.getDimensionPixelSize( statusBarId )

        gameView = GameView( this, width, height - statusBarHeight)
        pong = gameView.getPong()
        setContentView( gameView )

        gameTimer = Timer( )
        gameTimerTask = GameTimerTask( context )

        var handler : TouchHandler = TouchHandler( )
        detector = GestureDetector( this, handler )
        detector.setOnDoubleTapListener( handler )

        var poolBuilder = SoundPool.Builder()
        pool = poolBuilder.build()
        hitId = pool.load(context, R.raw.hit, 1)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if( event != null )
            detector.onTouchEvent( event )
        return super.onTouchEvent(event)
    }

    fun updateModel( ) {
        if( pong.outOfBounds()) {
            pong.setGameOver(true)
            if (pong.getScore() > pong.getBestScore()) {
                pong.updateBestScore()
                pong.writeData(this)
            }
            gameTimerTask.cancel()
            gameTimer.cancel()
        }
        else{
            if (pong.lineCollide())
                pool.play(hitId, 10.0f, 10.0f, 0, 0, 1.0f)
            pong.moveBall()
        }
    }


    fun updateView( ) {
        gameView.postInvalidate()
    }

    inner class TouchHandler : GestureDetector.SimpleOnGestureListener ( ) {
        override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
            if (start) {
                startGame(e)
            }
            return super.onSingleTapConfirmed(e)
        }

        override fun onScroll(e1: MotionEvent?, e2: MotionEvent, distanceX: Float, distanceY: Float): Boolean {
            updateLine(e2)
            return super.onScroll(e1, e2, distanceX, distanceY)
        }

        fun startGame(e: MotionEvent) {
            start = false
            gameTimer.schedule( gameTimerTask, 0L, GameView.DELTA_TIME.toLong() )
        }

        fun updateLine(event: MotionEvent){
            var x: Float = event.x
            pong.moveLine(x)
        }
    }
}