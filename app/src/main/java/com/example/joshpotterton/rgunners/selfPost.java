package com.example.joshpotterton.rgunners;


import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
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

                                str = commentData.getString("body_html");
                                TextView textView = new TextView(getApplicationContext());
                                textView.setText(Html.fromHtml(Html.fromHtml(str).toString()));
                                textView.setTextColor(Color.BLACK);
                                textView.setPadding(0, 15, 0, 5);

                                LinearLayout layout = new LinearLayout(getApplicationContext());
                                layout.setPadding(0,5,0,5);
                                layout.setOrientation(LinearLayout.VERTICAL);
                                layout.addView(textView);

                                JSONObject replies = commentData.getJSONObject("replies");
                                JSONObject repliesData = replies.optJSONObject("data");
                                JSONArray repliesChildren = repliesData.optJSONArray("children");
                                int NoReplies = repliesChildren.length();

                                for(int y = 0; y < NoReplies; y++){
                                    JSONObject ob1 = repliesChildren.optJSONObject(y);
                                    JSONObject obData = ob1.optJSONObject("data");
                                    String str1 = obData.getString("body_html");
                                    TextView textView1 = new TextView(getApplicationContext());
                                    textView1.setText(Html.fromHtml(Html.fromHtml(str1).toString()));
                                    textView1.setTextColor(Color.BLACK);
                                    textView1.setPadding(25, 5, 0, 5);
                                    textView1.setBackgroundResource(R.drawable.comments_thread_background);
                                    layout.addView(textView1);
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
