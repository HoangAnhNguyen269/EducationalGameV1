package au.edu.jcu.learn.cp3406.practical.educationalgamev1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.squareup.seismic.ShakeDetector;

import java.io.InputStream;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements ShakeDetector.Listener {

    private long lastPressedTime;

    Button scoreBoardButton;
    Button settingButton;

    //define the setting values from the db, therefore other class can refers these variables from MainActivity without invoke SQLite database
    public static String userName = "admin";
    public static boolean enableShaking = true, enableShufflingQuestions = true;
    public static int numOfQues = 5, secsPerQues;

    //define 2 InputStreams get from 2 csv files in the raw table
    public static InputStream mathcsvInputStream;
    public static InputStream computerCsvInputStream;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //load the csv file which is the input for the initial database
        mathcsvInputStream = getResources().openRawResource(R.raw.math_questions_data);
        computerCsvInputStream = getResources().openRawResource(R.raw.basic_computer_questions_data);


        //get settings from db
        getSettingsFromDatabase();


        //add sensor manager to shake detector
        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        ShakeDetector sd = new ShakeDetector(this);
        sd.start(sensorManager);

        //2 buttons that can start the quiz
        LinearLayout basicComputerBtn = findViewById(R.id.basic_computer_quiz_btn);
        basicComputerBtn.setOnClickListener(v -> startQuizzing(getResources().getStringArray(R.array.subjects)[0]));

        LinearLayout mathBtn = findViewById(R.id.math_quiz_btn);
        mathBtn.setOnClickListener(v -> startQuizzing(getResources().getStringArray(R.array.subjects)[1]));

        scoreBoardButton = findViewById(R.id.score_board);
        scoreBoardButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this,
                    HighScoreActivity.class);
            startActivity(intent);
        });

        settingButton = findViewById(R.id.setting_button_main_screen);
        settingButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this,
                    SettingActivity.class);
            startActivity(intent);
        });


    }

    void getSettingsFromDatabase() {
        //get the latest setting record from the SETTING_TABLE in the database
        try {
            SQLiteOpenHelper gameDatabaseHelper = new GameDatabaseHelper(MainActivity.this);
            SQLiteDatabase db;
            db = gameDatabaseHelper.getReadableDatabase();
            Cursor cursor = db.query(GameDatabaseHelper.SETTING_TABLE, new String[]{"_id", GameDatabaseHelper.SETTING_USER_NAME_COLUMN, GameDatabaseHelper.SETTING_ENABLE_SHAKING_COLUMN, GameDatabaseHelper.SETTING_ENABLE_SHUFFLING_QUESTIONS_COLUMN, GameDatabaseHelper.SETTING_NUMBER_OF_QUESTIONS_COLUMN, GameDatabaseHelper.SETTING_SECONDS_PER_QUESTION_COLUMN},
                    null, null, null, null, null);
            cursor.moveToFirst();
            cursor.moveToLast(); //we take the last record of setting
            userName = cursor.getString(1);

            enableShaking = cursor.getInt(2) == 1;
            enableShufflingQuestions = cursor.getInt(3) == 1;

            numOfQues = cursor.getInt(4);
            secsPerQues = cursor.getInt(5);
            db.close();
            cursor.close();


        } catch (SQLiteException e) {
            Toast toast = Toast.makeText(this, "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
            Log.i("Database available?:  ", "No");
        }

    }

    void startQuizzing() {
        //when the user shakes the phone, a subject is chosen randomly
        Random rand = new Random();
        String subject;
        if (rand.nextInt(2) == 1) {
            subject = getResources().getStringArray(R.array.subjects)[0];
        } else {
            subject = getResources().getStringArray(R.array.subjects)[1];
        }
        Intent intent = new Intent(MainActivity.this,
                GameActivity.class);
        intent.putExtra(GameActivity.SUBJECT_FINAL_STRING, subject);
        startActivity(intent);
    }

    void startQuizzing(String subject) {
        Intent intent = new Intent(MainActivity.this,
                GameActivity.class);
        intent.putExtra(GameActivity.SUBJECT_FINAL_STRING, subject);
        startActivity(intent);
    }

    @Override
    public void hearShake() {
        if (enableShaking) {
            startQuizzing();
        }
    }

    @Override
    protected void onRestart() {
        //in case the user press back from Setting activity
        getSettingsFromDatabase();
        super.onRestart();
    }

    @Override
    public void onBackPressed() {
        //require user press back two times to end the app
        if (lastPressedTime + 2000 > System.currentTimeMillis()) { //wait for 2 seconds
            super.onBackPressed();
            Intent backToHomeLauncherIntent = new Intent(Intent.ACTION_MAIN);
            backToHomeLauncherIntent.addCategory(Intent.CATEGORY_HOME);
            backToHomeLauncherIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(backToHomeLauncherIntent);
        } else {
            Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT).show();
        }
        lastPressedTime = System.currentTimeMillis();


    }


}