package com.tamsiree.rxdemo.view

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.tamsiree.rxdemo.R
import com.tamsiree.rxdemo.adapter.AdapterPopupDish
import com.tamsiree.rxdemo.interfaces.ShopCartInterface
import com.tamsiree.rxdemo.model.ModelShopCart
import kotlinx.android.synthetic.main.cart_popupview.*

/**
 * @author tamsiree
 * @date 16-12-22
 */
class RxDialogShopCart(context: Context?, private val mModelShopCart: ModelShopCart?, themeResId: Int) : Dialog(context!!, themeResId), View.OnClickListener, ShopCartInterface {

    var shopCartDialogImp: ShopCartDialogImp? = null
    override fun onCreate(savedInstanceState: Bundle) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.cart_popupview)

        shopping_cart_layout?.setOnClickListener(this)
        shopping_cart_bottom?.setOnClickListener(this)
        clear_layout.setOnClickListener(this)
        recycleview.layoutManager = LinearLayoutManager(context)
        val dishAdapter = AdapterPopupDish(context, mModelShopCart!!)
        recycleview.adapter = dishAdapter
        dishAdapter.shopCartInterface = this
        showTotalPrice()
    }

    override fun show() {
        super.show()
        animationShow(500)
    }

    override fun dismiss() {
        animationHide(500)
    }

    @SuppressLint("SetTextI18n")
    private fun showTotalPrice() {
        if (mModelShopCart != null && mModelShopCart.shoppingTotalPrice > 0) {
            shopping_cart_total_tv!!.visibility = View.VISIBLE
            shopping_cart_total_tv!!.text = context.resources.getString(R.string.rmb) + " " + mModelShopCart.shoppingTotalPrice
            shopping_cart_total_num!!.visibility = View.VISIBLE
            shopping_cart_total_num!!.text = "" + mModelShopCart.shoppingAccount
        } else {
            shopping_cart_total_tv!!.visibility = View.GONE
            shopping_cart_total_num!!.visibility = View.GONE
        }
    }

    private fun animationShow(mDuration: Int) {
        val animatorSet = AnimatorSet()
        animatorSet.playTogether(
                ObjectAnimator.ofFloat(linearlayout, "translationY", 1000f, 0f).setDuration(mDuration.toLong())
        )
        animatorSet.start()
    }

    private fun animationHide(mDuration: Int) {
        val animatorSet = AnimatorSet()
        animatorSet.playTogether(
                ObjectAnimator.ofFloat(linearlayout, "translationY", 0f, 1000f).setDuration(mDuration.toLong())
        )
        animatorSet.start()
        if (shopCartDialogImp != null) {
            shopCartDialogImp!!.dialogDismiss()
        }
        animatorSet.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animator: Animator) {}
            override fun onAnimationEnd(animator: Animator) {
                super@RxDialogShopCart.dismiss()
            }

            override fun onAnimationCancel(animator: Animator) {}
            override fun onAnimationRepeat(animator: Animator) {}
        })
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.shopping_cart_bottom, R.id.shopping_cart_layout -> dismiss()
            R.id.clear_layout -> clear()
        }
    }

    override fun add(view: View?, position: Int) {
        showTotalPrice()
    }

    override fun remove(view: View?, position: Int) {
        showTotalPrice()
        if (mModelShopCart!!.shoppingAccount == 0) {
            dismiss()
        }
    }

    interface ShopCartDialogImp {
        fun dialogDismiss()
    }

    fun clear() {
        mModelShopCart!!.clear()
        showTotalPrice()
        if (mModelShopCart.shoppingAccount == 0) {
            dismiss()
        }
    }

}