package au.edu.jcu.learn.cp3406.practical.educationalgamev1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class SettingActivity extends AppCompatActivity {

    boolean enableShaking;
    AppCompatButton testButton;
    String greeting;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        greeting = (String) getIntent().getExtras().get("Hello");
        testButton = findViewById(R.id.test_button);
        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this,
                        SettingActivity.class);
                intent.putExtra("Hello","Xin chao");
                startActivity(intent);
            }
        });

        Toast.makeText(this, greeting, Toast.LENGTH_SHORT).show();






    }
}