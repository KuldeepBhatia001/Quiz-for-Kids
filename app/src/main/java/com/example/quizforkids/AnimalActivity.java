package com.example.quizforkids;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Collections;
import java.util.List;

public class AnimalActivity extends AppCompatActivity {
    private ImageView questionImageView;
    private EditText answerEditText;
    private Button nextButton;

    private List<Question> questionsList;
    private int currentQuestionIndex = 0;
    private int correctAnswers = 0;
    private int incorrectAnswers = 0;
    private static final int MAX_QUESTIONS = 4;

    private int userId;
    private String chosenArea;
    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_animal);

        Intent intent = getIntent();
        userId = intent.getIntExtra("USER_ID", -1);
        chosenArea = intent.getStringExtra("CHOSEN_AREA");

        Log.d("AnimalActivity", "onCreate: userId = " + userId + ", chosenArea = " + chosenArea);

        // Check if userId is valid
        if (userId == -1) {
            Toast.makeText(this, "User ID is invalid", Toast.LENGTH_SHORT).show();
            finish(); // End the activity if userId is invalid
            return;
        }

        questionImageView = findViewById(R.id.questionImageView);
        answerEditText = findViewById(R.id.answerEditText);
        nextButton = findViewById(R.id.nextButton);

        dbHelper = new DatabaseHelper(this);
        questionsList = dbHelper.loadQuestionsFromTextFile(this, "Animalquestions.txt");
        Collections.shuffle(questionsList);

        Log.d("AnimalActivity", "onCreate: Loaded and shuffled questions");

        // Ensure only MAX_QUESTIONS are considered
        if (questionsList.size() > MAX_QUESTIONS) {
            questionsList = questionsList.subList(0, MAX_QUESTIONS);
        }

        Log.d("AnimalActivity", "onCreate: questionsList size = " + questionsList.size());

        // Display the first question
        displayQuestion();

        // Handle Next button click
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateAnswer();
            }
        });
    }

    private void displayQuestion() {
        if (currentQuestionIndex < questionsList.size()) {
            Question question = questionsList.get(currentQuestionIndex);
            questionImageView.setImageResource(question.getQuestionImageId());
            answerEditText.setText("");
            Log.d("AnimalActivity", "displayQuestion: Displayed question index " + currentQuestionIndex);
        } else {
            displayScore();
        }
    }

    private void validateAnswer() {
        String userAnswer = answerEditText.getText().toString().trim();
        Log.d("AnimalActivity", "validateAnswer: userAnswer = " + userAnswer);

        if (!userAnswer.isEmpty()) {
            String correctAnswer = questionsList.get(currentQuestionIndex).getCorrectAnswer();
            if (userAnswer.equalsIgnoreCase(correctAnswer)) {
                correctAnswers++;
                Log.d("AnimalActivity", "validateAnswer: Correct answer. correctAnswers = " + correctAnswers);
            } else {
                incorrectAnswers++;
                Log.d("AnimalActivity", "validateAnswer: Incorrect answer. incorrectAnswers = " + incorrectAnswers);
            }
        } else {
            incorrectAnswers++;
            Log.d("AnimalActivity", "validateAnswer: Empty answer. incorrectAnswers = " + incorrectAnswers);
        }

        currentQuestionIndex++;
        Log.d("AnimalActivity", "validateAnswer: currentQuestionIndex = " + currentQuestionIndex);

        if (currentQuestionIndex >= MAX_QUESTIONS) {
            displayScore();
        } else {
            displayQuestion();
        }
    }

    private void displayScore() {
        Log.d("AnimalActivity", "displayScore: Displaying score");
        int currentAttemptPoints = (correctAnswers * 3) - (incorrectAnswers * 1);
        int overallPoints = dbHelper.getOverallPoints(userId) + currentAttemptPoints;
        int attemptNumber = dbHelper.getAttemptNumber(userId);
        dbHelper.addScore(userId, currentAttemptPoints, attemptNumber, chosenArea);
        saveOverallPoints(overallPoints); // Save the new overall points

        String username = dbHelper.getUsername(userId);
        String chosenArea = getIntent().getStringExtra("CHOSEN_AREA");

        String message = "Well done " + username + ", you have finished the Animal quiz with "
                + correctAnswers + " correct and " + incorrectAnswers + " incorrect answers or "
                + currentAttemptPoints + " points for this attempt.\n"
                + "Overall you have " + overallPoints + " points.";

        // Create and show the AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Quiz Results");
        builder.setMessage(message);
        builder.setCancelable(false); // Prevent closing the dialog by clicking outside
        builder.setPositiveButton("Play Again", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Reset the quiz
                currentQuestionIndex = 0;
                correctAnswers = 0;
                incorrectAnswers = 0;
                Collections.shuffle(questionsList);
                displayQuestion();
            }
        });
        builder.setNegativeButton("Return to Dashboard", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Return to DashboardActivity
                Intent intent = new Intent(AnimalActivity.this, DashBoardActivity.class);
                intent.putExtra("USER_ID", userId);
                startActivity(intent);
                finish();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
        Log.d("AnimalActivity", "displayScore: AlertDialog shown");
    }






    private void saveOverallPoints(int points) {
    }

}




