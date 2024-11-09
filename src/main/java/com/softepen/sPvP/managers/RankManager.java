package com.softepen.sPvP.managers;
import static com.softepen.sPvP.sPvP.pointsManager;

public class RankManager {
    private final String playerName;

    public RankManager(String playerName) {
        this.playerName = playerName;
    }

    public double getPoints() {
        double points = pointsManager.getDouble("players." + playerName);
        points = Math.floor(points * 100) / 100;
        if (points < 0) {
            pointsManager.set("players." + playerName, 0);
            return 0;
        }
        else return points;
    }

    public void setPoints(double value) {
        pointsManager.set("players." + playerName, value);
    }

    public void addPoints(double amount) {
        amount = getPoints() + amount;
        pointsManager.set("players." + playerName, amount);
    }

    public void removePoints(double amount) {
        amount = getPoints() - amount;
        pointsManager.set("players." + playerName, amount);
    }
}
