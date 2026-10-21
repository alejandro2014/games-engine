package troplay;

public enum Difficulty {
    EASY,
    MEDIUM,
    HARD;

    public int databaseLevel() {
        return ordinal() + 1;
    }
}
