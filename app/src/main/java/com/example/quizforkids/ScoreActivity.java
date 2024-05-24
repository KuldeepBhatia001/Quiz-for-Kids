package com.example.quizforkids;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

// ScoreActivity.java
public class ScoreActivity extends AppCompatActivity {
    private TextView scoreDisplay;
    private Button backButton;
    private int score;
    private int userId;
    private String chosenArea;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        // Retrieve the score, userId, and chosenArea from the Intent
        Intent intent = getIntent();
        score = intent.getIntExtra("SCORE", 0);
        userId = intent.getIntExtra("USER_ID", -1);
        chosenArea = intent.getStringExtra("CHOSEN_AREA");

        // Initialize UI components
        scoreDisplay = findViewById(R.id.scoreDisplay);
        backButton = findViewById(R.id.backButton);

        // Display the score
        scoreDisplay.setText("Your score: " + score);

        // Set click listener for the "Back to Dashboard" button
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Navigate back to DashboardActivity
                Intent intent = new Intent(ScoreActivity.this, DashBoardActivity.class);
                intent.putExtra("USER_ID", userId);
                intent.putExtra("CHOSEN_AREA", chosenArea);
                startActivity(intent);
                finish();
            }
        });
    }
}
