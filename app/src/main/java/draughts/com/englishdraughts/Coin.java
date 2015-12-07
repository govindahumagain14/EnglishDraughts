package draughts.com.englishdraughts;

public class Coin {
    private boolean isPresent;

    public void setIsPresent(boolean isPresent) {
        this.isPresent = isPresent;
    }

    public void setIsRed(boolean isRed) {
        this.isRed = isRed;
    }

    public boolean isPresent() {
        return isPresent;
    }

    public boolean isRed() {
        return isRed;
    }

    private boolean isRed;
}
