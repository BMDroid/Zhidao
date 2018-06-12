package com.example.android.zhidao;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<News>>{

    private static final String REQUEST_URL_WORLD = "http://content.guardianapis.com/search?q=sections=world&api-key=test&show-tags=contributor";
    private static final String REQUEST_URL_US = "http://content.guardianapis.com/search?q=u.s.&api-key=test&show-tags=contributor";
    private static final String REQUEST_URL_CN = "http://content.guardianapis.com/search?q=china&api-key=test&show-tags=contributor";
    private static final String REQUEST_URL_GOOGLE = "http://content.guardianapis.com/search?q=google&api-key=test&show-tags=contributor";
    private static final String REQUEST_URL_APPLE = "http://content.guardianapis.com/search?q=apple&api-key=test&show-tags=contributor";
    private static final String REQUEST_URL_NFL = "http://content.guardianapis.com/search?q=nfl&api-key=test&show-tags=contributor";
    private static final String REQUEST_URL_MV = "http://content.guardianapis.com/search?q=movie&api-key=test&show-tags=contributor";
    private static final String REQUEST_URL_MU = "http://content.guardianapis.com/search?q=music&api-key=test&show-tags=contributor";
    private static final String REQUEST_URL_NBA = "http://content.guardianapis.com/search?q=nba&api-key=test&show-tags=contributor";
    private static final int EARTHQUAKE_LOADER_ID = 1;
    private ArrayList<String> requestUrl;
    private RecyclerView mRecyclerView;
    private TextView mEmptyView;
    private ProgressBar mLoadingBar;
    private ArrayList<News> mNewsList;
    private NewsAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestUrl = new ArrayList<>();
        mNewsList = new ArrayList<>();
        mEmptyView = (TextView) findViewById(R.id.empty_View);
        mLoadingBar = (ProgressBar) findViewById(R.id.loading_spinner);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(MainActivity.this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new NewsAdapter(mNewsList);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {

                    @Override
                    public void onItemClick(View view, int position) {
                        News news = mNewsList.get(position);
                        String title = news.getWebTitle();
                        String authors = news.getAuthorsString();
                        String body = news.getBody();
                        String url = news.getWebUrl();
                        Bitmap img = news.getImgBitmap();
                        Intent intent = new Intent(MainActivity.this, NewsActivity.class).putExtra("title", title)
                                .putExtra("authors", authors)
                                .putExtra("body", body)
                                .putExtra("url", url)
                                .putExtra("img", img);
                        startActivity(intent);
                    }
                    @Override
                    public void onItemLongPress(View childView, int position) {
                    }
                })
        );
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(EARTHQUAKE_LOADER_ID, null, this);
        } else {
            mLoadingBar.setVisibility(View.GONE);
            mEmptyView.setText(getResources().getString(R.string.no_internet));
        }
    }

    @Override
    public Loader<List<News>> onCreateLoader(int i, Bundle bundle) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean worldwide = sharedPreferences.getBoolean(getString(R.string.worldwide), false);
        boolean us = sharedPreferences.getBoolean(getString(R.string.us), false);
        boolean cn = sharedPreferences.getBoolean(getString(R.string.china), false);
        boolean google = sharedPreferences.getBoolean(getString(R.string.google), true);
        boolean apple = sharedPreferences.getBoolean(getString(R.string.apple), false);
        boolean mv = sharedPreferences.getBoolean(getString(R.string.movie), false);
        boolean mu = sharedPreferences.getBoolean(getString(R.string.music), false);
        boolean nba = sharedPreferences.getBoolean(getString(R.string.nba), false);
        boolean nfl = sharedPreferences.getBoolean(getString(R.string.nfl), false);
        if(worldwide) requestUrl.add(REQUEST_URL_WORLD);
        if(us) requestUrl.add(REQUEST_URL_US);
        if(cn) requestUrl.add(REQUEST_URL_CN);
        if(google) requestUrl.add(REQUEST_URL_GOOGLE);
        if(apple) requestUrl.add(REQUEST_URL_APPLE);
        if(mv) requestUrl.add(REQUEST_URL_MV);
        if(mu) requestUrl.add(REQUEST_URL_MU);
        if(nba) requestUrl.add(REQUEST_URL_NBA);
        if(nfl) requestUrl.add(REQUEST_URL_NFL);
        return new NewsLoader(this, requestUrl);
    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> newses) {
        mLoadingBar.setVisibility(View.GONE);
        if (newses != null && !newses.isEmpty()) {
            mNewsList = new ArrayList<>(newses);
            mAdapter = new NewsAdapter(mNewsList);
            mRecyclerView.setAdapter(mAdapter);
        }else{
            mEmptyView.setText(getResources().getText(R.string.no_news));
        }
    }

    @Override
    public void onLoaderReset(Loader<List<News>> loader) {
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
