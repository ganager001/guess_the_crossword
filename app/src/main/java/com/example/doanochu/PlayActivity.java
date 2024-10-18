package com.example.doanochu;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.content.Intent;

public class PlayActivity extends AppCompatActivity {
    EditText etFullName;
    Button btnPlaying;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        etFullName = findViewById(R.id.etFullName);
        btnPlaying = findViewById(R.id.btnPlaying);

        btnPlaying.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String playerName = etFullName.getText().toString().trim();
                if (playerName.isEmpty()) {
                    etFullName.setError("Please enter your name!");
                    etFullName.requestFocus();
                } else {
                    Intent intent = new Intent(PlayActivity.this, QuestionActivity.class);
                    intent.putExtra("player_name", playerName);
                    startActivity(intent);
                }
            }
        });
    }
}

