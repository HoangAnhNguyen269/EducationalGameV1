package au.edu.jcu.learn.cp3406.practical.educationalgamev1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.NavUtils;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.seismic.ShakeDetector;

import java.util.ArrayList;
import java.util.List;

public class GameActivity extends AppCompatActivity implements ShakeDetector.Listener {
    final public static String SUBJECT_FINAL_STRING = "selected subject";

    //two Strings below store the name of the subject that is stored in string.xml
    public String BASIC_COMPUTER;
    public String MATH;

    //check whether does the app allow to start quiz by shaking
    boolean enableShaking = MainActivity.enableShaking;
    boolean enableSound = MainActivity.enableSound;

    //set list of Question objects
    List<QuizQuestion> quizQuestions = new ArrayList<>();
    int currentQuestion;//the number of current question
    int selectedAns; //which answer the user choose
    public int timeUpAns = 10; //the answer the activity receives when the time is up
    int totalCorrectAns = 0;
    int totalSeconds = 0;//total seconds that the user spend on the quiz

    boolean isShowingResult;

    String subject; //the subject the user choose in the launch screen

    //define the View objects in the activity layout
    TextView questionCount;
    TextView questionTextView;
    AppCompatButton ans1Button, ans2Button, ans3Button, ans4Button;
    AppCompatButton nextButton;

    //stopwatch fragment
    StopwatchFragment stopwatch;


    //define the variables related to database
    private SQLiteDatabase db;

    //define a WorkerThread that can help to retrieved the quiz questions from the database
    private final GameWorkerThread gameWorkerThread;

    //define the ASyncTask object
    UpdateResultTask updateResultTask;

    //sound manager
    EffectAudioManager audioManager;

    public GameActivity() {
        gameWorkerThread = new GameWorkerThread(); //this = MainActivity; mainHandler refers to mainHandler in MainActivity
        gameWorkerThread.start();//The java.lang.Thread.start() method causes this thread to begin execution; start the worker thread
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);



        //soundPool Demo
        audioManager = new EffectAudioManager(this);


        //add sensor manager to shake detector
        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        ShakeDetector sd = new ShakeDetector(this); //from an external library
        sd.start(sensorManager);

        BASIC_COMPUTER = getResources().getStringArray(R.array.subjects)[0];
        MATH = getResources().getStringArray(R.array.subjects)[1];

        //ref to the fragment
        stopwatch = (StopwatchFragment) getSupportFragmentManager().findFragmentById(R.id.stopwatch);


        //ref the view in the toolbar
        //back to main button
        ImageView backToMainBtn = findViewById(R.id.back_to_main_btn);
        backToMainBtn.setOnClickListener(v -> onBackPressed());
        final TextView subjectTextView = findViewById(R.id.subject_name);

        //ref the view in the layout
        questionCount = findViewById(R.id.question_count);
        questionTextView = findViewById(R.id.question_text_view);
        ans1Button = findViewById(R.id.ans1_button);
        ans1Button.setSoundEffectsEnabled(false);
        ans2Button = findViewById(R.id.ans2_button);
        ans2Button.setSoundEffectsEnabled(false);
        ans3Button = findViewById(R.id.ans3_button);
        ans3Button.setSoundEffectsEnabled(false);
        ans4Button = findViewById(R.id.ans4_button);
        ans4Button.setSoundEffectsEnabled(false);

        nextButton = findViewById(R.id.next_button);

        nextButton.setOnClickListener(v -> onCLickNext());


        //get the selected subject
        subject = (String) getIntent().getExtras().get(SUBJECT_FINAL_STRING);
        subjectTextView.setText(subject);


        SQLiteOpenHelper gameDatabaseHelper = new GameDatabaseHelper(this);
        try {
            if (savedInstanceState == null) {
                db = gameDatabaseHelper.getReadableDatabase();
                gameWorkerThread.setDb(db);
                quizQuestions = gameWorkerThread.getQuizQuestions(subject);
                currentQuestion = 0;
                startCurrentQuestion();
            } else {
                isShowingResult = savedInstanceState.getBoolean("isShowingResult");
                currentQuestion = savedInstanceState.getInt("currentQuestion");
                totalCorrectAns = savedInstanceState.getInt("totalCorrectAns");
                totalSeconds = savedInstanceState.getInt("totalSeconds");
                quizQuestions = savedInstanceState.getParcelableArrayList("quizQuestions");
                if (isShowingResult) {
                    selectedAns = savedInstanceState.getInt("selectedAns");
                    displayResult();
                } else {
                    startCurrentQuestion();
                }
            }

        } catch (SQLiteException e) {
            Toast toast = Toast.makeText(this, "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
            Log.i("load successfully?", "not ok");
        }

    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putBoolean("isShowingResult", isShowingResult);
        savedInstanceState.putInt("currentQuestion", currentQuestion);
        savedInstanceState.putInt("selectedAns", selectedAns);
        savedInstanceState.putInt("totalCorrectAns", totalCorrectAns);
        savedInstanceState.putInt("totalSeconds", totalSeconds);
        savedInstanceState.putParcelableArrayList("quizQuestions", (ArrayList<? extends Parcelable>) quizQuestions);

    }


