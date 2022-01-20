package au.edu.jcu.learn.cp3406.practical.educationalgamev1;


import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 */
public class StopwatchFragment extends Fragment implements View.OnClickListener {
    //Number of seconds displayed on the stopwatch.
    public int settingSeconds =MainActivity.secsPerQues;
    public int seconds = settingSeconds;
    //Is the stopwatch running?
    private boolean running;
    private boolean wasRunning;
    public boolean finish;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            seconds = savedInstanceState.getInt("seconds");
            running = savedInstanceState.getBoolean("running");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_stopwatch, container, false);
        runTimer(layout);
        Button startButton = layout.findViewById(R.id.start_button);
        startButton.setOnClickListener(this);
        Button stopButton = layout.findViewById(R.id.stop_button);
        stopButton.setOnClickListener(this);
        Button resetButton = layout.findViewById(R.id.reset_button);
        resetButton.setOnClickListener(this);
        return layout;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.start_button) {
            onClickStart();
        } else if (id == R.id.stop_button) {
            onClickStop();
        } else if (id == R.id.reset_button) {
            onClickReset();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        wasRunning = running;
        running = false;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (wasRunning) {
            running = true;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putInt("seconds", seconds);
        savedInstanceState.putBoolean("running", running);
        savedInstanceState.putBoolean("wasRunning", wasRunning);
    }

    public void onClickStart() {
        running = true;
        finish =false;
    }

    public void onClickStop() {
        running = false;
    }

    public void onClickReset() {
        running = false;
        seconds = settingSeconds;
    }


    private void runTimer(View view) {
        final TextView timeView = view.findViewById(R.id.time_view);
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (running) {
                    if(seconds<1){
                        onClickStop();
                        finish = true;
                    }else{seconds--;}
                }
                int minutes = (seconds % 3600) / 60;
                int secs = seconds % 60;
                String time = String.format(Locale.getDefault(),
                        "%02d:%02d", minutes, secs);
                timeView.setText(time);

                handler.postDelayed(this, 1000);
            }
        });
    }
}
