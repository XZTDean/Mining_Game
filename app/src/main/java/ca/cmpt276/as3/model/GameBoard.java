package ca.cmpt276.as3.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameBoard {
    private static GameBoard instance;
    private Cell[][] board;
    private int scanUsed;
    private int mineFound;
    private int mineNumber;
    private int width;
    private int height;

    public static GameBoard getInstance(int width, int height, int mineNumber) {
        if (instance == null) {
            instance = new GameBoard(width, height, mineNumber);
        }
        return instance;
    }

    public static GameBoard endGame() {
        GameBoard game = instance;
        instance = null;
        return game;
    }

    public GameBoard(int width, int height, int mineNumber) {
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException("Width and Height should be positive");
        } else if (mineNumber > width * height) {
            throw new IllegalArgumentException("Too many mines");
        }

        this.width = width;
        this.height = height;
        this.board = new Cell[height][width];
        this.mineNumber = mineNumber;
        scanUsed = mineFound = 0;
        setMine();
    }

    private void setMine() {
        int i = 0;
        Random random = new Random();
        while (i < mineNumber) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            Cell cell = getCell(x, y);
            if (!cell.isMine()) {
                cell.setMine();
                i++;
            }
        }
    }

    public boolean scan(int x, int y) {
        Cell cell = getCell(x, y);
        if (!cell.isHidden()) { // already display the number
            return false;
        } else if (cell.isMine() && !cell.isMineRevealed()) { // Find a mine
            cell.revealMine();
            changeAroundNumber(x, y);
            mineFound++;
        } else {
            int mines = getAroundMines(x, y);
            cell.setMinesAround(mines);
            scanUsed++;
        }
        return true;
    }

    private void changeAroundNumber(int x, int y) {
        for (Cell around : getAroundCell(x, y)) {
            if (!around.isHidden()) {
                around.aroundMineRevealed();
            }
        }
    }

    private int getAroundMines(int x, int y) {
        int mines = 0;
        for (Cell around : getAroundCell(x, y)) {
            if (around.isMine() && !around.isMineRevealed()) {
                mines++;
            }
        }
        return mines;
    }

    private List<Cell> getAroundCell(int x, int y) {
        List<Cell> around = new ArrayList<>();
        for (int i = y - 1; i <= y + 1; i++) {
            if (i >= height || i < 0) {
                continue;
            }
            for (int j = x - 1; j <= x + 1; j++) {
                if (j >= width || j < 0) {
                    continue;
                }
                around.add(getCell(j, i));
            }
        }
        return around;
    }

    public int getScanUsed() {
        return scanUsed;
    }

    public int getMineFound() {
        return mineFound;
    }

    public int getMineNumber() {
        return mineNumber;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    private Cell getCell(int x, int y) {
        if (board == null) {
            throw new NullPointerException();
        }
        if (x >= width || y >= height || x < 0 || y < 0) {
            throw new IndexOutOfBoundsException();
        }
        return board[y][x];
    }
}
