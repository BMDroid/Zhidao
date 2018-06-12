package com.example.android.zhidao;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import static android.view.View.GONE;

/**
 * A RecyclerView Adapter for News.
 * Created by Jianyuan on 10/7/2016.
 */

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {

    private ArrayList<News> mNewsList;

    public static class NewsViewHolder extends RecyclerView.ViewHolder {
        private TextView mSection;
        private TextView mTitle;
        private TextView mDescription;

        public NewsViewHolder(View v) {
            super(v);
            mSection = (TextView) v.findViewById(R.id.section);
            mTitle = (TextView) v.findViewById(R.id.title);
            mDescription = (TextView) v.findViewById(R.id.description);
        }
    }

    public NewsAdapter(ArrayList<News> newsList) {
        mNewsList = newsList;
    }

    @Override
    public NewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_news, parent, false);
        return new NewsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(NewsViewHolder holder, int position) {
        final News mNews = mNewsList.get(position);
        holder.mSection.setText(mNews.getSection());
        holder.mTitle.setText(mNews.getWebTitle());
        holder.mDescription.setText(mNews.getDescription());
        if(mNews.getSection() == null) holder.mSection.setVisibility(GONE);
        if(mNews.getDescription() == null) holder.mDescription.setVisibility(GONE);
    }

    @Override
    public int getItemCount() {
        if (mNewsList == null) return 0;
        return mNewsList.size();
    }
}
