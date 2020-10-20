package ca.cmpt276.as3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

public class WelcomeActivity extends AppCompatActivity {
    final int waitingTime = 4000;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        Button skipButton = findViewById(R.id.skipWelcom);
        skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                skip();
            }
        });

        handler = new Handler();
        handler.postDelayed(this::skip, waitingTime);
    }

    private void skip() {
        Intent intent = MainActivity.makeIntent(this);
        startActivity(intent);
        finish();
        handler.removeCallbacksAndMessages(null);
    }


}