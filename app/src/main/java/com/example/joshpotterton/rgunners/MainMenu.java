package com.example.joshpotterton.rgunners;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class MainMenu extends AppCompatActivity {

    private JSONArray children;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private MyAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_slide);

        Intent intent = getIntent();

        String json = intent.getStringExtra("JSON");

        try {
            JSONObject reader = new JSONObject(json);
            JSONObject d = reader.optJSONObject("data");
            children = d.getJSONArray("children");
            Log.v("App Debug", children.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        final ViewPager mPager = (ViewPager) findViewById(R.id.pager);

        PagerAdapter mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        mPager.setCurrentItem(0);

        //mRecyclerView = (RecyclerView) findViewById(R.id.rView);
        //mRecyclerView.hasFixedSize();
        //mLayoutManager = new LinearLayoutManager(getApplicationContext());
        //mRecyclerView.setLayoutManager(mLayoutManager);
        //mAdapter = new MyAdapter(children);
        //mRecyclerView.setAdapter(mAdapter);

    }

    public class ScreenSlidePagerAdapter extends FragmentPagerAdapter {

        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Log.v("App Debug", Integer.toString(position));
            menu_item frag = null;
            try {
                frag = menu_item.create(children, position);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return frag;
        }

        @Override
        public int getCount() {
            return 1;
        }


    }
}
