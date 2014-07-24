package com.swissarmyutility.dataModel;

import java.util.ArrayList;

/**
 * Created by piyush.sharma on 7/16/2014.
 */
public class Sense {
    String definition;
    ArrayList<String> spanishTranslationList;
    ArrayList<String> spanishExampleList;
    ArrayList<String> exampleList;
    ArrayList<String> imageList;
    ArrayList<String> synonymList;
    ArrayList<String> similarWordList;

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public ArrayList<String> getSpanishTranslationList() {
        return spanishTranslationList;
    }

    public void setSpanishTranslationList(ArrayList<String> spanishTranslationList) {
        this.spanishTranslationList = spanishTranslationList;
    }

    public ArrayList<String> getSpanishExampleList() {
        return spanishExampleList;
    }

    public void setSpanishExampleList(ArrayList<String> spanishExampleList) {
        this.spanishExampleList = spanishExampleList;
    }

    public ArrayList<String> getExampleList() {
        return exampleList;
    }

    public void setExampleList(ArrayList<String> exampleList) {
        this.exampleList = exampleList;
    }

    public ArrayList<String> getImageList() {
        return imageList;
    }

    public void setImageList(ArrayList<String> imageList) {
        this.imageList = imageList;
    }

    public ArrayList<String> getSynonymList() {
        return synonymList;
    }

    public void setSynonymList(ArrayList<String> synonymList) {
        this.synonymList = synonymList;
    }

    public ArrayList<String> getSimilarWordList() {
        return similarWordList;
    }

    public void setSimilarWordList(ArrayList<String> similarWordList) {
        this.similarWordList = similarWordList;
    }
}
