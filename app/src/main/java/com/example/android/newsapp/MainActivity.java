package com.example.android.newsapp;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.newsapp.Model.News;
import com.example.android.newsapp.utilities.Key;
import com.example.android.newsapp.utilities.NetworkUtils;
import com.example.android.newsapp.utilities.openNewsJsonUtil;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class

MainActivity extends AppCompatActivity {

    static final String TAG = "mainactivity";

    private RecyclerView mRecyclerView;

    private  TextView errorMessage;

    private ProgressBar progressBar;

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

    }

    private void search()
    {
        String n= Key.key_value;

        String key= n;

        new fetchNews().execute(key);

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


    public  class fetchNews extends AsyncTask<String ,Void ,ArrayList<News>>
    {
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected ArrayList<News> doInBackground(String... param)
        {
            if(param.length == 0)
            {
                return null;
            }

            String key=param[0];

            URL newsurl= NetworkUtils.buildUrl(key);


            try{
                String JsonNewsData = NetworkUtils.getResponseFromHttpUrl(newsurl);

               ArrayList<News> simpleNeWsJson= openNewsJsonUtil.getSimpleNewsJson(MainActivity.this,JsonNewsData);

                return simpleNeWsJson;
            }
            catch(Exception e)
            {
                e.printStackTrace();
                return null;
            }


        }

        @Override
        protected  void onPostExecute(final ArrayList<News> newsdata)
        {
            progressBar.setVisibility(View.INVISIBLE);

            if(newsdata != null)
            {

                showJsondata();

                NewsAdapter adapter = new NewsAdapter(newsdata, new NewsAdapter.ItemClickListener() {
                    @Override
                    public void onItemClick(int clickedItemIndex) {
                        String url = newsdata.get(clickedItemIndex).getUrl();

                        Log.d(TAG, String.format("Url %s", url));
                        openWebPage(url);
                    }
                });
                mRecyclerView.setAdapter(adapter);

            }
            else
            {
                showerror();
            }
        }
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
}
