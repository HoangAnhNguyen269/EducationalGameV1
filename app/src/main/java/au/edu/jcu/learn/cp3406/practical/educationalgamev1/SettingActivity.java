package au.edu.jcu.learn.cp3406.practical.educationalgamev1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class SettingActivity extends AppCompatActivity implements UserNameDialog.UserNameDialogListener {

    TextView userNameSettingTextView;

    SeekBar numOfQuesSeekbar;
    TextView numOfQUesSeekbarLabel;

    SeekBar secsPerQuesSeekbar;
    TextView secsPerQuesSeekbarLabel;


    String userName;
    boolean enableShaking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        //User name text View
        userNameSettingTextView = findViewById(R.id.setting_user_name);
        userNameSettingTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });

        //num of ques seekbar
        numOfQuesSeekbar = (SeekBar) findViewById(R.id.num_of_ques_seekbar);
        numOfQUesSeekbarLabel = findViewById(R.id.num_of_ques_seekbar_label);
        numOfQuesSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                numOfQUesSeekbarLabel.setText("Number of questions: "+progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //replace this code below with the database related code
                Toast toast = Toast.makeText(SettingActivity.this,"Done tracking", Toast.LENGTH_SHORT);
                toast.show();
            }
        });

        //secs per ques seekbar
        secsPerQuesSeekbar = (SeekBar) findViewById(R.id.secs_per_ques_seekbar);
        secsPerQuesSeekbarLabel = (TextView) findViewById(R.id.secs_per_ques_seekbar_label);
        secsPerQuesSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                secsPerQuesSeekbarLabel.setText("Seconds per question: "+progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //replace this code below with the database related code
                Toast toast = Toast.makeText(SettingActivity.this,"Done tracking-Secs per ques", Toast.LENGTH_SHORT);
                toast.show();
            }
        });

    }

    protected void openDialog(){
            UserNameDialog userNameDialog = new UserNameDialog();
            userNameDialog.show(getSupportFragmentManager(),"user name dialog");
    }
    @Override
    public void applyText(String username) {
            this.userName = username;
            userNameSettingTextView.setText("User name:  "+ userName);
    }

    protected void saveSetting(){

    }
}