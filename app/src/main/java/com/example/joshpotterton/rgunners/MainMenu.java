package com.example.joshpotterton.rgunners;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class MainMenu extends AppCompatActivity {

    private JSONArray childrenH;
    private JSONArray childrenN;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private MyAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_slide);

        Intent intent = getIntent();

        String hotJSON = intent.getStringExtra("hotJSON");
        String newJSON = intent.getStringExtra("newJSON");

        try {
            JSONObject readerH = new JSONObject(hotJSON);
            JSONObject dH = readerH.optJSONObject("data");
            childrenH = dH.getJSONArray("children");
            Log.v("App Debug", childrenH.toString());

            JSONObject readerN = new JSONObject(newJSON);
            JSONObject dN = readerN.optJSONObject("data");
            childrenN = dN.getJSONArray("children");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        final ViewPager mPager = (ViewPager) findViewById(R.id.pager);

        PagerAdapter mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        mPager.setCurrentItem(0);
        mPager.setPageTransformer(true, new DepthPageTransformer());

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
            try {
                switch(position) {
                    case 0:
                        menu_item frag = menu_item.create(childrenH, position);
                        return frag;
                    case 1:
                        newFeedMenu frag2 = newFeedMenu.create(childrenN, position);
                        return frag2;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public int getCount() {
            return 2;
        }


    }

}
