package au.edu.jcu.learn.cp3406.practical.educationalgamev1;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class EducationalGameDatabaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "EducationalGameDatabase"; // the name of our database
    private static final int DB_VERSION = 1; // the initial version of the database

    //store table name
    public static final String MATH_TABLE ="MATH_TABLE";
    public static final String BASIC_COMPUTER_TABLE ="COMPUTER_TABLE";
    //store column name
    public static final String QUESTION_COLUMN ="QUESTION";
    public static final String ANS_1_COLUMN ="ANS_1";
    public static final String ANS_2_COLUMN ="ANS_2";
    public static final String ANS_3_COLUMN ="ANS_3";
    public static final String ANS_4_COLUMN ="ANS_4";
    public static final String CORRECT_ANS_NUM_COLUMN="CORRECT_ANS_NUM";



    EducationalGameDatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        updateEducationalGameDatabase(db, 0, DB_VERSION);
    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        updateEducationalGameDatabase(db, oldVersion, newVersion);
    }

    private static void insertQuiz(SQLiteDatabase db, String tableName,String question, String ans1,String ans2, String ans3, String ans4, int correctAnsNum) {
        ContentValues quizQuestionValues = new ContentValues();
        quizQuestionValues.put(QUESTION_COLUMN, question);
        quizQuestionValues.put(ANS_1_COLUMN, ans1);
        quizQuestionValues.put(ANS_2_COLUMN, ans2);
        quizQuestionValues.put(ANS_3_COLUMN, ans3);
        quizQuestionValues.put(ANS_4_COLUMN, ans4);
        quizQuestionValues.put(CORRECT_ANS_NUM_COLUMN, correctAnsNum);
        db.insert(tableName, null, quizQuestionValues);
    }

    private void updateEducationalGameDatabase(SQLiteDatabase db, int oldVersion, int newVersion) {

            db.execSQL("CREATE TABLE "+ MATH_TABLE +" (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + QUESTION_COLUMN+" TEXT, "
                    + ANS_1_COLUMN+" TEXT, "
                    + ANS_2_COLUMN+" TEXT, "
                    + ANS_3_COLUMN+" TEXT, "
                    + ANS_4_COLUMN+" TEXT, "
                    + CORRECT_ANS_NUM_COLUMN+" INTEGER);");

        InputStream mathTableIS = GameActivity.mathcsvInputStream;
        CSVFileReader mathFileReader = new CSVFileReader(mathTableIS);
        List<QuizQuestion> quizQuestionList = mathFileReader.getQuizQuestions();
        for (int i = 0; i < mathFileReader.getRowNum(); i++) {
            QuizQuestion quizQuestion = quizQuestionList.get(i);
            String question = quizQuestion.getQuestion();
            String ans1 = quizQuestion.getAns1();
            String ans2 = quizQuestion.getAns2();
            String ans3 = quizQuestion.getAns3();
            String ans4 = quizQuestion.getAns4();
            int correctAns = quizQuestion.getCorrectAnsNum();
            insertQuiz(db,MATH_TABLE,question,ans1,ans2,ans3,ans4,correctAns);
        }


//            db.execSQL("CREATE TABLE "+ BASIC_COMPUTER_TABLE +" (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
//                    + QUESTION_COLUMN+" TEXT, "
//                    + ANS_1_COLUMN+" TEXT, "
//                    + ANS_2_COLUMN+" TEXT, "
//                    + ANS_3_COLUMN+" TEXT, "
//                    + ANS_4_COLUMN+" TEXT, "
//                    + CORRECT_ANS_NUM_COLUMN+" INTEGER);");




    }

}
