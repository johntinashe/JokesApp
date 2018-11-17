package com.github.johntinashe.jokeandroidlibrary;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class JokeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joke);

        TextView jokeView = findViewById(R.id.joke_tv);
        Intent i = getIntent();
        if (i != null) {
            String joke = i.getStringExtra("jokes");
            jokeView.setText(joke);
        }
    }
}
