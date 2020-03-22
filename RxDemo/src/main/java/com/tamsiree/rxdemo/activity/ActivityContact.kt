package com.tamsiree.rxdemo.activity

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tamsiree.rxdemo.R
import com.tamsiree.rxkit.RxDeviceTool.setPortrait
import com.tamsiree.rxui.activity.ActivityBase
import com.tamsiree.rxui.model.ModelContactCity
import com.tamsiree.rxui.view.wavesidebar.ComparatorLetter
import com.tamsiree.rxui.view.wavesidebar.PinnedHeaderDecoration
import com.tamsiree.rxui.view.wavesidebar.adapter.AdapterContactCity
import kotlinx.android.synthetic.main.activity_contact.*
import java.util.*

/**
 * @author tamsiree
 */
class ActivityContact : ActivityBase() {

    private var mAdapterContactCity: AdapterContactCity? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact)
        setPortrait(this)
    }

    override fun initView() {
        rx_title.setLeftFinish(mContext)
        recycler_view.layoutManager = LinearLayoutManager(this)
        val decoration = PinnedHeaderDecoration()
        decoration.registerTypePinnedHeader(1) { parent, adapterPosition -> true }
        recycler_view.addItemDecoration(decoration)

        side_view.setOnTouchLetterChangeListener { letter ->
            val pos = mAdapterContactCity!!.getLetterPosition(letter)
            if (pos != -1) {
                recycler_view.scrollToPosition(pos)
                val mLayoutManager = recycler_view.layoutManager as LinearLayoutManager?
                mLayoutManager!!.scrollToPositionWithOffset(pos, 0)
            }
        }
    }

    override fun initData() {
        Thread(Runnable {
            val listType = object : TypeToken<ArrayList<ModelContactCity?>?>() {}.type
            val gson = Gson()
            val list = gson.fromJson<List<ModelContactCity>>(ModelContactCity.DATA, listType)
            Collections.sort(list, ComparatorLetter())
            runOnUiThread {
                mAdapterContactCity = AdapterContactCity(mContext, list)
                recycler_view.adapter = mAdapterContactCity
            }
        }).start()
    }

}