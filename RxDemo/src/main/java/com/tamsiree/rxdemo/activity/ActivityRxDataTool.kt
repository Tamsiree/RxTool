package com.tamsiree.rxdemo.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.tamsiree.rxdemo.R
import com.tamsiree.rxkit.RxDataTool.Companion.cn2PY
import com.tamsiree.rxkit.RxDataTool.Companion.format2Decimals
import com.tamsiree.rxkit.RxDataTool.Companion.formatCard
import com.tamsiree.rxkit.RxDataTool.Companion.formatCardEnd4
import com.tamsiree.rxkit.RxDataTool.Companion.getAmountValue
import com.tamsiree.rxkit.RxDataTool.Companion.getAstro
import com.tamsiree.rxkit.RxDataTool.Companion.getPYAllFirstLetter
import com.tamsiree.rxkit.RxDataTool.Companion.getPYFirstLetter
import com.tamsiree.rxkit.RxDataTool.Companion.getRoundUp
import com.tamsiree.rxkit.RxDataTool.Companion.hideMobilePhone4
import com.tamsiree.rxkit.RxDataTool.Companion.isDouble
import com.tamsiree.rxkit.RxDataTool.Companion.isInteger
import com.tamsiree.rxkit.RxDataTool.Companion.isNullString
import com.tamsiree.rxkit.RxDataTool.Companion.isNumber
import com.tamsiree.rxkit.RxDataTool.Companion.lowerFirstLetter
import com.tamsiree.rxkit.RxDataTool.Companion.oneCn2ASCII
import com.tamsiree.rxkit.RxDataTool.Companion.oneCn2PY
import com.tamsiree.rxkit.RxDataTool.Companion.reverse
import com.tamsiree.rxkit.RxDataTool.Companion.stringToDouble
import com.tamsiree.rxkit.RxDataTool.Companion.stringToFloat
import com.tamsiree.rxkit.RxDataTool.Companion.stringToInt
import com.tamsiree.rxkit.RxDataTool.Companion.stringToLong
import com.tamsiree.rxkit.RxDataTool.Companion.toDBC
import com.tamsiree.rxkit.RxDataTool.Companion.toSBC
import com.tamsiree.rxkit.RxDataTool.Companion.upperFirstLetter
import com.tamsiree.rxkit.RxDeviceTool.setPortrait
import com.tamsiree.rxui.activity.ActivityBase
import com.tamsiree.rxui.view.RxTitle

/**
 * @author tamsiree
 */
class ActivityRxDataTool : ActivityBase() {
    @JvmField
    @BindView(R.id.editText)
    var mEditText: EditText? = null

    @JvmField
    @BindView(R.id.btn_null)
    var mBtnNull: Button? = null

    @JvmField
    @BindView(R.id.tv_null)
    var mTvNull: TextView? = null

    @JvmField
    @BindView(R.id.btn_null_obj)
    var mBtnNullObj: Button? = null

    @JvmField
    @BindView(R.id.tv_null_obj)
    var mTvNullObj: TextView? = null

    @JvmField
    @BindView(R.id.btn_is_integer)
    var mBtnIsInteger: Button? = null

    @JvmField
    @BindView(R.id.tv_is_integer)
    var mTvIsInteger: TextView? = null

    @JvmField
    @BindView(R.id.btn_is_double)
    var mBtnIsDouble: Button? = null

    @JvmField
    @BindView(R.id.tv_is_double)
    var mTvIsDouble: TextView? = null

    @JvmField
    @BindView(R.id.btn_is_number)
    var mBtnIsNumber: Button? = null

    @JvmField
    @BindView(R.id.tv_is_number)
    var mTvIsNumber: TextView? = null

    @JvmField
    @BindView(R.id.ed_month)
    var mEdMonth: EditText? = null

    @JvmField
    @BindView(R.id.ed_day)
    var mEdDay: EditText? = null

    @JvmField
    @BindView(R.id.btn_astro)
    var mBtnAstro: Button? = null

    @JvmField
    @BindView(R.id.tv_astro)
    var mTvAstro: TextView? = null

    @JvmField
    @BindView(R.id.ed_mobile)
    var mEdMobile: EditText? = null

    @JvmField
    @BindView(R.id.btn_hind_mobile)
    var mBtnHindMobile: Button? = null

    @JvmField
    @BindView(R.id.tv_hind_mobile)
    var mTvHindMobile: TextView? = null

    @JvmField
    @BindView(R.id.ed_bank_card)
    var mEdBankCard: EditText? = null

    @JvmField
    @BindView(R.id.btn_format_bank_card)
    var mBtnFormatBankCard: Button? = null

    @JvmField
    @BindView(R.id.tv_format_bank_card)
    var mTvFormatBankCard: TextView? = null

