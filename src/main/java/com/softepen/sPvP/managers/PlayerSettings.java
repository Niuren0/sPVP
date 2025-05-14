package com.softepen.sPvP.managers;

public class PlayerSettings {
    private final boolean sound;
    private final boolean healthIndicator;
    private final String healthIndicatorColor;
    private final boolean comboMessages;
    private final Integer lastCombo;
    private final Integer comboRecord;
    private final Integer killSeriesRecord;
    private final Integer kills;
    private final Integer deaths;
    private final String killMessage;
    private final String comboSound;
    private final String particle;

    public PlayerSettings(boolean sound, boolean healthIndicator, String healthIndicatorColor, boolean comboMessages, Integer lastCombo, Integer comboRecord, Integer killSeriesRecord, Integer kills, Integer deaths, String killMessage, String comboSound, String particle) {
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
        this.comboSound = comboSound;
        this.particle = particle;
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

    public String getComboSound() {
        return comboSound;
    }

    public String getParticle() {
        return particle;
    }
}
