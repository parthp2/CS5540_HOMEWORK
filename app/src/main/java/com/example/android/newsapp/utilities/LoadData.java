package com.example.android.newsapp.utilities;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.android.newsapp.MainActivity;
import com.example.android.newsapp.Model.Contract;
import com.example.android.newsapp.Model.DBHelper;
import com.example.android.newsapp.Model.News;

import java.net.URL;
import java.util.ArrayList;


/**
 * Created by ppatel87 on 7/26/2017. this class have the method to load data from api to database
 */

public class LoadData {


    private static DBHelper helper;
    private static Cursor cursor;
    private static  SQLiteDatabase db;
    private static Context context;




    public static void DatabaseLoad(Context context)
    {

        helper = new DBHelper(context);
        db = helper.getWritableDatabase();

        ArrayList<News> simpleNeWsJson=null;

        URL newsurl= NetworkUtils.buildUrl();

        try {
            String JsonNewsData = NetworkUtils.getResponseFromHttpUrl(newsurl);

            simpleNeWsJson = openNewsJsonUtil.getSimpleNewsJson(context, JsonNewsData);

        }
        catch(Exception e)
        {
            e.printStackTrace();

        }


        deleteData(db);

        db.beginTransaction();
        try
        {
            for (News news : simpleNeWsJson) {
                ContentValues cv = new ContentValues();
                cv.put(Contract.TABLE_TODO.COLUMN_NAME_AUTHER, news.getAuthor());
                cv.put(Contract.TABLE_TODO.COLUMN_NAME_TITLE, news.getTitle());
                cv.put(Contract.TABLE_TODO.COLUMN_NAME_DESC, news.getDesc());
                cv.put(Contract.TABLE_TODO.COLUMN_NAME_URL, news.getUrl());
                cv.put(Contract.TABLE_TODO.COLUMN_NAME_IMAGE, news.getImageurl());
                cv.put(Contract.TABLE_TODO.COLUMN_NAME_DATE, news.getDate());
                db.insert(Contract.TABLE_TODO.TABLE_NAME, null, cv);


            }
            db.setTransactionSuccessful();

        }
        finally
        {
            db.endTransaction();

        }

    }

    public static void deleteData(SQLiteDatabase db) {
        db.delete(Contract.TABLE_TODO.TABLE_NAME, null, null);
    }


}
