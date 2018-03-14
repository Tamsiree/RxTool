package com.vondear.rxtools.view.popupwindows;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.vondear.rxtools.R;
import com.vondear.rxtools.RxDeviceTool;
import com.vondear.rxtools.model.ActionItem;

import java.util.ArrayList;

/**
 *
 * @author vondear
 * @date 2016/8/4
 */
public class RxPopupImply extends PopupWindow {

    // 列表弹窗的间隔
    protected final int LIST_PADDING = 10;
    // 坐标的位置（x、y）
    private final int[] mLocation = new int[2];
    private Context mContext;
    // 实例化一个矩形
    private Rect mRect = new Rect();
    // 屏幕的宽度和高度
    private int mScreenWidth, mScreenHeight;

    // 判断是否需要添加或更新列表子类项
    private boolean mIsDirty;

    // 位置不在中心
    private int popupGravity = Gravity.NO_GRAVITY;

    // 定义列表对象
    private ListView mListView;

    // 定义弹窗子类项列表
    private ArrayList<ActionItem> mActionItems = new ArrayList<ActionItem>();
    private TextView tv_imply;

    public RxPopupImply(Context context) {
        // 设置布局的参数
        this(context, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,"一小时后点这里\n有惊喜哦~");
    }

    public RxPopupImply(Context context,String str) {
        // 设置布局的参数
        this(context, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,str);
    }
    public RxPopupImply(Context context, int width, int height, String str) {
        this.mContext = context;

        // 设置可以获得焦点
        setFocusable(true);
        // 设置弹窗内可点击
        setTouchable(true);
        // 设置弹窗外可点击
        setOutsideTouchable(true);

        // 获得屏幕的宽度和高度
        mScreenWidth = RxDeviceTool.getScreenWidth(mContext);
        mScreenHeight = RxDeviceTool.getScreenHeight(mContext);

        // 设置弹窗的宽度和高度
        setWidth(width);
        setHeight(height);

        setBackgroundDrawable(new BitmapDrawable());

        // 设置弹窗的布局界面
        setContentView(LayoutInflater.from(mContext).inflate(
                R.layout.popup_imply, null));

        initUI(str);
    }

    /**
     * 初始化弹窗列表
     */
    private void initUI(String str) {
        tv_imply = (TextView) getContentView().findViewById(R.id.tv_imply);
        tv_imply.setText(str);
      /*  mListView = (ListView) getContentView().findViewById(R.id.title_list);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int index, long arg3) {
                // 点击子类项后，弹窗消失
                dismiss();
            }
        });*/
    }

    /**
     * 显示弹窗列表界面
     */
    public void show(View view) {
        // 获得点击屏幕的位置坐标
        view.getLocationOnScreen(mLocation);
        // 设置矩形的大小
        mRect.set(mLocation[0], mLocation[1], mLocation[0] + view.getWidth(), mLocation[1] + view.getHeight());
        // 显示弹窗的位置
        // showAtLocation(view, popupGravity, mScreenWidth - LIST_PADDING - (getWidth() / 2), mRect.bottom + VonUtils.dip2px(mContext, 7.5f));
        showAsDropDown(view);
    }


}
