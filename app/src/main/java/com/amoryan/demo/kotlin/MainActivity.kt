package com.amoryan.demo.kotlin

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.CheckBox
import android.widget.SeekBar
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), CircleProgressBar.OnProgressChangedListener,
        SeekBar.OnSeekBarChangeListener {

    private lateinit var progressBar: CircleProgressBar
    private lateinit var text: TextView

    private lateinit var startBar: SeekBar
    private lateinit var sweepBar: SeekBar
    private lateinit var rotateBar: SeekBar
    private lateinit var widthBar: SeekBar

    private lateinit var openAnimation: CheckBox

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        progressBar = findViewById(R.id.progressbar) as CircleProgressBar
        text = findViewById(R.id.text) as TextView

        progressBar.setOnProgressChangeListener(this)

        startBar = findViewById(R.id.startDegree_bar) as SeekBar
        startBar.setOnSeekBarChangeListener(this)
        sweepBar = findViewById(R.id.sweepDegree_bar) as SeekBar
        sweepBar.setOnSeekBarChangeListener(this)
        rotateBar = findViewById(R.id.rotateDegree_bar) as SeekBar
        rotateBar.setOnSeekBarChangeListener(this)
        widthBar = findViewById(R.id.width_bar) as SeekBar
        widthBar.setOnSeekBarChangeListener(this)

        openAnimation = findViewById(R.id.openAnimation) as CheckBox
    }

    override fun onProgressChanged(progress: Float) {
        text.text = "${progress.toInt()}"
    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        when (seekBar?.id) {
            R.id.startDegree_bar -> {
                progressBar.setStartDegree(seekBar?.progress.toFloat())
            }
            R.id.rotateDegree_bar -> {
                progressBar.setRotateDegree(seekBar?.progress.toFloat())
            }
            R.id.sweepDegree_bar -> {
                progressBar.setSweepDegree(seekBar?.progress.toFloat())
            }
            R.id.width_bar -> {
                progressBar.setStrokeWidth(seekBar?.progress.toFloat())
            }
        }
        progressBar.openAnimation(openAnimation.isChecked)
        progressbar.update()
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {
    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {
    }

}
