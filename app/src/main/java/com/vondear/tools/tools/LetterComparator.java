package com.vondear.tools.tools;

import com.vondear.tools.bean.ContactCityBean;

import java.util.Comparator;


public class LetterComparator implements Comparator<ContactCityBean> {

    @Override
    public int compare(ContactCityBean l, ContactCityBean r) {
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