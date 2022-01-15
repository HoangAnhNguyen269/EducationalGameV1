package au.edu.jcu.learn.cp3406.practical.educationalgamev1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;

import com.squareup.seismic.ShakeDetector;

public class MainActivity extends AppCompatActivity implements ShakeDetector.Listener{

    Spinner subjectSpinner;
    Button newAttemptButton;
    Button scoreBoardButton;
    Button settingButton;
    public static String userName = "admin";

    //only set the default value for developing, handle this with setting latter on
    static boolean enableShaking = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        userName = "Darkness";

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
                intent.putExtra("Hello","Hello");
                startActivity(intent);
            }
        });



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

}