package au.edu.jcu.learn.cp3406.practical.educationalgamev1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class HighScoreActivity extends AppCompatActivity {
    SQLiteDatabase db;
    Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_score);

        SQLiteOpenHelper gameDatabaseHelper = new GameDatabaseHelper(this);
        try {
             db = gameDatabaseHelper.getReadableDatabase();
             cursor = db.query(GameDatabaseHelper.RESULT_TABLE, new String[]{"_id", GameDatabaseHelper.USER_NAME_COLUMN,GameDatabaseHelper.SUBJECT_COLUMN,GameDatabaseHelper.SCORE_COLUMN, GameDatabaseHelper.AVERAGE_SECONDS_COLUMN},
                    null, null, null, null, GameDatabaseHelper.SCORE_COLUMN +" DESC" );
             displayTopResult();
        }catch(SQLiteException e) {
            Toast toast = Toast.makeText(this, "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
            Log.i("load successfully?", "not ok");
        }

    }
    public void displayTopResult(){
        cursor.moveToFirst();
        for (int i = 0; i <cursor.getCount() ; i++) {

            String userName;
            userName = cursor.getString(1);

            String subject;
            subject = cursor.getString(2);

            float score;
            score = cursor.getFloat(3);

            float avgSeconds;
            avgSeconds = cursor.getFloat(4);

            //check the availability of the result table
            Log.i("Score", String.valueOf(score));
            Log.i("User", userName);
            Log.i("Subject", subject);
            Log.i("avg second", String.valueOf(avgSeconds));

            //inflate the fragment
            ResultRowFragment rowFragment = new ResultRowFragment(userName, subject, score,avgSeconds);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(R.id.result_table,rowFragment);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.commit();
            //add
            cursor.moveToNext();
        }
    }



    @Override
    public void onDestroy(){
        super.onDestroy();
        db.close();
        cursor.close();
    }

    @Override
    public void onRestart() {
        super.onRestart();
        Cursor newCursor = db.query(GameDatabaseHelper.RESULT_TABLE, new String[]{"_id", GameDatabaseHelper.USER_NAME_COLUMN,GameDatabaseHelper.SUBJECT_COLUMN,GameDatabaseHelper.SCORE_COLUMN, GameDatabaseHelper.AVERAGE_SECONDS_COLUMN},
                null, null, null, null, GameDatabaseHelper.USER_NAME_COLUMN +"");
        cursor = newCursor;
        displayTopResult();

    }

}