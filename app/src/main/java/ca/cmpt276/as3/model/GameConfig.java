package ca.cmpt276.as3.model;

/**
 * Game config store the config information of game
 * This is a singleton pattern class, by using getInstance
 * there will be same copy of information provided.
 */
public class GameConfig {
    private static GameConfig instance;

    private int mineNumber;
    private int width;
    private int height;

    public static GameConfig getInstance() {
        if (instance == null) {
            instance = new GameConfig();
        }
        return instance;
    }

    private GameConfig() {
        mineNumber = 6;
        width = 6;
        height = 4;
    }

    public int getMineNumber() {
        return mineNumber;
    }

    public void setMineNumber(int mineNumber) {
        if (mineNumber <= 0) {
            throw new IllegalArgumentException("Number of Mine should be positive");
        }
        this.mineNumber = mineNumber;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        if (width <= 0) {
            throw new IllegalArgumentException("Width should be positive");
        }
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        if (height <= 0) {
            throw new IllegalArgumentException("Height should be positive");
        }
        this.height = height;
    }
}
