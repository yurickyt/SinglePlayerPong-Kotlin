// Yurick Tchakountio (UID: 117991758) Jocelyn Choo (UID: 117965773)
package com.example.project6

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Point
import android.graphics.Rect
import kotlin.math.cos
import kotlin.math.sin

class Pong {
    private var ballCenter: Point? = null
    private var lineCenter: Point? = null
    private var pongRect: Rect? = null
    private var ballSpeed = 0f
    private var ballAngle = 0f
    private var deltaTime = 0
    private var ballRadius = 0
    private var ballHit: Boolean
    private var score: Int = 0
    private var bestScore: Int = 0
    private var gameOver: Boolean = false
    private var newBestScore: Boolean = false


    constructor( context: Context, newBallCenter: Point?, newLineCenter: Point?, newBallRadius: Int
                 , newBallSpeed: Float) {
        setBallCenter(newBallCenter)
        setBallSpeed(newBallSpeed)
        setBallRadius(newBallRadius)
        setLineCenter(newLineCenter)
        deltaTime = GameView.DELTA_TIME
        var pref : SharedPreferences = context.getSharedPreferences(context.packageName + "_preferences",
            Context.MODE_PRIVATE)
        setBestScore(pref.getInt(PREF_BEST_SCORE, 0))

        ballHit = false
        ballAngle = 7*Math.PI.toFloat() / 4 // starting cannon angle
    }

    fun setGameOver(b: Boolean) {
        gameOver = b
    }

    private fun setLineCenter(newLineCenter: Point?) {
        if (newLineCenter != null){
            lineCenter = newLineCenter
        }
    }

    private fun setBallCenter(newBallCenter: Point?) {
        if (newBallCenter != null){
            ballCenter = newBallCenter
        }
    }

    private fun setBallRadius(newBallRadius: Int) {
        if (newBallRadius > 0)
            ballRadius = newBallRadius
    }

    fun setBallSpeed(newBallSpeed: Float) {
        if (newBallSpeed > 0)
            ballSpeed = newBallSpeed
    }

    fun setPongRect(newPongRect: Rect?) {
        if (newPongRect != null)
            pongRect = newPongRect
    }

    fun setBestScore(bs: Int){
        bestScore = bs
    }

    fun getGameOver(): Boolean {
        return gameOver
    }

    fun getNewBestScore(): Boolean {
        return newBestScore
    }

    fun getBallCenter(): Point{
        return ballCenter!!
    }

    fun getLineCenter(): Point{
        return lineCenter!!
    }

    fun getScore(): Int{
        return score
    }

    fun getBestScore(): Int{
        return bestScore
    }

    fun updateBestScore(){
        newBestScore = true
        bestScore = score
    }

    fun writeData(context: Context){
        var pref : SharedPreferences = context.getSharedPreferences(context.packageName + "_preferences",
            Context.MODE_PRIVATE)
        var editor : SharedPreferences.Editor = pref.edit()
        editor.putInt(PREF_BEST_SCORE, bestScore)
        editor.commit()
    }

    fun setBallAngle(currAngle: Float) {
        if (currAngle == Math.PI.toFloat() / 4) { // 45
            if (ballCenter!!.x + ballRadius >= pongRect!!.right) {
                ballAngle = 3 * Math.PI.toFloat() / 4
            } else {
                ballAngle = 7 * Math.PI.toFloat() / 4
            }
        } else if (currAngle == 3 * Math.PI.toFloat() / 4) { // 135 degrees
            if (ballCenter!!.x - ballRadius <= pongRect!!.left) {
                ballAngle = Math.PI.toFloat() / 4
            } else {
                ballAngle = 5 * Math.PI.toFloat() / 4
            }
        } else if (currAngle == 5 * Math.PI.toFloat() / 4) { // 225
            if (wallCollide()) {
                ballAngle = 7 * Math.PI.toFloat() / 4
            } else if (lineCollide()) {
                ballAngle = 3 * Math.PI.toFloat() / 4
            }
        } else if (currAngle == 7 * Math.PI.toFloat() / 4) { // 315 degrees
            if (wallCollide()) {
                ballAngle = 5 * Math.PI.toFloat() / 4
            } else if (lineCollide()) {
                ballAngle = Math.PI.toFloat() / 4
            }
        }
    }

    fun wallCollide() : Boolean {
        return (ballCenter!!.x - ballRadius <= pongRect!!.left ||
                ballCenter!!.x + ballRadius >= pongRect!!.right ||
                ballCenter!!.y - ballRadius <= pongRect!!.top)
    }


    fun lineCollide(): Boolean{
        return ballCenter!!.y + ballRadius >= lineCenter!!.y && ballCenter!!.y + ballRadius <= lineCenter!!.y + 10.0f
                && ballCenter!!.x >= lineCenter!!.x - 100.0f && ballCenter!!.x <= lineCenter!!.x + 100.0f
    }

    fun moveLine(newLine: Float){
        lineCenter!!.x = newLine.toInt()
    }

    fun moveBall() {
        if (wallCollide() || lineCollide()){
            if (lineCollide()){
                score++
                ballSpeed = ballSpeed + 0.1f
            }
            setBallAngle(ballAngle)
        }
            ballCenter!!.x += (ballSpeed * cos(ballAngle.toDouble()) * deltaTime).toInt()
            ballCenter!!.y -= (ballSpeed * sin(ballAngle.toDouble()) * deltaTime).toInt()
    }

    fun outOfBounds(): Boolean {
        return (ballCenter!!.y + ballRadius >= pongRect!!.bottom)
    }

    companion object{
        const val PREF_BEST_SCORE : String = "bestLevel"
    }
}