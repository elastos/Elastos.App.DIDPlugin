package com.ela.wallet.sdk.didlibrary.bean;


public class WordModel {

    private String word;
    private boolean clicked;

    public WordModel() {

    }

    public WordModel(String word, boolean clicked) {
        this.word = word;
        this.clicked = clicked;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public boolean isClicked() {
        return clicked;
    }

    public void setClicked(boolean clicked) {
        this.clicked = clicked;
    }
}
