package com.example.quizforkids;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class RegisterationActivity extends AppCompatActivity {

    private EditText inputUsername, inputEmail, inputPassword, inputConfirmPassword;
    private Button buttonRegister;
    private TextView signin;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registeration);

        inputUsername = findViewById(R.id.inputUsername);
        inputEmail = findViewById(R.id.inputEmail);
        inputPassword = findViewById(R.id.inputPassword);
        inputConfirmPassword = findViewById(R.id.inputConfirmPassword);
        buttonRegister = findViewById(R.id.buttonRegister);
        signin = findViewById(R.id.signin);

        db = new DatabaseHelper(this);

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = inputUsername.getText().toString().trim();
                String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();
                String confirmPassword = inputConfirmPassword.getText().toString().trim();

                if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                    Toast.makeText(RegisterationActivity.this, "Fields can't be empty", Toast.LENGTH_SHORT).show();
                } else if (!password.equals(confirmPassword)) {
                    Toast.makeText(RegisterationActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                } else {
                    boolean isInserted = db.addUser(email, username, password);
                    if (isInserted) {
                        Toast.makeText(RegisterationActivity.this, "Registered Successfully", Toast.LENGTH_SHORT).show();
                        // Redirect to LoginActivity
                        Intent intent = new Intent(RegisterationActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(RegisterationActivity.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterationActivity.this, LoginActivity.class );
                startActivity(intent);

            }
        });
    }
}
