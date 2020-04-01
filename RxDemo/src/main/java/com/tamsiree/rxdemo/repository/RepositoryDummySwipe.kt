package com.tamsiree.rxdemo.repository

/**
 * Class which acts in place of something which would perform a swipe action
 */
class RepositoryDummySwipe {

    private val activeStates: MutableMap<Int, Boolean> = hashMapOf()

    fun toggleActiveState(index: Int) {
        activeStates[index] = !(activeStates[index] ?: false)
    }

    fun isActive(index: Int): Boolean = activeStates[index] ?: false
}