package com.tamsiree.rxkit.demodata

import com.tamsiree.rxkit.demodata.base.GenericGenerator
import com.tamsiree.rxkit.demodata.kit.ChineseCharUtils
import org.apache.commons.lang3.RandomUtils

class ChineseAddressGenerator private constructor() : GenericGenerator() {
    override fun generate(): String {
        val result = StringBuilder(genProvinceAndCity())
        result.append(ChineseCharUtils.genRandomLengthChineseChars(2, 3) + "路")
        result.append(RandomUtils.nextInt(1, 8000).toString() + "号")
        result
                .append(ChineseCharUtils.genRandomLengthChineseChars(2, 3) + "小区")
        result.append(RandomUtils.nextInt(1, 20).toString() + "单元")
        result.append(RandomUtils.nextInt(101, 2500).toString() + "室")
        return result.toString()
    }

    companion object {
        val instance: GenericGenerator = ChineseAddressGenerator()

        private fun genProvinceAndCity(): String {
            return ChineseAreaList.provinceCityList[RandomUtils.nextInt(0, ChineseAreaList.provinceCityList.size)]
        }
    }
}