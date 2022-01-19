package au.edu.jcu.learn.cp3406.practical.educationalgamev1;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class QuizQuestion implements Parcelable {


    protected QuizQuestion(Parcel in) {
        question = in.readString();
        ans1 = in.readString();
        ans2 = in.readString();
        ans3 = in.readString();
        ans4 = in.readString();
        correctAnsNum = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(question);
        dest.writeString(ans1);
        dest.writeString(ans2);
        dest.writeString(ans3);
        dest.writeString(ans4);
        dest.writeInt(correctAnsNum);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<QuizQuestion> CREATOR = new Creator<QuizQuestion>() {
        @Override
        public QuizQuestion createFromParcel(Parcel in) {
            return new QuizQuestion(in);
        }

        @Override
        public QuizQuestion[] newArray(int size) {
            return new QuizQuestion[size];
        }
    };

    public String getQuestion() {
        return question;
    }

    public String getAns1() {
        return ans1;
    }

    public String getAns2() {
        return ans2;
    }

    public String getAns3() {
        return ans3;
    }

    public String getAns4() {
        return ans4;
    }

    public int getCorrectAnsNum() {
        return correctAnsNum;
    }

    public String question;
    public String ans1;
    public String ans2;
    public String ans3;
    public String ans4;
    public int correctAnsNum;

    public QuizQuestion() {
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public void setAns1(String ans1) {
        this.ans1 = ans1;
    }

    public void setAns2(String ans2) {
        this.ans2 = ans2;
    }

    public void setAns3(String ans3) {
        this.ans3 = ans3;
    }

    public void setAns4(String ans4) {
        this.ans4 = ans4;
    }

    public void setCorrectAnsNum(int correctAnsNum) {
        this.correctAnsNum = correctAnsNum;
    }


}
