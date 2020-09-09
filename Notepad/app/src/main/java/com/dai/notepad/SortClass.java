package com.dai.notepad;

import java.util.Comparator;


public class SortClass implements Comparator {
    @Override
    public int compare(Object o1, Object o2) {
        Note note1 = (Note) o1;
        Note note2 = (Note) o2;
        if (o1 == null || o2 == null) {
            return 0;
        }
        int flag = note2.getTime().compareTo(note1.getTime());
        return flag;

    }

}
