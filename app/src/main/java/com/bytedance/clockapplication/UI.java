package com.bytedance.clockapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class UI extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ui);
    }

    public void myClick(View view) {
        Intent intent = new Intent(UI.this,MainActivity.class);
        startActivity(intent);
    }
}
