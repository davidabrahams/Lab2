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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;

import org.apache.http.client.utils.URIBuilder;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment
{

    private static final String DEBUG_TAG = "Fragment";
    private static final String KEY = "AIzaSyAwzWr2-QbBfR5t13eimzo19Iy9ZUAZWco";
    private static final String CX = "016507790316430451546:c67etf_pbba";
    private EditText searchText;

    private RequestQueue mRequestQueue;

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
                performSearch(v.getText().toString());
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
        searchText = (EditText) view.findViewById(R.id.searchText);
        searchText.setOnEditorActionListener(searchListener);
        return view;
    }


    // This function is called every time the user presses search
    private void performSearch(String search)
    {
        URI uri = null;
        mRequestQueue = ((MainActivity) getActivity()).getmRequestQueue();
        try {
            uri = new URIBuilder()
                    .setScheme("https")
                    .setHost("www.googleapis.com")
                    .setPath("/customsearch/v1")
                    .setParameter("key", KEY)
                    .setParameter("cx", CX)
                    .setParameter("searchType", "image")
                    .setParameter("q", search)
                    .build();
            String url = uri.toString();
            Log.d(DEBUG_TAG, url);
            JsonObjectRequest jsObjRequest = new JsonObjectRequest
                    (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d(DEBUG_TAG, "hello");
                        }
                    }, new ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("ERROR", error.toString());

                        }
                    });
            mRequestQueue.add(jsObjRequest);
            // mRequestQueue.add(jsObjRequest);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } finally {
        }
    }
}
