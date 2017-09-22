package com.vondear.tools.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vondear.rxtools.RxBarTool;
import com.vondear.rxtools.RxDataTool;
import com.vondear.rxtools.activity.ActivityBase;
import com.vondear.rxtools.view.RxRulerWheelView;
import com.vondear.rxtools.view.RxTitle;
import com.vondear.rxtools.view.wheelhorizontal.AbstractWheel;
import com.vondear.rxtools.view.wheelhorizontal.ArrayWheelAdapter;
import com.vondear.rxtools.view.wheelhorizontal.OnWheelClickedListener;
import com.vondear.rxtools.view.wheelhorizontal.OnWheelScrollListener;
import com.vondear.rxtools.view.wheelhorizontal.WheelHorizontalView;
import com.vondear.tools.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ActivityWheelHorizontal extends ActivityBase {


    @BindView(R.id.rx_title)
    RxTitle mRxTitle;
    @BindView(R.id.wheelView_year_month)
    WheelHorizontalView mWheelViewYearMonth;
    @BindView(R.id.imageView1)
    ImageView mImageView1;
    @BindView(R.id.LinearLayout2)
    LinearLayout mLinearLayout2;
    @BindView(R.id.wheelview)
    RxRulerWheelView mWheelview;
    @BindView(R.id.wheelview2)
    RxRulerWheelView mWheelview2;
    @BindView(R.id.wheelview3)
    RxRulerWheelView mWheelview3;
    @BindView(R.id.wheelview4)
    RxRulerWheelView mWheelview4;
    @BindView(R.id.wheelview5)
    RxRulerWheelView mWheelview5;
    @BindView(R.id.changed_tv)
    TextView mChangedTv;
    @BindView(R.id.selected_tv)
    TextView mSelectedTv;
    @BindView(R.id.LinearLayout1)
    LinearLayout mLinearLayout1;
    private List<String> listYearMonth = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RxBarTool.noTitle(this);
        setContentView(R.layout.activity_wheel_horizontal);
        ButterKnife.bind(this);
        initView();
        initData();

        initRulerView();
    }

    private void initRulerView() {

        mWheelview = (RxRulerWheelView) findViewById(R.id.wheelview);
        mWheelview2 = (RxRulerWheelView) findViewById(R.id.wheelview2);
        mWheelview3 = (RxRulerWheelView) findViewById(R.id.wheelview3);
        mWheelview4 = (RxRulerWheelView) findViewById(R.id.wheelview4);
        mWheelview5 = (RxRulerWheelView) findViewById(R.id.wheelview5);
        mSelectedTv = (TextView) findViewById(R.id.selected_tv);
        mChangedTv = (TextView) findViewById(R.id.changed_tv);

        final List<String> items = new ArrayList<>();
        for (int i = 1; i <= 40; i++) {
            items.add(String.valueOf(i * 1000));
        }

        mWheelview.setItems(items);
        mWheelview.selectIndex(8);
        mWheelview.setAdditionCenterMark("元");

        List<String> items2 = new ArrayList<>();
        items2.add("一月");
        items2.add("二月");
        items2.add("三月");
        items2.add("四月");
        items2.add("五月");
        items2.add("六月");
        items2.add("七月");
        items2.add("八月");
        items2.add("九月");
        items2.add("十月");
        items2.add("十一月");
        items2.add("十二月");

        mWheelview2.setItems(items2);

        List<String> items3 = new ArrayList<>();
        items3.add("1");
        items3.add("2");
        items3.add("3");
        items3.add("5");
        items3.add("7");
        items3.add("11");
        items3.add("13");
        items3.add("17");
        items3.add("19");
        items3.add("23");
        items3.add("29");
        items3.add("31");

        mWheelview3.setItems(items3);
        mWheelview3.setAdditionCenterMark("m");

//		mWheelview4.setItems(items);
//		mWheelview4.setEnabled(false);

        mWheelview5.setItems(items);
        mWheelview5.setMinSelectableIndex(3);
        mWheelview5.setMaxSelectableIndex(items.size() - 3);

        items.remove(items.size() - 1);
        items.remove(items.size() - 2);
        items.remove(items.size() - 3);
        items.remove(items.size() - 4);

        mSelectedTv.setText(String.format("onWheelItemSelected：%1$s", ""));
        mChangedTv.setText(String.format("onWheelItemChanged：%1$s", ""));

        mWheelview5.setOnWheelItemSelectedListener(new RxRulerWheelView.OnWheelItemSelectedListener() {
            @Override
            public void onWheelItemSelected(RxRulerWheelView wheelView, int position) {
                mSelectedTv.setText(String.format("onWheelItemSelected：%1$s", wheelView.getItems().get(position)));
            }

            @Override
            public void onWheelItemChanged(RxRulerWheelView wheelView, int position) {
                mChangedTv.setText(String.format("onWheelItemChanged：%1$s", wheelView.getItems().get(position)));
            }
        });

        mWheelview4.postDelayed(new Runnable() {
            @Override
            public void run() {
                mWheelview4.setItems(items);
            }
        }, 3000);
    }

    private void initView() {
        mRxTitle.setLeftFinish(mContext);
    }

    private void initData() {
        // TODO Auto-generated method stub
        listYearMonth.clear();
        Calendar calendar = Calendar.getInstance(Locale.CHINA);
        for (int i = calendar.get(Calendar.YEAR) - 3; i <= calendar.get(Calendar.YEAR) + 2; i++) {
            for (int j = 1; j <= 12; j++) {
                listYearMonth.add(i + "年" + j + "月");
            }
        }
        String[] arr = listYearMonth.toArray(new String[listYearMonth.size()]);
        int CurrentIndex = 0;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i].equals(calendar.get(Calendar.YEAR) + "年" + (calendar.get(Calendar.MONTH) + 1) + "月")) {
                CurrentIndex = i;
                break;
            }
        }

        ArrayWheelAdapter<String> ampmAdapter = new ArrayWheelAdapter<String>(
                this, arr);
        ampmAdapter.setItemResource(R.layout.item_wheel_year_month);
        ampmAdapter.setItemTextResource(R.id.tv_year);
        mWheelViewYearMonth.setViewAdapter(ampmAdapter);
        // set current time
        mWheelViewYearMonth.setCurrentItem(CurrentIndex);

        mWheelViewYearMonth.addScrollingListener(new OnWheelScrollListener() {
            String before;
            String behind;

            @Override
            public void onScrollingStarted(AbstractWheel wheel) {
                before = listYearMonth.get(wheel.getCurrentItem());
            }

            @Override
            public void onScrollingFinished(AbstractWheel wheel) {
                behind = listYearMonth.get(wheel.getCurrentItem());
                Log.v("addScrollingListener", "listYearMonth:" + listYearMonth.get(wheel.getCurrentItem()));
                if (!before.equals(behind)) {
                    int year = RxDataTool.stringToInt(listYearMonth.get(
                            wheel.getCurrentItem()).substring(0, 4));
                    int month = RxDataTool.stringToInt(listYearMonth.get(
                            wheel.getCurrentItem()).substring(5, 6));
                    //initBarChart(VonUtil.getDaysByYearMonth(year, month));
                }
            }
        });
        mWheelViewYearMonth.addClickingListener(new OnWheelClickedListener() {

            @Override
            public void onItemClicked(AbstractWheel wheel, int itemIndex) {
                Log.v("addScrollingListener", "listYearMonth:" + listYearMonth.get(itemIndex));
                mWheelViewYearMonth.setCurrentItem(itemIndex, true);
                /*
                 * int year =
				 * VonUtil.StringToInt(listYearMonth.get(itemIndex)
				 * .substring(0, 4)); int month =
				 * VonUtil.StringToInt(listYearMonth
				 * .get(itemIndex).substring(5, 6));
				 * initBarChart(VonUtil.getDaysByYearMonth(year, month));
				 */
            }
        });
    }
}
