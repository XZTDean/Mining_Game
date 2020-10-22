package ca.cmpt276.as3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

/**
 * This class is java class for help page
 */
public class HelpActivity extends AppCompatActivity {

    public static Intent makeIntent(Context c) {
        return new Intent(c, HelpActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        TextView textView = findViewById(R.id.help_page_hyperlink);
        textView.setClickable(true);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        String text = "<a href='https://opencoursehub.cs.sfu.ca/bfraser/grav-cms/cmpt276/home'> Course Website </a>";
        textView.setText(Html.fromHtml(text));
    }
}