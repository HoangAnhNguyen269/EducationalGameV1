package au.edu.jcu.learn.cp3406.practical.educationalgamev1;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

public class SettingActivity extends AppCompatActivity implements UserNameDialog.UserNameDialogListener {

    TextView userNameSettingTextView;

    SwitchCompat enableShakingSwitch;
    SwitchCompat shuffleQuestionSwitch;

    SeekBar numOfQuesSeekbar;
    TextView numOfQuesSeekbarLabel;

    SeekBar secsPerQuesSeekbar;
    TextView secsPerQuesSeekbarLabel;

    Button settingSaveButton;

    ImageView backMainBtn;


    String userName;
    //SQLite does not have a separate Boolean storage class. Instead, Boolean values are stored as integers 0 (false) and 1 (true).
    int enableShaking, enableShufflingQuestions;
    int numOfQues, secsPerQues;
    boolean isSaved = true;

    //database
    private SQLiteDatabase db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        //User name text View
        userNameSettingTextView = findViewById(R.id.setting_user_name);
        userNameSettingTextView.setOnClickListener(v -> openDialog());

        //two switches
        enableShakingSwitch = findViewById(R.id.enable_shaking_switch);
        enableShakingSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (enableShakingSwitch.isChecked()) {
                enableShaking = 1;
            } else {
                enableShaking = 0;
            }
            isSaved = false;
        });
        shuffleQuestionSwitch = findViewById(R.id.shuffle_questions_switch);
        shuffleQuestionSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (shuffleQuestionSwitch.isChecked()) {
                enableShufflingQuestions = 1;
            } else {
                enableShufflingQuestions = 0;
            }
            isSaved = false;
        });

        //num of ques seekbar
        numOfQuesSeekbar = findViewById(R.id.num_of_ques_seekbar);
        numOfQuesSeekbarLabel = findViewById(R.id.num_of_ques_seekbar_label);
        numOfQuesSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                String numberOfQuestionText = "Number of questions: " + progress;
                numOfQuesSeekbarLabel.setText(numberOfQuestionText);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //replace this code below with the database related code
                numOfQues = seekBar.getProgress();
                isSaved = false;
            }
        });

        //secs per ques seekbar
        secsPerQuesSeekbar = findViewById(R.id.secs_per_ques_seekbar);
        secsPerQuesSeekbarLabel = findViewById(R.id.secs_per_ques_seekbar_label);
        secsPerQuesSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                String secsPerQuestionText = "Seconds per question: " + progress;
                secsPerQuesSeekbarLabel.setText(secsPerQuestionText);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //replace this code below with the database related code
                secsPerQues = seekBar.getProgress();
                isSaved = false;
            }
        });

        //refers to settingSaveButton
        settingSaveButton = findViewById(R.id.setting_save_button);
        settingSaveButton.setOnClickListener(v -> saveSettingOnDataBase());
        //back btn
        backMainBtn = findViewById(R.id.setting_back_to_main_btn);
        backMainBtn.setOnClickListener(v -> onBackPressed());


        //get Setting configuration from database
        SQLiteOpenHelper gameDatabaseHelper = new GameDatabaseHelper(this);
        try {
            db = gameDatabaseHelper.getReadableDatabase();
            getCurrentSettingOnDatabase();
            if (savedInstanceState != null) {
                userName = savedInstanceState.getString("userName");
                enableShaking = savedInstanceState.getInt("enableShaking");
                enableShufflingQuestions = savedInstanceState.getInt("enableShufflingQuestions");
                numOfQues = savedInstanceState.getInt("numOfQues");
                secsPerQues = savedInstanceState.getInt("secsPerQues");
                isSaved = savedInstanceState.getBoolean("isSaved");
                updateCurrentSettingUI();
            }
        } catch (SQLiteException e) {
            Toast toast = Toast.makeText(this, "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
            Log.i("Database available?:  ", "No");
        }

    }

    protected void getCurrentSettingOnDatabase() {
        Cursor cursor = db.query(GameDatabaseHelper.SETTING_TABLE, new String[]{"_id", GameDatabaseHelper.SETTING_USER_NAME_COLUMN, GameDatabaseHelper.SETTING_ENABLE_SHAKING_COLUMN, GameDatabaseHelper.SETTING_ENABLE_SHUFFLING_QUESTIONS_COLUMN, GameDatabaseHelper.SETTING_NUMBER_OF_QUESTIONS_COLUMN, GameDatabaseHelper.SETTING_SECONDS_PER_QUESTION_COLUMN},
                null, null, null, null, null);
        cursor.moveToFirst();
        cursor.moveToLast(); //we take the last record of setting
        userName = cursor.getString(1);
        enableShaking = cursor.getInt(2);
        enableShufflingQuestions = cursor.getInt(3);
        numOfQues = cursor.getInt(4);
        secsPerQues = cursor.getInt(5);

        updateCurrentSettingUI();
        cursor.close();
    }

    protected void updateCurrentSettingUI() {
        //help the activity can display the latest update from SETTING TABLE in the database
        //username
        String userNameText = "User name:  " + userName;
        userNameSettingTextView.setText(userNameText);
        //shaking
        enableShakingSwitch.setChecked(enableShaking != 0);
        //shuffling
        shuffleQuestionSwitch.setChecked(enableShufflingQuestions != 0);
        //number of questions
        String numOfQuesSeekbarLabelText = "Number of questions: " + numOfQues;
        numOfQuesSeekbarLabel.setText(numOfQuesSeekbarLabelText);
        numOfQuesSeekbar.setProgress(numOfQues);

        //secs per question
        String secsPerQuesSeekbarLabelText = "Seconds per question: " + secsPerQues;
        secsPerQuesSeekbarLabel.setText(secsPerQuesSeekbarLabelText);
        secsPerQuesSeekbar.setProgress(secsPerQues);

    }

    protected void saveSettingOnDataBase() {
        //save the information of the database in case of (for example) the screen is rotated
        ContentValues settingValues = new ContentValues();
        settingValues.put(GameDatabaseHelper.SETTING_USER_NAME_COLUMN, userName);
        settingValues.put(GameDatabaseHelper.SETTING_ENABLE_SHAKING_COLUMN, enableShaking); //no shaking
        settingValues.put(GameDatabaseHelper.SETTING_ENABLE_SHUFFLING_QUESTIONS_COLUMN, enableShufflingQuestions); //no shuffling
        settingValues.put(GameDatabaseHelper.SETTING_NUMBER_OF_QUESTIONS_COLUMN, numOfQues); //20 questions
        settingValues.put(GameDatabaseHelper.SETTING_SECONDS_PER_QUESTION_COLUMN, secsPerQues); //10 secs per question
        db.insert(GameDatabaseHelper.SETTING_TABLE, null, settingValues);
        isSaved = true;
        Toast toast = Toast.makeText(this, "Saved", Toast.LENGTH_SHORT);
        toast.show();
    }

    protected void openDialog() {
        UserNameDialog userNameDialog = new UserNameDialog();
        userNameDialog.show(getSupportFragmentManager(), "user name dialog");
    }

    @Override
    public void applyText(String username) {
        //store the userName
        this.userName = username;
        String userNameSettingText = "User name:  " + userName;
        userNameSettingTextView.setText(userNameSettingText);
        isSaved = false;
    }


    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString("userName", userName);
        savedInstanceState.putInt("enableShaking", enableShaking);
        savedInstanceState.putInt("enableShufflingQuestions", enableShufflingQuestions);
        savedInstanceState.putInt("numOfQues", numOfQues);
        savedInstanceState.putInt("secsPerQues", secsPerQues);
        savedInstanceState.putBoolean("isSaved", isSaved);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (db != null) {
            db.close();
        }
    }

    @Override
    public void onBackPressed() {
        // alert the user if they havent save the setting yet
        if (!isSaved) {
            AlertDialog dialog;
            AlertDialog.Builder builder;
            builder = new AlertDialog.Builder(SettingActivity.this);
            builder.setTitle("Do you want to leave? The setting will not be saved.");
            builder.setPositiveButton("LEAVE", (dialog1, which) -> {
                isSaved = true;
                onBackPressed();
            });
            builder.setNegativeButton("CANCEL", (dialog12, which) -> {

            });
            dialog = builder.create();
            dialog.show();
        } else {
            super.onBackPressed();
        }

    }

}
