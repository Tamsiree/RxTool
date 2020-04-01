package com.tamsiree.rxdemo.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tamsiree.rxdemo.R
import com.tamsiree.rxkit.demodata.*
import com.tamsiree.rxui.fragment.FragmentLazy
import com.tamsiree.rxui.model.ModelFVP
import kotlinx.android.synthetic.main.fragment_demo_data_person_info.*
import java.util.*

class FragmentDemoDataPersonInfo : FragmentLazy() {

    private val modelFVPList: MutableList<ModelFVP> = ArrayList()

    override fun inflateView(layoutInflater: LayoutInflater, viewGroup: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = layoutInflater.inflate(R.layout.fragment_demo_data_person_info, viewGroup, false)
        return view
    }

    override fun initView() {
        btnName.setOnClickListener {
            //生成常见姓名
            val name: String = ChineseNameGenerator.instance.generate()
            tvName.text = name
        }

        btnName1.setOnClickListener {
            //生成带有生僻名字部分的姓名
            val name: String = ChineseNameGenerator.instance.generateOdd()
            tvName1.text = name
        }
        btnEnglishName.setOnClickListener {
            //生成英文名
            val name: String = EnglishNameGenerator.instance.generate()
            tvEnglishName.text = name
        }

        btnAddress.setOnClickListener {
            //生成地址
            val address: String = ChineseAddressGenerator.instance.generate()
            tvAddress.text = address
        }

        btnIDOffice.setOnClickListener {
            //生成签发机关
            val idOffice: String = ChineseIDCardNumberGenerator.generateIssueOrg()
            tvIDOffice.text = idOffice
        }
        btnIDDate.setOnClickListener {
            //生成有效期限
            val idOffice: String = ChineseIDCardNumberGenerator.generateValidPeriod()
            tvIDDate.text = idOffice
        }

        btnID1.setOnClickListener {
            //生成身份证
            val id: String = ChineseIDCardNumberGenerator.instance.generate()
            tvID.text = id

            if (id[id.length - 2].toInt() % 2 == 0) {
                tvGender.text = "女"
            } else {
                tvGender.text = "男"
            }
        }

        btnMobile.setOnClickListener {
            //生成手机号码
            val mobile: String = ChineseMobileNumberGenerator.instance.generate()
            tvMobile.text = mobile
        }

        btnMobileFake.setOnClickListener {
            //生成虚假手机号码
            val mobile: String = ChineseMobileNumberGenerator.instance.generateFake()
            tvMobileFake.text = mobile
        }
        btnEmail.setOnClickListener {
            //生成虚假手机号码
            val email: String = EmailAddressGenerator.instance.generate()
            tvEmail.text = email
        }
    }

    override fun initData() {

    }

    companion object {
        fun newInstance(): FragmentDemoDataPersonInfo {
            return FragmentDemoDataPersonInfo()
        }
    }
}