    protected void startCurrentQuestion() {
        if (isShowingResult) {
            stopwatch.onClickReset();
        }
        //save Instance State to stop the timer
        stopwatch.onClickStart();

        isShowingResult = false;
        selectedAns = timeUpAns;//choose an answer that is out of range when times up
        displayQuestion();

        //this handler check whether the time for a question is up
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (stopwatch.finish) {
                    getResult();
                }
                handler.postDelayed(this, 1000);
            }
        });
    }

    protected void getResult() {
        isShowingResult = true;
        stopwatch.onClickStop();
        totalSeconds += (stopwatch.settingSeconds - stopwatch.seconds);
        if (selectedAns == quizQuestions.get(currentQuestion).getCorrectAnsNum()) {
            totalCorrectAns++;
            //add sound here
            if(enableSound){audioManager.play(EffectSound.CORRECT_ANS);}

        } else{
            if(enableSound) audioManager.play(EffectSound.INCORRECT_ANS);
        }
        displayResult();
        stopwatch.finish = false;

    }

    protected void displayQuestion() {
        //display a new question with it choices
        nextButton.setVisibility(View.GONE);
        QuizQuestion quiz = quizQuestions.get(currentQuestion);

        String trackQuestionCount = (currentQuestion + 1) + "/" + quizQuestions.size();
        questionCount.setText(trackQuestionCount);

        questionTextView.setText(quiz.getQuestion());
        ans1Button.setText(quiz.getAns1());
        ans1Button.setBackgroundResource(R.drawable.round_back_white_ans_button);
        ans2Button.setText(quiz.getAns2());
        ans2Button.setBackgroundResource(R.drawable.round_back_white_ans_button);
        ans3Button.setText(quiz.getAns3());
        ans3Button.setBackgroundResource(R.drawable.round_back_white_ans_button);
        ans4Button.setText(quiz.getAns4());
        ans4Button.setBackgroundResource(R.drawable.round_back_white_ans_button);
        setAllAnsButtonListener();

    }

    protected void displayResult() {
        displayQuestion();
        removeAllAnsButtonListener();
        nextButton.setVisibility(View.VISIBLE);
        int correctAnsNum = quizQuestions.get(currentQuestion).getCorrectAnsNum();
        switch (correctAnsNum) {
            case 1:
                ans1Button.setBackgroundResource(R.drawable.round_green_back_correct_ans_button);
                break;
            case 2:
                ans2Button.setBackgroundResource(R.drawable.round_green_back_correct_ans_button);
                break;
            case 3:
                ans3Button.setBackgroundResource(R.drawable.round_green_back_correct_ans_button);
                break;
            case 4:
                ans4Button.setBackgroundResource(R.drawable.round_green_back_correct_ans_button);
                break;

        }
        if (selectedAns != correctAnsNum) {
            switch (selectedAns) {
                case 1:
                    ans1Button.setBackgroundResource(R.drawable.round_red_back_incorrect_ans_button);
                    break;
                case 2:
                    ans2Button.setBackgroundResource(R.drawable.round_red_back_incorrect_ans_button);
                    break;
                case 3:
                    ans3Button.setBackgroundResource(R.drawable.round_red_back_incorrect_ans_button);
                    break;
                case 4:
                    ans4Button.setBackgroundResource(R.drawable.round_red_back_incorrect_ans_button);
                    break;
                case 10: //timeUpAns
                    ans1Button.setBackgroundResource(R.drawable.round_red_back_incorrect_ans_button);
                    ans2Button.setBackgroundResource(R.drawable.round_red_back_incorrect_ans_button);
                    ans3Button.setBackgroundResource(R.drawable.round_red_back_incorrect_ans_button);
                    ans4Button.setBackgroundResource(R.drawable.round_red_back_incorrect_ans_button);
                    ans4Button.setBackgroundResource(R.drawable.round_red_back_incorrect_ans_button);
                    switch (correctAnsNum) {
                        case 1:
                            ans1Button.setBackgroundResource(R.drawable.round_green_back_correct_ans_button);
                            break;
                        case 2:
                            ans2Button.setBackgroundResource(R.drawable.round_green_back_correct_ans_button);
                            break;
                        case 3:
                            ans3Button.setBackgroundResource(R.drawable.round_green_back_correct_ans_button);
                            break;
                        case 4:
                            ans4Button.setBackgroundResource(R.drawable.round_green_back_correct_ans_button);
                            break;
                    }
                    break;
            }
        }

    }


    protected void setAllAnsButtonListener() {
        //set listeners for answer buttons
        setAnsButtonListener(ans1Button);
        setAnsButtonListener(ans2Button);
        setAnsButtonListener(ans3Button);
        setAnsButtonListener(ans4Button);
    }

    protected void removeAllAnsButtonListener() {
        //remove listener when displaying the question's answer
        ans1Button.setOnClickListener(null);
        ans2Button.setOnClickListener(null);
        ans3Button.setOnClickListener(null);
        ans4Button.setOnClickListener(null);
    }

    protected void setAnsButtonListener(View view) {
        AppCompatButton button = (AppCompatButton) view;
        button.setOnClickListener(v -> {
            if (button.equals(ans1Button)) {
                selectedAns = 1;
            } else if (button.equals(ans2Button)) {
                selectedAns = 2;
            } else if (button.equals(ans3Button)) {
                selectedAns = 3;
            } else {
                selectedAns = 4;
            }
            getResult(); //this method is just for testing
        });
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (db != null) {
            db.close();
        }
        gameWorkerThread.interrupt();
        //prevent potential memory leak
        if (updateResultTask != null) {
            updateResultTask.cancel(true);
        }
    }

    protected void onCLickNext() {

        currentQuestion++;
        if (currentQuestion + 1 < quizQuestions.size()) {
            startCurrentQuestion();
        } else if (currentQuestion + 1 == quizQuestions.size()) {
            String finishButtonText = "Finish";
            nextButton.setText(finishButtonText);
            startCurrentQuestion();
        } else {
            //when the quiz is finished and the user wants to store their result.
            updateResultTask = new UpdateResultTask();
            updateResultTask.execute(totalCorrectAns, totalSeconds);
            Intent intent = new Intent(GameActivity.this,
                    ShowQuizResultActivity.class);
            intent.putExtra(ShowQuizResultActivity.SUBJECT_STRING, subject);
            intent.putExtra(ShowQuizResultActivity.CORRECT_QUESTION_NUM, totalCorrectAns);
            intent.putExtra(ShowQuizResultActivity.TOTAL_QUESTIONS, quizQuestions.size());
            intent.putExtra(ShowQuizResultActivity.TOTAL_SECONDS, totalSeconds);
            startActivity(intent);
        }
    }

    private class UpdateResultTask extends AsyncTask<Integer, Void, Boolean> {
        //this task help update the quiz result on the database
        ContentValues userResultValues;

        protected void onPreExecute() {
            userResultValues = new ContentValues();
        }

        protected Boolean doInBackground(Integer... intNums) {
            float scoreInDatabase = (float) totalCorrectAns / quizQuestions.size() * 10;
            float averageSecondsDatabase = (float) totalSeconds / quizQuestions.size();
            SQLiteOpenHelper gameDatabaseHelper = new GameDatabaseHelper(GameActivity.this);
            try {
                SQLiteDatabase db = gameDatabaseHelper.getWritableDatabase();
                userResultValues.put(GameDatabaseHelper.USER_NAME_COLUMN, MainActivity.userName);
                userResultValues.put(GameDatabaseHelper.SUBJECT_COLUMN, subject);
                userResultValues.put(GameDatabaseHelper.SCORE_COLUMN, scoreInDatabase);
                userResultValues.put(GameDatabaseHelper.AVERAGE_SECONDS_COLUMN, averageSecondsDatabase);
                db.insert(GameDatabaseHelper.RESULT_TABLE, null, userResultValues);
                db.close();
                return true;
            } catch (SQLiteException e) {
                return false;

            }
        }

        protected void onPostExecute(Boolean success) {
            if (!success) {
                Toast toast = Toast.makeText(GameActivity.this,
                        "Database unavailable", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }

    @Override
    public void hearShake() {
        if (enableShaking) {
            Intent intent = new Intent(GameActivity.this,
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