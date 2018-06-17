package com.example.inache.atmpt21gph;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ResponseParser {


    public List<Gif> parseGifs(JSONObject responseJson){
        List<Gif> gifArrayList = new ArrayList<>();

        try {
            JSONArray data = responseJson.getJSONArray("data");
            for (int i = 0; i<data.length(); i++) {
                JSONObject object = (JSONObject) data.get(i);
                JSONObject images = object.getJSONObject("images");
                JSONObject fixedHeight = images.getJSONObject("fixed_height");
                String imageUrl = fixedHeight.getString("url");
                Gif gif = new Gif(imageUrl);
                gifArrayList.add(gif);

            }
        } catch (JSONException e){

        }
        return gifArrayList;

    }

}
