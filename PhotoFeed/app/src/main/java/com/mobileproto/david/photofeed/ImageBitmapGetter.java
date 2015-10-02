package com.mobileproto.david.photofeed;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by david on 10/1/15.
 */
public class ImageBitmapGetter extends AsyncTask<String, Void, Bitmap>
{
    private static final String ERROR_TAG = "bitmapError";
    private ImageView myView;

    public ImageBitmapGetter(ImageView view)
    {
        super();
        myView = view;
    }

    @Override
    protected Bitmap doInBackground(String... urls)
    {
        Bitmap bm = null;
        try {
            URL aURL = new URL(urls[0]);
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

    protected void onPostExecute(Bitmap map) {
        myView.setImageBitmap(map);
    }
}
