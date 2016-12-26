package com.vondear.vontools.view.dialogWheel;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;


import com.vondear.vontools.R;
import com.vondear.vontools.VonDataUtils;
import com.vondear.vontools.view.TransparentDialog;

import java.util.Calendar;

public class DialogWheelYearMonthDay extends TransparentDialog {
	private Activity context;
	private WheelView year;
	private WheelView month;
	private WheelView day;
	private int curYear;
	private int curMonth;
	private int curDay;
	private TextView tv_sure;
	private TextView tv_cancle;
	private CheckBox checkBox_day;
	private Calendar calendar;
	private String months[] = new String[] { "01", "02", "03", "04", "05",
			"06", "07", "08", "09", "10", "11", "12" };
	public DialogWheelYearMonthDay(Activity context) {
		super(context);
		// TODO Auto-generated constructor stub
		this.context = context;
		build();
	}
	
	public DialogWheelYearMonthDay(Activity context, TextView tv_time) {
		super(context);
		// TODO Auto-generated constructor stub
		this.context = context;
		build();
		tv_time.setText(curYear+"年"+months[curMonth]+"月");
	}
	
	private void build(){
		calendar = Calendar.getInstance();
		final View dialogView1 = LayoutInflater.from(
				context).inflate(
				R.layout.dialog_year_month_day, null);
		
		OnWheelChangedListener listener = new OnWheelChangedListener() {
			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				updateDays(year, month,day);
			}
		};
		
		//year
		year = (WheelView) dialogView1
				.findViewById(R.id.wheelView_year);
		year.setBackgroundResource(R.drawable.transparent_bg);
		year.setWheelBackground(R.drawable.transparent_bg);
		year.setWheelForeground(R.drawable.wheel_val_holo);
		year.setShadowColor(0xFFDADCDB, 0x88DADCDB, 0x00DADCDB);
		curYear = calendar.get(Calendar.YEAR);
		year.setViewAdapter(new DateNumericAdapter(context, curYear-5, curYear,5));
		year.setCurrentItem(5);
		year.addChangingListener(listener);
		
		
		// month
		month = (WheelView) dialogView1
				.findViewById(R.id.wheelView_month);
		month.setBackgroundResource(R.drawable.transparent_bg);
		month.setWheelBackground(R.drawable.transparent_bg);
		month.setWheelForeground(R.drawable.wheel_val_holo);
		month.setShadowColor(0xFFDADCDB, 0x88DADCDB, 0x00DADCDB);
		curMonth = calendar.get(Calendar.MONTH);
		month.setViewAdapter(new DateArrayAdapter(context, months, curMonth));
		month.setCurrentItem(curMonth);
		month.addChangingListener(listener);
		
		
		//day
		day = (WheelView) dialogView1.findViewById(R.id.wheelView_day);
		updateDays(year, month, day);
		curDay = calendar.get(Calendar.DAY_OF_MONTH);
		day.setCurrentItem(curDay-1);
		day.setBackgroundResource(R.drawable.transparent_bg);
		day.setWheelBackground(R.drawable.transparent_bg);
		day.setWheelForeground(R.drawable.wheel_val_holo);
		day.setShadowColor(0xFFDADCDB, 0x88DADCDB, 0x00DADCDB);
		
		tv_sure = (TextView) dialogView1.findViewById(R.id.tv_sure);
		tv_cancle = (TextView) dialogView1
				.findViewById(R.id.tv_cancle);
		
		checkBox_day = (CheckBox) dialogView1.findViewById(R.id.checkBox_day);
		checkBox_day.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {
					day.setVisibility(View.VISIBLE);
				}else{
					day.setVisibility(View.GONE);
				}
			}
		});
		
		getAttr().gravity = Gravity.CENTER;
		setContentView(dialogView1);
	}
	

	public WheelView getDay() {
		return day;
	}

	public int getCurDay() {
		return curDay;
	}

	public CheckBox getCheckBox_day() {
		return checkBox_day;
	}

	public WheelView getYear() {
		return year;
	}

	public WheelView getMonth() {
		return month;
	}

	public int getCurYear() {
		return curYear;
	}

	public int getCurMonth() {
		return curMonth;
	}

	public TextView getTv_sure() {
		return tv_sure;
	}

	public TextView getTv_cancle() {
		return tv_cancle;
	}

	public String[] getMonths() {
		return months;
	}

	
	/**
	 * Updates day wheel. Sets max days according to selected month and year
	 */
	private void updateDays(WheelView year, WheelView month, WheelView day) {
		calendar.set(Calendar.YEAR, curYear-5 + year.getCurrentItem());
		calendar.set(Calendar.MONTH, month.getCurrentItem());
		int maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		int maxDays = VonDataUtils.getDaysByYearMonth(curYear-5 + year.getCurrentItem(), month.getCurrentItem()+1);
		day.setViewAdapter(new DateNumericAdapter(context, 1, maxDays, calendar.get(Calendar.DAY_OF_MONTH) - 1));
		int curDay = Math.min(maxDays, day.getCurrentItem() + 1);
		day.setCurrentItem(curDay - 1, true);
		

		
	}
	
	/**
	 * Adapter for numeric wheels. Highlights the current value.
	 */
	private class DateNumericAdapter extends NumericWheelAdapter {
		// Index of current item
		int currentItem;
		// Index of item to be highlighted
		int currentValue;

		/**
		 * Constructor
		 */
		public DateNumericAdapter(Context context, int minValue, int maxValue, int current) {
			super(context, minValue, maxValue);
			this.currentValue = current;
			setTextSize(16);
		}

		@Override
		protected void configureTextView(TextView view) {
			super.configureTextView(view);
			/*if (currentItem == currentValue) {
				view.setTextColor(0xFF0000F0);
			}*/
			view.setTypeface(Typeface.SANS_SERIF);
		}

		@Override
		public View getItem(int index, View cachedView, ViewGroup parent) {
			currentItem = index;
			return super.getItem(index, cachedView, parent);
		}
	}
}
