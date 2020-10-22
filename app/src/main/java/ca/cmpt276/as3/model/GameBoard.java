package ca.cmpt276.as3.model;

import java.util.Random;

/**
 * GameBoard class keep the information for each game, it get
 * config directly from GameConfig.
 */
public class GameBoard {
    private Cell[][] board;
    private int scanUsed;
    private int mineFound;
    private int mineNumber;
    private int width;
    private int height;

    public GameBoard() {
        GameConfig config = GameConfig.getInstance();
        if (config.getWidth() <= 0 || config.getHeight() <= 0) {
            throw new IllegalArgumentException("Width and Height should be positive");
        } else if (config.getMineNumber() > config.getWidth() * config.getHeight()) {
            throw new IllegalArgumentException("Too many mines");
        }

        this.width = config.getWidth();
        this.height = config.getHeight();
        this.board = new Cell[height][width];
        this.mineNumber = config.getMineNumber();
        scanUsed = mineFound = 0;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                board[i][j] = new Cell();
            }
        }
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

    public int scan(int x, int y) {
        Cell cell = getCell(x, y);
        if (!cell.isHidden()) { // already display the number
            return -1;
        } else if (cell.isMine() && !cell.isMineRevealed()) { // Find a mine
            cell.revealMine();
            changeRelevantCellNumber(x, y);
            mineFound++;
            return 1;
        } else {
            int mines = getRelevantMines(x, y);
            cell.setMinesRelevant(mines);
            scanUsed++;
            if (cell.isMine()) {
                return 2;
            } else {
                return 0;
            }
        }
    }

    private void changeRelevantCellNumber(int x, int y) {
        for (int i = 0; i < width; i++) {
            Cell cell = getCell(i, y);
            if (!cell.isHidden()) {
                cell.relevantMineRevealed();
            }
        }
        for (int i = 0; i < height; i++) {
            Cell cell = getCell(x, i);
            if (!cell.isHidden()) {
                cell.relevantMineRevealed();
            }
        }
    }

    private int getRelevantMines(int x, int y) {
        int mines = 0;
        for (int i = 0; i < width; i++) {
            Cell cell = getCell(i, y);
            if (cell.isMine() && !cell.isMineRevealed()) {
                mines++;
            }
        }
        for (int i = 0; i < height; i++) {
            Cell cell = getCell(x, i);
            if (cell.isMine() && !cell.isMineRevealed()) {
                mines++;
            }
        }
        return mines;
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

    public int getMineRelevantForCell(int x, int y) {
        return getCell(x, y).getMinesRelevant();
    }

    public boolean cellIsHidden(int x, int y) {
        return getCell(x, y).isHidden();
    }

    public boolean isWin() {
        return mineFound == mineNumber;
    }
}
