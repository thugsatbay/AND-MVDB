package com.mvreview.www.mvr;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class MovieActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);
        if(savedInstanceState==null){
            getSupportFragmentManager().beginTransaction().add(R.id.container_movie,new MovieFragment()).commit();

        }
    }
}
