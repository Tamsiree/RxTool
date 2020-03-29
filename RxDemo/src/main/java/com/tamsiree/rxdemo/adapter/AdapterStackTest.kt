package com.tamsiree.rxdemo.adapter

import android.content.Context
import android.graphics.PorterDuff
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.tamsiree.rxdemo.R
import com.tamsiree.rxdemo.adapter.AdapterStackTest.ColorItemLargeHeaderViewHolder
import com.tamsiree.rxui.view.cardstack.RxCardStackView
import com.tamsiree.rxui.view.cardstack.tools.RxAdapterStack

/**
 * @author tamsiree
 */
class AdapterStackTest(context: Context) : RxAdapterStack<Int>(context) {
    override fun bindView(data: Int, position: Int, holder: RxCardStackView.ViewHolder) {
        if (holder is ColorItemLargeHeaderViewHolder) {
            holder.onBind(data, position)
        }
        if (holder is ColorItemWithNoHeaderViewHolder) {
            holder.onBind(data, position)
        }
        if (holder is ColorItemViewHolder) {
            holder.onBind(data, position)
        }
    }

    override fun onCreateView(parent: ViewGroup, viewType: Int): RxCardStackView.ViewHolder {
        val view: View
        return when (viewType) {
            R.layout.list_card_item_larger_header -> {
                view = layoutInflater.inflate(R.layout.list_card_item_larger_header, parent, false)
                ColorItemLargeHeaderViewHolder(view)
            }
            R.layout.list_card_item_with_no_header -> {
                view = layoutInflater.inflate(R.layout.list_card_item_with_no_header, parent, false)
                ColorItemWithNoHeaderViewHolder(view)
            }
            else -> {
                view = layoutInflater.inflate(R.layout.list_card_item, parent, false)
                ColorItemViewHolder(view)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 6) { //TODO TEST LARGER ITEM
            R.layout.list_card_item_larger_header
        } else if (position == 10) {
            R.layout.list_card_item_with_no_header
        } else {
            R.layout.list_card_item
        }
    }

    internal class ColorItemViewHolder(view: View) : RxCardStackView.ViewHolder(view) {
        var mLayout: View
        var mContainerContent: View
        var mTextTitle: TextView
        override fun onItemExpand(b: Boolean) {
            mContainerContent.visibility = if (b) View.VISIBLE else View.GONE
        }

        fun onBind(data: Int?, position: Int) {
            mLayout.background.setColorFilter(ContextCompat.getColor(context, data!!), PorterDuff.Mode.SRC_IN)
            mTextTitle.text = position.toString()
        }

        init {
            mLayout = view.findViewById(R.id.frame_list_card_item)
            mContainerContent = view.findViewById(R.id.container_list_content)
            mTextTitle = view.findViewById<View>(R.id.text_list_card_title) as TextView
        }
    }

    internal class ColorItemWithNoHeaderViewHolder(view: View) : RxCardStackView.ViewHolder(view) {
        var mLayout: View
        var mTextTitle: TextView
        override fun onItemExpand(b: Boolean) {}
        fun onBind(data: Int?, position: Int) {
            mLayout.background.setColorFilter(ContextCompat.getColor(context, data!!), PorterDuff.Mode.SRC_IN)
            mTextTitle.text = position.toString()
        }

        init {
            mLayout = view.findViewById(R.id.frame_list_card_item)
            mTextTitle = view.findViewById<View>(R.id.text_list_card_title) as TextView
        }
    }

    internal class ColorItemLargeHeaderViewHolder(view: View) : RxCardStackView.ViewHolder(view) {
        var mLayout: View
        var mContainerContent: View
        var mTextTitle: TextView
        override fun onItemExpand(b: Boolean) {
            mContainerContent.visibility = if (b) View.VISIBLE else View.GONE
        }

        override fun onAnimationStateChange(state: Int, willBeSelect: Boolean) {
            super.onAnimationStateChange(state, willBeSelect)
            if (state == RxCardStackView.ANIMATION_STATE_START && willBeSelect) {
                onItemExpand(true)
            }
            if (state == RxCardStackView.ANIMATION_STATE_END && !willBeSelect) {
                onItemExpand(false)
            }
        }

        fun onBind(data: Int?, position: Int) {
            mLayout.background.setColorFilter(ContextCompat.getColor(context, data!!), PorterDuff.Mode.SRC_IN)
            mTextTitle.text = position.toString()
            itemView.findViewById<View>(R.id.text_view).setOnClickListener { (itemView.parent as RxCardStackView).performItemClick(this@ColorItemLargeHeaderViewHolder) }
        }

        init {
            mLayout = view.findViewById(R.id.frame_list_card_item)
            mContainerContent = view.findViewById(R.id.container_list_content)
            mTextTitle = view.findViewById<View>(R.id.text_list_card_title) as TextView
        }
    }
}