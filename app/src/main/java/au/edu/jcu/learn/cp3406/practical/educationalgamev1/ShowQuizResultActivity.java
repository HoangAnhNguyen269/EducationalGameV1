package au.edu.jcu.learn.cp3406.practical.educationalgamev1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.NavUtils;

import android.content.Intent;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.squareup.seismic.ShakeDetector;

public class ShowQuizResultActivity extends AppCompatActivity implements ShakeDetector.Listener {

    public static String SUBJECT_STRING = "SUBJECT";
    public static String CORRECT_QUESTION_NUM = "CORRECT QUESTION NUMBER";
    public static String TOTAL_QUESTIONS = "TOTAL NUMBER";
    public static String TOTAL_SECONDS = "TOTAL_SECONDS";

    private String subject;
    private int totalCorrectQuestion;
    private int totalQuestion;
    private int totalSeconds;

    TextView yourNameView;
    TextView yourScoreView;
    TextView timeView;
    Button backToHomeButton;
    Button newGameButton;
    AppCompatButton shareOnTwitter;

    //check whether shaking is available
    boolean enableShaking = MainActivity.enableShaking;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_quiz_result);

        //add sensor manager to shake detector
        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        ShakeDetector sd = new ShakeDetector(this);
        sd.start(sensorManager);

        subject = (String) getIntent().getExtras().get(SUBJECT_STRING);
        totalCorrectQuestion = (int) getIntent().getExtras().get(CORRECT_QUESTION_NUM);
        totalQuestion = (int) getIntent().getExtras().get(TOTAL_QUESTIONS);
        totalSeconds = (int) getIntent().getExtras().get(TOTAL_SECONDS);

        yourNameView = findViewById(R.id.result_user_name);
        String yourNameViewText = "Your name: " + MainActivity.userName;
        yourNameView.setText(yourNameViewText);

        yourScoreView = findViewById(R.id.result_score);
        String yourScoreViewText = "Your score: " + totalCorrectQuestion + "/" + totalQuestion;
        yourScoreView.setText(yourScoreViewText);





        timeView = findViewById(R.id.result_total_seconds);
        String timeViewText = "Time: " + totalSeconds + " seconds";
        timeView.setText(timeViewText);


        backToHomeButton = findViewById(R.id.back_to_home_page_btn);
        backToHomeButton.setOnClickListener(v -> {
            Intent upIntent = NavUtils.getParentActivityIntent(ShowQuizResultActivity.this);
            startActivity(upIntent);
        });


        newGameButton = findViewById(R.id.take_new_quiz);
        newGameButton.setOnClickListener(v -> {
            Intent intent = new Intent(ShowQuizResultActivity.this, GameActivity.class);
            intent.putExtra(GameActivity.SUBJECT_FINAL_STRING, subject);
            startActivity(intent);
        });
        EffectAudioManager audioManager = new EffectAudioManager(this);
        shareOnTwitter = findViewById(R.id.share_on_twitter_button);
        shareOnTwitter.setOnClickListener(v -> {
            Intent intent = new Intent(ShowQuizResultActivity.this, ShareOnTwitterActivity.class);
            intent.putExtra("userName", MainActivity.userName);
            intent.putExtra("subject", subject);
            float score = (float) totalCorrectQuestion / totalQuestion * 10;
            intent.putExtra("score", score);
            float avgSeconds = (float) totalSeconds / totalQuestion;
            intent.putExtra("avgSeconds", avgSeconds);

            if(MainActivity.enableSound){audioManager.play(EffectSound.CONGRATULATION);}
            startActivity(intent);
        });



    }

    @Override
    public void hearShake() {
        if (enableShaking) {
            Intent intent = new Intent(ShowQuizResultActivity.this,
                    GameActivity.class);
            intent.putExtra(GameActivity.SUBJECT_FINAL_STRING, subject);
            startActivity(intent);
        }

    }

    @Override
    public void onBackPressed() {
        Intent upIntent = NavUtils.getParentActivityIntent(this);
        startActivity(upIntent);
    }
}