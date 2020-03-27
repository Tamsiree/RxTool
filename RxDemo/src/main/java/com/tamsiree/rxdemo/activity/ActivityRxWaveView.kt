package com.tamsiree.rxdemo.activity

import android.graphics.Color
import android.os.Bundle
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import com.tamsiree.rxdemo.R
import com.tamsiree.rxkit.RxDeviceTool.setPortrait
import com.tamsiree.rxkit.RxImageTool.changeColorAlpha
import com.tamsiree.rxkit.TLog
import com.tamsiree.rxui.activity.ActivityBase
import com.tamsiree.rxui.view.waveview.RxWaveView
import kotlinx.android.synthetic.main.activity_rx_wave_view.*

/**
 * @author tamsiree
 */
class ActivityRxWaveView : ActivityBase() {

    private var mBorderColor = Color.parseColor("#4489CFF0")
    private var mBorderWidth = 10

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rx_wave_view)
        setPortrait(this)

    }

    override fun initView() {
        rx_title.setLeftFinish(this)

//        mWave.setBorder(mBorderWidth, mBorderColor);
//        mWaveHelper = new RxWaveHelper(mWave);
        shapeChoice.setOnCheckedChangeListener { radioGroup, i ->
            when (i) {
                R.id.shapeCircle -> wave.shapeType = RxWaveView.ShapeType.CIRCLE
                R.id.shapeSquare -> wave.shapeType = RxWaveView.ShapeType.SQUARE
                else -> {
                }
            }
        }
        seekBar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                mBorderWidth = i
                wave.setBorder(mBorderWidth, wave.borderColor)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })
        color_picker_view.addOnColorChangedListener { selectedColor ->
            // Handle on color change
            TLog.d("selectedColor", "selectedColor: $selectedColor")
            wave.setWaveColor(changeColorAlpha(selectedColor, 40),
                    changeColorAlpha(selectedColor, 60))
            mBorderColor = changeColorAlpha(selectedColor, 68)
            wave.setBorder(wave.borderWidth, mBorderColor)

//                mCobwebView.setSpiderColor(selectedColor);
        }
        color_picker_view.addOnColorSelectedListener {
            //mCobwebView.setSpiderColor(selectedColor);
        }
    }

    override fun initData() {

    }

    override fun onPause() {
        super.onPause()
        if (wave != null) {
            wave.cancel()
        }
    }

    override fun onResume() {
        super.onResume()
        if (wave != null) {
            wave.start()
        }
    }
}