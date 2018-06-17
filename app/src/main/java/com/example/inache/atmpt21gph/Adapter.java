package com.example.inache.atmpt21gph;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;



public class Adapter extends ArrayAdapter<Gif> {
    public Adapter( Context context, List<Gif> gifs) {
        super(context,android.R.layout.simple_list_item_1,gifs);
    }


    @Override
    public View getView(int position,  View convertView, ViewGroup parent) {
        Gif gif = this.getItem(position);

        if (convertView == null){
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.gif_item,parent,false);
        }

        ImageView imageView = convertView.findViewById(R.id.imV_image);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        imageView.setImageResource(0);


        String url = gif.getUrl();

     Glide.with(getContext()).load(url).asGif().diskCacheStrategy(DiskCacheStrategy.SOURCE).placeholder(R.mipmap.ic_launcher).centerCrop().into(imageView);
return convertView;

    }
}
