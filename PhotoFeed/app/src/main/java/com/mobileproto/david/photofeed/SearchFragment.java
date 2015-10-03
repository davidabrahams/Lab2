package com.mobileproto.david.photofeed;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.apache.http.client.utils.URIBuilder;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class SearchFragment extends Fragment
{

    private static final String DEBUG_TAG = "searchFragmentDebug";
    private static final String ERROR_TAG = "searchFragmentError";
    private static final String KEY = "AIzaSyDYCakn7Ro2OySe2cLs1MHvVpN-x5HfO4k";
    private static final String CX = "016507790316430451546:c67etf_pbba";

    private ArrayList<String> urls;

    private EditText searchText;
    private GridViewAdapter gridViewAdapter;

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

    private Response.Listener listener = new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject response) {
            try {
                JSONArray images = response.getJSONArray("items");

                String[] imgURLs = new String[images.length()];
                for (int i = 0; i < imgURLs.length; i++)
                    imgURLs[i] = images.getJSONObject(i).getJSONObject("image")
                            .getString("thumbnailLink");

                for (String url : imgURLs)
                    urls.add(url);

                gridViewAdapter.notifyDataSetChanged();

            } catch (JSONException e) {
                Log.e(ERROR_TAG, e.toString());;
            }
        }
    };

    private AdapterView.OnItemLongClickListener longClick = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("Added image to Feed");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ((MainActivity) getActivity()).addUrlToDb(urls.get(position));
                    dialog.cancel();
                }
            });
            builder.setNegativeButton("Undo", null);
            builder.show();
            return false;
        }
    };

    public SearchFragment()
    {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {

        View view = inflater.inflate(R.layout.fragment_search, container, false);
        searchText = (EditText) view.findViewById(R.id.searchText);
        searchText.setOnEditorActionListener(searchListener);

        urls = new ArrayList<>();

        GridView gridView = (GridView) view.findViewById(R.id.gridView);
        gridViewAdapter = new GridViewAdapter(getActivity(), R.layout.grid_item_layout, urls);
        gridView.setAdapter(gridViewAdapter);
        gridView.setOnItemLongClickListener(longClick);

        return view;
    }


    // This function is called every time the user presses search
    private void performSearch(String search)
    {
        // searchText.clearFocus();
        RequestQueue mRequestQueue = ((MainActivity) getActivity()).getmRequestQueue();
        try {
            int[] startVals = {1, 11, 21};
            for (int i : startVals) {
                URI uri = new URIBuilder()
                        .setScheme("https")
                        .setHost("www.googleapis.com")
                        .setPath("/customsearch/v1")
                        .setParameter("key", KEY)
                        .setParameter("cx", CX)
                        .setParameter("searchType", "image")
                        .setParameter("q", search)
                        .setParameter("start", Integer.toString(i))
                        .build();
                String url = uri.toString();
                JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                        listener, new ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError e) {
                        Log.e(ERROR_TAG, e.toString());
                    }
                });
                mRequestQueue.add(jsObjRequest);
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } finally {
        }
    }
}
