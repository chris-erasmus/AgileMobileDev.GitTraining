package com.swissarmyutility.dataModel;

import com.j256.ormlite.field.DatabaseField;

/**
 * Created by maidul.islam on 7/23/2014.
 */
public class WeightTrackModel {
    @DatabaseField(generatedId=true)
    private int id;
    @DatabaseField
    private String weight;
    @DatabaseField
    private String time;

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
        System.out.println("weight>>>>>>>>>>>"+weight);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
        System.out.println("time>>>>>>>>>>>"+time);
    }


}
