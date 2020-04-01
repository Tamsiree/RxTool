package com.tamsiree.rxkit.demodata

import com.tamsiree.rxkit.demodata.base.GenericGenerator
import org.apache.commons.lang3.RandomUtils
import org.apache.commons.lang3.StringUtils

class ChineseMobileNumberGenerator private constructor() : GenericGenerator() {
    override fun generate(): String {
        return genMobilePre() + StringUtils
                .leftPad("" + RandomUtils.nextInt(0, 99999999 + 1), 8, "0")
    }

    /**
     * 生成假的手机号，以19开头
     */
    fun generateFake(): String {
        return "19" + StringUtils
                .leftPad("" + RandomUtils.nextInt(0, 999999999 + 1), 9, "0")
    }

    companion object {
        private val MOBILE_PREFIX = intArrayOf(133, 153, 177, 180,
                181, 189, 134, 135, 136, 137, 138, 139, 150, 151, 152, 157, 158, 159,
                178, 182, 183, 184, 187, 188, 130, 131, 132, 155, 156, 176, 185, 186,
                145, 147, 170)
        val instance = ChineseMobileNumberGenerator()

        private fun genMobilePre(): String {
            return "" + MOBILE_PREFIX[RandomUtils.nextInt(0, MOBILE_PREFIX.size)]
        }
    }
}