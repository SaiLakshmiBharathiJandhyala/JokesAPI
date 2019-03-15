package com.example.jokesapi;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    private EditText numOfJokes;
    private Button getJokes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        numOfJokes = findViewById(R.id.num_jokes);
        getJokes = findViewById(R.id.get_joke_button);

        getJokes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, JokesActivity.class);
                i.putExtra("num", numOfJokes.getText().toString());
                startActivity(i);
            }
        });
    }


}
