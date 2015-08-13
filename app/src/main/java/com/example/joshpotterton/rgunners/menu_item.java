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
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class menu_item extends Fragment {

    private static String title;
    private View view;
    private static JSONArray data;

    private TextView menuName;

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private MyAdapter mAdapter;
    private static int position;


    public static menu_item create(JSONArray jsonArray, int pos){
        menu_item frag = new menu_item();
        try {
            data = jsonArray;
            position = pos;
            //data = jsonObject.getJSONObject("data");
            //title = data.getString("title");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = (View) inflater.inflate(R.layout.menu_item, container, false);
        menuName = (TextView) view.findViewById(R.id.menuName);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //Log.v("App Debug", title);

        switch(position){
            case 0:
            menuName.setText("Hot");

        }

        mRecyclerView = (RecyclerView) view.findViewById(R.id.rView);
        mRecyclerView.hasFixedSize();
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new MyAdapter(data, getActivity());
        mRecyclerView.setAdapter(mAdapter);
    }
}
