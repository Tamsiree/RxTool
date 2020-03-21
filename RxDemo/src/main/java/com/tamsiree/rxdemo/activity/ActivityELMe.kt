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
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
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
import com.tamsiree.rxui.view.RxTitle
import java.util.*

/**
 * @author tamsiree
 */
class ActivityELMe : ActivityBase(), onItemSelectedListener, ShopCartInterface, ShopCartDialogImp {
    @JvmField
    @BindView(R.id.rx_title)
    var mRxTitle: RxTitle? = null

    @JvmField
    @BindView(R.id.shopping_cart_total_tv)
    var totalPriceTextView: TextView? = null

    @JvmField
    @BindView(R.id.shopping_cart_bottom)
    var mShoppingCartBottom: LinearLayout? = null

    @JvmField
    @BindView(R.id.left_menu)
    var mLeftMenu //左侧菜单栏
            : RecyclerView? = null

    @JvmField
    @BindView(R.id.right_menu)
    var mRightMenu //右侧菜单栏
            : RecyclerView? = null

    @JvmField
    @BindView(R.id.right_menu_tv)
    var headerView: TextView? = null

    @JvmField
    @BindView(R.id.right_menu_item)
    var headerLayout //右侧菜单栏最上面的菜单
            : LinearLayout? = null

    @JvmField
    @BindView(R.id.shopping_cart)
    var mShoppingCart: ImageView? = null

    @JvmField
    @BindView(R.id.shopping_cart_layout)
    var mShoppingCartLayout: FrameLayout? = null

    @JvmField
    @BindView(R.id.shopping_cart_total_num)
    var totalPriceNumTextView: TextView? = null

    @JvmField
    @BindView(R.id.main_layout)
    var mMainLayout: RelativeLayout? = null
    private var headMenu: ModelDishMenu? = null
    private var leftAdapter: AdapterLeftMenu? = null
    private var rightAdapter: AdapterRightDish? = null
    private var mModelDishMenuList //数据源
            : ArrayList<ModelDishMenu>? = null
    private var leftClickType = false //左侧菜单点击引发的右侧联动
    private var mModelShopCart: ModelShopCart? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_elme)
        ButterKnife.bind(this)
        setPortrait(this)
        mRxTitle!!.setLeftFinish(mContext)
        initData()
        initView()
        initAdapter()
    }

    private fun initView() {
        mLeftMenu!!.layoutManager = LinearLayoutManager(this)
        mRightMenu!!.layoutManager = LinearLayoutManager(this)
        mRightMenu!!.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!recyclerView.canScrollVertically(1)) { //无法下滑
                    showHeadView()
                    return
                }
                var underView: View? = null
                underView = if (dy > 0) {
                    mRightMenu!!.findChildViewUnder(headerLayout!!.x, headerLayout!!.measuredHeight + 1.toFloat())
                } else {
                    mRightMenu!!.findChildViewUnder(headerLayout!!.x, 0f)
                }
                if (underView != null && underView.contentDescription != null) {
                    val position = underView.contentDescription.toString().toInt()
                    val menu = rightAdapter!!.getMenuOfMenuByPosition(position)
                    if (leftClickType || menu.menuName != headMenu!!.menuName) {
                        if (dy > 0 && headerLayout!!.translationY <= 1 && headerLayout!!.translationY >= -1 * headerLayout!!.measuredHeight * 4 / 5 && !leftClickType) { // underView.getTop()>9
                            val dealtY = underView.top - headerLayout!!.measuredHeight
                            headerLayout!!.translationY = dealtY.toFloat()
                            //                            Log.e(TAG, "onScrolled: "+headerLayout.getTranslationY()+"   "+headerLayout.getBottom()+"  -  "+headerLayout.getMeasuredHeight() );
                        } else if (dy < 0 && headerLayout!!.translationY <= 0 && !leftClickType) {
                            headerView!!.text = menu.menuName
                            val dealtY = underView.bottom - headerLayout!!.measuredHeight
                            headerLayout!!.translationY = dealtY.toFloat()
                            //                            Log.e(TAG, "onScrolled: "+headerLayout.getTranslationY()+"   "+headerLayout.getBottom()+"  -  "+headerLayout.getMeasuredHeight() );
                        } else {
                            headerLayout!!.translationY = 0f
                            headMenu = menu
                            headerView!!.text = headMenu!!.menuName
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
        mShoppingCartLayout!!.setOnClickListener { view -> showCart(view) }
    }

    private fun initData() {
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
    }

    private fun initAdapter() {
        leftAdapter = AdapterLeftMenu(this, mModelDishMenuList)
        rightAdapter = AdapterRightDish(this, mModelDishMenuList, mModelShopCart)
        mRightMenu!!.adapter = rightAdapter
        mLeftMenu!!.adapter = leftAdapter
        leftAdapter!!.addItemSelectedListener(this)
        rightAdapter!!.shopCartInterface = this
        initHeadView()
    }

    private fun initHeadView() {
        headMenu = rightAdapter!!.getMenuOfMenuByPosition(0)
        headerLayout!!.contentDescription = "0"
        headerView!!.text = headMenu?.menuName
    }

    override fun onDestroy() {
        super.onDestroy()
        leftAdapter!!.removeItemSelectedListener(this)
    }

    private fun showHeadView() {
        headerLayout!!.translationY = 0f
        val underView = mRightMenu!!.findChildViewUnder(headerView!!.x, 0f)
        if (underView != null && underView.contentDescription != null) {
            val position = underView.contentDescription.toString().toInt()
            val menu = rightAdapter!!.getMenuOfMenuByPosition(position + 1)
            headMenu = menu
            headerView!!.text = headMenu!!.menuName
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
        val layoutManager = mRightMenu!!.layoutManager as LinearLayoutManager?
        layoutManager!!.scrollToPositionWithOffset(sum, 0)
        leftClickType = true
    }

    @SuppressLint("ObjectAnimatorBinding")
    override fun add(view: View?, position: Int) {
        val addLocation = IntArray(2)
        val cartLocation = IntArray(2)
        val recycleLocation = IntArray(2)
        view!!.getLocationInWindow(addLocation)
        mShoppingCart!!.getLocationInWindow(cartLocation)
        mRightMenu!!.getLocationInWindow(recycleLocation)
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
        mMainLayout!!.addView(rxFakeAddImageView)
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
                mMainLayout!!.removeView(rxFakeAddImageView)
            }

            override fun onAnimationCancel(animator: Animator) {}
            override fun onAnimationRepeat(animator: Animator) {}
        })
        val scaleAnimatorX: ObjectAnimator = ObjectAnimator.ofFloat(mShoppingCart, "scaleX", 0.6f, 1.0f)
        val scaleAnimatorY: ObjectAnimator = ObjectAnimator.ofFloat(mShoppingCart, "scaleY", 0.6f, 1.0f)
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
            totalPriceTextView!!.visibility = View.VISIBLE
            totalPriceTextView!!.text = "¥ " + mModelShopCart!!.shoppingTotalPrice
            totalPriceNumTextView!!.visibility = View.VISIBLE
            totalPriceNumTextView!!.text = "" + mModelShopCart!!.shoppingAccount
        } else {
            totalPriceTextView!!.visibility = View.GONE
            totalPriceNumTextView!!.visibility = View.GONE
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

    companion object {
        private const val TAG = "MainActivity"
    }
}