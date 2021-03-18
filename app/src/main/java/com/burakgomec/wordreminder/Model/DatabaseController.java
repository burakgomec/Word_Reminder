package com.burakgomec.wordreminder.Model;

import java.util.ArrayList;

public class DatabaseController { //Singleton Design Pattern
    //One object and one array list

    private static DatabaseController databaseController;
    private final ArrayList<TranslatedWord> translatedWordArrayList;
    public Boolean userPermissionCheck;

    public static  DatabaseController getInstance(){
        if(databaseController == null){
            databaseController = new DatabaseController();
        }
        return databaseController;
    }

    private DatabaseController(){
        translatedWordArrayList = new ArrayList<>();
    }


    public ArrayList<TranslatedWord> getTranslatedWordArrayList() {
        return translatedWordArrayList;
    }

}
