package com.example.joshpotterton.rgunners;


import android.app.ActionBar;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class selfPost extends AppCompatActivity {

    private String postURL;
    private LinearLayout commentsArea;
    private JSONArray children;

    Thread thread = new Thread(new Runnable() {
        @Override
        public void run() {
            getComments comments = new getComments();

            try {
                JSONArray array = new JSONArray(comments.doInBackground(commentsArea));
                JSONObject object = array.optJSONObject(1);
                JSONObject data = object.optJSONObject("data");
                children = data.optJSONArray("children");
                Log.v("App Debug", children.toString());

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        int i = children.length();

                        for(int x = 0; x < i;x++){
                            try {
                                JSONObject comment = children.optJSONObject(x);
                                JSONObject commentData = comment.optJSONObject("data");
                                String str = null;

                                //Comment
                                str = commentData.getString("body_html");
                                LinearLayout commentLayout = new LinearLayout(getApplicationContext());
                                TextView textView = new TextView(getApplicationContext());
                                textView.setText(Html.fromHtml(Html.fromHtml(str).toString()).toString().trim());
                                textView.setTextColor(Color.BLACK);
                                textView.setPadding(0, 0, 0, 0);
                                commentLayout.setOrientation(LinearLayout.VERTICAL);

                                //Author
                                String author = commentData.getString("author");
                                TextView authorTV = new TextView(getApplicationContext());
                                authorTV.setTextColor(Color.DKGRAY);
                                authorTV.setText("Posted by: " + author);
                                authorTV.setTextSize(8);
                                LinearLayout postDetails = new LinearLayout(getApplicationContext());
                                postDetails.setOrientation(LinearLayout.HORIZONTAL);
                                postDetails.addView(authorTV);
                                String flair = commentData.optString("author_flair_text");
                                if(!flair.equalsIgnoreCase("null")) {
                                    TextView flairTextView = new TextView(getApplicationContext());
                                    flairTextView.setText(flair);
                                    flairTextView.setTextColor(Color.BLACK);
                                    flairTextView.setBackgroundColor(Color.LTGRAY);
                                    flairTextView.setTextSize(8);
                                    postDetails.addView(flairTextView);
                                }




                                //Divider
                                View view = new View(getApplicationContext());
                                ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, 3);
                                view.setLayoutParams(layoutParams);
                                view.setBackgroundColor(Color.RED);
                                view.setPadding(0,0,0,10);

                                commentLayout.addView(textView);
                                commentLayout.addView(postDetails);
                                commentLayout.addView(view);

                                LinearLayout layout = new LinearLayout(getApplicationContext());
                                layout.setPadding(0, 5, 0, 10);
                                layout.setOrientation(LinearLayout.VERTICAL);
                                layout.addView(commentLayout);

                                JSONObject replies = commentData.getJSONObject("replies");
                                JSONObject repliesData = replies.optJSONObject("data");
                                JSONArray repliesChildren = repliesData.optJSONArray("children");
                                int NoReplies = repliesChildren.length();

                                //Add first set of replies
                                for(int y = 0; y < NoReplies; y++){
                                    JSONObject ob1 = repliesChildren.optJSONObject(y);
                                    JSONObject obData = ob1.optJSONObject("data");
                                    String str1 = obData.getString("body_html");
                                    TextView textView1 = new TextView(getApplicationContext());
                                    textView1.setText(Html.fromHtml(Html.fromHtml(str1).toString()).toString().trim());
                                    textView1.setTextColor(Color.BLACK);
                                    textView1.setPadding(0,0,0,0);

                                    String author1 = obData.getString("author");
                                    TextView authorTV1 = new TextView(getApplicationContext());
                                    authorTV1.setTextColor(Color.DKGRAY);
                                    authorTV1.setText("Posted by: " + author1);
                                    authorTV1.setTextSize(8);

                                    LinearLayout postDetails1 = new LinearLayout(getApplicationContext());
                                    postDetails1.setOrientation(LinearLayout.HORIZONTAL);
                                    postDetails1.addView(authorTV1);

                                    String flair1 = obData.getString("author_flair_text");
                                    if(!flair1.equalsIgnoreCase("null")) {
                                        TextView flairTextView1 = new TextView(getApplicationContext());
                                        flairTextView1.setText(flair1);
                                        flairTextView1.setTextColor(Color.BLACK);
                                        flairTextView1.setBackgroundColor(Color.LTGRAY);
                                        flairTextView1.setTextSize(8);
                                        postDetails1.addView(flairTextView1);
                                    }


                                    View view1 = new View(getApplicationContext());
                                    ViewGroup.LayoutParams layoutParams1 = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, 3);
                                    view1.setLayoutParams(layoutParams1);
                                    view1.setBackgroundColor(Color.RED);
                                    view1.setPadding(0, 0, 0, 10);

                                    LinearLayout commentLayout1 = new LinearLayout(getApplicationContext());
                                    commentLayout1.setOrientation(LinearLayout.VERTICAL);
                                    commentLayout1.setPadding(25, 0, 0, 15);
                                    commentLayout1.setBackgroundResource(R.drawable.comments_thread_background);
                                    commentLayout1.addView(textView1);
                                    commentLayout1.addView(postDetails1);
                                    commentLayout1.addView(view1);

                                    layout.addView(commentLayout1);

                                    //JSONObject replies1 = obData.optJSONObject("replies");
                                    //if(replies1 != null) {
                                        //JSONObject repliesData1 = replies1.optJSONObject("data");
                                        //JSONArray repliesChildren1 = repliesData1.optJSONArray("children");
                                        //int NoReplies2 = repliesChildren1.length();

                                        //Add first set of replies
                                        //for (int z = 0; z < NoReplies2; z++) {
                                            //JSONObject ob2 = repliesChildren1.optJSONObject(z);
                                            //JSONObject obData2 = ob2.optJSONObject("data");
                                            //String str2 = obData2.getString("body_html");
                                            //TextView textView2 = new TextView(getApplicationContext());
                                            //textView2.setText(Html.fromHtml(Html.fromHtml(str1).toString()).toString().trim());
                                            //textView2.setTextColor(Color.BLACK);
                                            //textView2.setPadding(0, 0, 0, 0);


                                            //String author2 = obData.getString("author");
                                            //TextView authorTV2 = new TextView(getApplicationContext());
                                            //authorTV2.setTextColor(Color.DKGRAY);
                                            //authorTV2.setText("Posted by: " + author1);
                                            //authorTV2.setTextSize(8);

                                            //View view2 = new View(getApplicationContext());
                                            //ViewGroup.LayoutParams layoutParams2 = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, 3);
                                            //view2.setLayoutParams(layoutParams2);
                                            //view2.setBackgroundColor(Color.RED);
                                            //view2.setPadding(0, 0, 0, 10);

                                            //LinearLayout commentLayout2 = new LinearLayout(getApplicationContext());
                                            //commentLayout2.setOrientation(LinearLayout.VERTICAL);
                                            //commentLayout2.setPadding(50, 0, 0, 15);
                                            //commentLayout2.setBackgroundResource(R.drawable.comments_thread_background);
                                            //commentLayout2.addView(textView2);
                                            //commentLayout2.addView(authorTV2);
                                            //commentLayout2.addView(view2);

                                            //layout.addView(commentLayout2);
                                        //}
                                    //}

                                }

                                commentsArea.addView(layout);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }
                    }
                });



            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.self_post);

        TextView titleTextView = (TextView) findViewById(R.id.post_title);
        TextView contentTextView = (TextView) findViewById(R.id.post_content);
        TextView userTextView = (TextView) findViewById(R.id.user);
        TextView userFlairTextView = (TextView) findViewById(R.id.userFlair);
        TextView upsTextView = (TextView) findViewById(R.id.selfUps);
        TextView downsTextView = (TextView) findViewById(R.id.selfDowns);
        commentsArea = (LinearLayout) findViewById(R.id.comments_area);

        if(getIntent().hasExtra("title") && getIntent().hasExtra("content")
                && getIntent().hasExtra("user") && getIntent().hasExtra("userFlair")
                && getIntent().hasExtra("ups") && getIntent().hasExtra("downs")
                && getIntent().hasExtra("url")){
            String title = getIntent().getStringExtra("title");
            String content = getIntent().getStringExtra("content");
            String user = getIntent().getStringExtra("user");
            String userFlair = getIntent().getStringExtra("userFlair");
            String ups = Integer.toString(getIntent().getIntExtra("ups", 0));
            String downs = Integer.toString(getIntent().getIntExtra("downs", 0));
            postURL = getIntent().getStringExtra("url");
            postURL = postURL + "about.json";
            titleTextView.setText(title);
            contentTextView.setText(content);
            userTextView.setText("Posted by: " + user);
            upsTextView.setText(ups);
            downsTextView.setText(downs);

            if(!userFlair.equalsIgnoreCase("null")) {
                userFlairTextView.setText(userFlair);
            }
            else{
                userFlairTextView.setText("");
                userFlairTextView.setVisibility(View.INVISIBLE);
            }

            thread.start();
        }

    }

    public class getComments extends AsyncTask<LinearLayout, Void, String> {
        private String commentsJSON;
        private LinearLayout commentsArea;
        private int requests = 1;
        @Override
        protected String doInBackground(LinearLayout... params) {
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

            commentsArea = params[0];


            StringRequest stringRequest = new StringRequest(Request.Method.GET, postURL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        commentsJSON = response;
                        requests--;
                    }
                    catch(Exception e){
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });

            queue.add(stringRequest);

            while(true) {
                if(requests == 0) {
                    break;
                }
            }
            return commentsJSON;
        }

        @Override
        protected void onPostExecute(String string) {
            super.onPostExecute(string);

            try {
                JSONArray array = new JSONArray(string);
                JSONObject object = array.optJSONObject(1);
                JSONObject data = object.optJSONObject("data");
                JSONArray children = data.optJSONArray("children");
                Log.v("App Debug", children.toString());

                int i = children.length();

                for(int x = 0; x < i;x++){
                    JSONObject comment = children.optJSONObject(x);
                    JSONObject commentData = comment.optJSONObject("data");
                    String str = commentData.getString("body_html");

                    TextView textView = new TextView(getApplicationContext());
                    textView.setText(Html.fromHtml(str).toString());
                    commentsArea.addView(textView);
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}