    @JvmField
    @BindView(R.id.btn_format_bank_card_4)
    var mBtnFormatBankCard4: Button? = null

    @JvmField
    @BindView(R.id.tv_format_bank_card_4)
    var mTvFormatBankCard4: TextView? = null

    @JvmField
    @BindView(R.id.btn_getAmountValue)
    var mBtnGetAmountValue: Button? = null

    @JvmField
    @BindView(R.id.tv_getAmountValue)
    var mTvGetAmountValue: TextView? = null

    @JvmField
    @BindView(R.id.btn_getRoundUp)
    var mBtnGetRoundUp: Button? = null

    @JvmField
    @BindView(R.id.tv_getRoundUp)
    var mTvGetRoundUp: TextView? = null

    @JvmField
    @BindView(R.id.ed_text)
    var mEdText: EditText? = null

    @JvmField
    @BindView(R.id.btn_string_to_int)
    var mBtnStringToInt: Button? = null

    @JvmField
    @BindView(R.id.tv_string_to_int)
    var mTvStringToInt: TextView? = null

    @JvmField
    @BindView(R.id.btn_string_to_long)
    var mBtnStringToLong: Button? = null

    @JvmField
    @BindView(R.id.tv_string_to_long)
    var mTvStringToLong: TextView? = null

    @JvmField
    @BindView(R.id.btn_string_to_double)
    var mBtnStringToDouble: Button? = null

    @JvmField
    @BindView(R.id.tv_string_to_double)
    var mTvStringToDouble: TextView? = null

    @JvmField
    @BindView(R.id.btn_string_to_float)
    var mBtnStringToFloat: Button? = null

    @JvmField
    @BindView(R.id.tv_string_to_float)
    var mTvStringToFloat: TextView? = null

    @JvmField
    @BindView(R.id.btn_string_to_two_number)
    var mBtnStringToTwoNumber: Button? = null

    @JvmField
    @BindView(R.id.tv_string_to_two_number)
    var mTvStringToTwoNumber: TextView? = null

    @JvmField
    @BindView(R.id.btn_upperFirstLetter)
    var mBtnUpperFirstLetter: Button? = null

    @JvmField
    @BindView(R.id.tv_upperFirstLetter)
    var mTvUpperFirstLetter: TextView? = null

    @JvmField
    @BindView(R.id.btn_lowerFirstLetter)
    var mBtnLowerFirstLetter: Button? = null

    @JvmField
    @BindView(R.id.tv_lowerFirstLetter)
    var mTvLowerFirstLetter: TextView? = null

    @JvmField
    @BindView(R.id.btn_reverse)
    var mBtnReverse: Button? = null

    @JvmField
    @BindView(R.id.tv_reverse)
    var mTvReverse: TextView? = null

    @JvmField
    @BindView(R.id.btn_toDBC)
    var mBtnToDBC: Button? = null

    @JvmField
    @BindView(R.id.tv_toDBC)
    var mTvToDBC: TextView? = null

    @JvmField
    @BindView(R.id.btn_toSBC)
    var mBtnToSBC: Button? = null

    @JvmField
    @BindView(R.id.tv_toSBC)
    var mTvToSBC: TextView? = null

    @JvmField
    @BindView(R.id.btn_oneCn2ASCII)
    var mBtnOneCn2ASCII: Button? = null

    @JvmField
    @BindView(R.id.tv_oneCn2ASCII)
    var mTvOneCn2ASCII: TextView? = null

    @JvmField
    @BindView(R.id.btn_oneCn2PY)
    var mBtnOneCn2PY: Button? = null

    @JvmField
    @BindView(R.id.tv_oneCn2PY)
    var mTvOneCn2PY: TextView? = null

    @JvmField
    @BindView(R.id.btn_getPYFirstLetter)
    var mBtnGetPYFirstLetter: Button? = null

    @JvmField
    @BindView(R.id.tv_getPYFirstLetter)
    var mTvGetPYFirstLetter: TextView? = null

    @JvmField
    @BindView(R.id.btn_cn2PY)
    var mBtnCn2PY: Button? = null

    @JvmField
    @BindView(R.id.tv_cn2PY)
    var mTvCn2PY: TextView? = null

    @JvmField
    @BindView(R.id.ed_money)
    var mEdMoney: EditText? = null

    @JvmField
    @BindView(R.id.ed_string)
    var mEdString: EditText? = null

    @JvmField
    @BindView(R.id.rx_title)
    var mRxTitle: RxTitle? = null

    @JvmField
    @BindView(R.id.btn_getPYAllFirstLetter)
    var mBtnGetPYAllFirstLetter: Button? = null

