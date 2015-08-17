package com.example.joshpotterton.rgunners;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ResourceBundle;


public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private String title;
    private JSONArray jsonArray;
    private JSONObject data;
    private Activity activity;
    private ImageView imageView;
    private int looper;
    private LruCache<String, Bitmap> memoryCache;


    //public MyAdapter(String title){
        //this.title = title;
    //}

    public MyAdapter(JSONArray jsonObject, Activity activity){
        this.jsonArray = jsonObject;
        this.activity = activity;
        int maxMemory = (int) (Runtime.getRuntime().maxMemory() /1024);
        int cacheSize = maxMemory /8;

        memoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap){
                return bitmap.getByteCount() / 1024;
            }
        };
    }

    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_item_layout, parent,
                false);
        ViewHolder vh = new ViewHolder(view, jsonArray, activity);



        return vh;
    }

    @Override
    public void onBindViewHolder(MyAdapter.ViewHolder holder, int position) {
        //menu_item frag = new menu_item();

        try {
            JSONObject jsonObject = jsonArray.optJSONObject(position);
            data = jsonObject.getJSONObject("data");

            title = data.getString("title");
            Log.v("App Debug", title);
            holder.title.setText(this.title);

            //Get upvotes and downvotes on each item
            String upvotesNum = Integer.toString(data.getInt("ups"));
            String downvotesNum = Integer.toString(data.getInt("downs"));

            holder.upvote.setText(upvotesNum);
            holder.downvote.setText(downvotesNum);

            //Get Author name and flair
            String user = data.getString("author");
            String flair = data.getString("author_flair_text").trim();

            holder.userTextView.setText("Posted by: " + user);
            if(!flair.equalsIgnoreCase("null")) {
                holder.flairTextView.setVisibility(View.VISIBLE);
                holder.flairTextView.setText(flair);
            }
            else{
                holder.flairTextView.setText("");
                holder.flairTextView.setVisibility(View.INVISIBLE);
            }

            String id = data.getString("id");
            Bitmap bitmap = getBitmapFromMemCache(id);
            String url = data.getString("thumbnail");
            Log.v("App Debug", url);
            //if(bitmap != null){
                //holder.thumb.setImageBitmap(bitmap);
            //}
            if(url.equalsIgnoreCase("self") || url.equalsIgnoreCase("default")) {
                holder.thumb.setImageResource(R.drawable.ab_stacked_solid_gunnersactionbar);
            }
            else{

                imageView = holder.thumb;

                //try {
                        //RequestQueue queue = Volley.newRequestQueue(activity);

                        //ImageRequest imageRequest = new ImageRequest(url, new Response.Listener<Bitmap>() {

                            //@Override
                            //public void onResponse(Bitmap response) {
                                //imageView.setImageBitmap(response);
                                //try {
                                    //addBitmapToMemoryCache(data.getString("id"), response);
                                //} catch (Exception e) {
                                    //e.printStackTrace();
                                //}
                            //}
                        //}, 0, 0, null, new Response.ErrorListener() {

                            //@Override
                            //public void onErrorResponse(VolleyError error) {
                                //Log.v("App Debug", "Image not Loaded");
                            //}
                        //});
                        //queue.add(imageRequest);

                //} catch (Exception e) {
                    //e.printStackTrace();
                //}

                RequestQueue queue = Volley.newRequestQueue(activity);

                ImageLoader imageLoader = new ImageLoader(queue, new ImageLoader.ImageCache(){
                    private final LruCache<String, Bitmap> cache = new LruCache<String, Bitmap>(25);

                    @Override
                    public Bitmap getBitmap(String url){
                        return cache.get(url);
                    }

                    @Override
                    public void putBitmap(String url, Bitmap bitmap1){
                        cache.put(url, bitmap1);
                    }
                });

                imageLoader.get(url, ImageLoader.getImageListener(holder.thumb, R.drawable.ab_stacked_solid_gunnersactionbar, R.drawable.ab_stacked_solid_gunnersactionbar));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }



    public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            memoryCache.put(key, bitmap);
        }
    }

    public Bitmap getBitmapFromMemCache(String key) {
        return memoryCache.get(key);
    }

    @Override
    public int getItemCount() {
        return 25;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
        implements View.OnClickListener{

        private TextView title;
        private ImageView thumb;
        private TextView upvote;
        private TextView downvote;
        private TextView userTextView;
        private TextView flairTextView;
        private JSONArray array;
        private Activity activity;
        private Context context;
        private JSONObject data;

        public ViewHolder(View itemView, JSONArray jsonArray, Activity act) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            thumb = (ImageView) itemView.findViewById(R.id.thumbnail);
            upvote = (TextView) itemView.findViewById(R.id.upvote);
            downvote = (TextView) itemView.findViewById(R.id.downvote);
            userTextView = (TextView) itemView.findViewById(R.id.userMenu);
            flairTextView = (TextView) itemView.findViewById(R.id.flairMenu);
            array = jsonArray;
            activity = act;
            context = itemView.getContext();
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int pos = getLayoutPosition();
            try {
                JSONObject object = array.optJSONObject(pos);
                data = object.optJSONObject("data");

                //Log.v("App Debug", object.getString("thumbail") + " Post");
                if(data.getBoolean("is_self")){
                    Log.v("App Debug", "Item Clicked");
                    Intent intent = new Intent(context, selfPost.class);
                    String title = data.getString("title");
                    String content = data.getString("selftext");
                    String user = data.getString("author");
                    String userFlair = data.getString("author_flair_text");
                    String postURL = data.getString("url");
                    int ups = data.getInt("ups");
                    int downs = data.getInt("downs");
                    intent.putExtra("title", title);
                    intent.putExtra("content", content);
                    intent.putExtra("user", user);
                    intent.putExtra("userFlair", userFlair);
                    intent.putExtra("ups", ups);
                    intent.putExtra("downs", downs);
                    intent.putExtra("url", postURL);
                    context.startActivity(intent);
                }
                else{

                    AlertDialog.Builder alert = new AlertDialog.Builder(context);
                    alert.setMessage("Do you wish to view the post or view the comments?")
                            .setCancelable(true)
                            .setPositiveButton("Post", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Uri uri = null;
                                    try {
                                        uri = Uri.parse(data.getString("url"));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    Intent broswerIntent = new Intent(Intent.ACTION_VIEW, uri);
                                    context.startActivity(broswerIntent);
                                }
                            })
                            .setNegativeButton("Comments", new DialogInterface.OnClickListener(){

                                @Override
                                public void onClick(DialogInterface dialog, int which) {


                                    try {Intent intent = new Intent(context, selfPost.class);
                                        String title = data.getString("title");
                                        String user = data.getString("author");
                                        String userFlair = data.getString("author_flair_text");
                                        String postURL = data.getString("url");
                                        int ups = data.getInt("ups");
                                        int downs = data.getInt("downs");
                                        intent.putExtra("title", title);
                                        intent.putExtra("content", "");
                                        intent.putExtra("user", user);
                                        intent.putExtra("userFlair", userFlair);
                                        intent.putExtra("ups", ups);
                                        intent.putExtra("downs", downs);
                                        intent.putExtra("url", postURL);
                                        context.startActivity(intent);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }
                            });

                    AlertDialog dialog = alert.create();
                    dialog.show();


                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }


}
