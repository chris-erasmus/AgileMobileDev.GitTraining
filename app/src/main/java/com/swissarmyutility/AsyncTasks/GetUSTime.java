package com.swissarmyutility.AsyncTasks;

import android.content.Context;
import android.os.AsyncTask;
import com.swissarmyutility.Entity.Time;
import com.swissarmyutility.Parser.TimeDataParser;
import com.swissarmyutility.Utility.HttpConnection;
import org.apache.http.HttpEntity;
import java.io.InputStream;
/**
 * Created by hemant.bareja on 28-07-2014.
 */
public class GetUSTime extends AsyncTask<String, Void, String> {
    private Context context;
    String responseStr = null;
    InputStream is = null;
    Time time;
    HttpEntity responseEntity;
    DataDownloadListener dataDownloadListener;
    public GetUSTime(Context context) {
        this.context = context;
    }
    @Override
    protected String doInBackground(String... params) {
        responseStr = HttpConnection.getData(context, params[0]);
        if (responseStr != null) {
            time = new TimeDataParser().parseTimeData(responseStr);
            //   System.out.println("response in if" + responseStr);
        } else {
            System.out.println("happy coding");
        }
        return responseStr;
    }
    public void setDataDownloadListener(DataDownloadListener dataDownloadListener) {
        this.dataDownloadListener = dataDownloadListener;
    }
    @Override
    protected void onPostExecute(String result) {

        super.onPostExecute(result);
        if(result != null)
        {
            dataDownloadListener.dataDownloadedSuccessfully(time);
        }
        else
            dataDownloadListener.dataDownloadFailed();
    }
    public static interface DataDownloadListener {
        void dataDownloadedSuccessfully(Time data);
        void dataDownloadFailed();
    }
}

