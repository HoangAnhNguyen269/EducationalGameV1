package au.edu.jcu.learn.cp3406.practical.educationalgamev1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

public class GameActivity extends AppCompatActivity {
    final public static String SUBJECT_FINAL_STRING = "subjects";
    private String BASIC_COMPUTER;
    private String  MATH;
    String subject;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        BASIC_COMPUTER = getResources().getStringArray(R.array.subjects)[0];
        MATH = getResources().getStringArray(R.array.subjects)[1];




        subject = (String) getIntent().getExtras().get(SUBJECT_FINAL_STRING);

    }

}