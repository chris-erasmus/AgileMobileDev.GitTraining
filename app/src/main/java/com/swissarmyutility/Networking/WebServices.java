package com.swissarmyutility.Networking;

/**
 * Created by piyush.sharma on 7/15/2014.
 */



import com.swissarmyutility.Parser.DictionaryDataParser;
import com.swissarmyutility.dataModel.WordsmythDictionaryData;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;

public class WebServices
{
    private String DICTIONARY_URL = "http://dev.wordsmyth.net/testdir/web_services/all_fields_entry.xml.php?ent=%s&dict=1";
    public static String MUSIC_URL = "http://www.moviesoundclips.net/effects/weapons/riflecock-1.wav";
    public static String IMAGE_URL = "http://kids.wordsmyth.net/media/wcdt/image/";
    private static WebServices mSingletonWebServicesInstance;

    HttpClient mHttpClient;
    HttpGet mHttpGet;

    private WebServices()
    {

    }

    public static WebServices getSingletonInstance()
    {
        if(mSingletonWebServicesInstance == null)
        {
            mSingletonWebServicesInstance = new WebServices();
            mSingletonWebServicesInstance.initializeHttpClientAndHttpGetObject();
        }
        return mSingletonWebServicesInstance;
    }

   private void initializeHttpClientAndHttpGetObject()
   {
        mHttpClient = new DefaultHttpClient();
        mHttpGet = new HttpGet();
   }

    private String fetchXMLData(String dataURL)
    {
        String xmlString = "";
        try {

            mHttpGet.setURI(new URI(dataURL));
            StringBuilder dataBuffer = new StringBuilder();
            String currentDataLine = "";
            HttpResponse serverDataResponse = null;
            serverDataResponse = mHttpClient.execute(mHttpGet);
            System.out.println(serverDataResponse.getStatusLine());
            HttpEntity httpResponseEntity = serverDataResponse.getEntity();
            BufferedReader bufferReader = null;
            bufferReader = new BufferedReader(new InputStreamReader(httpResponseEntity.getContent()));
            while ((currentDataLine = bufferReader.readLine()) != null)
            {
                dataBuffer.append(currentDataLine);
            }
            xmlString = dataBuffer.toString();
            bufferReader.close();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return xmlString;
    }

    public WordsmythDictionaryData getDictionaryData(String searchString)
    {
        WordsmythDictionaryData dictionaryData = null;
        String searchURL = DICTIONARY_URL.replace("%s",searchString);
        String placesJsonData = fetchXMLData(searchURL);
        if(placesJsonData != null)
        {
            DictionaryDataParser parser = new DictionaryDataParser();

            dictionaryData = parser.parseDictionaryData(placesJsonData);
        }
        return  dictionaryData;
    }

}
