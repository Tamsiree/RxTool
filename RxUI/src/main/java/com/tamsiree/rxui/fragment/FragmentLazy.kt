package com.tamsiree.rxui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

/**
 *
 * 若需要采用Lazy方式加载的Fragment，初始化内容放到initData实现
 * 若不需要Lazy加载则initData方法内留空,初始化内容放到initViews即可
 *
 * 注意事项 1:
 * 如果是与ViewPager一起使用，调用的是setUserVisibleHint。
 *
 * 注意事项 2:
 * 如果是通过FragmentTransaction的show和hide的方法来控制显示，调用的是onHiddenChanged.
 * 针对初始就show的Fragment 为了触发onHiddenChanged事件 达到lazy效果 需要先hide再show
 *
 * @author tamsiree
 * @date 2015/11/21.
 */
abstract class FragmentLazy : Fragment() {
    /**
     * 是否可见状态
     */
    private var isFragmentVisible: Boolean = false

    /**
     * 标志位，View已经初始化完成。
     */
    private var isPrepared = false

    /**
     * 是否第一次加载
     */
    private var isFirstLoad = true
    lateinit var mContext: FragmentActivity
    var rootView: View? = null
        private set

    override fun onCreateView(layoutInflater: LayoutInflater, viewGroup: ViewGroup?, savedInstanceState: Bundle?): View? {
        // 若 viewpager 不设置 setOffscreenPageLimit 或设置数量不够
        // 销毁的Fragment onCreateView 每次都会执行(但实体类没有从内存销毁)
        // 导致initData反复执行,所以这里注释掉
        // isFirstLoad = true;

        // 取消 isFirstLoad = true的注释 , 因为上述的initData本身就是应该执行的
        // onCreateView执行 证明被移出过FragmentManager initData确实要执行.
        // 如果这里有数据累加的Bug 请在initViews方法里初始化您的数据 比如 list.clear();
        mContext = this.activity!!
        isFirstLoad = true
        rootView = inflateView(layoutInflater, viewGroup, savedInstanceState)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        isPrepared = true
        lazyLoad()
    }

    /**
     * 如果是与ViewPager一起使用，调用的是setUserVisibleHint
     *
     * @param isVisibleToUser 是否显示出来了
     */
    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (userVisibleHint) {
            isFragmentVisible = true
            onVisible()
        } else {
            isFragmentVisible = false
            onInvisible()
        }
    }

    /**
     * 如果是通过FragmentTransaction的show和hide的方法来控制显示，调用的是onHiddenChanged.
     * 若是初始就show的Fragment 为了触发该事件 需要先hide再show
     *
     * @param hidden hidden True if the fragment is now hidden, false if it is not
     * visible.
     */
    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden) {
            isFragmentVisible = true
            onVisible()
        } else {
            isFragmentVisible = false
            onInvisible()
        }
    }

    protected fun onVisible() {
        lazyLoad()
    }

    protected fun onInvisible() {}

    /**
     * 要实现延迟加载Fragment内容,需要在 onCreateView
     * isPrepared = true;
     */
    protected fun lazyLoad() {
        if (!isPrepared || !isFragmentVisible || !isFirstLoad) {
            return
        }
        isFirstLoad = false
        initData()
    }


    protected abstract fun inflateView(layoutInflater: LayoutInflater, viewGroup: ViewGroup?, savedInstanceState: Bundle?): View
    protected abstract fun initView()
    protected abstract fun initData()
}