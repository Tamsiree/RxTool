package com.tamsiree.rxui.view.wavesidebar;


import com.tamsiree.rxui.model.ModelContactCity;

import java.util.Comparator;


/**
 * 首字母排序
 * 此类经供参考
 * @author tamsiree
 */
public class ComparatorLetter implements Comparator<ModelContactCity> {

    @Override
    public int compare(ModelContactCity l, ModelContactCity r) {
        if (l == null || r == null) {
            return 0;
        }

        String lhsSortLetters = l.pys.substring(0, 1).toUpperCase();
        String rhsSortLetters = r.pys.substring(0, 1).toUpperCase();
        if (lhsSortLetters == null || rhsSortLetters == null) {
            return 0;
        }
        return lhsSortLetters.compareTo(rhsSortLetters);
    }
}