package au.edu.jcu.learn.cp3406.practical.educationalgamev1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.SwitchCompat;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class SettingActivity extends AppCompatActivity implements UserNameDialog.UserNameDialogListener {

    TextView userNameSettingTextView;

    SwitchCompat enableShakingSwitch;
    SwitchCompat shuffleQuestionSwitch;

    SeekBar numOfQuesSeekbar;
    TextView numOfQuesSeekbarLabel;

    SeekBar secsPerQuesSeekbar;
    TextView secsPerQuesSeekbarLabel;

    AppCompatButton settingSaveButton;


    String userName;
    //SQLite does not have a separate Boolean storage class. Instead, Boolean values are stored as integers 0 (false) and 1 (true).
    int enableShaking, enableShufflingQuestions;
    int numOfQues,secsPerQues;

    //database
    private SQLiteDatabase db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        //User name text View
        userNameSettingTextView = findViewById(R.id.setting_user_name);
        userNameSettingTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });

        //two switches
        enableShakingSwitch = findViewById(R.id.enable_shaking_switch);
        enableShakingSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(enableShakingSwitch.isChecked()){
                    enableShaking =1;
                }else{
                    enableShaking =0;
                }
            }
        });
        shuffleQuestionSwitch = findViewById(R.id.shuffle_questions_switch);
        shuffleQuestionSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(shuffleQuestionSwitch.isChecked()){
                    enableShufflingQuestions =1;
                }else{
                    enableShufflingQuestions =0;
                }
            }
        });

        //num of ques seekbar
        numOfQuesSeekbar = (SeekBar) findViewById(R.id.num_of_ques_seekbar);
        numOfQuesSeekbarLabel = findViewById(R.id.num_of_ques_seekbar_label);
        numOfQuesSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                numOfQuesSeekbarLabel.setText("Number of questions: "+progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //replace this code below with the database related code
                numOfQues = seekBar.getProgress();
//                Toast toast = Toast.makeText(SettingActivity.this,"Number of questions "+numOfQues, Toast.LENGTH_SHORT);
//                toast.show();
            }
        });

        //secs per ques seekbar
        secsPerQuesSeekbar = (SeekBar) findViewById(R.id.secs_per_ques_seekbar);
        secsPerQuesSeekbarLabel = (TextView) findViewById(R.id.secs_per_ques_seekbar_label);
        secsPerQuesSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                secsPerQuesSeekbarLabel.setText("Seconds per question: "+progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //replace this code below with the database related code
                secsPerQues = seekBar.getProgress();
//                Toast toast = Toast.makeText(SettingActivity.this,"Done tracking-Secs per ques", Toast.LENGTH_SHORT);
//                toast.show();
            }
        });

        //refers to settingSaveButton
        settingSaveButton = findViewById(R.id.setting_save_button);
        settingSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveSettingOnDataBase();
            }
        });


        //get Setting configuration from database
        SQLiteOpenHelper gameDatabaseHelper = new GameDatabaseHelper(this);
        try {
            db = gameDatabaseHelper.getReadableDatabase();
            getCurrentSettingOnDatabase();
            if(savedInstanceState!= null){
                userName=savedInstanceState.getString("userName");
                enableShaking = savedInstanceState.getInt("enableShaking");
                enableShufflingQuestions = savedInstanceState.getInt("enableShufflingQuestions");
                numOfQues = savedInstanceState.getInt("numOfQues");
                secsPerQues = savedInstanceState.getInt("secsPerQues");
                updateCurrentSettingUI();
            }
        } catch(SQLiteException e) {
            Toast toast = Toast.makeText(this, "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
            Log.i("Database available?:  ", "No");
        }

    }

    protected void getCurrentSettingOnDatabase(){
        Cursor cursor = db.query(GameDatabaseHelper.SETTING_TABLE, new String[]{"_id", GameDatabaseHelper.SETTING_USER_NAME_COLUMN,GameDatabaseHelper.SETTING_ENABLE_SHAKING_COLUMN,GameDatabaseHelper.SETTING_ENABLE_SHUFFLING_QUESTIONS_COLUMN, GameDatabaseHelper.SETTING_NUMBER_OF_QUESTIONS_COLUMN, GameDatabaseHelper.SETTING_SECONDS_PER_QUESTION_COLUMN},
                null, null, null, null, null );
        cursor.moveToFirst();
        cursor.moveToLast(); //we take the last record of setting
        userName = cursor.getString(1);
        enableShaking =cursor.getInt(2);
        enableShufflingQuestions =cursor.getInt(3);
        numOfQues =cursor.getInt(4);
        secsPerQues =cursor.getInt(5);

        updateCurrentSettingUI();
        cursor.close();
    }

    protected void updateCurrentSettingUI(){
        //username
        userNameSettingTextView.setText("User name:  "+ userName);
        //shaking
        if(enableShaking ==0){
            enableShakingSwitch.setChecked(false);
        }else{
            enableShakingSwitch.setChecked(true);
        }
        //shuffling
        if(enableShufflingQuestions ==0){
            shuffleQuestionSwitch.setChecked(false);
        }else{
            shuffleQuestionSwitch.setChecked(true);
        }
        //number of questions
        numOfQuesSeekbarLabel.setText("Number of questions: "+numOfQues);
        numOfQuesSeekbar.setProgress(numOfQues);

        //secs per question
        secsPerQuesSeekbarLabel.setText("Number of questions: "+secsPerQues);
        secsPerQuesSeekbar.setProgress(secsPerQues);

    }

    protected void saveSettingOnDataBase(){
        //save the information of the database
        ContentValues settingValues = new ContentValues();
        settingValues.put(GameDatabaseHelper.SETTING_USER_NAME_COLUMN, userName);
        settingValues.put(GameDatabaseHelper.SETTING_ENABLE_SHAKING_COLUMN,enableShaking); //no shaking
        settingValues.put(GameDatabaseHelper.SETTING_ENABLE_SHUFFLING_QUESTIONS_COLUMN, enableShufflingQuestions); //no shuffling
        settingValues.put(GameDatabaseHelper.SETTING_NUMBER_OF_QUESTIONS_COLUMN, numOfQues); //20 questions
        settingValues.put(GameDatabaseHelper.SETTING_SECONDS_PER_QUESTION_COLUMN, secsPerQues); //10 secs per question
        db.insert(GameDatabaseHelper.SETTING_TABLE, null, settingValues);

    }

    protected void openDialog(){
            UserNameDialog userNameDialog = new UserNameDialog();
            userNameDialog.show(getSupportFragmentManager(),"user name dialog");
    }
    @Override
    public void applyText(String username) {
            this.userName = username;
            userNameSettingTextView.setText("User name:  "+ userName);
    }


    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString("userName",userName);
        savedInstanceState.putInt("enableShaking",enableShaking);
        savedInstanceState.putInt("enableShufflingQuestions",enableShufflingQuestions);
        savedInstanceState.putInt("numOfQues",numOfQues);
        savedInstanceState.putInt("secsPerQues",secsPerQues);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        if(db != null){
            db.close();
        }
    }

    @Override
    public void onBackPressed() {
        saveSettingOnDataBase();
        super.onBackPressed();
    }
}