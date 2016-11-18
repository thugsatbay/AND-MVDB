package com.mvreview.www.mvr;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;


public class MainActivity extends AppCompatActivity{

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_menu,menu);
        return true;
        //return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState==null) {
            getSupportFragmentManager().beginTransaction().add(R.id.container,new HomeFragment()).commit();
        }
    }
}
