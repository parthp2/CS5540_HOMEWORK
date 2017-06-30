package com.example.android.newsapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.newsapp.Model.News;

import java.util.ArrayList;

/**
 * Created by ppatel87 on 6/24/2017.
 */

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsAdapterViewHolder> {


    private ArrayList<News> mNewsData;
    ItemClickListener listener;

    public NewsAdapter(ArrayList<News> mNewsData,ItemClickListener listener)
    {
            this.mNewsData=mNewsData;
            this.listener=listener;
    }

    public  interface  ItemClickListener
    {
        void onItemClick(int clickedItemIndex);
    }

    public class NewsAdapterViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener{

       // public final TextView mAuthor;
        public final TextView mTitle;
        public final TextView mDesc;
      //  public final TextView mUrl;
        public final TextView mDate;
      //  public final TextView mImage;


        public NewsAdapterViewHolder (View view)
        {
            super(view);

        //    mAuthor =(TextView) view.findViewById(R.id.author);
            mTitle = (TextView) view.findViewById(R.id.title);
            mDesc = (TextView) view.findViewById(R.id.desc);
         //   mUrl =(TextView) view.findViewById(R.id.url);
          //  mImage = (TextView) view.findViewById(R.id.image);
            mDate =(TextView) view.findViewById(R.id.date);
            view.setOnClickListener(this);
        }


        public void bind(int pos){
            News n = mNewsData.get(pos);
         //   mAuthor.setText(n.getAuthor());
            mTitle.setText(n.getTitle());
            mDesc.setText(n.getDesc());
         //   mUrl.setText(n.getUrl());
        //    mImage.setText(n.getImage());
            mDate.setText(n.getDate());
            // url.setText(repo.getUrl());
        }

        @Override
        public void onClick(View view)
        {
            int pos=getAdapterPosition();
            listener.onItemClick(pos);
        }



    }

    @Override
    public NewsAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup,int viewtype)
    {
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

        newsAdapterViewHolder.bind(Position);

    }


    @Override
    public int getItemCount()
    {
        if(mNewsData == null)
        {
            return 0;
        }

        return mNewsData.size();
    }




}
