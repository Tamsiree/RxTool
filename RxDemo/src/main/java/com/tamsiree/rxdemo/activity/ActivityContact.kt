package com.tamsiree.rxdemo.activity

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tamsiree.rxdemo.R
import com.tamsiree.rxkit.RxDeviceTool.setPortrait
import com.tamsiree.rxui.activity.ActivityBase
import com.tamsiree.rxui.model.ModelContactCity
import com.tamsiree.rxui.view.RxTitle
import com.tamsiree.rxui.view.wavesidebar.ComparatorLetter
import com.tamsiree.rxui.view.wavesidebar.PinnedHeaderDecoration
import com.tamsiree.rxui.view.wavesidebar.WaveSideBarView
import com.tamsiree.rxui.view.wavesidebar.adapter.AdapterContactCity
import java.util.*

/**
 * @author tamsiree
 */
class ActivityContact : ActivityBase() {
    @JvmField
    @BindView(R.id.recycler_view)
    var mRecyclerView: RecyclerView? = null

    @JvmField
    @BindView(R.id.side_view)
    var mSideBarView: WaveSideBarView? = null

    @JvmField
    @BindView(R.id.rx_title)
    var mRxTitle: RxTitle? = null
    private var mAdapterContactCity: AdapterContactCity? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact)
        ButterKnife.bind(this)
        setPortrait(this)
        initView()
    }

    private fun initView() {
        mRxTitle!!.setLeftFinish(mContext)
        mRecyclerView!!.layoutManager = LinearLayoutManager(this)
        val decoration = PinnedHeaderDecoration()
        decoration.registerTypePinnedHeader(1) { parent, adapterPosition -> true }
        mRecyclerView!!.addItemDecoration(decoration)
        Thread(Runnable {
            val listType = object : TypeToken<ArrayList<ModelContactCity?>?>() {}.type
            val gson = Gson()
            val list = gson.fromJson<List<ModelContactCity>>(ModelContactCity.DATA, listType)
            Collections.sort(list, ComparatorLetter())
            runOnUiThread {
                mAdapterContactCity = AdapterContactCity(mContext, list)
                mRecyclerView!!.adapter = mAdapterContactCity
            }
        }).start()
        mSideBarView!!.setOnTouchLetterChangeListener { letter ->
            val pos = mAdapterContactCity!!.getLetterPosition(letter)
            if (pos != -1) {
                mRecyclerView!!.scrollToPosition(pos)
                val mLayoutManager = mRecyclerView!!.layoutManager as LinearLayoutManager?
                mLayoutManager!!.scrollToPositionWithOffset(pos, 0)
            }
        }
    }
}