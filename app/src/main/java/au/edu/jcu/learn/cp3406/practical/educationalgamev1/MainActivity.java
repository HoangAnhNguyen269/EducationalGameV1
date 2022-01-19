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
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.squareup.seismic.ShakeDetector;

import java.io.InputStream;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements ShakeDetector.Listener{

    Spinner subjectSpinner;
    Button newAttemptButton;
    Button scoreBoardButton;
    Button settingButton;

    //define the setting values from the db, therefore other class can refers in this activity without invoke SQLite database
    public static String userName="hahah";
    public static boolean enableShaking =true, enableShufflingQuestions = true;
    public static int numOfQues=5,secsPerQues;

    public static InputStream mathcsvInputStream;
    public static InputStream computerCsvInputStream;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //load the csv file which is the input for the initial database
        mathcsvInputStream = getResources().openRawResource(R.raw.math_questions_data);
        computerCsvInputStream =getResources().openRawResource(R.raw.basic_computer_questions_data);


        //get settings from db
        getSettingsFromDatabase();


        //add sensor manager to shake detector
        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        ShakeDetector sd = new ShakeDetector(this);
        sd.start(sensorManager);

//        //add tool bar
//        Toolbar toolbar = findViewById(R.id.tool_bar);
//        setSupportActionBar(toolbar);

        //2 buttons that can start the quiz
        LinearLayout basicComputerBtn = findViewById(R.id.basic_computer_quiz_btn);
        basicComputerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startQuizzing(getResources().getStringArray(R.array.subjects)[0]);
            }
        });

        LinearLayout mathBtn = findViewById(R.id.math_quiz_btn);
        mathBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startQuizzing(getResources().getStringArray(R.array.subjects)[1]);
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

        try {
            SQLiteOpenHelper gameDatabaseHelper = new GameDatabaseHelper(MainActivity.this);
            SQLiteDatabase db;
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
        //start the quiz randomly
        Random rand = new Random();
        String subject;
        if(rand.nextInt(2)==1){
            subject = getResources().getStringArray(R.array.subjects)[0];
        } else{
            subject = getResources().getStringArray(R.array.subjects)[1];
        }
        Intent intent = new Intent(MainActivity.this,
                GameActivity.class);
        intent.putExtra(GameActivity.SUBJECT_FINAL_STRING, subject);
        startActivity(intent);
    }
    void startQuizzing(String subject){
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

    @Override
    public void onBackPressed() {
        //still has error here, cant fix
            //add press back twice to exit
        //https://www.geeksforgeeks.org/how-to-implement-press-back-again-to-exit-in-android/
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);

    }




}