package com.vondear.rxtools.view.popupwindows;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.vondear.rxtools.R;
import com.vondear.rxtools.RxDeviceTool;
import com.vondear.rxtools.RxImageTool;
import com.vondear.rxtools.model.ActionItem;

import java.util.ArrayList;

/***
 * @author vondear
 * 功能描述：标题按钮上的弹窗（继承自PopupWindow）
 */
public class RxPopupSingleView extends PopupWindow {
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

    // 弹窗子类项选中时的监听
    private OnItemOnClickListener mItemOnClickListener;

    // 定义列表对象
    private ListView mListView;

    // 定义弹窗子类项列表
    private ArrayList<ActionItem> mActionItems = new ArrayList<ActionItem>();
    private int colorItemText = 0;

    public RxPopupSingleView(Context context) {
        // 设置布局的参数
        this(context, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    }

    public RxPopupSingleView(Context context, int width, int height) {
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
                R.layout.popupwindow_layout, null));

        initUI();
    }

    public RxPopupSingleView(Context context, int width, int height, int layout) {
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
                layout, null));

        initUI();
    }

    /**
     * 初始化弹窗列表
     */
    private void initUI() {
        mListView = (ListView) getContentView().findViewById(R.id.title_list);

        mListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int index,
                                    long arg3) {
                // 点击子类项后，弹窗消失
                dismiss();

                if (mItemOnClickListener != null)
                    mItemOnClickListener.onItemClick(mActionItems.get(index),
                            index);
            }
        });
    }

    /**
     * 显示弹窗列表界面
     */
    public void show(View view) {
        // 获得点击屏幕的位置坐标
        view.getLocationOnScreen(mLocation);

        // 设置矩形的大小
        mRect.set(mLocation[0], mLocation[1], mLocation[0] + view.getWidth(),
                mLocation[1] + view.getHeight());

        // 判断是否需要添加或更新列表子类项
        if (mIsDirty) {
            populateActions();
        }

        // 显示弹窗的位置
        showAtLocation(view, popupGravity, mScreenWidth - LIST_PADDING - (getWidth() / 2), mRect.bottom + RxImageTool.dp2px(7.5f));
    }

    /**
     * 显示弹窗列表界面
     */
    public void show(View view, int dex) {
        // 获得点击屏幕的位置坐标
        view.getLocationOnScreen(mLocation);

        // 设置矩形的大小
        mRect.set(mLocation[0], mLocation[1], mLocation[0] + view.getWidth(),
                mLocation[1] + view.getHeight());

        // 判断是否需要添加或更新列表子类项
        if (mIsDirty) {
            populateActions();
        }

        // 显示弹窗的位置
        showAtLocation(view, popupGravity, mLocation[0], mRect.bottom + dex);
    }

    public void setColorItemText(int colorItemText) {
        this.colorItemText = colorItemText;
    }

    /**
     * 设置弹窗列表子项
     */
    private void populateActions() {
        mIsDirty = false;

        // 设置列表的适配器
        mListView.setAdapter(new BaseAdapter() {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView tv_itpop = null;
                ImageView iv_itpop = null;
                if (convertView == null) {
                    convertView = LayoutInflater.from(mContext).inflate(R.layout.item_listview_popup, null);
                }
                tv_itpop = (TextView) convertView.findViewById(R.id.tv_itpop);
                iv_itpop = (ImageView) convertView.findViewById(R.id.iv_itpop);
                if (colorItemText == 0) {
                    colorItemText = mContext.getResources().getColor(android.R.color.white);
                }
                tv_itpop.setTextColor(colorItemText);
                tv_itpop.setTextSize(14);
                // 设置文本居中
                tv_itpop.setGravity(Gravity.CENTER);
                // 设置文本域的范围
                tv_itpop.setPadding(0, 10, 0, 10);
                // 设置文本在一行内显示（不换行）
                tv_itpop.setSingleLine(true);

                ActionItem item = mActionItems.get(position);

                // 设置文本文字
                tv_itpop.setText(item.mTitle);
                if (item.mResourcesId == 0) {
                    iv_itpop.setVisibility(View.GONE);
                } else {
                    iv_itpop.setVisibility(View.VISIBLE);
                    iv_itpop.setImageResource(item.mResourcesId);
                }

                return convertView;
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public Object getItem(int position) {
                return mActionItems.get(position);
            }

            @Override
            public int getCount() {
                return mActionItems.size();
            }
        });
    }

    /**
     * 添加子类项
     */
    public void addAction(ActionItem action) {
        if (action != null) {
            mActionItems.add(action);
            mIsDirty = true;
        }
    }

    /**
     * 清除子类项
     */
    public void cleanAction() {
        if (mActionItems.isEmpty()) {
            mActionItems.clear();
            mIsDirty = true;
        }
    }

    /**
     * 根据位置得到子类项
     */
    public ActionItem getAction(int position) {
        if (position < 0 || position > mActionItems.size())
            return null;
        return mActionItems.get(position);
    }

    /**
     * 设置监听事件
     */
    public void setItemOnClickListener(
            OnItemOnClickListener onItemOnClickListener) {
        this.mItemOnClickListener = onItemOnClickListener;
    }

    /**
     * @author yangyu 功能描述：弹窗子类项按钮监听事件
     */
    public interface OnItemOnClickListener {
        void onItemClick(ActionItem item, int position);
    }
}
