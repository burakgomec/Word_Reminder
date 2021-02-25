package com.burakgomec.wordreminder;

import android.provider.BaseColumns;

public class TablesInfo {

    private TablesInfo(){ }

    public static class WordEntry implements BaseColumns {
        public static final String TABLE_NAME = "words";
        public static final String COLUMN_NAME_FIRST_TRANSLATED_WORD = "firstWord";
        public static final String COLUMN_NAME_SECOND_TRANSLATED_WORD = "secondWord";
    }



}
