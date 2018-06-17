package com.example.inache.atmpt21gph;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;


//http://loopj.com/android-async-http/      ...developer.alexanderklimov.ru/android...


public class Httpclient extends AsyncHttpClient {
    public void getTrendingGifs(int offset, JsonHttpResponseHandler responseHandler){
        RequestParams params = new RequestParams();
        params.put("api_key" , "dc6zaTOxFJmzC" );
        params.put("offset", offset);
        super.get("http://api.giphy.com//v1/gifs/trending", params, responseHandler);


    }
    public void getSearchGifs (int offset, String searchQuery, JsonHttpResponseHandler responseHandler){
        if (searchQuery == "" || searchQuery == null){
            getTrendingGifs(offset,responseHandler);
        }
        RequestParams params = new RequestParams();
        params.put("api_key", "dc6zaTOxFJmzC");
        params.put("q", searchQuery);
        params.put("offset", offset);

        super.get("http://api.giphy.com/v1/gifs/search", params, responseHandler);
    }
}
