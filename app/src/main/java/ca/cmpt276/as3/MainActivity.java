package ca.cmpt276.as3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    public static Intent makeIntent(Context c) {
        return new Intent(c, MainActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setButton();
    }

    private void setButton() {
        Button button = findViewById(R.id.startGame);
        button.setOnClickListener(v -> {
            Intent intent = GameActivity.makeIntent(MainActivity.this);
            startActivity(intent);
        });

        button = findViewById(R.id.option);
        button.setOnClickListener(v -> {
            Intent intent = OptionActivity.makeIntent(MainActivity.this);
            startActivity(intent);
        });

        button = findViewById(R.id.helpPage);
        button.setOnClickListener(v -> {
            Intent intent = HelpActivity.makeIntent(MainActivity.this);
            startActivity(intent);
        });
    }
}