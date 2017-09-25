package com.amoryan.demo.kotlin

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.CheckBox
import android.widget.SeekBar
import android.widget.TextView

class MainActivity : AppCompatActivity(), CircleProgressBar.OnProgressChangedListener,
        SeekBar.OnSeekBarChangeListener {

    private lateinit var progressBar: CircleProgressBar
    private lateinit var text: TextView

    private lateinit var startBar: SeekBar
    private lateinit var sweepBar: SeekBar
    private lateinit var rotateBar: SeekBar
    private lateinit var maxBar: SeekBar
    private lateinit var currentBar: SeekBar

    private lateinit var openAnimation: CheckBox

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        progressBar = findViewById(R.id.progressbar) as CircleProgressBar
        text = findViewById(R.id.text) as TextView

        progressBar.setOnProgressChangeListener(this)

        startBar = findViewById(R.id.duration_bar) as SeekBar
        startBar.setOnSeekBarChangeListener(this)
        sweepBar = findViewById(R.id.sweepDegree_bar) as SeekBar
        sweepBar.setOnSeekBarChangeListener(this)
        rotateBar = findViewById(R.id.rotateDegree_bar) as SeekBar
        rotateBar.setOnSeekBarChangeListener(this)
        maxBar = findViewById(R.id.max_bar) as SeekBar
        maxBar.setOnSeekBarChangeListener(this)
        currentBar = findViewById(R.id.progress_bar) as SeekBar
        currentBar.setOnSeekBarChangeListener(this)

        openAnimation = findViewById(R.id.openAnimation) as CheckBox
    }

    override fun onProgressChanged(progress: Float) {
        text.text = "${progress.toInt()}"
    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        when (seekBar?.id) {
            R.id.duration_bar -> {
                progressBar.setDuration(seekBar?.progress * 500.toLong())
            }
            R.id.rotateDegree_bar -> {
                progressBar.setRotateDegree(seekBar?.progress.toFloat())
            }
            R.id.sweepDegree_bar -> {
                progressBar.setSweepDegree(seekBar?.progress.toFloat())
            }
            R.id.max_bar -> {
                currentBar.max = seekBar?.progress
                currentBar.progress = 0
                progressBar.setMaxProgress(seekBar?.progress.toFloat())
            }
            R.id.progress_bar -> {
                progressBar.setProgress(seekBar?.progress.toFloat())
            }
        }
        progressBar.openAnimation(openAnimation.isChecked)
        progressBar.update()
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {
    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {
    }

}
