package ca.cmpt276.as3.model;

import java.util.Objects;

/**
 * Game history store the history of game, it identified by GameConfig
 * the smaller bestScore is better in this game.
 * This class always be convert to JSON to store in disk.
 */
public class GameHistory {
    private int width;
    private int height;
    private int mineNumber;
    private int gameNumber;
    private int bestScore;

    public GameHistory() {
    }

    public GameHistory(int width, int height, int mineNumber) {
        this.width = width;
        this.height = height;
        this.mineNumber = mineNumber;
        gameNumber = 0;
        bestScore = width * height + 1;
    }

    public GameHistory(GameConfig config) {
        width = config.getWidth();
        height = config.getHeight();
        mineNumber = config.getMineNumber();
        gameNumber = 0;
        bestScore = -1;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getMineNumber() {
        return mineNumber;
    }

    public int getGameNumber() {
        return gameNumber;
    }

    public void increaseGameNumber() {
        gameNumber++;
    }

    public void setGameNumber(int gameNumber) {
        this.gameNumber = gameNumber;
    }

    public int getBestScore() {
        return bestScore;
    }

    public boolean updateBestScore(int score) {
        if (score < bestScore || bestScore == -1) {
            bestScore = score;
            return true;
        }
        return false;
    }

    public void clear() {
        bestScore = -1;
        gameNumber = 0;
    }

    public boolean isEqualConfig(GameConfig config) {
        return width == config.getWidth() && height == config.getHeight()
                && mineNumber == config.getMineNumber();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameHistory that = (GameHistory) o;
        return width == that.width &&
                height == that.height &&
                mineNumber == that.mineNumber;
    }

    @Override
    public int hashCode() {
        return Objects.hash(width, height, mineNumber);
    }
}
