package au.edu.jcu.learn.cp3406.practical.educationalgamev1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;

public class MainActivity extends AppCompatActivity {

    Spinner subjectSpinner;
    Button newAttemptButton;
    Button scoreBoardButton;
    public static String userName = "admin";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        userName = "Darkness";

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
                String subject = subjectSpinner.getSelectedItem().toString();
                Intent intent = new Intent(MainActivity.this,
                        GameActivity.class);
                intent.putExtra(GameActivity.SUBJECT_FINAL_STRING, subject);
                startActivity(intent);
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



    }

}