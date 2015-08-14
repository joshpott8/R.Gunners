package com.example.joshpotterton.rgunners;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.LruCache;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity {

    private int requests = 2;
    private String hotJSON = "";
    private String newJSON = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get JSON Data from Reddit.com/r/Gunners
        RequestQueue queue = Volley.newRequestQueue(this);
        String str = "https://reddit.com/r/gunners/hot.json";
        String str2 = "https://reddit.com/r/gunners/new.json";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, str, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    //Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                    hotJSON = response;
                    requests = requests -1;
                }
                catch(Exception e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
            }
        });

        StringRequest stringRequest2 = new StringRequest(Request.Method.GET, str2, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    newJSON = response;
                    requests = requests -1;
                }
                catch(Exception e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(stringRequest);
        queue.add(stringRequest2);

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) if (requests == 0) {
                    Intent intent = new Intent(getApplicationContext(), MainMenu.class);
                    intent.putExtra("hotJSON", hotJSON);
                    intent.putExtra("newJSON", newJSON);
                    startActivity(intent);
                    break;
                }
            }
        });

        thread.start();

    }


}
