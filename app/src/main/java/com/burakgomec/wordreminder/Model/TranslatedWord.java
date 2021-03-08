package com.burakgomec.wordreminder.Model;

public class TranslatedWord {

    private int id;
    private final String firstWord;
    private final String secondWord;

    public TranslatedWord(int id, String firstWord, String secondWord) {
        this.id = id;
        this.firstWord = firstWord;
        this.secondWord = secondWord;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getFirstWord() {
        return firstWord;
    }

    public String getSecondWord() {
        return secondWord;
    }
}
