package au.edu.jcu.learn.cp3406.practical.educationalgamev1;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Looper;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameWorkerThread extends Thread {


    List<QuizQuestion> quizQuestions = new ArrayList<>();
    int numOfQues = MainActivity.numOfQues;
    boolean enableShufflingQuestions = MainActivity.enableShufflingQuestions;


    private SQLiteDatabase db;

    GameWorkerThread() {

    }

    //setter methods
    public void setDb(SQLiteDatabase db) {
        this.db = db;
    }

    @Override
    public void run() {
        Looper.prepare();
        Looper looper = Looper.myLooper();
        Handler handler = new Handler(looper);
        Looper.loop();

    }

    public List<QuizQuestion> getQuizQuestions(String subject) {
        String tableName;
        if (subject.equals(GameDatabaseHelper.BASIC_COMPUTER)) {
            tableName = GameDatabaseHelper.BASIC_COMPUTER_TABLE;
        } else {
            tableName = GameDatabaseHelper.MATH_TABLE; //change later
        }

        Cursor cursor = db.query(tableName,
                new String[]{"_id", GameDatabaseHelper.QUESTION_COLUMN, GameDatabaseHelper.ANS_1_COLUMN, GameDatabaseHelper.ANS_2_COLUMN, GameDatabaseHelper.ANS_3_COLUMN, GameDatabaseHelper.ANS_4_COLUMN, GameDatabaseHelper.CORRECT_ANS_NUM_COLUMN},
                null, null, null, null, null);
        cursor.moveToFirst();
        for (int i = 0; i < cursor.getCount(); i++) {
            QuizQuestion quizQuestion = new QuizQuestion();
            quizQuestion.setQuestion(cursor.getString(1));
            quizQuestion.setAns1(cursor.getString(2));
            quizQuestion.setAns2(cursor.getString(3));
            quizQuestion.setAns3(cursor.getString(4));
            quizQuestion.setAns4(cursor.getString(5));
            quizQuestion.setCorrectAnsNum(cursor.getInt(6));
            quizQuestions.add(quizQuestion);

            cursor.moveToNext();
        }

        cursor.close();

        //now we have a complete list of questions
        //we only returns list of questions based on numOfQues and enableShufflingQuestions
        List<QuizQuestion> selectedQuizQuestions = new ArrayList<>();
        int count = 0;
        if (enableShufflingQuestions) {
            while (count < numOfQues) {
                Random rand = new Random();
                selectedQuizQuestions.add(quizQuestions.remove(rand.nextInt(quizQuestions.size())));
                count++;
            }
        } else {
            while (count < numOfQues) {
                selectedQuizQuestions.add(quizQuestions.get(count));
                count++;
            }
        }

        return selectedQuizQuestions;
    }

}
