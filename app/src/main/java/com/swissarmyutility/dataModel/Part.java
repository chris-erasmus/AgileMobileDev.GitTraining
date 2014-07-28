package com.swissarmyutility.dataModel;

import java.util.ArrayList;

/**
 * Created by piyush.sharma on 7/16/2014.
 */
public class Part {

    private ArrayList<String> nameList;
    private ArrayList<String> relatedWordList;
    private ArrayList<Sense> senseList;

    public ArrayList<String> getNameList() {
        return nameList;
    }

    public void setNameList(ArrayList<String> nameList) {
        this.nameList = nameList;
    }

    public ArrayList<String> getRelatedWordList() {
        return relatedWordList;
    }

    public void setRelatedWordList(ArrayList<String> relatedWordList) {
        this.relatedWordList = relatedWordList;
    }

    public ArrayList<Sense> getSenseList() {
        return senseList;
    }

    public void setSenseList(ArrayList<Sense> senseList) {
        this.senseList = senseList;
    }
}
