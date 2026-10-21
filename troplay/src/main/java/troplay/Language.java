package troplay;

public enum Language {
    SPANISH("es", "Jugador %d acierta", "Jugador %d falla", "Jugador %d frena", "Jugador %d gana"),
    ENGLISH("en", "Player %d is right", "Player %d fails", "Player %d brakes", "Player %d wins");

    private final String code;
    private final String correctMessage;
    private final String wrongMessage;
    private final String brakeMessage;
    private final String winMessage;

    Language(String code, String correctMessage, String wrongMessage,
             String brakeMessage, String winMessage) {
        this.code = code;
        this.correctMessage = correctMessage;
        this.wrongMessage = wrongMessage;
        this.brakeMessage = brakeMessage;
        this.winMessage = winMessage;
    }

    /** Name of the variant of a localized graphic, which has one per language inside its folder */
    public String code() { return code; }
    public int databaseId() { return ordinal() + 1; }

    public String correct(int player) { return correctMessage.formatted(player); }
    public String wrong(int player) { return wrongMessage.formatted(player); }
    public String brake(int player) { return brakeMessage.formatted(player); }
    public String win(int player) { return winMessage.formatted(player); }
}
