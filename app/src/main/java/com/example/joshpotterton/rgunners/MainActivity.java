package com.example.joshpotterton.rgunners;

import android.content.Context;
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

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;


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
                    FileOutputStream outputStream;
                    try {
                        outputStream = openFileOutput("hotJSON", Context.MODE_PRIVATE);
                        outputStream.write(response.getBytes());
                        outputStream.flush();
                        outputStream.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                catch(Exception e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                try {
                    FileInputStream inputStream = openFileInput("hotJSON");
                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                    String str;

                    while((str = bufferedReader.readLine()) != null){
                        hotJSON = hotJSON + str;
                    }
                    requests--;

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        StringRequest stringRequest2 = new StringRequest(Request.Method.GET, str2, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    newJSON = response;
                    requests = requests -1;
                    FileOutputStream outputStream;
                    try {
                        outputStream = openFileOutput("newJSON", Context.MODE_PRIVATE);
                        outputStream.write(response.getBytes());
                        outputStream.flush();
                        outputStream.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                catch(Exception e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                try {
                    FileInputStream inputStream = openFileInput("hotJSON");
                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                    String str;

                    while((str = bufferedReader.readLine()) != null){
                        newJSON = newJSON + str;
                    }
                    requests--;

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
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
                    finish();
                    break;
                }
            }
        });

        thread.start();

    }


}
