package com.tamsiree.rxkit.demodata.base

import java.util.*

abstract class GenericGenerator {
    abstract fun generate(): String
    protected val randomInstance: Random
        get() {
            if (random == null) {
                random = Random(Date().time)
            }
            return random as Random
        }

    companion object {
        private var random: Random? = null
    }
}