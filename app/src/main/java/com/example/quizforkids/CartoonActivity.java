package com.example.quizforkids;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Collections;
import java.util.List;

public class CartoonActivity extends AppCompatActivity {
    private TextView questionDisplay;
    private RadioButton option1, option2, option3, option4;
    private Button nextButton;
    private int currentQuestionIndex = 0;
    private int score = 0;
    private List<QuestionC> questions;
    private DatabaseHelper databaseHelper;

    private String selectedOptionText; // Variable to store the selected option text

    private int userId;
    private String chosenArea;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cartoon);

        // Retrieve user ID and chosen area from Intent
        Intent intent = getIntent();
        userId = intent.getIntExtra("USER_ID", -1);
        chosenArea = intent.getStringExtra("CHOSEN_AREA");

        // Initialize UI components
        questionDisplay = findViewById(R.id.questionDisplay);
        option1 = findViewById(R.id.option1);
        option2 = findViewById(R.id.option2);
        option3 = findViewById(R.id.option3);
        option4 = findViewById(R.id.option4);
        nextButton = findViewById(R.id.nextC);

        // Initialize database helper
        databaseHelper = new DatabaseHelper(this);

        // Fetch questions from the database helper
        questions = databaseHelper.loadQuestionsFromTextFileCartoon(this, "Cartoonquestions.txt");




        // Display random 4 questions
        displayRandomQuestions();

        // Set click listeners for each option
        option1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedOptionText = option1.getText().toString(); // Update the selected option
            }
        });

        option2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedOptionText = option2.getText().toString(); // Update the selected option
            }
        });

        option3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedOptionText = option3.getText().toString(); // Update the selected option
            }
        });

        option4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedOptionText = option4.getText().toString(); // Update the selected option
            }
        });

        // Set click listener for the "Next" button
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(selectedOptionText); // Check the answer before moving to the next question
                currentQuestionIndex++;
                if (currentQuestionIndex < questions.size()) {
                    displayQuestion(currentQuestionIndex);
                } else {
                    // Handle end of quiz
                    showScoreDialog();
                }
            }
        });
    }

    private void displayRandomQuestions() {
        // Shuffle the list of questions
        Collections.shuffle(questions);

        // Pick any 4 random questions
        List<QuestionC> randomQuestions = questions.subList(0, Math.min(4, questions.size()));

        // Set current questions to the selected random questions
        questions = randomQuestions;

        // Display the first question
        if (!questions.isEmpty()) {
            displayQuestion(currentQuestionIndex);
        }
    }

    private void displayQuestion(int index) {
        option1.setChecked(false);
        option2.setChecked(false);
        option3.setChecked(false);
        option4.setChecked(false);

        QuestionC question = questions.get(index);
        questionDisplay.setText(question.getQuestionText());
        List<String> options = question.getOptions();
        option1.setText(options.get(0));
        option2.setText(options.get(1));
        option3.setText(options.get(2));
        option4.setText(options.get(3));

        // Set the selected option if it's not null
        if (selectedOptionText != null) {
            if (option1.getText().toString().equals(selectedOptionText)) {
                option1.setChecked(true);
            } else if (option2.getText().toString().equals(selectedOptionText)) {
                option2.setChecked(true);
            } else if (option3.getText().toString().equals(selectedOptionText)) {
                option3.setChecked(true);
            } else if (option4.getText().toString().equals(selectedOptionText)) {
                option4.setChecked(true);
            }
        }
    }

    private void checkAnswer(String selectedAnswer) {
        // Check if the selected option is the correct answer
        String correctAnswer = questions.get(currentQuestionIndex).getCorrectAnswer();
        if (selectedAnswer != null && selectedAnswer.equals(correctAnswer)) {
            // Increment score if the answer is correct
            score++;
        }
    }

    private void showScoreDialog() {
        // Calculate the score message
        int totalQuestions = questions.size();
        int correctAnswers = score;
        int incorrectAnswers = totalQuestions - correctAnswers;
        int currentAttemptPoints = (correctAnswers * 3) - (incorrectAnswers * 1);
        int overallPoints = databaseHelper.getOverallPoints(userId) + currentAttemptPoints;
        int attemptNumber = databaseHelper.getAttemptNumber(userId);
        String chosenArea = getIntent().getStringExtra("CHOSEN_AREA");
        String username = databaseHelper.getUsername(userId);

        String message = "Well done " + username + ", you have finished the Cartoon quiz with "
                + correctAnswers + " correct and " + incorrectAnswers + " incorrect answers or "
                + currentAttemptPoints + " points for this attempt.\n"
                + "Overall you have " + overallPoints + " points.";

        // Display the score in an AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Quiz Finished")
                .setMessage(message)
                .setPositiveButton("Play Again", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Reset variables for next quiz
                        currentQuestionIndex = 0;
                        score = 0;
                        displayRandomQuestions();
                    }
                })
                .setNegativeButton("Return to Dashboard", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(CartoonActivity.this, DashBoardActivity.class);
                        intent.putExtra("USER_ID", userId);
                        startActivity(intent);
                        finish();
                    }
                })
                .setCancelable(false) // Prevent dismiss by tapping outside of the dialog
                .show();
    }

}
