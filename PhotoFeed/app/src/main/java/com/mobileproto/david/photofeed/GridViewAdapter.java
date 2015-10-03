package com.mobileproto.david.photofeed;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import java.util.ArrayList;

// This class is for managing the data in the GridView on the search page
public class GridViewAdapter extends ArrayAdapter {

    private static final String ERROR_TAG = "adapterError";
    private static final String DEBUG_TAG = "adapterDebug";
    private Context mContext;
    private ArrayList<String> imageURLs;
    private int layoutResourceId;

    public GridViewAdapter(Context c, int layoutResourceId, ArrayList data) {
        super(c, layoutResourceId, data);
        mContext = c;
        imageURLs = data;
        this.layoutResourceId = layoutResourceId;
    }

    public int getCount() {
        return imageURLs.size();
    }

    public Object getItem(int position) {
        return imageURLs.get(position);
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {

        ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            convertView = inflater.inflate(layoutResourceId, parent, false);
            imageView = (ImageView) convertView.findViewById(R.id.image);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        } else {
            imageView = (ImageView) convertView;
        }

        String imageURL = imageURLs.get(position);

        // Load the image asynchronously.
        (new ImageBitmapGetter(imageView, 80, 80)).execute(imageURL);

        return imageView;
    }

}