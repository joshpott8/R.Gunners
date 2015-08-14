package com.example.joshpotterton.rgunners;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class selfPost extends AppCompatActivity {

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

        if(getIntent().hasExtra("title") && getIntent().hasExtra("content")
                && getIntent().hasExtra("user") && getIntent().hasExtra("userFlair")
                && getIntent().hasExtra("ups") && getIntent().hasExtra("downs")){
            String title = getIntent().getStringExtra("title");
            String content = getIntent().getStringExtra("content");
            String user = getIntent().getStringExtra("user");
            String userFlair = getIntent().getStringExtra("userFlair");
            String ups = Integer.toString(getIntent().getIntExtra("ups", 0));
            String downs = Integer.toString(getIntent().getIntExtra("downs", 0));
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

        }

    }
}
