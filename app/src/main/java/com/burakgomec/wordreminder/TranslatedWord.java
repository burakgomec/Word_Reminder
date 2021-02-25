package com.burakgomec.wordreminder;

public class TranslatedWord {

    private int id;
    private String firstWord;
    private String secondWord;

    public TranslatedWord(int id, String firstWord, String secondWord) {
        this.id = id;
        this.firstWord = firstWord;
        this.secondWord = secondWord;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setFirstWord(String firstWord) {
        this.firstWord = firstWord;
    }

    public void setSecondWord(String secondWord) {
        this.secondWord = secondWord;
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
