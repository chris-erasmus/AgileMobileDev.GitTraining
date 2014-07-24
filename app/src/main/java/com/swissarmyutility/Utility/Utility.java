package com.swissarmyutility.Utility;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by piyush.sharma on 7/18/2014.
 */
public class Utility {

    public static boolean isNetworkAvailable(Context inContext)
    {
        ConnectivityManager ConnectMgr = (ConnectivityManager) inContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(ConnectMgr == null) return false;
        NetworkInfo NetInfo = ConnectMgr.getActiveNetworkInfo();
        if(NetInfo == null) return false;
        return NetInfo.isConnected();
    }
}
