package models;

public class GameSettingsModel {
    private int maxTurnSeconds;

    public GameSettingsModel(int maxTurnSeconds) {
        this.maxTurnSeconds = maxTurnSeconds;
    }

    public int getMaxTurnSeconds() {
        return maxTurnSeconds;
    }

    public void setMaxTurnSeconds(int maxTurnSeconds) {
        this.maxTurnSeconds = maxTurnSeconds;
    }
}
