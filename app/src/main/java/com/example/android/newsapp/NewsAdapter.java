package com.example.android.newsapp;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.newsapp.Model.Contract;
import com.example.android.newsapp.Model.News;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.sql.Date;
import java.util.ArrayList;

/**
 * Created by ppatel87 on 6/24/2017.
 */

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsAdapterViewHolder> {

    static final String TAG = "NewsAdapter";
    private Cursor cursor;
    private ItemClickListener listener;
    private  Context context;



    public NewsAdapter(Cursor cursor ,ItemClickListener listener)
    {
           // this.mNewsData=mNewsData;
            this.listener=listener;
            this.cursor=cursor;
    }

    public  interface  ItemClickListener
    {
        void onItemClick(Cursor cursor, int clickedItemIndex, String url);
    }

    public void swapCursor(Cursor newCursor){
       this.cursor=newCursor;
        this.notifyDataSetChanged();

    }

    public class NewsAdapterViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener{

        public TextView mAuthor;
        public TextView mTitle;
        public TextView mDesc;
        public TextView mUrl;
        public TextView mDate;
        ImageView mImage;
        String auth;
        String title;
        String desc;
        String url;
        String image;
        String date;

        long id;

        public NewsAdapterViewHolder (View view)
        {
            super(view);

            mAuthor =(TextView) view.findViewById(R.id.author);
            mTitle = (TextView) view.findViewById(R.id.title);
            mDesc = (TextView) view.findViewById(R.id.desc);
           // mUrl =(TextView) view.findViewById(R.id.url);
            mImage = (ImageView) view.findViewById(R.id.image);
            mDate =(TextView) view.findViewById(R.id.date);
            view.setOnClickListener(this);
        }


        public void bind(NewsAdapterViewHolder holder, int pos){
            Log.d(TAG,"jiji"+pos);
             cursor.moveToPosition(pos);
            id = cursor.getLong(cursor.getColumnIndex(Contract.TABLE_TODO._ID));

            auth=cursor.getString(cursor.getColumnIndex(Contract.TABLE_TODO.COLUMN_NAME_AUTHER));
            title=cursor.getString(cursor.getColumnIndex(Contract.TABLE_TODO.COLUMN_NAME_TITLE));
            desc=cursor.getString(cursor.getColumnIndex(Contract.TABLE_TODO.COLUMN_NAME_DESC));
            url=cursor.getString(cursor.getColumnIndex(Contract.TABLE_TODO.COLUMN_NAME_URL));
            image=cursor.getString(cursor.getColumnIndex(Contract.TABLE_TODO.COLUMN_NAME_IMAGE));
            date=cursor.getString(cursor.getColumnIndex(Contract.TABLE_TODO.COLUMN_NAME_DATE));


            mAuthor.setText(auth);
            mTitle.setText(title);
            mDesc.setText(desc);
         //   mUrl.setText(url);
            mDate.setText(date);
            // url.setText(repo.getUrl());

         //Checking imageUrl and displaying Image using Picasso

            if(image !=null)
            {
                Log.d(TAG, ""+image);
                Uri pathUri = Uri.fromFile(new File(image));
                Picasso.with(holder.mImage.getContext()).load(image).into(mImage);
            }

            holder.itemView.setTag(id);
        }

        @Override
        public void onClick(View view)
        {
            if(url !=null) {
                int pos = getAdapterPosition();
                listener.onItemClick(cursor, pos, url);
            }
        }



    }

    @Override
    public NewsAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup,int viewtype)
    {

        Log.d(TAG,"create");
        Context context=viewGroup.getContext();
        int layoutIdForListItem = R.layout.news_list_item;
        LayoutInflater inflater=LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately=false;

        View view=inflater.inflate(layoutIdForListItem,viewGroup,shouldAttachToParentImmediately);
        return new NewsAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NewsAdapterViewHolder newsAdapterViewHolder,int Position)
    {

        newsAdapterViewHolder.bind(newsAdapterViewHolder,Position);

    }


    @Override
    public int getItemCount()
    {
       return cursor.getCount();
    }




}
