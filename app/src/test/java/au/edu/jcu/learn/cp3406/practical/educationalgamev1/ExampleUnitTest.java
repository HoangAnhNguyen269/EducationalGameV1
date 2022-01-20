package au.edu.jcu.learn.cp3406.practical.educationalgamev1;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }
    QuizQuestion quiz1 = new QuizQuestion();

    @Test
    public void checkGetQuestionFromQuizQuestion(){
        quiz1.setQuestion("what is 1+1?");
        quiz1.setAns1("2");
        quiz1.setAns2("5");
        quiz1.setAns3("7");
        quiz1.setAns4("3");
        quiz1.setCorrectAnsNum(1);
        assertEquals("what is 1+1?",quiz1.getQuestion());
    }

    @Test
    public void checkGetAns1FromQuizQuestion(){
        quiz1.setQuestion("what is 1+1?");
        quiz1.setAns1("2");
        quiz1.setAns2("5");
        quiz1.setAns3("7");
        quiz1.setAns4("3");
        quiz1.setCorrectAnsNum(1);
        assertEquals("2",quiz1.getAns1());
    }

    @Test
    public void checkGetAns2FromQuizQuestion(){
        quiz1.setQuestion("what is 1+1?");
        quiz1.setAns1("2");
        quiz1.setAns2("5");
        quiz1.setAns3("7");
        quiz1.setAns4("3");
        quiz1.setCorrectAnsNum(1);
        assertEquals("5",quiz1.getAns2());
    }

    @Test
    public void checkActivities(){
        assertEquals("admin",MainActivity.userName);
        assertEquals("selected subject",GameActivity.SUBJECT_FINAL_STRING);
    }

    @Test
    public void checkGetAns3FromQuizQuestion(){
        quiz1.setQuestion("what is 1+1?");
        quiz1.setAns1("2");
        quiz1.setAns2("5");
        quiz1.setAns3("7");
        quiz1.setAns4("3");
        quiz1.setCorrectAnsNum(1);
        assertEquals("7",quiz1.getAns3());
    }

    @Test
    public void checkGetAns4FromQuizQuestion(){
        quiz1.setQuestion("what is 1+1?");
        quiz1.setAns1("2");
        quiz1.setAns2("5");
        quiz1.setAns3("7");
        quiz1.setAns4("3");
        quiz1.setCorrectAnsNum(1);
        assertEquals("3",quiz1.getAns4());
    }

    @Test
    public void checkGetCorrectAnsFromQuizQuestion(){
        quiz1.setQuestion("what is 1+1?");
        quiz1.setAns1("2");
        quiz1.setAns2("5");
        quiz1.setAns3("7");
        quiz1.setAns4("3");
        quiz1.setCorrectAnsNum(1);
        assertEquals(1,quiz1.getCorrectAnsNum());
    }


}