    @JvmField
    @BindView(R.id.tv_getPYAllFirstLetter)
    var mTvGetPYAllFirstLetter: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rx_data_utils)
        ButterKnife.bind(this)
        setPortrait(this)
        mRxTitle!!.setLeftFinish(mContext)
    }

    @SuppressLint("SetTextI18n")
    @OnClick(R.id.btn_null, R.id.btn_null_obj, R.id.btn_is_integer, R.id.btn_is_double, R.id.btn_is_number, R.id.btn_astro, R.id.btn_hind_mobile, R.id.btn_format_bank_card, R.id.btn_format_bank_card_4, R.id.btn_getAmountValue, R.id.btn_getRoundUp, R.id.btn_string_to_int, R.id.btn_string_to_long, R.id.btn_string_to_double, R.id.btn_string_to_float, R.id.btn_string_to_two_number, R.id.btn_upperFirstLetter, R.id.btn_lowerFirstLetter, R.id.btn_reverse, R.id.btn_toDBC, R.id.btn_toSBC, R.id.btn_oneCn2ASCII, R.id.btn_oneCn2PY, R.id.btn_getPYFirstLetter, R.id.btn_getPYAllFirstLetter, R.id.btn_cn2PY)
    fun onViewClicked(view: View) {
        when (view.id) {
            R.id.btn_null -> mTvNull!!.text = isNullString(mEditText!!.text.toString()).toString() + ""
            R.id.btn_null_obj -> mTvNullObj!!.text = isNullString(mEditText!!.text.toString()).toString() + ""
            R.id.btn_is_integer -> mTvIsInteger!!.text = isInteger(mEditText!!.text.toString()).toString() + ""
            R.id.btn_is_double -> mTvIsDouble!!.text = isDouble(mEditText!!.text.toString()).toString() + ""
            R.id.btn_is_number -> mTvIsNumber!!.text = isNumber(mEditText!!.text.toString()).toString() + ""
            R.id.btn_astro -> mTvAstro!!.text = getAstro(stringToInt(mEdMonth!!.text.toString()), stringToInt(mEdDay!!.text.toString()))
            R.id.btn_hind_mobile -> mTvHindMobile!!.text = hideMobilePhone4(mEdMobile!!.text.toString())
            R.id.btn_format_bank_card -> mTvFormatBankCard!!.text = formatCard(mEdBankCard!!.text.toString())
            R.id.btn_format_bank_card_4 -> mTvFormatBankCard4!!.text = formatCardEnd4(mEdBankCard!!.text.toString())
            R.id.btn_getAmountValue -> mTvGetAmountValue!!.text = getAmountValue(mEdMoney!!.text.toString())
            R.id.btn_getRoundUp -> mTvGetRoundUp!!.text = getRoundUp(mEdMoney!!.text.toString(), 2)
            R.id.btn_string_to_int -> mTvStringToInt!!.text = stringToInt(mEdText!!.text.toString()).toString() + ""
            R.id.btn_string_to_long -> mTvStringToLong!!.text = stringToLong(mEdText!!.text.toString()).toString() + ""
            R.id.btn_string_to_double -> mTvStringToDouble!!.text = stringToDouble(mEdText!!.text.toString()).toString() + ""
            R.id.btn_string_to_float -> mTvStringToFloat!!.text = stringToFloat(mEdText!!.text.toString()).toString() + ""
            R.id.btn_string_to_two_number -> mTvStringToTwoNumber!!.text = format2Decimals(mEdText!!.text.toString()) + ""
            R.id.btn_upperFirstLetter -> mTvUpperFirstLetter!!.text = upperFirstLetter(mEdString!!.text.toString())
            R.id.btn_lowerFirstLetter -> mTvLowerFirstLetter!!.text = lowerFirstLetter(mEdString!!.text.toString())
            R.id.btn_reverse -> mTvReverse!!.text = reverse(mEdString!!.text.toString())
            R.id.btn_toDBC -> mTvToDBC!!.text = toDBC(mEdString!!.text.toString())
            R.id.btn_toSBC -> mTvToSBC!!.text = toSBC(mEdString!!.text.toString())
            R.id.btn_oneCn2ASCII -> mTvOneCn2ASCII!!.text = oneCn2ASCII(mEdString!!.text.toString()).toString() + ""
            R.id.btn_oneCn2PY -> mTvOneCn2PY!!.text = oneCn2PY(mEdString!!.text.toString())
            R.id.btn_getPYFirstLetter -> mTvGetPYFirstLetter!!.text = getPYFirstLetter(mEdString!!.text.toString())
            R.id.btn_getPYAllFirstLetter -> mTvGetPYAllFirstLetter!!.text = getPYAllFirstLetter(mEdString!!.text.toString())
            R.id.btn_cn2PY -> mTvCn2PY!!.text = cn2PY(mEdString!!.text.toString())
        }
    }
}