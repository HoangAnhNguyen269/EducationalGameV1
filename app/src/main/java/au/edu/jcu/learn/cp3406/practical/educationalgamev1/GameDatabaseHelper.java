package au.edu.jcu.learn.cp3406.practical.educationalgamev1;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.InputStream;
import java.util.List;

public class GameDatabaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "EducationalGameDatabase"; // the name of our database
    private static final int DB_VERSION = 1; // the initial version of the database

    //store the subject name
    //check strings.xml
    public static final String MATH = "Math";
    public static final String BASIC_COMPUTER = "Basic Computer";
    //store table name
    public static final String MATH_TABLE ="MATH_TABLE";
    public static final String BASIC_COMPUTER_TABLE ="COMPUTER_TABLE";
    public static final String RESULT_TABLE ="RESULT_TABLE";

    //store the column name
    public static final String USER_NAME_COLUMN = "USER_NAME";
    public static final String SUBJECT_COLUMN = "SUBJECT";
    public static final String SCORE_COLUMN = "SCORE";
    public static final String AVERAGE_SECONDS_COLUMN ="AVERAGE_SECONDS";


    //store column name of question tables
    public static final String QUESTION_COLUMN ="QUESTION";
    public static final String ANS_1_COLUMN ="ANS_1";
    public static final String ANS_2_COLUMN ="ANS_2";
    public static final String ANS_3_COLUMN ="ANS_3";
    public static final String ANS_4_COLUMN ="ANS_4";
    public static final String CORRECT_ANS_NUM_COLUMN="CORRECT_ANS_NUM";



    GameDatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        updateGameDatabase(db, 0, DB_VERSION);
    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        updateGameDatabase(db, oldVersion, newVersion);
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



    private void updateGameDatabase(SQLiteDatabase db, int oldVersion, int newVersion) {

        if(oldVersion <1){
            //import questions to math table
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

            //import questions to basic computer table
            db.execSQL("CREATE TABLE "+ BASIC_COMPUTER_TABLE +" (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + QUESTION_COLUMN+" TEXT, "
                    + ANS_1_COLUMN+" TEXT, "
                    + ANS_2_COLUMN+" TEXT, "
                    + ANS_3_COLUMN+" TEXT, "
                    + ANS_4_COLUMN+" TEXT, "
                    + CORRECT_ANS_NUM_COLUMN+" INTEGER);");
            InputStream basicComputerTableIS = GameActivity.computerCsvInputStream;
            CSVFileReader computerFileReader = new CSVFileReader(basicComputerTableIS);
            quizQuestionList = computerFileReader.getQuizQuestions();
            for (int i = 0; i < mathFileReader.getRowNum(); i++) {
                QuizQuestion quizQuestion = quizQuestionList.get(i);
                String question = quizQuestion.getQuestion();
                String ans1 = quizQuestion.getAns1();
                String ans2 = quizQuestion.getAns2();
                String ans3 = quizQuestion.getAns3();
                String ans4 = quizQuestion.getAns4();
                int correctAns = quizQuestion.getCorrectAnsNum();
                insertQuiz(db,BASIC_COMPUTER_TABLE,question,ans1,ans2,ans3,ans4,correctAns);
            }

            //create table that get the result


            db.execSQL("CREATE TABLE "+ RESULT_TABLE +" (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + USER_NAME_COLUMN+" TEXT, "
                    + SUBJECT_COLUMN+" TEXT, "
                    + SCORE_COLUMN+" REAL, "
                    + AVERAGE_SECONDS_COLUMN+" REAL);");


        }








    }

}
