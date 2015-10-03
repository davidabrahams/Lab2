package com.mobileproto.david.photofeed;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;


/*
 * This class is the application's main activity. It contains the reference to the DB,
  * a Volley request que, and has a ViewPager to allow for sliding back and forth between fragments.
 */
public class MainActivity extends AppCompatActivity
{

    private static final int NUM_PAGES = 2;
    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;
    private RequestQueue mRequestQueue;
    private DatabaseHandler mDbHelper;
    private static final String DEBUG_TAG = "Activity";

    public RequestQueue getmRequestQueue()
    {
        return mRequestQueue;
    }

    @Override
    // When creating the activity, initialize a DB helper, a pager, and a request que for Volley.
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_slide);

        mDbHelper = new DatabaseHandler(this);

        mPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        mRequestQueue = Volley.newRequestQueue(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // This class manages the page-sliding in this app.
    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter
    {
        public ScreenSlidePagerAdapter(FragmentManager fm)
        {
            super(fm);
        }

        @Override
        public Fragment getItem(int position)
        {
            if (position == 0)
                return new SearchFragment();
            else if (position == 1)
                return new FeedFragment();
            return null;
        }

        @Override
        public int getCount()
        {
            return NUM_PAGES;
        }
    }


    public DatabaseHandler getmDbHelper()
    {
        return mDbHelper;
    }

    // This funciton adds a url to the DB, if it isn't already there.
    public void addUrlToDb(String url)
    {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues vals = new ContentValues();
        vals.put(DatabaseHandler.FeedEntry.COLUMN_NAME_URL, url);


        String Query = "Select * from " + DatabaseHandler.FeedEntry.TABLE_NAME + " where " +
                DatabaseHandler.FeedEntry.COLUMN_NAME_URL + " = \"" + url + "\"";
        Cursor cursor = db.rawQuery(Query, null);
        if (cursor == null || cursor.getCount() <= 0)
        {
            long newRowId;
            newRowId = db.insert(DatabaseHandler.FeedEntry.TABLE_NAME, null, vals);
            Log.d(DEBUG_TAG, url + " added to Database");
        } else
            Log.d(DEBUG_TAG, url + " already in Database");
        cursor.close();
        db.close();
    }

    // This method removes a url from the database
    public void removeUrlFromDb(String url)
    {
        Log.d(DEBUG_TAG, "Removing " + url + " from DB");
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        db.delete(DatabaseHandler.FeedEntry.TABLE_NAME,
                DatabaseHandler.FeedEntry.COLUMN_NAME_URL + " = ?", new String[]{url});
        db.close();

    }


}
