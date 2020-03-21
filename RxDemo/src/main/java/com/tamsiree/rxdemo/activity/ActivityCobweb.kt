package com.tamsiree.rxdemo.activity

import android.os.Bundle
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import butterknife.BindView
import butterknife.ButterKnife
import com.tamsiree.rxdemo.R
import com.tamsiree.rxkit.RxDeviceTool.setPortrait
import com.tamsiree.rxkit.model.ModelSpider
import com.tamsiree.rxui.activity.ActivityBase
import com.tamsiree.rxui.view.RxCobwebView
import com.tamsiree.rxui.view.RxTitle
import com.tamsiree.rxui.view.colorpicker.ColorPickerView
import com.tamsiree.rxui.view.colorpicker.slider.AlphaSlider
import com.tamsiree.rxui.view.colorpicker.slider.LightnessSlider
import java.util.*

/**
 * @author tamsiree
 */
class ActivityCobweb : ActivityBase(), OnSeekBarChangeListener {
    @JvmField
    @BindView(R.id.rx_title)
    var mRxTitle: RxTitle? = null

    @JvmField
    @BindView(R.id.cobweb_view)
    var mCobwebView: RxCobwebView? = null

    @JvmField
    @BindView(R.id.seekbar_level)
    var mSeekbarLevel: SeekBar? = null

    @JvmField
    @BindView(R.id.seekbar_spider_number)
    var mSeekbarSpiderNumber: SeekBar? = null

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
    @BindView(R.id.color_picker_view_level)
    var mColorPickerViewLevel: ColorPickerView? = null

    @JvmField
    @BindView(R.id.v_lightness_slider_level)
    var mVLightnessSliderLevel: LightnessSlider? = null

    @JvmField
    @BindView(R.id.v_alpha_slider_level)
    var mVAlphaSliderLevel: AlphaSlider? = null
    private val nameStrs = arrayOf(
            "金钱", "能力", "美貌", "智慧", "交际",
            "口才", "力量", "智力", "体力", "体质",
            "敏捷", "精神", "耐力", "精通", "急速",
            "暴击", "回避", "命中", "跳跃", "反应",
            "幸运", "魅力", "感知", "活力", "意志"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //        RxBarTool.setTransparentStatusBar(mContext);
        setContentView(R.layout.activity_cobweb)
        ButterKnife.bind(this)
        setPortrait(this)
        mRxTitle!!.setLeftFinish(mContext)
        mSeekbarLevel!!.setOnSeekBarChangeListener(this)
        mSeekbarSpiderNumber!!.setOnSeekBarChangeListener(this)
        mColorPickerView!!.addOnColorChangedListener { selectedColor -> // Handle on color change
            mCobwebView!!.spiderColor = selectedColor
        }
        mColorPickerView!!.addOnColorSelectedListener {
            //mCobwebView.setSpiderColor(selectedColor);
        }
        mColorPickerViewLevel!!.addOnColorChangedListener { selectedColor -> mCobwebView!!.spiderLevelColor = selectedColor }
    }

    override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
        when (seekBar.id) {
            R.id.seekbar_level -> mCobwebView!!.spiderMaxLevel = progress + 1
            R.id.seekbar_spider_number -> {
                val number = progress + 1
                val modelSpiders: MutableList<ModelSpider> = ArrayList()
                var i = 0
                while (i < number) {
                    modelSpiders.add(ModelSpider(nameStrs[i], (1 + Random().nextInt(mCobwebView!!.spiderMaxLevel)).toFloat()))
                    i++
                }
                mCobwebView!!.spiderList = modelSpiders
            }
            else -> {
            }
        }
    }

    override fun onStartTrackingTouch(seekBar: SeekBar) {}
    override fun onStopTrackingTouch(seekBar: SeekBar) {}
}