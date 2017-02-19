package com.vondear.tools.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vondear.tools.R;
import com.vondear.rxtools.RxBarUtils;
import com.vondear.rxtools.RxDataUtils;
import com.vondear.rxtools.view.wheelhorizontal.AbstractWheel;
import com.vondear.rxtools.view.wheelhorizontal.ArrayWheelAdapter;
import com.vondear.rxtools.view.wheelhorizontal.OnWheelClickedListener;
import com.vondear.rxtools.view.wheelhorizontal.OnWheelScrollListener;
import com.vondear.rxtools.view.wheelhorizontal.WheelHorizontalView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ActivityWheelHorizontal extends Activity {

    @BindView(R.id.iv_finish)
    ImageView mIvFinish;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.ll_include_title)
    LinearLayout mLlIncludeTitle;
    @BindView(R.id.imageView1)
    ImageView mImageView1;
    @BindView(R.id.LinearLayout2)
    LinearLayout mLinearLayout2;
    @BindView(R.id.LinearLayout1)
    LinearLayout mLinearLayout1;
    @BindView(R.id.wheelView_year_month)
    WheelHorizontalView mWheelViewYearMonth;

    private List<String> listYearMonth = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RxBarUtils.noTitle(this);
        setContentView(R.layout.activity_wheel_horizontal);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    private void initView() {
        mLlIncludeTitle.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        mTvTitle.setText("横向滑动选择日期");
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
        String[] arr = (String[]) listYearMonth.toArray(new String[listYearMonth.size()]);
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
                    int year = RxDataUtils.stringToInt(listYearMonth.get(
                            wheel.getCurrentItem()).substring(0, 4));
                    int month = RxDataUtils.stringToInt(listYearMonth.get(
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

    @OnClick(R.id.iv_finish)
    public void onClick() {
        finish();
    }
}
