package ca.cmpt276.as3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import ca.cmpt276.as3.model.GameConfig;
import ca.cmpt276.as3.model.GameHistory;

/**
 * This is class for option choosing page, it will read and update
 * history data, and display history data to user. It saved the
 * change of the config, both in memory and disk.
 */
public class OptionActivity extends AppCompatActivity {

    public static final String MINE_KEY = "Number of mine";
    public static final String WIDTH_KEY = "Width of board";
    public static final String HEIGHT_KEY = "Height of board";
    private List<GameHistory> historyList;

    public static Intent makeIntent(Context c) {
        return new Intent(c, OptionActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option);

        createBoardSizeSelection();
        createMineSelection();

        Button button = findViewById(R.id.clear_history);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GameHistory history = getHistoryByConfig(GameConfig.getInstance());
                history.clear();
                updateHistoryDisplay(history);
                saveHistory();
            }
        });

        getHistory();
        GameHistory history = getHistoryByConfig(GameConfig.getInstance());
        updateHistoryDisplay(history);
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
                    GameHistory history = getHistoryByConfig(config);
                    updateHistoryDisplay(history);
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
                    GameHistory history = getHistoryByConfig(config);
                    updateHistoryDisplay(history);
                }
            });

            radioGroup.addView(button);

            GameConfig config = GameConfig.getInstance();
            if (config.getMineNumber() == num) {
                button.setChecked(true);
            }
        }
    }

    private void updateHistoryDisplay(GameHistory history) {
        TextView textView = findViewById(R.id.num_played);
        String str = getString(R.string.num_played, history.getGameNumber());
        if (history.getBestScore() < 0) {
            str += getString(R.string.no_best_score);
        } else {
            str += getString(R.string.best_score, history.getBestScore());
        }
        textView.setText(str);
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

    private void getHistory() {
        String json = null;
        try {
            File historyFile = new File(this.getFilesDir(), GameHistory.HISTORY_DIR);
            json = readFromFile(historyFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Gson gson = new Gson();
        Type collectionType = new TypeToken<ArrayList<GameHistory>>(){}.getType();
        historyList = gson.fromJson(json, collectionType);
    }

    private void saveHistory() {
        File historyFile = new File(this.getFilesDir(), GameHistory.HISTORY_DIR);
        Gson gson = new Gson();
        String json = gson.toJson(historyList);
        try {
            writeToFile(historyFile, json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeToFile(File file, String json) throws IOException {
        FileWriter writer = new FileWriter(file);
        writer.write(json);
        writer.close();
    }

    private String readFromFile(File file) throws IOException {
        if (!file.exists()) {
            return "[]";
        }
        BufferedReader reader = new BufferedReader(new FileReader(file));
        StringBuilder builder = new StringBuilder();
        String tmp;
        while ((tmp = reader.readLine()) != null) {
            builder.append(tmp).append('\n');
        }
        return builder.toString();
    }

    private GameHistory getHistoryByConfig(GameConfig config) {
        for (GameHistory history : historyList) {
            if (history.isEqualConfig(config)) {
                return history;
            }
        }
        GameHistory gameHistory = new GameHistory(config);
        historyList.add(gameHistory);
        saveHistory();
        return gameHistory;
    }
}