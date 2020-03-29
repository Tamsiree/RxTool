package com.tamsiree.camera

/**
 * Immutable class for describing width and height dimensions in pixels.
 */
class Size
/**
 * Create a new immutable Size instance.
 *
 * @param width  The width of the size, in pixels
 * @param height The height of the size, in pixels
 */(val width: Int, val height: Int) : Comparable<Size> {

    override fun equals(o: Any?): Boolean {
        if (o == null) {
            return false
        }
        if (this === o) {
            return true
        }
        if (o is Size) {
            val size = o
            return width == size.width && height == size.height
        }
        return false
    }

    override fun toString(): String {
        return width.toString() + "x" + height
    }

    override fun hashCode(): Int {
        // assuming most sizes are <2^16, doing a rotate will give us perfect hashing
        return height xor (width shl Integer.SIZE / 2 or (width ushr Integer.SIZE / 2))
    }

    override fun compareTo(another: Size): Int {
        return width * height - another.width * another.height
    }

}