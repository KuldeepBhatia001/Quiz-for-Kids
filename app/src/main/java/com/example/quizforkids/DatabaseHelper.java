package com.example.quizforkids;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "quiz_app.db";
    private static final int DATABASE_VERSION = 3;

    // User table
    private static final String TABLE_USERS = "users";
    private static final String TABLE_SCORES = "scores";
    private static final String COLUMN_USER_ID = "id";
    private static final String COLUMN_USER_EMAIL = "email";
    private static final String COLUMN_USER_USERNAME = "username";
    private static final String COLUMN_USER_PASSWORD = "password";

    private static final String COLUMN_SCORE_ID = "score_id";
    private static final String COLUMN_SCORE = "score";
    private static final String COLUMN_ATTEMPT = "attempt";
    private static final String COLUMN_TIMESTAMP = "timestamp";
    private static final String COLUMN_CHOSEN_AREA = "chosenArea";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create users table
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + "("
                + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_USER_EMAIL + " TEXT,"
                + COLUMN_USER_USERNAME + " TEXT,"
                + COLUMN_USER_PASSWORD + " TEXT" + ")";
        db.execSQL(CREATE_USERS_TABLE);

        // Create scores table
        String CREATE_USER_SCORES_TABLE = "CREATE TABLE " + TABLE_SCORES + "("
                + COLUMN_SCORE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_USER_ID + " INTEGER,"
                + COLUMN_SCORE + " INTEGER,"
                + COLUMN_ATTEMPT + " INTEGER,"
                + COLUMN_CHOSEN_AREA + " TEXT,"
                + COLUMN_TIMESTAMP + " INTEGER,"
                + "FOREIGN KEY(" + COLUMN_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_USER_ID + "))";
        db.execSQL(CREATE_USER_SCORES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SCORES);
        onCreate(db);
    }

    public boolean addUser(String email, String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_EMAIL, email);
        values.put(COLUMN_USER_USERNAME, username);
        values.put(COLUMN_USER_PASSWORD, password);

        long result = db.insert(TABLE_USERS, null, values);
        db.close();

        return result != -1; // Return true if insertion is successful
    }



    public int getOverallPoints(int userId) {
        int totalPoints = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM(" + COLUMN_SCORE + ") FROM " + TABLE_SCORES + " WHERE " + COLUMN_USER_ID + "=?", new String[]{String.valueOf(userId)});
        if (cursor.moveToFirst()) {
            totalPoints = cursor.getInt(0);
        }
        cursor.close();
        db.close();
        return totalPoints;
    }

    public void addScore(int userId, int points, int attemptNumber, String chosenArea) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_ID, userId);
        values.put(COLUMN_SCORE, points);
        values.put(COLUMN_ATTEMPT, attemptNumber);
        values.put(COLUMN_CHOSEN_AREA, chosenArea);
        values.put(COLUMN_TIMESTAMP, System.currentTimeMillis() / 1000); // Store timestamp as seconds since epoch
        db.insert(TABLE_SCORES, null, values);
        db.close();
    }



    public String getUsername(int userId) {
        String username = "";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COLUMN_USER_USERNAME + " FROM " + TABLE_USERS + " WHERE " + COLUMN_USER_ID + "=?", new String[]{String.valueOf(userId)});
        if (cursor.moveToFirst()) {
            username = cursor.getString(0);
        }
        cursor.close();
        db.close();
        return username;
    }





    // Method to get the number of attempts for a user
    public int getAttemptNumber(int userId) {
        int attemptNumber = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(" + COLUMN_SCORE_ID + ") FROM " + TABLE_SCORES + " WHERE " + COLUMN_USER_ID + "=?", new String[]{String.valueOf(userId)});
        if (cursor.moveToFirst()) {
            attemptNumber = cursor.getInt(0) + 1; // Increment by 1 for the next attempt
        }
        cursor.close();
        db.close();
        return attemptNumber;
    }


    public boolean checkUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {COLUMN_USER_ID};
        String selection = COLUMN_USER_EMAIL + " = ? AND " + COLUMN_USER_PASSWORD + " = ?";
        String[] selectionArgs = {email, password};

        Cursor cursor = db.query(TABLE_USERS, columns, selection, selectionArgs, null, null, null);
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();

        return cursorCount > 0; // Return true if user exists
    }




    public List<Question> loadQuestionsFromTextFile(Context context, String filename) {
        List<Question> questionList = new ArrayList<>();
        AssetManager assetManager = context.getAssets();
        try {
            // Open the text file for reading
            BufferedReader reader = new BufferedReader(new InputStreamReader(assetManager.open(filename)));

            // Read each line (question object) from the text file
            String line;
            while ((line = reader.readLine()) != null) {
                // Parse the line and extract relevant information
                String[] parts = line.split(",");
                String category = parts[0];
                String drawing = parts[1];
                String correctAnswer = parts[2];

                // Use the parsed information to create a Question object
                int questionImageId = context.getResources().getIdentifier(drawing, "drawable", context.getPackageName());
                Question question = new Question(category, questionImageId, correctAnswer);

                // Add the question to the list
                questionList.add(question);
            }

            // Close the reader
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Return the list of questions
        return questionList;
    }

    public List<QuestionC> loadQuestionsFromTextFileCartoon(Context context, String filename) {
        List<QuestionC> questionListCartoon = new ArrayList<>();
        AssetManager assetManager = context.getAssets();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(context.getAssets().open(filename)))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 6) {
                    String questionText = parts[0];
                    String option1 = parts[1];
                    String option2 = parts[2];
                    String option3 = parts[3];
                    String option4 = parts[4];
                    String correctAnswer = parts[5];

                    List<String> options = Arrays.asList(option1, option2, option3, option4);
                    QuestionC question = new QuestionC(questionText, options, correctAnswer);
                    questionListCartoon.add(question);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Return the list of questions
        return questionListCartoon;
    }

    public int getUserIdByEmailAndPassword(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COLUMN_USER_ID + " FROM " + TABLE_USERS + " WHERE "
                        + COLUMN_USER_EMAIL + " = ? AND " + COLUMN_USER_PASSWORD + " = ?",
                new String[]{email, password});

        int userId = -1;
        if (cursor.moveToFirst()) {
            userId = cursor.getInt(0);
        }
        cursor.close();
        return userId;
    }




}
