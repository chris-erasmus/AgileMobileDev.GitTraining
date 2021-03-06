package com.swissarmyutility.dataModel;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.ArrayList;

/**
 * Created by piyush.sharma on 7/16/2014.
 */
public class WordsmythDictionaryData
{
    private String headWord;
    private ArrayList<Spelling> spellingList;
    private ArrayList<Pronunciation> pronunciationList;
    private ArrayList<Part> partList;
    private ArrayList<String> featuresWordHistoryList;
    private ArrayList<WePhrase> wePhraseList;

    public String getHeadWord() {
        return headWord;
    }

    public void setHeadWord(String headWord) {
        this.headWord = headWord;
    }

    public ArrayList<Spelling> getSpellingList() {
        return spellingList;
    }

    public void setSpellingList(ArrayList<Spelling> spellingList) {
        this.spellingList = spellingList;
    }

    public ArrayList<Pronunciation> getPronunciationList() {
        return pronunciationList;
    }

    public void setPronunciationList(ArrayList<Pronunciation> pronunciationList) {
        this.pronunciationList = pronunciationList;
    }

    public ArrayList<Part> getPartList() {
        return partList;
    }

    public void setPartList(ArrayList<Part> partList) {
        this.partList = partList;
    }

    public ArrayList<String> getFeaturesWordHistoryList() {
        return featuresWordHistoryList;
    }

    public void setFeaturesWordHistoryList(ArrayList<String> featuresWordHistoryList) {
        this.featuresWordHistoryList = featuresWordHistoryList;
    }

    public ArrayList<WePhrase> getWePhraseList() {
        return wePhraseList;
    }

    public void setWePhraseList(ArrayList<WePhrase> wePhraseList) {
        this.wePhraseList = wePhraseList;
    }


}
