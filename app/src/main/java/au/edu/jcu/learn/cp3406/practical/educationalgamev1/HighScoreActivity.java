package au.edu.jcu.learn.cp3406.practical.educationalgamev1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.Locale;

public class HighScoreActivity extends AppCompatActivity {
    SQLiteDatabase db;
    Cursor cursor;
    ImageView backHomeBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_score);
        backHomeBtn = findViewById(R.id.high_score_back_to_main_btn);
        backHomeBtn.setOnClickListener(v -> onBackPressed());


        try {
            SQLiteOpenHelper gameDatabaseHelper = new GameDatabaseHelper(this);
            db = gameDatabaseHelper.getReadableDatabase();
            cursor = db.query(GameDatabaseHelper.RESULT_TABLE, new String[]{"_id", GameDatabaseHelper.USER_NAME_COLUMN,GameDatabaseHelper.SUBJECT_COLUMN,GameDatabaseHelper.SCORE_COLUMN, GameDatabaseHelper.AVERAGE_SECONDS_COLUMN},
                    null, null, null, null, GameDatabaseHelper.SCORE_COLUMN +" DESC" );
             displayTopResult();

        }catch(Exception e) {
            Toast toast = Toast.makeText(this, "The database is unavailable", Toast.LENGTH_SHORT);
            toast.show();
            Log.i("can load", "not ok");
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
                Log.i("avg second", String.format(Locale.getDefault(),"%.2f", avgSeconds));

//                inflate the fragment
                ResultRowFragment rowFragment = new ResultRowFragment(userName, subject, score,avgSeconds);
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.add(R.id.result_table,rowFragment);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.commit();

                cursor.moveToNext();
            }


    }

    @Override
    public void onDestroy(){
        if(db!= null ){
            db.close();
        }
        if(cursor!= null){
            cursor.close();
        }
        super.onDestroy();

    }




    @Override
    public void onRestart() {
        super.onRestart();
        cursor = db.query(GameDatabaseHelper.RESULT_TABLE, new String[]{"_id", GameDatabaseHelper.USER_NAME_COLUMN,GameDatabaseHelper.SUBJECT_COLUMN,GameDatabaseHelper.SCORE_COLUMN, GameDatabaseHelper.AVERAGE_SECONDS_COLUMN},
                null, null, null, null, GameDatabaseHelper.USER_NAME_COLUMN +"");
        displayTopResult();

    }

    @Override
    public void onBackPressed() {
       finish();
    }

}