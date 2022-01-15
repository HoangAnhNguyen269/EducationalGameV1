package au.edu.jcu.learn.cp3406.practical.educationalgamev1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.squareup.seismic.ShakeDetector;

public class MainActivity extends AppCompatActivity implements ShakeDetector.Listener{

    Spinner subjectSpinner;
    Button newAttemptButton;
    Button scoreBoardButton;
    Button settingButton;

    //define the setting values from the db, therefore other class can refers in this activity without invoke SQLite database
    public static String userName;
    public static boolean enableShaking, enableShufflingQuestions;
    public static int numOfQues,secsPerQues;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //get settings from db
        getSettingsFromDatabase();

        //add sensor manager to shake detector
        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        ShakeDetector sd = new ShakeDetector(this);
        sd.start(sensorManager);

        //add tool bar
        Toolbar toolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);


        //match the subject to their View
        subjectSpinner = findViewById(R.id.subject_spinner);
        newAttemptButton = findViewById(R.id.new_attempt);
        //set up the listener for attempt button
        newAttemptButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                startQuizzing();
            }
        });
        scoreBoardButton = findViewById(R.id.score_board);
        scoreBoardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,
                        HighScoreActivity.class);
                startActivity(intent);
            }
        });

        settingButton = findViewById(R.id.setting_button_main_screen);
        settingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,
                        SettingActivity.class);
                startActivity(intent);
            }
        });



    }

    void getSettingsFromDatabase(){

        SQLiteDatabase db;
        SQLiteOpenHelper gameDatabaseHelper = new GameDatabaseHelper(this);
        try {
            db = gameDatabaseHelper.getReadableDatabase();
            Cursor cursor = db.query(GameDatabaseHelper.SETTING_TABLE, new String[]{"_id", GameDatabaseHelper.SETTING_USER_NAME_COLUMN,GameDatabaseHelper.SETTING_ENABLE_SHAKING_COLUMN,GameDatabaseHelper.SETTING_ENABLE_SHUFFLING_QUESTIONS_COLUMN, GameDatabaseHelper.SETTING_NUMBER_OF_QUESTIONS_COLUMN, GameDatabaseHelper.SETTING_SECONDS_PER_QUESTION_COLUMN},
                    null, null, null, null, null );
            cursor.moveToFirst();
            cursor.moveToLast(); //we take the last record of setting
            userName = cursor.getString(1);
            if(cursor.getInt(2)==1){
                enableShaking =true;
            }else{
                enableShaking =false;
            }
            if(cursor.getInt(3) ==1){
                enableShufflingQuestions=true;
            }else{
                enableShufflingQuestions =false;
            }
            numOfQues =cursor.getInt(4);
            secsPerQues =cursor.getInt(5);
            db.close();
            cursor.close();

        } catch(SQLiteException e) {
            Toast toast = Toast.makeText(this, "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
            Log.i("Database available?:  ", "No");
        }

    }
    void startQuizzing(){
        String subject = subjectSpinner.getSelectedItem().toString();
        Intent intent = new Intent(MainActivity.this,
                GameActivity.class);
        intent.putExtra(GameActivity.SUBJECT_FINAL_STRING, subject);
        startActivity(intent);
    }

    @Override public void hearShake() {
        if(enableShaking){
            startQuizzing();
        }
    }
    @Override
    protected void onRestart() {
        //in case the user press back from Setting activity
        getSettingsFromDatabase();
        super.onRestart();
    }



}