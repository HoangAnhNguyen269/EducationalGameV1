package au.edu.jcu.learn.cp3406.practical.educationalgamev1;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import java.io.InputStream;

public class GameActivity extends AppCompatActivity {
    final public static String SUBJECT_FINAL_STRING = "subjects";
    private String BASIC_COMPUTER;
    private String  MATH;
    String subject;

    private SQLiteDatabase db;
    private Cursor cursor;
    public static InputStream mathcsvInputStream;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        BASIC_COMPUTER = getResources().getStringArray(R.array.subjects)[0];
        MATH = getResources().getStringArray(R.array.subjects)[1];

        //test the database
        subject = (String) getIntent().getExtras().get(SUBJECT_FINAL_STRING);

        ListView listQuestion = (ListView) findViewById(R.id.question_name);

        //load the csv file which is the input for the initial database
        mathcsvInputStream = getResources().openRawResource(R.raw.math_questions_data);


        SQLiteOpenHelper educationalGameDatabaseHelper = new EducationalGameDatabaseHelper(this);
        try {
            db = educationalGameDatabaseHelper.getReadableDatabase();
            cursor = db.query(EducationalGameDatabaseHelper.MATH_TABLE,
                    new String[]{"_id", EducationalGameDatabaseHelper.QUESTION_COLUMN},
                    null, null, null, null, null);
            SimpleCursorAdapter listAdapter = new SimpleCursorAdapter(this,
                    android.R.layout.simple_list_item_1,
                    cursor,
                    new String[]{EducationalGameDatabaseHelper.QUESTION_COLUMN},
                    new int[]{android.R.id.text1},
                    0);
            listQuestion.setAdapter(listAdapter);
            Log.i("load successfully many?", String.valueOf(cursor.getCount()));

        } catch(SQLiteException e) {
            Toast toast = Toast.makeText(this, "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
            Log.i("load successfully?", "not ok");

        }

    }

}