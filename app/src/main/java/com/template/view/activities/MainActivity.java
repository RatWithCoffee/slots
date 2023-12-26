package com.template.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.template.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        ImageButton startButton = findViewById(R.id.button_start);
        startButton.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), GameActivity.class);
            view.getContext().startActivity(intent);
        });
    }
}