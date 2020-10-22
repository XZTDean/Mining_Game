package ca.cmpt276.as3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
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

import ca.cmpt276.as3.model.GameBoard;
import ca.cmpt276.as3.model.GameConfig;
import ca.cmpt276.as3.model.GameHistory;

public class GameActivity extends AppCompatActivity {
    private GameBoard game;
    private FloatingActionButton[][] buttons;

    public static Intent makeIntent(Context c) {
        return new Intent(c, GameActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        game = new GameBoard();
        populateTable();
        try {
            updateHistory();
        } catch (IOException e) {
            e.printStackTrace();
        }

        TextView textView = findViewById(R.id.mineTotal);
        String totalMine = String.valueOf(game.getMineNumber());
        textView.setText(totalMine);
    }

    private void populateTable() {
        TableLayout table = findViewById(R.id.buttonTable);
        GameConfig config = GameConfig.getInstance();
        buttons = new FloatingActionButton[config.getHeight()][config.getWidth()];
        int color = ContextCompat.getColor(this, R.color.potential);
        for (int row = 0; row < config.getHeight(); row++) {
            TableRow tableRow = new TableRow(this);
            tableRow.setLayoutParams(new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.MATCH_PARENT,
                    1));
            table.addView(tableRow);
            final int FINAL_ROW = row;

            for (int col = 0; col < config.getWidth(); col++) {
                final int FINAL_COL = col;
                FloatingActionButton button = new FloatingActionButton(this);
                TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.MATCH_PARENT,
                        1);
                layoutParams.setMargins(8,8,8,8);
                button.setLayoutParams(layoutParams);

                button.setImageResource(R.drawable.ic_user);
                button.setBackgroundTintList(ColorStateList.valueOf(color));
                button.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        scan(FINAL_COL, FINAL_ROW);
                    }
                });
                buttons[row][col] = button;
                tableRow.addView(button);
            }
        }
    }

    private void scan(int col, int row) {
        switch (game.scan(col, row)) {
            case -1: // already scan
                playSound(R.raw.deny);
                break;
            case 0: // new scan
                setNumber(col, row, true);
                playSound(R.raw.number);
                scanRowCol(col, row);
                setScanNum();
                break;
            case 1: // new mine
                setMine(col, row);
                playSound(R.raw.mine);
                reSetRelevantNumber(col, row);
                setFoundNumber();
                if (game.isWin()) {
                    win();
                }
                break;
            case 2: // mine with scan
                setNumber(col, row, false);
                playSound(R.raw.number);
                scanRowCol(col, row);
                setScanNum();
        }
    }

    private void playSound(int resid) {
        MediaPlayer mp = MediaPlayer.create(this, resid);
        mp.start();
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.release();
            }
        });
    }

    private void setFoundNumber() {
        TextView textView = findViewById(R.id.mineFound);
        String found = String.valueOf(game.getMineFound());
        textView.setText(found);
    }

    private void setScanNum() {
        TextView textView = findViewById(R.id.scanNum);
        String scanUsed = String.valueOf(game.getScanUsed());
        textView.setText(scanUsed);
    }

    private void setMine(int col, int row) {
        FloatingActionButton button = buttons[row][col];
        int color = ContextCompat.getColor(this, R.color.confirmed);
        button.setBackgroundTintList(ColorStateList.valueOf(color));
        button.setImageResource(R.drawable.ic_bacteria);
    }

    private void reSetRelevantNumber(int col, int row) {
        for (int i = 0; i < game.getWidth(); i++) {
            if (!game.cellIsHidden(i, row)) {
                setNumber(i, row, false);
            }
        }
        for (int i = 0; i < game.getHeight(); i++) {
            if (!game.cellIsHidden(col, i)) {
                setNumber(col, i, false);
            }
        }
    }

    private void setNumber(int col, int row, boolean changeColor) {
        FloatingActionButton button = buttons[row][col];
        if (changeColor) {
            int color = ContextCompat.getColor(this, R.color.safe);
            button.setBackgroundTintList(ColorStateList.valueOf(color));
        }
        int num = game.getMineRelevantForCell(col, row);
        String resourceName = "ic_";
        if (num < 10) {
            resourceName += num;
        } else {
            resourceName += "9_plus";
        }
        int id = getResources().getIdentifier(resourceName, "drawable", this.getPackageName());
        button.setImageResource(id);
    }

    private void scanAnimation(int col, int row) {
        FloatingActionButton button = buttons[row][col];
        ObjectAnimator animation = ObjectAnimator.ofFloat(button, "alpha", 1f, 0.3f);
        animation.setRepeatCount(1);
        animation.setRepeatMode(ObjectAnimator.REVERSE);
        animation.setDuration(200);
        animation.start();
    }

    private void scanRowCol(int col, int row) {
        for (int i = 0; i < game.getWidth(); i++) {
            scanAnimation(i, row);
        }
        for (int i = 0; i < game.getHeight(); i++) {
            scanAnimation(col, i);
        }
    }

    private void win() {
        GameHistory history = null;
        try {
            history = updateWin();
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert history != null;

        WinDialogFragment winDialog = WinDialogFragment.getInstance(history, game.getScanUsed());
        winDialog.show(getSupportFragmentManager(), "WinDialogFragment");
    }

    private GameHistory updateWin() throws IOException {
        File historyFile = new File(this.getFilesDir(), "history");
        String json = readFromFile(historyFile);
        Gson gson = new Gson();
        Type collectionType = new TypeToken<ArrayList<GameHistory>>(){}.getType();
        List<GameHistory> historyList = gson.fromJson(json, collectionType);
        GameHistory history = getHistory(historyList);
        if (updateBestScore(history)) {
            json = gson.toJson(historyList);
            writeToFile(historyFile, json);
        }
        return history;
    }

    private void updateHistory() throws IOException {
        File historyFile = new File(this.getFilesDir(), "history");
        String json = readFromFile(historyFile);
        Gson gson = new Gson();
        Type collectionType = new TypeToken<ArrayList<GameHistory>>(){}.getType();
        List<GameHistory> historyList = gson.fromJson(json, collectionType);
        addDate(historyList);
        json = gson.toJson(historyList);
        writeToFile(historyFile, json);
    }

    private GameHistory getHistory(List<GameHistory> historyList) {
        GameConfig config = GameConfig.getInstance();
        for (GameHistory history : historyList) {
            if (history.isEqualConfig(config)) {
                return history;
            }
        }
        GameHistory gameHistory = new GameHistory(config);
        historyList.add(gameHistory);
        return gameHistory;
    }

    private boolean updateBestScore(GameHistory history) {
        int score = game.getScanUsed();
        return history.updateBestScore(score);
    }

    private void addDate(List<GameHistory> historyList) {
        GameHistory history = getHistory(historyList);
        history.increaseGameNumber();
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
}