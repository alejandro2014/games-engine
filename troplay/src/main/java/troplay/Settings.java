package troplay;

public class Settings {
    private Language language = Language.SPANISH;
    private int numPlayers = 1;

    public Language getLanguage() { return language; }
    public int getNumPlayers() { return numPlayers; }

    public void setLanguage(Language language) { this.language = language; }
    public void setNumPlayers(int numPlayers) { this.numPlayers = numPlayers; }
}
