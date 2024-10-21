package com.softepen.sPvP.managers;

public class PlayerSettings {
    private final boolean sound;
    private final boolean healthIndicator;
    private final String healthIndicatorColor;
    private final boolean comboMessages;
    private final int lastCombo;
    private final int comboRecord;
    private final int killSeriesRecord;
    private final int kills;
    private final int deaths;
    private final String killMessage;

    public PlayerSettings(boolean sound, boolean healthIndicator, String healthIndicatorColor, boolean comboMessages, int lastCombo, int comboRecord, int killSeriesRecord, int kills, int deaths, String killMessage) {
        this.sound = sound;
        this.healthIndicator = healthIndicator;
        this.healthIndicatorColor = healthIndicatorColor;
        this.comboMessages = comboMessages;
        this.lastCombo = lastCombo;
        this.comboRecord = comboRecord;
        this.killSeriesRecord = killSeriesRecord;
        this.kills = kills;
        this.deaths = deaths;
        this.killMessage = killMessage;
    }

    public boolean getSound() {
        return sound;
    }

    public boolean getHealthIndicator() {
        return healthIndicator;
    }

    public String getHealthIndicatorColor() {
        return healthIndicatorColor;
    }

    public boolean getComboMessages() {
        return comboMessages;
    }

    public int getLastCombo() {
        return lastCombo;
    }

    public int getComboRecord() {
        return comboRecord;
    }

    public int getKillSeriesRecord() {
        return killSeriesRecord;
    }

    public int getKills() {
        return kills;
    }

    public int getDeaths() {
        return deaths;
    }

    public String getKillMessage() {
        return killMessage;
    }
}
