package com.vondear.rxtools.view.dialog.wheel;

/**
 * @author vondear
 * Range for visible items.
 */
public class ItemsRange {
	// First item number
	private int first;
	
	// Items count
	private int count;

	/**
	 * Default constructor. Creates an empty range
	 */
    public ItemsRange() {
        this(0, 0);
    }
    
	/**
	 * Constructor
	 * @param first the number of first item
	 * @param count the count of items
	 */
	public ItemsRange(int first, int count) {
		this.first = first;
		this.count = count;
	}
	
	/**
	 * Gets number of  first item
	 * @return the number of the first item
	 */
	public int getFirst() {
		return first;
	}
	
	/**
	 * Gets number of last item
	 * @return the number of last item
	 */
	public int getLast() {
		return getFirst() + getCount() - 1;
	}
	
	/**
	 * Get items count
	 * @return the count of items
	 */
	public int getCount() {
		return count;
	}
	
	/**
	 * Tests whether item is contained by range
	 * @param index the item number
	 * @return true if item is contained
	 */
	public boolean contains(int index) {
		return index >= getFirst() && index <= getLast();
	}
}