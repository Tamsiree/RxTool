package com.tamsiree.rxdemo.activity

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.graphics.PointF
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.view.animation.AccelerateInterpolator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tamsiree.rxdemo.R
import com.tamsiree.rxdemo.adapter.AdapterLeftMenu
import com.tamsiree.rxdemo.adapter.AdapterLeftMenu.onItemSelectedListener
import com.tamsiree.rxdemo.adapter.AdapterRightDish
import com.tamsiree.rxdemo.interfaces.ShopCartInterface
import com.tamsiree.rxdemo.model.ModelDish
import com.tamsiree.rxdemo.model.ModelDishMenu
import com.tamsiree.rxdemo.model.ModelShopCart
import com.tamsiree.rxdemo.view.RxDialogShopCart
import com.tamsiree.rxdemo.view.RxDialogShopCart.ShopCartDialogImp
import com.tamsiree.rxdemo.view.RxFakeAddImageView
import com.tamsiree.rxdemo.view.RxPointFTypeEvaluator
import com.tamsiree.rxkit.RxDeviceTool.setPortrait
import com.tamsiree.rxui.activity.ActivityBase
import kotlinx.android.synthetic.main.activity_elme.*
import kotlinx.android.synthetic.main.right_menu_item.*
import java.util.*

/**
 * @author tamsiree
 */
class ActivityELMe : ActivityBase(), onItemSelectedListener, ShopCartInterface, ShopCartDialogImp {

    private var headMenu: ModelDishMenu? = null
    private var leftAdapter: AdapterLeftMenu? = null
    private var rightAdapter: AdapterRightDish? = null

