package com.example.android.zhidao;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * A news class store its information including title, description, etc.
 * Created by Jianyuan on 10/7/2016.
 */

public class News {
    private String webTitle;
    private String webUrl;
    private ArrayList<String> authors;
    private String description;
    private String keywords;
    private String section;
    private String imgUrl;
    private String body;
    private Bitmap imgBitmap;

    public News(String webTitle, String webUrl) {
        this.webTitle = webTitle;
        this.webUrl = webUrl;
    }

    public News(String webTitle, String webUrl, ArrayList<String> authors, String description, String keywords, String section, String imgUrl, String body) {
        this(webTitle, webUrl);
        this.authors = authors;
        this.description = description;
        this.keywords = keywords;
        this.section = section;
        this.imgUrl = imgUrl;
        this.body = body;
    }

    public void setAuthors(ArrayList<String> authors) {
        this.authors = authors;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setImgBitmap(Bitmap imgBitmap) {
        this.imgBitmap = imgBitmap;
    }

    public String getWebTitle() {
        return webTitle;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public ArrayList<String> getAuthors() {
        return authors;
    }

    public String getAuthorsString() {
        String authorsString = "";
        authorsString = Arrays.toString(authors.toArray(new String[authors.size()]))
                .replace("[", "").replace("]", "");
        return authorsString;
    }

    public String getDescription() {
        return description;
    }

    public String getKeywords() {
        return keywords;
    }

    public String getSection() {
        return section;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public String getBody() {
        return body;
    }

    public Bitmap getImgBitmap() {
        return imgBitmap;
    }


    @Override
    public String toString() {
        String news = "Title: " + webTitle + "\n"
                + "Authors: " + Arrays.toString(authors.toArray(new String[authors.size()])) + "\n"
                + "Keywords: " + keywords + "\n"
                + "Description: " + description + "\n"
                + "Body: " + body + "\n"
                + "URl: " + webUrl + "\n"
                + "Image URL: " + imgUrl;
        return news;
    }
}
