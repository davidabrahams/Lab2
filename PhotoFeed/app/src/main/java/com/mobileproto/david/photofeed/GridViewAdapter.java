package com.mobileproto.david.photofeed;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class GridViewAdapter extends ArrayAdapter {

    private static final String ERROR_TAG = "adapterError";
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
        return 0;
    }

    public Object getItem(int position) {
        return null;
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
        Bitmap bitmap = getImageBitmap(imageURL);
        imageView.setImageBitmap(bitmap);

        return imageView;
    }

    private static Bitmap getImageBitmap(String url)
    {
        Bitmap bm = null;
        try {
            URL aURL = new URL(url);
            URLConnection conn = aURL.openConnection();
            conn.connect();
            InputStream is = conn.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
            bm = BitmapFactory.decodeStream(bis);
            bis.close();
            is.close();
        } catch (IOException e) {
            Log.e(ERROR_TAG, "Error getting bitmap", e);
        }
        return bm;
    }
}