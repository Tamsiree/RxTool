package com.vondear.tools.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.vondear.rxtools.RxDataTool;
import com.vondear.rxtools.activity.ActivityBase;
import com.vondear.rxtools.view.RxTitle;
import com.vondear.tools.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author vondear
 */
public class ActivityRxDataTool extends ActivityBase {

    @BindView(R.id.editText)
    EditText mEditText;
    @BindView(R.id.btn_null)
    Button mBtnNull;
    @BindView(R.id.tv_null)
    TextView mTvNull;
    @BindView(R.id.btn_null_obj)
    Button mBtnNullObj;
    @BindView(R.id.tv_null_obj)
    TextView mTvNullObj;
    @BindView(R.id.btn_is_integer)
    Button mBtnIsInteger;
    @BindView(R.id.tv_is_integer)
    TextView mTvIsInteger;
    @BindView(R.id.btn_is_double)
    Button mBtnIsDouble;
    @BindView(R.id.tv_is_double)
    TextView mTvIsDouble;
    @BindView(R.id.btn_is_number)
    Button mBtnIsNumber;
    @BindView(R.id.tv_is_number)
    TextView mTvIsNumber;
    @BindView(R.id.ed_month)
    EditText mEdMonth;
    @BindView(R.id.ed_day)
    EditText mEdDay;
    @BindView(R.id.btn_astro)
    Button mBtnAstro;
    @BindView(R.id.tv_astro)
    TextView mTvAstro;
    @BindView(R.id.ed_mobile)
    EditText mEdMobile;
    @BindView(R.id.btn_hind_mobile)
    Button mBtnHindMobile;
    @BindView(R.id.tv_hind_mobile)
    TextView mTvHindMobile;
    @BindView(R.id.ed_bank_card)
    EditText mEdBankCard;
    @BindView(R.id.btn_format_bank_card)
    Button mBtnFormatBankCard;
    @BindView(R.id.tv_format_bank_card)
    TextView mTvFormatBankCard;
    @BindView(R.id.btn_format_bank_card_4)
    Button mBtnFormatBankCard4;
    @BindView(R.id.tv_format_bank_card_4)
    TextView mTvFormatBankCard4;
    @BindView(R.id.btn_getAmountValue)
    Button mBtnGetAmountValue;
    @BindView(R.id.tv_getAmountValue)
    TextView mTvGetAmountValue;
    @BindView(R.id.btn_getRoundUp)
    Button mBtnGetRoundUp;
    @BindView(R.id.tv_getRoundUp)
    TextView mTvGetRoundUp;
    @BindView(R.id.ed_text)
    EditText mEdText;
    @BindView(R.id.btn_string_to_int)
    Button mBtnStringToInt;
    @BindView(R.id.tv_string_to_int)
    TextView mTvStringToInt;
    @BindView(R.id.btn_string_to_long)
    Button mBtnStringToLong;
    @BindView(R.id.tv_string_to_long)
    TextView mTvStringToLong;
    @BindView(R.id.btn_string_to_double)
    Button mBtnStringToDouble;
    @BindView(R.id.tv_string_to_double)
    TextView mTvStringToDouble;
    @BindView(R.id.btn_string_to_float)
    Button mBtnStringToFloat;
    @BindView(R.id.tv_string_to_float)
    TextView mTvStringToFloat;
    @BindView(R.id.btn_string_to_two_number)
    Button mBtnStringToTwoNumber;
    @BindView(R.id.tv_string_to_two_number)
    TextView mTvStringToTwoNumber;
    @BindView(R.id.btn_upperFirstLetter)
    Button mBtnUpperFirstLetter;
    @BindView(R.id.tv_upperFirstLetter)
    TextView mTvUpperFirstLetter;
    @BindView(R.id.btn_lowerFirstLetter)
    Button mBtnLowerFirstLetter;
    @BindView(R.id.tv_lowerFirstLetter)
    TextView mTvLowerFirstLetter;
    @BindView(R.id.btn_reverse)
    Button mBtnReverse;
    @BindView(R.id.tv_reverse)
    TextView mTvReverse;
    @BindView(R.id.btn_toDBC)
    Button mBtnToDBC;
    @BindView(R.id.tv_toDBC)
    TextView mTvToDBC;
    @BindView(R.id.btn_toSBC)
    Button mBtnToSBC;
    @BindView(R.id.tv_toSBC)
    TextView mTvToSBC;
    @BindView(R.id.btn_oneCn2ASCII)
    Button mBtnOneCn2ASCII;
    @BindView(R.id.tv_oneCn2ASCII)
    TextView mTvOneCn2ASCII;
    @BindView(R.id.btn_oneCn2PY)
    Button mBtnOneCn2PY;
    @BindView(R.id.tv_oneCn2PY)
    TextView mTvOneCn2PY;
    @BindView(R.id.btn_getPYFirstLetter)
    Button mBtnGetPYFirstLetter;
    @BindView(R.id.tv_getPYFirstLetter)
    TextView mTvGetPYFirstLetter;
    @BindView(R.id.btn_cn2PY)
    Button mBtnCn2PY;
    @BindView(R.id.tv_cn2PY)
    TextView mTvCn2PY;
    @BindView(R.id.ed_money)
    EditText mEdMoney;
    @BindView(R.id.ed_string)
    EditText mEdString;
    @BindView(R.id.rx_title)
    RxTitle mRxTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx_data_utils);
        ButterKnife.bind(this);
        mRxTitle.setLeftFinish(mContext);
    }

    @OnClick({R.id.btn_null, R.id.btn_null_obj, R.id.btn_is_integer, R.id.btn_is_double, R.id.btn_is_number, R.id.btn_astro, R.id.btn_hind_mobile, R.id.btn_format_bank_card, R.id.btn_format_bank_card_4, R.id.btn_getAmountValue, R.id.btn_getRoundUp, R.id.btn_string_to_int, R.id.btn_string_to_long, R.id.btn_string_to_double, R.id.btn_string_to_float, R.id.btn_string_to_two_number, R.id.btn_upperFirstLetter, R.id.btn_lowerFirstLetter, R.id.btn_reverse, R.id.btn_toDBC, R.id.btn_toSBC, R.id.btn_oneCn2ASCII, R.id.btn_oneCn2PY, R.id.btn_getPYFirstLetter, R.id.btn_cn2PY})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_null:
                mTvNull.setText(RxDataTool.isNullString(mEditText.getText().toString()) + "");
                break;
            case R.id.btn_null_obj:
                mTvNullObj.setText(RxDataTool.isNullString(mEditText.getText().toString()) + "");
                break;
            case R.id.btn_is_integer:
                mTvIsInteger.setText(RxDataTool.isInteger(mEditText.getText().toString()) + "");
                break;
            case R.id.btn_is_double:
                mTvIsDouble.setText(RxDataTool.isDouble(mEditText.getText().toString()) + "");
                break;
            case R.id.btn_is_number:
                mTvIsNumber.setText(RxDataTool.isNumber(mEditText.getText().toString()) + "");
                break;
            case R.id.btn_astro:
                mTvAstro.setText(RxDataTool.getAstro(RxDataTool.stringToInt(mEdMonth.getText().toString()), RxDataTool.stringToInt(mEdDay.getText().toString())));
                break;
            case R.id.btn_hind_mobile:
                mTvHindMobile.setText(RxDataTool.hideMobilePhone4(mEdMobile.getText().toString()));
                break;
            case R.id.btn_format_bank_card:
                mTvFormatBankCard.setText(RxDataTool.formatCard(mEdBankCard.getText().toString()));
                break;
            case R.id.btn_format_bank_card_4:
                mTvFormatBankCard4.setText(RxDataTool.formatCardEnd4(mEdBankCard.getText().toString()));
                break;
            case R.id.btn_getAmountValue:
                mTvGetAmountValue.setText(RxDataTool.getAmountValue(mEdMoney.getText().toString()));
                break;
            case R.id.btn_getRoundUp:
                mTvGetRoundUp.setText(RxDataTool.getRoundUp(mEdMoney.getText().toString(), 2));
                break;
            case R.id.btn_string_to_int:
                mTvStringToInt.setText(RxDataTool.stringToInt(mEdText.getText().toString()) + "");
                break;
            case R.id.btn_string_to_long:
                mTvStringToLong.setText(RxDataTool.stringToLong(mEdText.getText().toString()) + "");
                break;
            case R.id.btn_string_to_double:
                mTvStringToDouble.setText(RxDataTool.stringToDouble(mEdText.getText().toString()) + "");
                break;
            case R.id.btn_string_to_float:
                mTvStringToFloat.setText(RxDataTool.stringToFloat(mEdText.getText().toString()) + "");
                break;
            case R.id.btn_string_to_two_number:
                mTvStringToTwoNumber.setText(RxDataTool.format2Decimals(mEdText.getText().toString()) + "");
                break;
            case R.id.btn_upperFirstLetter:
                mTvUpperFirstLetter.setText(RxDataTool.upperFirstLetter(mEdString.getText().toString()));
                break;
            case R.id.btn_lowerFirstLetter:
                mTvLowerFirstLetter.setText(RxDataTool.lowerFirstLetter(mEdString.getText().toString()));
                break;
            case R.id.btn_reverse:
                mTvReverse.setText(RxDataTool.reverse(mEdString.getText().toString()));
                break;
            case R.id.btn_toDBC:
                mTvToDBC.setText(RxDataTool.toDBC(mEdString.getText().toString()));
                break;
            case R.id.btn_toSBC:
                mTvToSBC.setText(RxDataTool.toSBC(mEdString.getText().toString()));
                break;
            case R.id.btn_oneCn2ASCII:
                mTvOneCn2ASCII.setText(RxDataTool.oneCn2ASCII(mEdString.getText().toString()) + "");
                break;
            case R.id.btn_oneCn2PY:
                mTvOneCn2PY.setText(RxDataTool.oneCn2PY(mEdString.getText().toString()));
                break;
            case R.id.btn_getPYFirstLetter:
                mTvGetPYFirstLetter.setText(RxDataTool.getPYFirstLetter(mEdString.getText().toString()));
                break;
            case R.id.btn_cn2PY:
                mTvCn2PY.setText(RxDataTool.cn2PY(mEdString.getText().toString()));
                break;
        }
    }
}
