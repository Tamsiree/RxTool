package com.tamsiree.rxdemo.activity

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.tamsiree.rxdemo.R
import com.tamsiree.rxkit.RxDeviceTool.setPortrait
import com.tamsiree.rxkit.RxImageTool.changeColorAlpha
import com.tamsiree.rxui.activity.ActivityBase
import com.tamsiree.rxui.view.RxTitle
import com.tamsiree.rxui.view.colorpicker.ColorPickerView
import com.tamsiree.rxui.view.colorpicker.slider.AlphaSlider
import com.tamsiree.rxui.view.colorpicker.slider.LightnessSlider
import com.tamsiree.rxui.view.waveview.RxWaveView

/**
 * @author tamsiree
 */
class ActivityRxWaveView : ActivityBase() {
    @JvmField
    @BindView(R.id.wave)
    var mWave: RxWaveView? = null

    @JvmField
    @BindView(R.id.border)
    var mBorder: TextView? = null

    @JvmField
    @BindView(R.id.seekBar)
    var mSeekBar: SeekBar? = null

    @JvmField
    @BindView(R.id.shape)
    var mShape: TextView? = null

    @JvmField
    @BindView(R.id.shapeCircle)
    var mShapeCircle: RadioButton? = null

    @JvmField
    @BindView(R.id.shapeSquare)
    var mShapeSquare: RadioButton? = null

    @JvmField
    @BindView(R.id.shapeChoice)
    var mShapeChoice: RadioGroup? = null

    @JvmField
    @BindView(R.id.color_picker_view)
    var mColorPickerView: ColorPickerView? = null

    @JvmField
    @BindView(R.id.v_lightness_slider)
    var mVLightnessSlider: LightnessSlider? = null

    @JvmField
    @BindView(R.id.v_alpha_slider)
    var mVAlphaSlider: AlphaSlider? = null

    @JvmField
    @BindView(R.id.rx_title)
    var mRxTitle: RxTitle? = null

    //    private RxWaveHelper mWaveHelper;
    private var mBorderColor = Color.parseColor("#4489CFF0")
    private var mBorderWidth = 10
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rx_wave_view)
        ButterKnife.bind(this)
        setPortrait(this)
        mRxTitle!!.setLeftFinish(this)

//        mWave.setBorder(mBorderWidth, mBorderColor);
//        mWaveHelper = new RxWaveHelper(mWave);
        (findViewById<View>(R.id.shapeChoice) as RadioGroup)
                .setOnCheckedChangeListener { radioGroup, i ->
                    when (i) {
                        R.id.shapeCircle -> mWave!!.shapeType = RxWaveView.ShapeType.CIRCLE
                        R.id.shapeSquare -> mWave!!.shapeType = RxWaveView.ShapeType.SQUARE
                        else -> {
                        }
                    }
                }
        (findViewById<View>(R.id.seekBar) as SeekBar)
                .setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
                    override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                        mBorderWidth = i
                        mWave!!.setBorder(mBorderWidth, mWave!!.borderColor)
                    }

                    override fun onStartTrackingTouch(seekBar: SeekBar) {}
                    override fun onStopTrackingTouch(seekBar: SeekBar) {}
                })
        mColorPickerView!!.addOnColorChangedListener { selectedColor ->
            // Handle on color change
            Log.d("selectedColor", "selectedColor: $selectedColor")
            mWave!!.setWaveColor(changeColorAlpha(selectedColor, 40),
                    changeColorAlpha(selectedColor, 60))
            mBorderColor = changeColorAlpha(selectedColor, 68)
            mWave!!.setBorder(mWave!!.borderWidth, mBorderColor)

//                mCobwebView.setSpiderColor(selectedColor);
        }
        mColorPickerView!!.addOnColorSelectedListener {
            //mCobwebView.setSpiderColor(selectedColor);
        }
    }

    override fun onPause() {
        super.onPause()
        if (mWave != null) {
            mWave!!.cancel()
        }
    }

    override fun onResume() {
        super.onResume()
        if (mWave != null) {
            mWave!!.start()
        }
    }
}