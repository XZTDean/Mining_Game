package ca.cmpt276.as3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import ca.cmpt276.as3.model.GameConfig;

public class OptionActivity extends AppCompatActivity {

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
                    config.setHeight(height);
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
                }
            });

            radioGroup.addView(button);

            GameConfig config = GameConfig.getInstance();
            if (config.getMineNumber() == num) {
                button.setChecked(true);
            }
        }
    }
}