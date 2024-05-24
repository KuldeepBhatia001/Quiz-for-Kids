public class Attempt {
    private String chosenArea;
    private int attemptNumber;
    private int pointsEarned;
    private String timestamp;

    public Attempt(String chosenArea, int attemptNumber, int pointsEarned, String timestamp) {
        this.chosenArea = chosenArea;
        this.attemptNumber = attemptNumber;
        this.pointsEarned = pointsEarned;
        this.timestamp = timestamp;
    }

    public String getChosenArea() {
        return chosenArea;
    }

    public int getAttemptNumber() {
        return attemptNumber;
    }

    public int getPointsEarned() {
        return pointsEarned;
    }

    public String getTimestamp() {
        return timestamp;
    }
}
