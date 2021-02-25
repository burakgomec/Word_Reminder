package com.burakgomec.wordreminder;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class Database extends SQLiteOpenHelper {

    SQLiteDatabase database;
    ContentValues contentValues;
    TranslatedWord translatedWord;


    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TablesInfo.WordEntry.TABLE_NAME + " (" +
                    "id" + " INTEGER PRIMARY KEY," +
                    TablesInfo.WordEntry.COLUMN_NAME_FIRST_TRANSLATED_WORD + " TEXT," +
                    TablesInfo.WordEntry.COLUMN_NAME_SECOND_TRANSLATED_WORD + " TEXT)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TablesInfo.WordEntry.TABLE_NAME;

    private static final String SQL_GET_WORDS = "SELECT * FROM "+TablesInfo.WordEntry.TABLE_NAME;

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "TranslatedWord.db";


    public Database(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public boolean saveWords(String word1, String word2){
        database = this.getWritableDatabase();
        contentValues = new ContentValues();
        contentValues.put(TablesInfo.WordEntry.COLUMN_NAME_FIRST_TRANSLATED_WORD,word1);
        contentValues.put(TablesInfo.WordEntry.COLUMN_NAME_SECOND_TRANSLATED_WORD,word2);
        long result = database.insert(TablesInfo.WordEntry.TABLE_NAME, null, contentValues);
        database.close();
        return result != -1;
    }

    public ArrayList<TranslatedWord> getWords(){
        ArrayList<TranslatedWord> translatedWords = new ArrayList<>();
        database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery(SQL_GET_WORDS,null);
        while (cursor.moveToNext()){
            translatedWord = new TranslatedWord(cursor.getInt(cursor.getColumnIndex("id")),
                    cursor.getString(cursor.getColumnIndex(TablesInfo.WordEntry.COLUMN_NAME_FIRST_TRANSLATED_WORD)),
                    cursor.getString(cursor.getColumnIndex(TablesInfo.WordEntry.COLUMN_NAME_SECOND_TRANSLATED_WORD)));
            translatedWords.add(translatedWord);
        }

        cursor.close();
        database.close();
        return translatedWords;
    }

    public void deleteWord(String id){
        database = this.getWritableDatabase();
        String selection = "id" + "=?";
        database.delete(TablesInfo.WordEntry.TABLE_NAME,selection,new String[]{id});
        database.close();
    }

    public void deleteAllWords(){
        database = this.getWritableDatabase();
        database.delete(TablesInfo.WordEntry.TABLE_NAME,null,null);
        database.close();
    }

}
