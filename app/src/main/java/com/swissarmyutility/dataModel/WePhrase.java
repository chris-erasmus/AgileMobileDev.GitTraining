package com.swissarmyutility.dataModel;

import java.util.ArrayList;

/**
 * Created by piyush.sharma on 7/16/2014.
 */
public class WePhrase
{
    private String title;
    private ArrayList<String> weWordList;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<String> getWeWordList() {
        return weWordList;
    }

    public void setWeWordList(ArrayList<String> weWordList) {
        this.weWordList = weWordList;
    }
}
