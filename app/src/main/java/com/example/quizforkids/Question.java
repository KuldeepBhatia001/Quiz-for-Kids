package com.example.quizforkids;

public class Question {
    private String category;
    private int questionImageId;
    private String correctAnswer;

    // Constructor
    public Question(String category, int questionImageId, String correctAnswer) {
        this.category = category;
        this.questionImageId = questionImageId;
        this.correctAnswer = correctAnswer;
    }

    // Getters and setters
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getQuestionImageId() {
        return questionImageId;
    }

    public void setQuestionImageId(int questionImageId) {
        this.questionImageId = questionImageId;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }
}
