package com.mobileproto.david.photofeed;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;

/**
 * Created by david on 10/2/15.
 */
public class FeedFragment extends Fragment {

    private static final String DEBUG_TAG = "feedFragmentDebug";
    private static final String ERROR_TAG = "feedFragmentError";

    private Button back, next;
    private ImageView imgView;
    private int pos;

    private String currUrl;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {

        View view = inflater.inflate(R.layout.fragment_feed, container, false);
        back = (Button) view.findViewById(R.id.backBtn);
        next = (Button) view.findViewById(R.id.nextBtn);
        imgView = (ImageView) view.findViewById(R.id.imageView1);
        imgView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        pos = 0;
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pos -= 1;
                updateFeed(((MainActivity) getActivity()).getmDbHelper());
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pos += 1;
                updateFeed(((MainActivity) getActivity()).getmDbHelper());
            }
        });
        imgView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Remove image from feed?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ((MainActivity) getActivity()).removeUrlFromDb(currUrl);
                        updateFeed(((MainActivity) getActivity()).getmDbHelper());
                        dialog.cancel();
                    }
                });
                builder.setNegativeButton("Cancel", null);
                builder.show();
                return false;
            }
        });
        updateFeed(((MainActivity) getActivity()).getmDbHelper());
        return view;
    }

    public void updateFeed(DatabaseHandler handler)
    {
        SQLiteDatabase db = handler.getReadableDatabase();
        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                DatabaseHandler.FeedEntry._ID,
                DatabaseHandler.FeedEntry.COLUMN_NAME_URL
        };

        Cursor c = db.query(
                DatabaseHandler.FeedEntry.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );

        if (pos >= c.getCount()) {
            pos = 0;
            Log.d(DEBUG_TAG, "DB overflow. Resetting to 0");
        }
        if (pos < 0) {
            pos = c.getCount() - 1;
            Log.d(DEBUG_TAG, "DB overflow. Resetting to end");
        }
        Log.d(DEBUG_TAG, "Current position: " + Integer.toString(pos));
        c.moveToPosition(pos);
        currUrl = c.getString(1);
        (new ImageBitmapGetter(imgView, 500, 500)).execute(currUrl);
    }

}
