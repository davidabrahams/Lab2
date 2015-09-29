package com.mobileproto.david.photofeed;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment
{

    private static final String DEBUG_TAG = "Fragment";

    public MainActivityFragment()
    {
    }

    // The listener for the search text field
    private TextView.OnEditorActionListener searchListener = new TextView.OnEditorActionListener()
    {

        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
        {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                performSearch();
                return true;
            }
            return false;
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {

        View view = inflater.inflate(R.layout.fragment_main, container, false);

        EditText searchText = (EditText) view.findViewById(R.id.searchText);
        searchText.setOnEditorActionListener(searchListener);
        return view;
    }


    // This function is called every time the user presses search
    private void performSearch()
    {
        Log.d(DEBUG_TAG, "Search");
    }
}
