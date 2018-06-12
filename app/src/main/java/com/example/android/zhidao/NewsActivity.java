package com.example.android.zhidao;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import static android.view.View.GONE;


public class NewsActivity extends AppCompatActivity {
    private static final String ORIGINAL = "Original: ";
    private TextView mTitle;
    private TextView mAuthors;
    private ImageView mImageView;
    private TextView mBody;
    private TextView mUrl;
    private String url;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        String title = getIntent().getStringExtra("title");
        String authors = getIntent().getStringExtra("authors");
        String body = getIntent().getStringExtra("body");
        Bitmap img = getIntent().getParcelableExtra("img");
        mTitle = (TextView) findViewById(R.id.title);
        mTitle.setText(title);
        mAuthors = (TextView) findViewById(R.id.authors);
        mAuthors.setText(authors);
        if(authors.equals("")) mAuthors.setVisibility(GONE);
        mImageView = (ImageView) findViewById(R.id.img);
        mImageView.setImageBitmap(img);
        if(img == null) mImageView.setVisibility(GONE);
        mBody = (TextView) findViewById(R.id.body);
        mBody.setText(body);
        mUrl = (TextView) findViewById(R.id.url);
        url = getIntent().getStringExtra("url");
        String text = ORIGINAL + url;
        mUrl.setText(text);
        mUrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse(url);
                Intent browser = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(browser);
            }
        });
    }
}
