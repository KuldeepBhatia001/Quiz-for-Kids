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

public class LoginActivity extends AppCompatActivity {
    private EditText lgnEmail, lgnPassword;
    private TextView  forgotPassword, signuplinear;
    private Button Btnlogin;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        lgnEmail = (EditText) findViewById(R.id.inputEmail);
        lgnPassword = (EditText) findViewById(R.id.inputPassword);
        forgotPassword = (TextView) findViewById(R.id.forgotPassword);
        signuplinear = (TextView) findViewById(R.id.signuplinear);

        Btnlogin = (Button) findViewById(R.id.Btnlogin);

        db = new DatabaseHelper(this);


        Btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = lgnEmail.getText().toString().trim();
                String password = lgnPassword.getText().toString().trim();

                boolean isAuthenticated = db.checkUser(email, password);
                int userId = db.getUserIdByEmailAndPassword(email, password);
                if(isAuthenticated && userId != -1 ){
                    Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                    //Reedirect to main Dashboard after login
                    Intent intent = new Intent(LoginActivity.this, DashBoardActivity.class);
                    intent.putExtra("USER_ID", userId);
                    startActivity(intent);
                    finish();
                } else{
                    Toast.makeText(LoginActivity.this, "Invalid email or password", Toast.LENGTH_SHORT).show();
                }
            }
        });

        signuplinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterationActivity.class );
                startActivity(intent);
            }
        });





    }
}