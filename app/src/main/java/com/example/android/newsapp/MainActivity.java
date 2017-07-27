package com.example.android.newsapp;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.newsapp.Model.Contract;
import com.example.android.newsapp.Model.DBHelper;
import com.example.android.newsapp.Model.News;
import com.example.android.newsapp.utilities.Key;
import com.example.android.newsapp.utilities.LoadData;
import com.example.android.newsapp.utilities.NetworkUtils;
import com.example.android.newsapp.utilities.openNewsJsonUtil;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class

MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Void>,NewsAdapter.ItemClickListener{

    static final String TAG = "mainactivity";

    private RecyclerView mRecyclerView;

    private static final int NEWSAPP_LOADER = 22;

    private  TextView errorMessage;

    private ProgressBar progressBar;

    private NewsAdapter newsAdapter;

    private DBHelper helper;
    private Cursor cursor;
    private SQLiteDatabase db;

    private Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView =(RecyclerView) findViewById(R.id.recyclerview_news);


        progressBar =(ProgressBar) findViewById(R.id.pb_loader);

        errorMessage =(TextView) findViewById(R.id.error_message);



        LinearLayoutManager layoutManager=new LinearLayoutManager(this);

        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.setHasFixedSize(true);

        helper = new DBHelper(this);
        db = helper.getWritableDatabase();

        cursor = getAllItems(db);

        newsAdapter  =new NewsAdapter(cursor,this);
        SharedPreferences  f = PreferenceManager.getDefaultSharedPreferences(this);

        boolean check = f.getBoolean("RunFirst", true);
        if (check) {
            search();
            SharedPreferences.Editor editor = f.edit();
            editor.putBoolean("RunFirst", false);
            editor.commit();
        }

        if(cursor != null)
        {
            Log.d(TAG,"n");
        }


        newsAdapter.swapCursor(cursor);
        mRecyclerView.setAdapter(newsAdapter);

    }

    private void search()
    {


        LoaderManager loaderManager= getSupportLoaderManager();
       Loader<ArrayList<News>> newsloader= loaderManager.getLoader(NEWSAPP_LOADER);


        if(newsloader == null)
        {

            loaderManager.initLoader(NEWSAPP_LOADER, null, this).forceLoad();
        }
        else
        {
            loaderManager.restartLoader(NEWSAPP_LOADER, null, this).forceLoad();
        }

    }

    private void  showJsondata()
    {
        errorMessage.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showerror()
    {
        errorMessage.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onStart() {
        super.onStart();

        NewsSchedular.schedule(this);
    }

    @Override
    protected void onStop() {
        super.onStop();

        db.close();
        cursor.close();
        NewsSchedular.stopSchedular(this);
    }



        @Override
    public Loader<Void> onCreateLoader(int id,final Bundle args) {
        return new AsyncTaskLoader<Void>(this) {
            protected  void onStartLoading()
            {


                progressBar.setVisibility(View.VISIBLE);
            }


            @Override
            public Void loadInBackground() {

                LoadData.DatabaseLoad(MainActivity.this);



                return null;
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<Void> loader, Void data) {

        progressBar.setVisibility(View.INVISIBLE);



        if(cursor != null)
        {

            showJsondata();

            Log.d(TAG, "hh");


            newsAdapter = new NewsAdapter(cursor,this);
            mRecyclerView.setAdapter(newsAdapter);

        }
        else
        {
            showerror();
        }


    }


    @Override
    public void onLoaderReset(Loader<Void> loader) {

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search,menu);

        return  true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id =item.getItemId();

        if(id==R.id.action_serach)
        {
            search();
            return  true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void openWebPage(String url) {
        Uri webpage = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        if (intent.resolveActivity(getPackageManager()) != null)
        {
            startActivity(intent);
        }
    }

    private Cursor getAllItems(SQLiteDatabase db) {
        return db.query(
                Contract.TABLE_TODO.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                Contract.TABLE_TODO.COLUMN_NAME_DATE
        );
    }

    @Override
    public void onItemClick(Cursor cursor, int clickedItemIndex,String url) {

        Log.d(TAG, String.format("Url %s", url));
        openWebPage(url);

    }


}
