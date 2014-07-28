package com.swissarmyutility.data;

import android.content.Context;

import com.swissarmyutility.HeadTails.CoinDao;
import com.swissarmyutility.dataModel.WeightTrackModel;

import java.sql.SQLException;
import java.util.List;



public class DatabaseManager {

	static private DatabaseManager instance;


	static public DatabaseManager getInstance(Context ctx) {
		if(instance == null)
            instance = new DatabaseManager(ctx);
        return instance;
	}

	private DatabaseHelper helper;
	private DatabaseManager(Context ctx) {
		helper = new DatabaseHelper(ctx);
	}

	private DatabaseHelper getHelper() {
		return helper;
	}

	public List<CoinDao> getAllFlipResults() {
		List<CoinDao> headTail = null;
		try {
            headTail = getHelper().getCoinDao().queryForAll();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return headTail;
	}

	public void addFlipResult(CoinDao l) {
		try {
			getHelper().getCoinDao().create(l);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

    /////Weight Tracker
    public List<WeightTrackModel> getAllWeightTrackerLists() {
        List<WeightTrackModel> weightTrackModel = null;
        try {
            weightTrackModel = getHelper().WeightTrackModelListDao().queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return weightTrackModel;
    }

    public void addWeightTrackList(WeightTrackModel l) {
        try {
            getHelper().WeightTrackModelListDao().create(l);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public WeightTrackModel getWeightTrackWithId(int WeightTrackListId) {
        WeightTrackModel weightTrackModel = null;
        try {
            weightTrackModel = getHelper().WeightTrackModelListDao().queryForId(WeightTrackListId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return weightTrackModel;
    }







}