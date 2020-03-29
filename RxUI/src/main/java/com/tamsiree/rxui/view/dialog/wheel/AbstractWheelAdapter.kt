package com.tamsiree.rxui.view.dialog.wheel

import android.database.DataSetObserver
import android.view.View
import android.view.ViewGroup
import java.util.*

/**
 * @author tamsiree
 * @date 2018/6/11 11:36:40 整合修改
 * Abstract Wheel adapter.
 */
abstract class AbstractWheelAdapter : WheelViewAdapter {
    // Observers
    private var datasetObservers: MutableList<DataSetObserver?>? = null

    override fun getEmptyItem(convertView: View?, parent: ViewGroup?): View? {
        return null
    }

    override fun registerDataSetObserver(observer: DataSetObserver?) {
        if (datasetObservers == null) {
            datasetObservers = LinkedList()
        }
        datasetObservers?.add(observer)
    }

    override fun unregisterDataSetObserver(observer: DataSetObserver?) {
        if (datasetObservers != null) {
            datasetObservers?.remove(observer)
        }
    }

    /**
     * Notifies observers about data changing
     */
    protected fun notifyDataChangedEvent() {
        if (datasetObservers != null) {
            for (observer in datasetObservers!!) {
                observer?.onChanged()
            }
        }
    }

    /**
     * Notifies observers about invalidating data
     */
    protected fun notifyDataInvalidatedEvent() {
        if (datasetObservers != null) {
            for (observer in datasetObservers!!) {
                observer?.onInvalidated()
            }
        }
    }
}