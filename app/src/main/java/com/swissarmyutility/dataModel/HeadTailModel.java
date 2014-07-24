package com.swissarmyutility.dataModel;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
@DatabaseTable
public class HeadTailModel {
    @DatabaseField(generatedId=true)
    private int id;
    @DatabaseField
    private int head;
    //    @DatabaseField
    //    private int tail;

    public int getToss() {
        return head;
    }

    public void setToss(int head) {
        this.head = head;

    }



    //    public int getTail() {
    //        return tail;
    //    }
    //
    //    public void setTail(int tail) {
    //        this.tail = tail;
    //        Utils.logV("head = "+head);
    //    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}