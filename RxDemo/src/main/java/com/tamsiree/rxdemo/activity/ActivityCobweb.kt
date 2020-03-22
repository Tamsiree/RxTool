package com.tamsiree.rxdemo.activity

import android.os.Bundle
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import com.tamsiree.rxdemo.R
import com.tamsiree.rxkit.RxDeviceTool.setPortrait
import com.tamsiree.rxkit.model.ModelSpider
import com.tamsiree.rxui.activity.ActivityBase
import kotlinx.android.synthetic.main.activity_cobweb.*
import java.util.*

/**
 * @author tamsiree
 */
class ActivityCobweb : ActivityBase(), OnSeekBarChangeListener {

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
        setPortrait(this)

    }

    override fun initView() {
        rx_title.setLeftFinish(mContext)
        seekbar_level.setOnSeekBarChangeListener(this)
        seekbar_spider_number.setOnSeekBarChangeListener(this)
        color_picker_view.addOnColorChangedListener { selectedColor -> // Handle on color change
            cobweb_view.spiderColor = selectedColor
        }
        color_picker_view.addOnColorSelectedListener {
            //mCobwebView.setSpiderColor(selectedColor);
        }
        color_picker_view_level.addOnColorChangedListener { selectedColor -> cobweb_view.spiderLevelColor = selectedColor }
    }

    override fun initData() {

    }

    override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
        when (seekBar.id) {
            R.id.seekbar_level -> cobweb_view.spiderMaxLevel = progress + 1
            R.id.seekbar_spider_number -> {
                val number = progress + 1
                val modelSpiders: MutableList<ModelSpider> = ArrayList()
                var i = 0
                while (i < number) {
                    modelSpiders.add(ModelSpider(nameStrs[i], (1 + Random().nextInt(cobweb_view.spiderMaxLevel)).toFloat()))
                    i++
                }
                cobweb_view.spiderList = modelSpiders
            }
            else -> {
            }
        }
    }

    override fun onStartTrackingTouch(seekBar: SeekBar) {}
    override fun onStopTrackingTouch(seekBar: SeekBar) {}
}