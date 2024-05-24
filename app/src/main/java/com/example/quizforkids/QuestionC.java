package com.example.quizforkids;

import java.util.List;

public class QuestionC {
    private String questionText;
    private List<String> options;
    private String correctAnswer;

    public QuestionC(String questionText, List<String> options, String correctAnswer) {
        this.questionText = questionText;
        this.options = options;
        this.correctAnswer = correctAnswer;
    }

    // Getters
    public String getQuestionText() {
        return questionText;
    }

    public List<String> getOptions() {
        return options;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }
}
