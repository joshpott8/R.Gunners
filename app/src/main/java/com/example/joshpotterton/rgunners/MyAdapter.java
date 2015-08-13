package com.example.joshpotterton.rgunners;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private String title;
    private JSONArray jsonArray;
    private Activity activity;
    private ImageView imageView;
    private int looper;

    private Bitmap[] thumbnails = new Bitmap[25];

    //public MyAdapter(String title){
        //this.title = title;
    //}

    public MyAdapter(JSONArray jsonObject, Activity activity){
        this.jsonArray = jsonObject;
        this.activity = activity;
    }

    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_item_layout, parent,
                false);
        ViewHolder vh = new ViewHolder(view);



        return vh;
    }

    @Override
    public void onBindViewHolder(MyAdapter.ViewHolder holder, int position) {
        menu_item frag = new menu_item();
        try {
            JSONObject jsonObject = jsonArray.optJSONObject(position);
            JSONObject data = jsonObject.getJSONObject("data");

            title = data.getString("title");
            Log.v("App Debug", title);
            holder.title.setText(this.title);

            String url = data.getString("thumbnail");
            Log.v("App Debug", url);

            imageView = holder.thumb;

            try{

                    if(!url.equalsIgnoreCase("self") && !url.equalsIgnoreCase("default")){
                        RequestQueue queue = Volley.newRequestQueue(activity);

                        ImageRequest imageRequest = new ImageRequest(url, new Response.Listener<Bitmap>() {

                            @Override
                            public void onResponse(Bitmap response) {
                                imageView.setImageBitmap(response);
                            }
                        }, 0, 0, null, new Response.ErrorListener(){

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.v("App Debug", "Image not Loaded");
                            }
                        });
                        queue.add(imageRequest);

                    }

            }
            catch(Exception e){
                e.printStackTrace();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return 25;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
        implements View.OnClickListener{

        private TextView title;
        private ImageView thumb;

        public ViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            thumb = (ImageView) itemView.findViewById(R.id.thumbnail);
        }

        @Override
        public void onClick(View v) {


        }
    }


}
