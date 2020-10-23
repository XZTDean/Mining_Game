package ca.cmpt276.as3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * This class is java class for help page
 */
public class HelpActivity extends AppCompatActivity {
    private final int THEME = R.id.theme_button;
    private final int COPYING = R.id.resource_button;
    private final int AUTHOR = R.id.author_button;
    private int current = THEME;

    public static Intent makeIntent(Context c) {
        return new Intent(c, HelpActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        setButton();

        TextView textView = findViewById(R.id.help_page_hyperlink);
        textView.setClickable(true);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        textView.setText(R.string.website);
    }

    private void setButton() {
        Button theme = findViewById(THEME);
        Button copying = findViewById(COPYING);
        Button author = findViewById(AUTHOR);

        theme.setOnClickListener(v -> select(THEME));
        copying.setOnClickListener(v -> select(COPYING));
        author.setOnClickListener(v -> select(AUTHOR));
    }

    private void select(int id) {
        deselect();

        Button button = findViewById(id);
        int color = ContextCompat.getColor(HelpActivity.this,
                R.color.btn_selected_help_page);
        button.setBackgroundTintList(ColorStateList.valueOf(color));

        View view;
        switch (id) {
            case THEME:
                view = findViewById(R.id.help_page_main);
                break;
            case COPYING:
                view = findViewById(R.id.help_page_cite);
                break;
            case AUTHOR:
                view = findViewById(R.id.help_page_author);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + id);
        }
        view.setVisibility(View.VISIBLE);

        current = id;
    }

    private void deselect() {
        Button button = findViewById(current);
        int color = ContextCompat.getColor(HelpActivity.this,
                R.color.btn_not_selected);
        button.setBackgroundTintList(ColorStateList.valueOf(color));

        View view;
        switch (current) {
            case THEME:
                view = findViewById(R.id.help_page_main);
                break;
            case COPYING:
                view = findViewById(R.id.help_page_cite);
                break;
            case AUTHOR:
                view = findViewById(R.id.help_page_author);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + current);
        }
        view.setVisibility(View.INVISIBLE);
    }
}