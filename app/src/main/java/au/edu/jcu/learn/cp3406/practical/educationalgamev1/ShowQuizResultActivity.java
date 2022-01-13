package au.edu.jcu.learn.cp3406.practical.educationalgamev1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class ShowQuizResultActivity extends AppCompatActivity {

    public static String SUBJECT_STRING="SUBJECT";
    public static String CORRECT_QUESTION_NUM ="CORRECT QUESTION NUMBER";
    public static String TOTAL_QUESTIONS ="TOTAL NUMBER";
    public static String TOTAL_SECONDS="TOTAL_SECONDS";

    private String subject;
    private int totalCorrectQuestion;
    private int totalQuestion;
    private int totalSeconds;

    TextView yourNameView;
    TextView yourScoreView;
    TextView timeView;
    AppCompatButton backToHomeButton;
    AppCompatButton newGameButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_quiz_result);

        subject = (String) getIntent().getExtras().get(SUBJECT_STRING);
        totalCorrectQuestion = (int) getIntent().getExtras().get(CORRECT_QUESTION_NUM);
        totalQuestion = (int) getIntent().getExtras().get(TOTAL_QUESTIONS);
        totalSeconds = (int) getIntent().getExtras().get(TOTAL_SECONDS);
//        Toast toast = Toast.makeText(ShowQuizResultActivity.this, String.valueOf(totalSeconds), Toast.LENGTH_LONG);
//        toast.show();

        yourNameView = findViewById(R.id.result_user_name);
        yourNameView.setText("your name: "+MainActivity.userName);

        yourScoreView =findViewById(R.id.result_score);
        yourScoreView.setText("your score: "+String.valueOf(totalCorrectQuestion)+"/"+String.valueOf(totalQuestion));

        timeView = findViewById(R.id.result_total_seconds);
        timeView.setText("time: "+ String.valueOf(totalSeconds));


        backToHomeButton = findViewById(R.id.back_to_home_page_btn);
        backToHomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShowQuizResultActivity.this,  MainActivity.class);
                startActivity(intent);
            }
        });


        newGameButton = findViewById(R.id.take_new_quiz);
        newGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShowQuizResultActivity.this, GameActivity.class);
                intent.putExtra(GameActivity.SUBJECT_FINAL_STRING, subject);
                startActivity(intent);
            }
        });


//        SQLiteOpenHelper gameDatabaseHelper = new GameDatabaseHelper(this);
//        try {
//            SQLiteDatabase db = gameDatabaseHelper.getReadableDatabase();
//            Cursor cursor = db.query(GameDatabaseHelper.RESULT_TABLE,
//                    new String[]{"_id", GameDatabaseHelper.USER_NAME_COLUMN,GameDatabaseHelper.SUBJECT_COLUMN,GameDatabaseHelper.SCORE_COLUMN, GameDatabaseHelper.AVERAGE_SECONDS_COLUMN},
//                    null, null, null, null, null);
//            cursor.moveToLast();
//            Toast toast = Toast.makeText(ShowQuizResultActivity.this, String.valueOf(cursor.getString(1)), Toast.LENGTH_LONG);
//            toast.show();
//
//
//        } catch(SQLiteException e) {
//            Toast toasted = Toast.makeText(this, "Database unavailable", Toast.LENGTH_SHORT);
//            toasted.show();
//            Log.i("load successfully?", "not ok");
//        }
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ShowQuizResultActivity.this,  MainActivity.class);
        startActivity(intent);
    }
}