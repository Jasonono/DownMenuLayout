package com.jason.downmenulayout;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DownMenuLayout downMenuLayout  =  findViewById(R.id.down_menu_layout);
        downMenuLayout.setAdapter(new ListMenuAdapter(this));
    }
}
