package com.swissarmyutility.Utility;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;


import com.app.swissarmyutility.R;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by piyush.sharma on 7/15/2014.
 */
public class ImageLoader
{
    private Context mContext;
    private ExecutorService mExecutorService;
    private Map<String,Bitmap> mUrlBitmapRTagMap = Collections.synchronizedMap(new WeakHashMap<String, Bitmap>());
    private Map<ImageView,String> mImageViewUrlTagMap = Collections.synchronizedMap(new WeakHashMap<ImageView, String>());

    public ImageLoader(Context context)
    {
        mContext = context;
        mExecutorService = Executors.newFixedThreadPool(5);
    }

     public void displayImage(ImageView imageView , String imageURL)
     {
         mImageViewUrlTagMap.put(imageView,imageURL);
         Bitmap bmp = mUrlBitmapRTagMap.get(imageURL);
         if(bmp != null)
         {
            imageView.setImageBitmap(bmp);
         }
         else
         {
             imageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.placeholder));
             putImageURLInFetchingBitmapQueue(imageURL,imageView);
         }
     }

    private void putImageURLInFetchingBitmapQueue(String imageURL,ImageView imageView)
    {
         BitmapToLoad bmpToLoad = new BitmapToLoad(imageURL,imageView);
         mExecutorService.submit(new BitmapLoaderTask(bmpToLoad));
    }

    private class BitmapToLoad
     {
         String mImageURL;
         ImageView mImageView;
         BitmapToLoad(String url,ImageView imageView)
         {
             mImageURL = url;
             mImageView = imageView;
         }
     }

    private class BitmapLoaderTask implements Runnable
    {
        BitmapToLoad mBitmapToLoad;
        BitmapLoaderTask(BitmapToLoad bitmapToLoad)
        {
            mBitmapToLoad = bitmapToLoad;
        }
        @Override
        public void run() {
          if(isImageViewReused(mBitmapToLoad))
              return;
            Bitmap bmp = fetchBitmap(mBitmapToLoad.mImageURL);
            mUrlBitmapRTagMap.put(mBitmapToLoad.mImageURL,bmp);
            if(isImageViewReused(mBitmapToLoad))
                return;
           ((Activity)mContext).runOnUiThread(new BitmapDisplayer(bmp,mBitmapToLoad));
        }
    }

    private boolean isImageViewReused(BitmapToLoad bitmapToLoad)
    {
         String tagURL  = mImageViewUrlTagMap.get(bitmapToLoad.mImageView);
        if(tagURL == null || !tagURL.equals(bitmapToLoad.mImageURL))
           return  true;
        return false;
    }

    private Bitmap fetchBitmap(String imageURL)
    {
        Bitmap bmp = null;
        try {
            URL url = new URL(imageURL);
            HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
            urlConnection.setConnectTimeout(30000);
            urlConnection.setReadTimeout(30000);
            urlConnection.setInstanceFollowRedirects(true);
            InputStream inputStream = urlConnection.getInputStream();
            if(inputStream != null)
            {
                BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
                 bmp = BitmapFactory.decodeStream(bufferedInputStream);
            }
        }
        catch (MalformedURLException e)
        {

        }
        catch (IOException e)
        {

        }
        catch (Exception e)
        {

        }
        return bmp;
    }

    private class BitmapDisplayer implements Runnable
    {
        private Bitmap mBitmap;
        private BitmapToLoad mBitmapToLoad;
        BitmapDisplayer(Bitmap bmp,BitmapToLoad bitmapToLoad)
        {
            mBitmap = bmp;
            mBitmapToLoad = bitmapToLoad;

        }
        @Override
        public void run() {
           if(isImageViewReused(mBitmapToLoad))
               return;
            if(mBitmap == null)
                mBitmapToLoad.mImageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.placeholder));
            else
                mBitmapToLoad.mImageView.setImageBitmap(mBitmap);
        }
    }

public void clearCache()
{
    if(mUrlBitmapRTagMap != null)
            mUrlBitmapRTagMap.clear();

    if(mImageViewUrlTagMap != null)
          mImageViewUrlTagMap.clear();
}
}
