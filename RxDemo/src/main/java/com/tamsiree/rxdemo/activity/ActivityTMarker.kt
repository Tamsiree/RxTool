package com.tamsiree.rxdemo.activity

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.tamsiree.rxdemo.R
import com.tamsiree.rxdemo.adapter.AdapterTMarker
import com.tamsiree.rxdemo.repository.RepositoryDummySwipe
import com.tamsiree.rxdemo.viewholder.ViewHolderSwipeable
import com.tamsiree.rxui.activity.ActivityBase
import com.tamsiree.rxui.view.mark.TMarker
import kotlinx.android.synthetic.main.activity_tmarker.*

class ActivityTMarker : ActivityBase() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tmarker)
    }

    override fun initView() {
        val tMarker = TMarker.with(covertConfig)
                .setIsActiveCallback {
                    it is ViewHolderSwipeable && repository.isActive(it.adapterPosition)
                }
                .doOnSwipe { viewHolder, _ ->
                    repository.toggleActiveState(viewHolder.adapterPosition)
                }
                .attachTo(recyclerView)

        recyclerView.adapter = AdapterTMarker(tMarker)
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    override fun initData() {

    }

    private val covertConfig = TMarker.Config(
            iconRes = R.drawable.ic_star_border_black_24dp,
            iconDefaultColorRes = android.R.color.black,
            actionColorRes = R.color.colorPrimary
    )

    private val repository = RepositoryDummySwipe()

}
