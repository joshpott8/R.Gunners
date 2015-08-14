package com.example.joshpotterton.rgunners;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class selfPost extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.self_post);

        TextView titleTextView = (TextView) findViewById(R.id.post_title);
        TextView contentTextView = (TextView) findViewById(R.id.post_content);
        TextView userTextView = (TextView) findViewById(R.id.user);

        if(getIntent().hasExtra("title") && getIntent().hasExtra("content")
                && getIntent().hasExtra("user")){
            String title = getIntent().getStringExtra("title");
            String content = getIntent().getStringExtra("content");
            String user = getIntent().getStringExtra("user");
            titleTextView.setText(title);
            contentTextView.setText(content);
            userTextView.setText("Posted by: " + user);
        }

    }
}
