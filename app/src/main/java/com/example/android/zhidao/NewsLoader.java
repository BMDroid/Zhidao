package com.example.android.zhidao;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * AsyncTaskLoader for News.
 * Created by Jianyuan on 10/7/2016.
 */

public class NewsLoader extends AsyncTaskLoader<List<News>>{

    private ArrayList<String> mUrl;

    public NewsLoader(Context context, ArrayList<String> url){
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<News> loadInBackground() {
        if(mUrl == null) return null;
        List<News> all = new ArrayList<>();
        for(String url : mUrl){
            List<News> newsList = QueryUtils.fetchNews(url);
            all.addAll(newsList);
        }
        return all;
    }
}
