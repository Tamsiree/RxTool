package com.tamsiree.rxdemo.activity

import android.annotation.SuppressLint
import android.os.Bundle
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
import kotlinx.android.synthetic.main.activity_rx_data_utils.*

/**
 * @author tamsiree
 */
class ActivityRxDataTool : ActivityBase() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rx_data_utils)
        setPortrait(this)
    }

    @SuppressLint("SetTextI18n")
    override fun initView() {
        rx_title.setLeftFinish(mContext)
        btn_null.setOnClickListener { tv_null.text = isNullString(editText.text.toString()).toString() }
        btn_null_obj.setOnClickListener { tv_null_obj.text = isNullString(editText.text.toString()).toString() }
        btn_is_integer.setOnClickListener { tv_is_integer.text = isInteger(editText.text.toString()).toString() }
        btn_is_double.setOnClickListener { tv_is_double.text = isDouble(editText.text.toString()).toString() }
        btn_is_number.setOnClickListener { tv_is_number.text = isNumber(editText.text.toString()).toString() }
        btn_astro.setOnClickListener { tv_astro.text = getAstro(stringToInt(ed_month.text.toString()), stringToInt(ed_day.text.toString())) }
        btn_hind_mobile.setOnClickListener { tv_hind_mobile.text = hideMobilePhone4(ed_mobile.text.toString()) }
        btn_format_bank_card.setOnClickListener { tv_format_bank_card.text = formatCard(ed_bank_card.text.toString()) }
        btn_format_bank_card_4.setOnClickListener { tv_format_bank_card_4.text = formatCardEnd4(ed_bank_card.text.toString()) }
        btn_getAmountValue.setOnClickListener { tv_getAmountValue.text = getAmountValue(ed_money.text.toString()) }
        btn_getRoundUp.setOnClickListener { tv_getRoundUp.text = getRoundUp(ed_money.text.toString(), 2) }
        btn_string_to_int.setOnClickListener { tv_string_to_int.text = stringToInt(ed_text.text.toString()).toString() }
        btn_string_to_long.setOnClickListener { tv_string_to_long.text = stringToLong(ed_text.text.toString()).toString() }
        btn_string_to_double.setOnClickListener { tv_string_to_double.text = stringToDouble(ed_text.text.toString()).toString() }
        btn_string_to_float.setOnClickListener { tv_string_to_float.text = stringToFloat(ed_text.text.toString()).toString() }
        btn_string_to_two_number.setOnClickListener { tv_string_to_two_number.text = format2Decimals(ed_text.text.toString()) }
        btn_upperFirstLetter.setOnClickListener { tv_upperFirstLetter.text = upperFirstLetter(ed_string.text.toString()) }
        btn_lowerFirstLetter.setOnClickListener { tv_lowerFirstLetter.text = lowerFirstLetter(ed_string.text.toString()) }
        btn_reverse.setOnClickListener { tv_reverse.text = reverse(ed_string.text.toString()) }
        btn_toDBC.setOnClickListener { tv_toDBC.text = toDBC(ed_string.text.toString()) }
        btn_toSBC.setOnClickListener { tv_toSBC.text = toSBC(ed_string.text.toString()) }
        btn_oneCn2ASCII.setOnClickListener { tv_oneCn2ASCII.text = oneCn2ASCII(ed_string.text.toString()).toString() }
        btn_oneCn2PY.setOnClickListener { tv_oneCn2PY.text = oneCn2PY(ed_string.text.toString()) }
        btn_getPYFirstLetter.setOnClickListener { tv_getPYFirstLetter.text = getPYFirstLetter(ed_string.text.toString()) }
        btn_getPYAllFirstLetter.setOnClickListener { tv_getPYAllFirstLetter.text = getPYAllFirstLetter(ed_string.text.toString()) }
        btn_cn2PY.setOnClickListener { tv_cn2PY.text = cn2PY(ed_string.text.toString()) }
    }

    override fun initData() {

    }

}