package com.tamsiree.rxui.view.dialog.wheel

/**
 * @author tamsiree
 * Range for visible items.
 */
class ItemsRange
/**
 * Default constructor. Creates an empty range
 */ @JvmOverloads constructor(
        /**
         * Gets number of  first item
         * @return the number of the first item
         */
        // First item number
        val first: Int = 0,
        /**
         * Get items count
         * @return the count of items
         */
        // Items count
        val count: Int = 0) {

    /**
     * Gets number of last item
     * @return the number of last item
     */
    val last: Int
        get() = first + count - 1

    /**
     * Tests whether item is contained by range
     * @param index the item number
     * @return true if item is contained
     */
    operator fun contains(index: Int): Boolean {
        return index >= first && index <= last
    }
    /**
     * Constructor
     * @param first the number of first item
     * @param count the count of items
     */
}