    //数据源
    private var mModelDishMenuList: ArrayList<ModelDishMenu>? = null
    private var leftClickType = false //左侧菜单点击引发的右侧联动
    private var mModelShopCart: ModelShopCart? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_elme)
        setPortrait(this)
    }

    override fun initView() {
        rx_title.setLeftFinish(mContext)

        left_menu.layoutManager = LinearLayoutManager(this)
        right_menu.layoutManager = LinearLayoutManager(this)
        right_menu.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!recyclerView.canScrollVertically(1)) { //无法下滑
                    showHeadView()
                    return
                }
                var underView: View? = null
                underView = if (dy > 0) {
                    right_menu.findChildViewUnder(right_menu_item.x, right_menu_item.measuredHeight + 1.toFloat())
                } else {
                    right_menu.findChildViewUnder(right_menu_item.x, 0f)
                }
                if (underView != null && underView.contentDescription != null) {
                    val position = underView.contentDescription.toString().toInt()
                    val menu = rightAdapter!!.getMenuOfMenuByPosition(position)
                    if (leftClickType || menu.menuName != headMenu!!.menuName) {
                        if (dy > 0 && right_menu_item.translationY <= 1 && right_menu_item.translationY >= -1 * right_menu_item.measuredHeight * 4 / 5 && !leftClickType) { // underView.getTop()>9
                            val dealtY = underView.top - right_menu_item.measuredHeight
                            right_menu_item.translationY = dealtY.toFloat()
                            //                            Log.e(TAG, "onScrolled: "+headerLayout.getTranslationY()+"   "+headerLayout.getBottom()+"  -  "+headerLayout.getMeasuredHeight() );
                        } else if (dy < 0 && right_menu_item.translationY <= 0 && !leftClickType) {
                            right_menu_tv.text = menu.menuName
                            val dealtY = underView.bottom - right_menu_item.measuredHeight
                            right_menu_item.translationY = dealtY.toFloat()
                            //                            Log.e(TAG, "onScrolled: "+headerLayout.getTranslationY()+"   "+headerLayout.getBottom()+"  -  "+headerLayout.getMeasuredHeight() );
                        } else {
                            right_menu_item.translationY = 0f
                            headMenu = menu
                            right_menu_tv.text = headMenu!!.menuName
                            for (i in mModelDishMenuList!!.indices) {
                                if (mModelDishMenuList!![i] == headMenu) {
                                    leftAdapter!!.selectedNum = i
                                    break
                                }
                            }
                            if (leftClickType) {
                                leftClickType = false
                            }
                            Log.e(TAG, "onScrolled: " + menu.menuName)
                        }
                    }
                }
            }
        })
        shopping_cart_layout.setOnClickListener { view -> showCart(view) }
    }

    override fun initData() {
        mModelShopCart = ModelShopCart()
        mModelDishMenuList = ArrayList()
        val dishs1 = ArrayList<ModelDish>()
        dishs1.add(ModelDish("面包", 1.0, 10))
        dishs1.add(ModelDish("蛋挞", 1.0, 10))
        dishs1.add(ModelDish("牛奶", 1.0, 10))
        dishs1.add(ModelDish("肠粉", 1.0, 10))
        dishs1.add(ModelDish("绿茶饼", 1.0, 10))
        dishs1.add(ModelDish("花卷", 1.0, 10))
        dishs1.add(ModelDish("包子", 1.0, 10))
        val breakfast = ModelDishMenu("早点", dishs1)
        val dishs2 = ArrayList<ModelDish>()
        dishs2.add(ModelDish("粥", 1.0, 10))
        dishs2.add(ModelDish("炒饭", 1.0, 10))
        dishs2.add(ModelDish("炒米粉", 1.0, 10))
        dishs2.add(ModelDish("炒粿条", 1.0, 10))
        dishs2.add(ModelDish("炒牛河", 1.0, 10))
        dishs2.add(ModelDish("炒菜", 1.0, 10))
        val launch = ModelDishMenu("午餐", dishs2)
        val dishs3 = ArrayList<ModelDish>()
        dishs3.add(ModelDish("淋菜", 1.0, 10))
        dishs3.add(ModelDish("川菜", 1.0, 10))
        dishs3.add(ModelDish("湘菜", 1.0, 10))
        dishs3.add(ModelDish("粤菜", 1.0, 10))
        dishs3.add(ModelDish("赣菜", 1.0, 10))
        dishs3.add(ModelDish("东北菜", 1.0, 10))
        val evening = ModelDishMenu("晚餐", dishs3)
        val dishs4 = ArrayList<ModelDish>()
        dishs4.add(ModelDish("淋菜", 1.0, 10))
        dishs4.add(ModelDish("川菜", 1.0, 10))
        dishs4.add(ModelDish("湘菜", 1.0, 10))
        dishs4.add(ModelDish("湘菜", 1.0, 10))
        dishs4.add(ModelDish("湘菜1", 1.0, 10))
        dishs4.add(ModelDish("湘菜2", 1.0, 10))
        dishs4.add(ModelDish("湘菜3", 1.0, 10))
        dishs4.add(ModelDish("湘菜4", 1.0, 10))
        dishs4.add(ModelDish("湘菜5", 1.0, 10))
        dishs4.add(ModelDish("湘菜6", 1.0, 10))
        dishs4.add(ModelDish("湘菜7", 1.0, 10))
        dishs4.add(ModelDish("湘菜8", 1.0, 10))
        dishs4.add(ModelDish("粤菜", 1.0, 10))
        dishs4.add(ModelDish("赣菜", 1.0, 10))
        dishs4.add(ModelDish("东北菜", 1.0, 10))
        val menu1 = ModelDishMenu("夜宵", dishs4)
        mModelDishMenuList!!.add(breakfast)
        mModelDishMenuList!!.add(launch)
        mModelDishMenuList!!.add(evening)
        mModelDishMenuList!!.add(menu1)

        initAdapter()

    }

    private fun initAdapter() {
        leftAdapter = AdapterLeftMenu(this, mModelDishMenuList)
        rightAdapter = AdapterRightDish(this, mModelDishMenuList, mModelShopCart)
        right_menu.adapter = rightAdapter
        left_menu.adapter = leftAdapter
        leftAdapter!!.addItemSelectedListener(this)
        rightAdapter!!.shopCartInterface = this
        initHeadView()
    }

    private fun initHeadView() {
        headMenu = rightAdapter!!.getMenuOfMenuByPosition(0)
        right_menu_item.contentDescription = "0"
        right_menu_tv.text = headMenu?.menuName
    }

    override fun onDestroy() {
        super.onDestroy()
        leftAdapter!!.removeItemSelectedListener(this)
    }

    private fun showHeadView() {
        right_menu_item.translationY = 0f
        val underView = right_menu.findChildViewUnder(right_menu_tv.x, 0f)
        if (underView != null && underView.contentDescription != null) {
            val position = underView.contentDescription.toString().toInt()
            val menu = rightAdapter!!.getMenuOfMenuByPosition(position + 1)
            headMenu = menu
            right_menu_tv.text = headMenu!!.menuName
            for (i in mModelDishMenuList!!.indices) {
                if (mModelDishMenuList!![i] == headMenu) {
                    leftAdapter!!.selectedNum = i
                    break
                }
            }
        }
    }

    override fun onLeftItemSelected(position: Int, menu: ModelDishMenu) {
        var sum = 0
        for (i in 0 until position) {
            sum += mModelDishMenuList!![i].modelDishList!!.size + 1
        }
        val layoutManager = right_menu.layoutManager as LinearLayoutManager?
        layoutManager!!.scrollToPositionWithOffset(sum, 0)
        leftClickType = true
    }

    @SuppressLint("ObjectAnimatorBinding")
    override fun add(view: View?, position: Int) {
        val addLocation = IntArray(2)
        val cartLocation = IntArray(2)
        val recycleLocation = IntArray(2)
        view!!.getLocationInWindow(addLocation)
        shopping_cart.getLocationInWindow(cartLocation)
        right_menu.getLocationInWindow(recycleLocation)
        val startP = PointF()
        val endP = PointF()
        val controlP = PointF()
        startP.x = addLocation[0].toFloat()
        startP.y = addLocation[1] - recycleLocation[1].toFloat()
        endP.x = cartLocation[0].toFloat()
        endP.y = cartLocation[1] - recycleLocation[1].toFloat()
        controlP.x = endP.x
        controlP.y = startP.y
        val rxFakeAddImageView = RxFakeAddImageView(this)
        main_layout.addView(rxFakeAddImageView)
        rxFakeAddImageView.setImageResource(R.drawable.ic_add_circle_blue_700_36dp)
        rxFakeAddImageView.layoutParams.width = resources.getDimensionPixelSize(R.dimen.item_dish_circle_size)
        rxFakeAddImageView.layoutParams.height = resources.getDimensionPixelSize(R.dimen.item_dish_circle_size)
        rxFakeAddImageView.visibility = View.VISIBLE
        val addAnimator = ObjectAnimator.ofObject(rxFakeAddImageView, "mPointF",
                RxPointFTypeEvaluator(controlP), startP, endP)
        addAnimator.interpolator = AccelerateInterpolator()
        addAnimator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animator: Animator) {
                rxFakeAddImageView.visibility = View.VISIBLE
            }

            override fun onAnimationEnd(animator: Animator) {
                rxFakeAddImageView.visibility = View.GONE
                main_layout.removeView(rxFakeAddImageView)
            }

            override fun onAnimationCancel(animator: Animator) {}
            override fun onAnimationRepeat(animator: Animator) {}
        })
        val scaleAnimatorX: ObjectAnimator = ObjectAnimator.ofFloat(shopping_cart, "scaleX", 0.6f, 1.0f)
        val scaleAnimatorY: ObjectAnimator = ObjectAnimator.ofFloat(shopping_cart, "scaleY", 0.6f, 1.0f)
        scaleAnimatorX.interpolator = AccelerateInterpolator()
        scaleAnimatorY.interpolator = AccelerateInterpolator()
        val animatorSet = AnimatorSet()
        animatorSet.play(scaleAnimatorX).with(scaleAnimatorY).after(addAnimator)
        animatorSet.duration = 800
        animatorSet.start()
        showTotalPrice()
    }

    override fun remove(view: View?, position: Int) {
        showTotalPrice()
    }

    private fun showTotalPrice() {
        if (mModelShopCart != null && mModelShopCart!!.shoppingTotalPrice > 0) {
            shopping_cart_total_tv.visibility = View.VISIBLE
            shopping_cart_total_tv.text = "¥ " + mModelShopCart!!.shoppingTotalPrice
            shopping_cart_total_num.visibility = View.VISIBLE
            shopping_cart_total_num.text = "" + mModelShopCart!!.shoppingAccount
        } else {
            shopping_cart_total_tv.visibility = View.GONE
            shopping_cart_total_num.visibility = View.GONE
        }
    }

    private fun showCart(view: View) {
        if (mModelShopCart != null && mModelShopCart!!.shoppingAccount > 0) {
            val dialog = RxDialogShopCart(this, mModelShopCart, R.style.cartdialog)
            val window = dialog.window
            dialog.shopCartDialogImp = this
            dialog.setCanceledOnTouchOutside(true)
            dialog.setCancelable(true)
            dialog.show()
            val params = window!!.attributes
            params.width = WindowManager.LayoutParams.MATCH_PARENT
            params.height = WindowManager.LayoutParams.WRAP_CONTENT
            params.gravity = Gravity.BOTTOM
            params.dimAmount = 0.5f
            window.attributes = params
        }
    }

    override fun dialogDismiss() {
        showTotalPrice()
        rightAdapter!!.notifyDataSetChanged()
    }

}