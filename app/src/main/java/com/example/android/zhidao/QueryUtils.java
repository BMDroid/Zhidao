package com.example.android.zhidao;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

import static android.R.attr.tag;
import static com.example.android.zhidao.R.id.authors;


/**
 * Helper methods related to requesting and receiving news data.
 * Created by Jianyuan on 10/7/2016.
 */

public final class QueryUtils {
    public static final String LOG_TAG = QueryUtils.class.getSimpleName();

    private QueryUtils(){}

    public static ArrayList<News> fetchNews(String requestUrl){
        URL url = creatUrl(requestUrl);
        String jsonResponse = null;
        ArrayList<News> newsList = new ArrayList<>();
        try{
            jsonResponse = makeHttpRequest(url);
            newsList = extractNews(jsonResponse);
        }catch (IOException e){
            Log.e(LOG_TAG, "Error with request url", e);
        }
        return newsList;
    }

    private static ArrayList<News> extractNews(String jsonResponse) throws IOException{
        ArrayList<News> newsList = new ArrayList<>();
        try{
            JSONObject json = new JSONObject(jsonResponse);
            JSONObject response = json.getJSONObject("response");
            JSONArray results = response.optJSONArray("results");
            for(int i = 0; i < results.length(); i ++){
                JSONObject newsObject = results.getJSONObject(i);
                String webTitle = newsObject.getString("webTitle");
                String webUrl = newsObject.getString("webUrl");
                JSONArray tags = newsObject.getJSONArray("tags");
                ArrayList<String> authors = new ArrayList<>();
                if(tags != null){
                    for(int j = 0; j < tags.length(); j ++){
                        JSONObject author = tags.getJSONObject(j);
                        authors.add(author.getString("webTitle"));
                    }
                }
                News news = new News(webTitle, webUrl);
                news.setAuthors(authors);
                ArrayList<String> newsHtml = parseHtml(webUrl);
                news.setDescription(newsHtml.get(0));
                news.setKeywords(newsHtml.get(1));
                news.setSection(newsHtml.get(2));
                news.setBody(newsHtml.get(3));
                news.setImgBitmap(urlToBitmap(newsHtml.get(4)));
                newsList.add(news);
            }
        }catch (JSONException e){
            Log.e(LOG_TAG, "Error with json response", e);
        }
        return newsList;
    }

    private static URL creatUrl(String stringUrl){
        URL url = null;
        try{
            url = new URL(stringUrl);
        }catch (MalformedURLException e){
           Log.e(LOG_TAG, "Error with url", e);
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException{
        String jsonResponse = "";
        if(url == null) return jsonResponse;
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try{
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            if(urlConnection.getResponseCode() == 200){
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            }else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        }catch(IOException e){
            Log.e(LOG_TAG, "Error retrieving json response", e);
        }finally{
            if(urlConnection != null){
                urlConnection.disconnect();
            }
            if(inputStream != null){
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException{
        StringBuilder output = new StringBuilder();
        if(inputStream != null){
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line = bufferedReader.readLine();
            while(line != null){
                output.append(line);
                line = bufferedReader.readLine();
            }
        }
        return output.toString();
    }

    private static ArrayList<String> parseHtml(String webUrl) throws IOException{
        ArrayList<String> html = new ArrayList<>();
        StringBuilder output = new StringBuilder();
        try{
            Document doc = Jsoup.connect(webUrl).get();
            // Get news's description
            Elements description = doc.select("meta[property=og:description]");
            html.add(description.attr("content"));
            //Get news's keywords
            Elements keywords = doc.select("meta[property=og:keywords]");
            html.add(keywords.attr("content"));
            //Get news's section
            Elements section = doc.select("meta[property=article:section]");
            html.add(section.attr("content"));
            // Get article body
            Elements articleBody = doc.select("div[class = content__article-body from-content-api js-article__body] > p");
            for (Element el : articleBody) {
                output.append(el.text());
                output.append(System.getProperty("line.separator"));
                output.append(System.getProperty("line.separator"));
            }
            html.add(output.toString());
            //Get news's image url if exists
            Elements imgUrl = doc.select("img[class=maxed responsive-img]");
            if(imgUrl != null) {
                html.add(imgUrl.attr("src"));
                Log.d("Image URL", html.get(4));
            }else{
                html.add("");
                Log.d("Image URL", html.get(4));
            }
        }catch(MalformedURLException e){
            Log.e(LOG_TAG, "Error with url", e);
        }
        return html;
    }

    private static Bitmap urlToBitmap(String stringUrl){
        Bitmap imgBitmap = null;
        if(stringUrl.equals("")) return null;
        try {
            InputStream inputStream = new URL(stringUrl).openStream();
            imgBitmap = BitmapFactory.decodeStream(inputStream);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Bad bitmap", e);
        } catch (IOException e) {
            Log.e(LOG_TAG, "IOException", e);
        }
        return imgBitmap;
    }
}
