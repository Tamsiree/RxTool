package com.tamsiree.rxui.view.wavesidebar

import com.tamsiree.rxui.model.ModelContactCity
import java.util.*

/**
 * 首字母排序
 * 此类经供参考
 * @author tamsiree
 */
class ComparatorLetter : Comparator<ModelContactCity?> {
    override fun compare(l: ModelContactCity?, r: ModelContactCity?): Int {
        if (l == null || r == null) {
            return 0
        }
        val lhsSortLetters = l.pys!!.substring(0, 1).toUpperCase()
        val rhsSortLetters = r.pys!!.substring(0, 1).toUpperCase()
        return if (lhsSortLetters == null || rhsSortLetters == null) {
            0
        } else lhsSortLetters.compareTo(rhsSortLetters)
    }
}