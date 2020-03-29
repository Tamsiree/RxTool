package com.tamsiree.rxui.view.dialog.wheel

import android.content.Context
import android.graphics.Typeface
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.tamsiree.rxkit.TLog.e

/**
 * @author tamsiree
 * @date 2018/6/11 11:36:40 整合修改
 * Abstract wheel adapter provides common functionality for adapters.
 */
abstract class AbstractWheelTextAdapter protected constructor(

        // Current context
        protected var context: Context,

        /**
         * Sets resource Id for items views
         * @param itemResourceId the resource Id to set
         */
        // Items resources
        var itemResource: Int = TEXT_VIEW_ITEM_RESOURCE,

        /**
         * Sets resource Id for text view in item layout
         * @param itemTextResourceId the item text resource Id to set
         */
        var itemTextResource: Int = NO_RESOURCE) : AbstractWheelAdapter() {

    /**
     * Gets text color
     * @return the text color
     */
    /**
     * Sets text color
     * @param textColor the text color to set
     */
    // Text settings
    var textColor = DEFAULT_TEXT_COLOR
    /**
     * Gets text size
     * @return the text size
     */
    /**
     * Sets text size
     * @param textSize the text size to set
     */
    var textSize = DEFAULT_TEXT_SIZE

    // Layout inflater
    protected var inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    /**
     * Gets resource Id for items views
     * @return the item resource Id
     */
    /**
     * Gets resource Id for text view in item layout
     * @return the item text resource Id
     */

    /**
     * Gets resource Id for empty items views
     * @return the empty item resource Id
     */
    /**
     * Sets resource Id for empty items views
     * @param emptyItemResourceId the empty item resource Id to set
     */
    // Empty items resources
    var emptyItemResource = 0

    /**
     * Returns text for specified item
     * @param index the item index
     * @return the text of specified items
     */
    protected abstract fun getItemText(index: Int): CharSequence?

    override fun getItem(index: Int, convertView: View?, parent: ViewGroup?): View? {
        var convertView1 = convertView
        if (index in 0 until itemsCount) {
            if (convertView1 == null) {
                convertView1 = getView(itemResource, parent)
            }
            val textView = getTextView(convertView1, itemTextResource)
            if (textView != null) {
                var text = getItemText(index)
                if (text == null) {
                    text = ""
                }
                textView.text = text
                if (itemResource == TEXT_VIEW_ITEM_RESOURCE) {
                    configureTextView(textView)
                }
            }
            return convertView1
        }
        return null
    }

    override fun getEmptyItem(convertView: View?, parent: ViewGroup?): View? {
        var convertView1 = convertView
        if (convertView1 == null) {
            convertView1 = getView(emptyItemResource, parent)
        }
        if (emptyItemResource == TEXT_VIEW_ITEM_RESOURCE && convertView1 is TextView) {
            configureTextView(convertView1)
        }
        return convertView1
    }

    /**
     * Configures text view. Is called for the TEXT_VIEW_ITEM_RESOURCE views.
     * @param view the text view to be configured
     */
    protected open fun configureTextView(view: TextView) {
        view.setTextColor(textColor)
        view.gravity = Gravity.CENTER
        view.textSize = textSize.toFloat()
        view.setLines(1)
        view.setTypeface(Typeface.SANS_SERIF, Typeface.BOLD)
    }

    /**
     * Loads a text view from view
     * @param view the text view or layout containing it
     * @param textResource the text resource Id in layout
     * @return the loaded text view
     */
    private fun getTextView(view: View?, textResource: Int): TextView? {
        var text: TextView? = null
        try {
            if (textResource == NO_RESOURCE && view is TextView) {
                text = view
            } else if (textResource != NO_RESOURCE) {
                text = view?.findViewById(textResource)
            }
        } catch (e: ClassCastException) {
            e("AbstractWheelAdapter", "You must supply a resource ID for a TextView")
            throw IllegalStateException(
                    "AbstractWheelAdapter requires the resource ID to be a TextView", e)
        }
        return text
    }

    /**
     * Loads view from resources
     * @param resource the resource Id
     * @return the loaded view or null if resource is not set
     */
    private fun getView(resource: Int, parent: ViewGroup?): View? {
        return when (resource) {
            NO_RESOURCE -> null
            TEXT_VIEW_ITEM_RESOURCE -> TextView(context)
            else -> inflater.inflate(resource, parent, false)
        }
    }

    companion object {
        /** Text view resource. Used as a default view for adapter.  */
        const val TEXT_VIEW_ITEM_RESOURCE = -1

        /** No resource constant.  */
        protected const val NO_RESOURCE = 0

        /** Default text color  */
        const val DEFAULT_TEXT_COLOR = -0xefeff0

        /** Default text color  */
        const val LABEL_COLOR = -0x8fff90

        /** Default text size  */
        const val DEFAULT_TEXT_SIZE = 24
    }

}