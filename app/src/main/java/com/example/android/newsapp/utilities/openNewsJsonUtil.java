package com.example.android.newsapp.utilities;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.android.newsapp.Model.Contract;
import com.example.android.newsapp.Model.DBHelper;
import com.example.android.newsapp.Model.News;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.jar.JarException;

/**
 * Created by ppatel87 on 6/19/2017.
 */

public class openNewsJsonUtil
{
    private static DBHelper helper;
    private static Cursor cursor;
    private static SQLiteDatabase db;
    private static final String TAG = "dbhelper";

    public static ArrayList<News> getSimpleNewsJson(Context context, String NewsJsonString)
            throws JSONException
    {

        ArrayList<News> parsenewsdata=new ArrayList<>();

        JSONObject newsjason=new JSONObject(NewsJsonString);


        if (newsjason.has("status"))
        {

                String status = newsjason.getString("status");

                switch (status) {
                    case "ok":
                        break;

                    default:
                    /* Server probably down */
                        return null;
                }



        }


         final String TAG_ARTICLES = "articles";
         final String TAG_AUTHER = "author";
         final String TAG_TITLE= "title";
         final String TAG_DESC = "description";
         final String TAG_URL = "url";
         final String TAG_IMAGE = "urlToImage";
         final String TAG_PUBLISH = "publishedAt";

        JSONArray news=newsjason.getJSONArray(TAG_ARTICLES);


        for(int i=0; i<news.length();i++)
        {

            JSONObject n=news.getJSONObject(i);


            String auther,title,desc,url,image,publish;

            auther=n.getString(TAG_AUTHER);
            title=n.getString(TAG_TITLE);
            desc=n.getString(TAG_DESC);
            url=n.getString(TAG_URL);
            image=n.getString(TAG_IMAGE);
            publish=n.getString(TAG_PUBLISH);

            News nw=new News(auther,title,desc,url,image,publish);


           parsenewsdata.add(nw);

        }

        if(parsenewsdata.size()>0)
        {
            Log.d(TAG,"pass tahy");
        }

        return parsenewsdata;

    }

    public String formatDate(String date) {
        return String.format("%04d-%02d-%02d", date);
    }
}
