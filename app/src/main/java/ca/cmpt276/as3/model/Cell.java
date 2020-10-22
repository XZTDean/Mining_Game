package ca.cmpt276.as3.model;

/**
 * Cell store the information of each cell in the game,
 * including whether it is mine, whether it is revealed and etc.
 */
public class Cell {
    private boolean mine;
    private boolean mineRevealed;
    private boolean hidden;
    private int minesRelevant;

    public Cell() {
        mine = false;
        mineRevealed = false;
        hidden = true;
    }

    public void setMine() {
        this.mine = true;
    }

    private void revealRelevant() {
        if (mine && !mineRevealed) {
            throw new IllegalStateException();
        }
        this.hidden = false;
    }

    public void setMinesRelevant(int minesRelevant) {
        if (!hidden) {
            throw new IllegalStateException("Already reveal");
        } else if (minesRelevant < 0) {
            throw new IllegalArgumentException("Mines around should not smaller than 0");
        }
        this.minesRelevant = minesRelevant;
        revealRelevant();
    }

    public void relevantMineRevealed() {
        if (hidden) {
            throw new IllegalStateException("Cell is hidden");
        }
        minesRelevant--;
        if (minesRelevant < 0) {
            throw new IllegalStateException("Mines around should not smaller than 0");
        }
    }

    public void revealMine() {
        if (!mine) {
            throw new IllegalArgumentException("Not a mine");
        }
        this.mineRevealed = true;
    }

    public int getMinesRelevant() {
        if (!hidden) {
            return minesRelevant;
        } else {
            throw new IllegalStateException();
        }
    }

    public boolean isMine() {
        return mine;
    }

    public boolean isHidden() {
        return hidden;
    }

    public boolean isMineRevealed() {
        return mineRevealed;
    }
}
