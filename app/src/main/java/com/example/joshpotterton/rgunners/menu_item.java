package com.example.joshpotterton.rgunners;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class menu_item extends Fragment {

    private static String title;
    private View view;

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private MyAdapter mAdapter;


    public static menu_item create(JSONObject jsonObject){
        menu_item frag = new menu_item();
        try {
            JSONObject data = jsonObject.getJSONObject("data");
            title = data.getString("title");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = (View) inflater.inflate(R.layout.menu_item, container, false);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.v("App Debug", title);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.rView);
        mRecyclerView.hasFixedSize();
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        //mAdapter = new MyAdapter(title);
        mRecyclerView.setAdapter(mAdapter);
    }
}
