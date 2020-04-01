package com.tamsiree.rxdemo.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tamsiree.rxdemo.R
import com.tamsiree.rxkit.RxDataTool
import com.tamsiree.rxkit.demodata.bank.BankCardNumberGenerator
import com.tamsiree.rxkit.demodata.bank.BankCardNumberValidator
import com.tamsiree.rxkit.demodata.bank.BankCardTypeEnum
import com.tamsiree.rxkit.demodata.bank.BankNameEnum
import com.tamsiree.rxkit.view.RxToast
import com.tamsiree.rxui.fragment.FragmentLazy
import kotlinx.android.synthetic.main.fragment_demo_data_bank_card.*

class FragmentDemoDataBankCard : FragmentLazy() {

    override fun inflateView(layoutInflater: LayoutInflater, viewGroup: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = layoutInflater.inflate(R.layout.fragment_demo_data_bank_card, viewGroup, false)
        return view
    }

    override fun initView() {

    }

    override fun initData() {
        checkboxFormatCard.setOnCheckedChangeListener { buttonView, isChecked ->
            val value0 = tvBankCardNumber0.text.toString()
            val value = tvBankCardNumber.text.toString()
            val value1 = tvBankCardNumberICBC.text.toString()
            val value2 = tvBankCardNumberICBC1.text.toString()
            val value3 = tvBankCardNumber1.text.toString()
            if (isChecked) {
                if (!RxDataTool.isContainSpace(value)) {
                    tvBankCardNumber.text = RxDataTool.formatCard(value)
                }
                if (!RxDataTool.isContainSpace(value)) {
                    tvBankCardNumber0.text = RxDataTool.formatCard(value0)
                }
                if (!RxDataTool.isContainSpace(value1)) {
                    tvBankCardNumberICBC.text = RxDataTool.formatCard(value1)
                }
                if (!RxDataTool.isContainSpace(value2)) {
                    tvBankCardNumberICBC1.text = RxDataTool.formatCard(value2)
                }
                if (!RxDataTool.isContainSpace(value3)) {
                    tvBankCardNumber1.text = RxDataTool.formatCard(value3)
                }
            } else {
                tvBankCardNumber0.text = RxDataTool.cleanSpace(value0)
                tvBankCardNumber.text = RxDataTool.cleanSpace(value)
                tvBankCardNumberICBC.text = RxDataTool.cleanSpace(value1)
                tvBankCardNumberICBC1.text = RxDataTool.cleanSpace(value2)
                tvBankCardNumber1.text = RxDataTool.cleanSpace(value3)
            }
        }

        btnBankCardNumber0.setOnClickListener {
            //银行
            val bankCardNo: String = BankCardNumberGenerator.instance.generate()
            if (checkboxFormatCard.isChecked) {
                tvBankCardNumber0.text = RxDataTool.formatCard(bankCardNo)
            } else {
                tvBankCardNumber0.text = bankCardNo
            }
        }

        btnBankCardNumber.setOnClickListener {
            //华润银行
            val bankCardNo: String = BankCardNumberGenerator.generate(BankNameEnum.CR, null)
            if (checkboxFormatCard.isChecked) {
                tvBankCardNumber.text = RxDataTool.formatCard(bankCardNo)
            } else {
                tvBankCardNumber.text = bankCardNo
            }
        }



        btnBankCardNumberICBC.setOnClickListener {
            //中国工商银行 信用卡/贷记卡
            val bankCardNo: String = BankCardNumberGenerator.generate(BankNameEnum.ICBC, BankCardTypeEnum.CREDIT)
            if (checkboxFormatCard.isChecked) {
                tvBankCardNumberICBC.text = RxDataTool.formatCard(bankCardNo)
            } else {
                tvBankCardNumberICBC.text = bankCardNo
            }
        }

        btnBankCardNumberICBC1.setOnClickListener {
            //中国工商银行 借记卡/储蓄卡
            val bankCardNo: String = BankCardNumberGenerator.generate(BankNameEnum.ICBC, BankCardTypeEnum.DEBIT)
            if (checkboxFormatCard.isChecked) {
                tvBankCardNumberICBC1.text = RxDataTool.formatCard(bankCardNo)
            } else {
                tvBankCardNumberICBC1.text = bankCardNo
            }
        }

        btnBankCardNumber1.setOnClickListener {
            val prefix = edBankCardNumber1.text.toString()
            if (edBankCardNumber1.text.length == 6 && RxDataTool.isInteger(edBankCardNumber1.text.toString())) {
                //根据给定前六位生成卡号
                val bankCardNo: String = BankCardNumberGenerator.generateByPrefix(prefix.toInt())
                if (checkboxFormatCard.isChecked) {
                    tvBankCardNumber1.text = RxDataTool.formatCard(bankCardNo)
                } else {
                    tvBankCardNumber1.text = bankCardNo
                }
            } else {
                RxToast.error("银行卡前缀格式输入有误")
            }
        }

        btnBankCardNumberV.setOnClickListener {
            val value = edBankCardNumberV.text.toString()

            if (BankCardNumberValidator.validate(value)) {
                tvBankCardNumberV.text = "合法"
            } else {
                tvBankCardNumberV.text = "不合法"
            }
        }
    }

    companion object {
        fun newInstance(): FragmentDemoDataBankCard {
            return FragmentDemoDataBankCard()
        }
    }


}