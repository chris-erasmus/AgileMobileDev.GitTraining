package com.swissarmyutility.Utility;
import android.content.Context;
import com.swissarmyutility.TimeZone.Client;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import java.io.BufferedReader;
import java.io.InputStreamReader;
/**
 * Created by hemant.bareja on 28-07-2014.
 */
public class HttpConnection {
   static StringBuilder sb;
   public static String getData(Context context,String url)
   {
       // Instantiate the custom HttpClient
       DefaultHttpClient client = new Client(
               context);
       HttpGet get = new HttpGet(url);
       try {
           HttpResponse getResponse = client.execute(get);
           HttpEntity responseEntity = getResponse.getEntity();
           BufferedReader reader = new BufferedReader(
                   new InputStreamReader(responseEntity.getContent(),
                           "UTF-8")
           );
           sb = new StringBuilder();
           sb.append(reader.readLine() + "\n");
           int cp;
           while ((cp = reader.read()) != -1) {
               sb.append((char) cp);
           }
           reader.close();
       }
       catch (Exception e)
       {
           e.printStackTrace();
       }
       return sb.toString();
   }
}
