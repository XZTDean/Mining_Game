package ca.cmpt276.as3.model;

public class Cell {
    private boolean mine;
    private boolean mineRevealed;
    private boolean hidden;
    private int minesAround;

    public Cell() {
        mine = false;
        mineRevealed = false;
        hidden = true;
    }

    public void setMine() {
        this.mine = true;
    }

    public void revealAround() {
        if (mine && !mineRevealed) {
            throw new IllegalStateException();
        }
        this.hidden = false;
    }

    public void setMinesAround(int minesAround) {
        if (!hidden) {
            throw new IllegalStateException("Already reveal");
        } else if (minesAround < 0) {
            throw new IllegalArgumentException("Mines around should not smaller than 0");
        }
        this.minesAround = minesAround;
        revealAround();
    }

    public void aroundMineRevealed() {
        if (hidden) {
            throw new IllegalStateException("Cell is hidden");
        }
        minesAround--;
        if (minesAround < 0) {
            throw new IllegalStateException("Mines around should not smaller than 0");
        }
    }

    public void revealMine() {
        if (!mine) {
            throw new IllegalArgumentException("Not a mine");
        }
        this.mineRevealed = true;
    }

    public int getMinesAround() {
        if (!hidden) {
            return minesAround;
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
