package com.thegalos.maththinkers.objects;

public class Score {
    private String playerName;
    private String type;
    private int score;
    private int totalQuestions;
    private int timeTaken;
    private int totalWrong;
    private float ratio;

    public Score() {
    }

    public String getPlayerName() {
        return playerName;
    }
    public int getScore() {
        return score;
    }
    public int getTotalQuestions() {
        return totalQuestions;
    }
    public int getTimeTaken() {
        return timeTaken;
    }

    public float getRatio() {
        return ratio;
    }
    public String getType() {
        return type;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }
    public void setScore(int score) {
        this.score = score;
    }
    public void setTotalQuestions(int totalQuestions) {
        this.totalQuestions = totalQuestions;
    }
    public void setTimeTaken(int timeTaken) {
        this.timeTaken = timeTaken;
    }
    public void setTotalWrong(int totalWrong) {
        this.totalWrong = totalWrong;
    }
    public void setRatio(float ratio) {
        this.ratio = ratio;
    }
    public void setType(String type) {
        this.type = type;
    }


}
