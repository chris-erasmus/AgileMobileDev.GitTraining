package com.swissarmyutility.dataModel;

import java.util.ArrayList;

/**
 * Created by piyush.sharma on 7/16/2014.
 */
public class DictionaryData
{
    String headWord;
    ArrayList<Spelling> spellingList;
    ArrayList<Pronunciation> pronunciationList;
    ArrayList<Part> partList;
    ArrayList<String> featuresWordHistoryList;
    ArrayList<WePhrase> wePhraseList;

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
