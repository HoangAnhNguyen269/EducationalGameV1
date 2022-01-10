package au.edu.jcu.learn.cp3406.practical.educationalgamev1;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class CSVFileReader {
    InputStream is;



    int rowNum;


    List<QuizQuestion> quizQuestions = new ArrayList<>();

    public CSVFileReader(InputStream is) {
        this.is = is;

        //Initialize number of rows
        rowNum =0;

        BufferedReader reader = new BufferedReader(new InputStreamReader(this.is, Charset.forName("UTF-8")));
        String line;
        try{
            while((line=reader.readLine()) != null){
                rowNum ++;
                String[] tokens = line.split(",");
                QuizQuestion quizQuestion = new QuizQuestion();
                quizQuestion.setQuestion(tokens[0]);
                quizQuestion.setAns1(tokens[1]);
                quizQuestion.setAns2(tokens[2]);
                quizQuestion.setAns3(tokens[3]);
                quizQuestion.setAns4(tokens[4]);
                quizQuestion.setCorrectAnsNum(Integer.parseInt(tokens[5]));

                quizQuestions.add(quizQuestion);
            }
        }catch (IOException e){
            e.printStackTrace();
        }

    }
    public List<QuizQuestion> getQuizQuestions(){
        return this.quizQuestions;
    }

    public int getRowNum() {
        return rowNum;
    }
}
