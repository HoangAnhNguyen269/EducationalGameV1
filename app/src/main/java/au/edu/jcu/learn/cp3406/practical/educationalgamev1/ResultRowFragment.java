package au.edu.jcu.learn.cp3406.practical.educationalgamev1;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ResultRowFragment extends Fragment {

    private String userName;
    private String subject;
    private float score;
    private float avgSeconds;


    TextView userNameView;
    TextView subjectView;
    TextView scoreView;
    TextView avgSecondsView;


    public ResultRowFragment(String userName, String subject, float score, float avgSeconds) {
        this.userName = userName;
        this.subject = subject;
        this.score = score;
        this.avgSeconds= avgSeconds;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        if(savedInstanceState != null){
            userName = savedInstanceState.getString("userName");
            subject = savedInstanceState.getString("subject");
            score = savedInstanceState.getFloat("score");
            avgSeconds = savedInstanceState.getFloat("avgSeconds");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_result_row, container, false);
    }

    @Override
    public void onStart() {

        super.onStart();
        View layout = getView();
        if(layout != null){
            userNameView = (TextView) layout.findViewById(R.id.result_row_user_name);
            userNameView.setText(userName);

            subjectView = (TextView) layout.findViewById(R.id.result_row_subject);
            subjectView.setText(subject);

            scoreView = (TextView)  layout.findViewById(R.id.result_row_score);
            scoreView.setText(String.valueOf(score));

            avgSecondsView = (TextView) layout.findViewById(R.id.result_row_avg_seconds);
            avgSecondsView.setText(String.valueOf(avgSeconds));
        }

    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putString("userName", userName);
        savedInstanceState.putString("subject", subject);
        savedInstanceState.putFloat("score", score);
        savedInstanceState.putFloat("avgSeconds", avgSeconds);
    }





}