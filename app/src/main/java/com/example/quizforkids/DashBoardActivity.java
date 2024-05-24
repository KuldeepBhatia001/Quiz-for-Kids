package com.example.quizforkids;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class DashBoardActivity extends AppCompatActivity {
    private TextView logOffDash;
    private Button buttonAnimal, buttonCartoon, Instruct;
    private TextView userscore;
   private DatabaseHelper dbHelper;

    private int userId;
    private String chosenArea;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dashboard);

        buttonAnimal = (Button) findViewById(R.id.buttonAnimal);
        buttonCartoon = (Button) findViewById(R.id.buttonCartoon);
        Instruct = (Button) findViewById(R.id.instruct);
        logOffDash = (TextView) findViewById(R.id.logOffDash);
        userscore = (TextView) findViewById(R.id.userscore);
        dbHelper = new DatabaseHelper(this);

        userId = getIntent().getIntExtra("USER_ID", -1);

        // Check if userId is valid
        if (userId == -1) {
            Toast.makeText(this, "User ID is invalid", Toast.LENGTH_SHORT).show();
            finish(); // End the activity if userId is invalid
            return;
        }

        buttonAnimal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashBoardActivity.this, AnimalActivity.class);
                intent.putExtra("USER_ID", userId);
                intent.putExtra("CHOSEN_AREA", chosenArea);
                startActivity(intent);
                finish();

            }
        });

        buttonCartoon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashBoardActivity.this, CartoonActivity.class);
                intent.putExtra("USER_ID", userId);
                intent.putExtra("CHOSEN_AREA", chosenArea);
                startActivity(intent);
                finish();
            }
        });

        Instruct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRulesDialog();

            }
        });


        logOffDash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Fetch overall points and username
                int overallPoints = dbHelper.getOverallPoints(userId);
                String username = dbHelper.getUsername(userId);

                // Construct the message
                String message = username + ", you have overall " + overallPoints + " points.";

                // Display AlertDialog
                AlertDialog.Builder builder = new AlertDialog.Builder(DashBoardActivity.this);
                builder.setTitle("Log Off")
                        .setMessage(message)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Redirect to login screen
                                Intent intent = new Intent(DashBoardActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        })
                        .setCancelable(false)
                        .show();
            }
        });



        displayUserScore();


    }

    private void showRulesDialog() {
        Dialog dialog = new Dialog(DashBoardActivity.this);
        dialog.setContentView(R.layout.rules_popup);

        // Show the dialog
        dialog.show();
    }

    private void displayUserScore() {
        int overallPoints = dbHelper.getOverallPoints(userId);
        String username = dbHelper.getUsername(userId);
        userscore.setText("Hi " + username + ", you have overall " + overallPoints + " points.");
    }
}