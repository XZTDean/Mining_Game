package ca.cmpt276.as3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import ca.cmpt276.as3.model.GameConfig;

public class OptionActivity extends AppCompatActivity {

    public static final String MINE_KEY = "Number of mine";
    public static final String WIDTH_KEY = "Width of board";
    public static final String HEIGHT_KEY = "Height of board";

    public static Intent makeIntent(Context c) {
        return new Intent(c, OptionActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option);

        createBoardSizeSelection();
        createMineSelection();
    }

    private void createBoardSizeSelection() {
        RadioGroup radioGroup = findViewById(R.id.board_size_option);
        int[] widthList = getResources().getIntArray(R.array.board_width);
        int[] heightList = getResources().getIntArray(R.array.board_height);

        for (int i = 0; i < widthList.length && i < heightList.length; i++) {
            int width = widthList[i];
            int height = heightList[i];

            RadioButton button = new RadioButton(this);
            button.setText(getString(R.string.board_size_show, height, width));

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GameConfig config = GameConfig.getInstance();
                    config.setWidth(width);
                    saveValue(WIDTH_KEY, width);
                    config.setHeight(height);
                    saveValue(HEIGHT_KEY, height);
                }
            });

            radioGroup.addView(button);

            GameConfig config = GameConfig.getInstance();
            if (config.getWidth() == width && config.getHeight() == height) {
                button.setChecked(true);
            }
        }
    }

    private void createMineSelection() {
        RadioGroup radioGroup = findViewById(R.id.num_mine_option);
        int[] mineNumList = getResources().getIntArray(R.array.mine_num);

        for (int num : mineNumList) {
            RadioButton button = new RadioButton(this);
            button.setText(String.valueOf(num));

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GameConfig config = GameConfig.getInstance();
                    config.setMineNumber(num);
                    saveValue(MINE_KEY, num);
                }
            });

            radioGroup.addView(button);

            GameConfig config = GameConfig.getInstance();
            if (config.getMineNumber() == num) {
                button.setChecked(true);
            }
        }
    }

    private void saveValue(String key, int num) {
        SharedPreferences prefs = this.getSharedPreferences("AppPreference", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(key, num);
        editor.apply();
    }

    static public int getValue(Context context, String key) {
        SharedPreferences prefs = context.getSharedPreferences("AppPreference", MODE_PRIVATE);
        int defaultSize;
        switch (key) {
            case MINE_KEY:
                defaultSize = context.getResources().getInteger(R.integer.default_mine_num);
                break;
            case WIDTH_KEY:
                defaultSize = context.getResources().getInteger(R.integer.default_board_width);
                break;
            case HEIGHT_KEY:
                defaultSize = context.getResources().getInteger(R.integer.default_board_height);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + key);
        }
        return prefs.getInt(key, defaultSize);
    }
}