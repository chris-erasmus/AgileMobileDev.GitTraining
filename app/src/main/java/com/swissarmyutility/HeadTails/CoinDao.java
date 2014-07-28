package com.swissarmyutility.HeadTails;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
@DatabaseTable
public class CoinDao {
    @DatabaseField
    private boolean flipResult;

    public boolean getFlipResult() {
        return flipResult;
    }

    public void setFlipResult(boolean flipResult) {
        this.flipResult = flipResult;

    }

}