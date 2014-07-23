package com.swissarmyutility.data;

import android.content.Context;

import com.swissarmyutility.dataModel.HeadTailModel;

import java.sql.SQLException;
import java.util.List;



public class DatabaseManager {

	static private DatabaseManager instance;

	static public void init(Context ctx) {

			instance = new DatabaseManager(ctx);

	}

	static public DatabaseManager getInstance() {
		return instance;
	}

	private DatabaseHelper helper;
	private DatabaseManager(Context ctx) {
		helper = new DatabaseHelper(ctx);
	}

	private DatabaseHelper getHelper() {
		return helper;
	}

	public List<HeadTailModel> getAllWatchLists() {
		List<HeadTailModel> headTail = null;
		try {
            headTail = getHelper().HeadTailListDao().queryForAll();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return headTail;
	}

	public void addWishLi(HeadTailModel l) {
		try {
			getHelper().HeadTailListDao().create(l);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public HeadTailModel getHeadTailWithId(int wishListId) {
        HeadTailModel headTail = null;
		try {
            headTail = getHelper().HeadTailListDao().queryForId(wishListId);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return headTail;
	}


}