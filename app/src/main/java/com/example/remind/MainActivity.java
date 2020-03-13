package com.example.remind;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private EditText username;
    private EditText password;
    private TextView attemptsRemaining;
    private Button login;
    private int disable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        username = findViewById(R.id.user);
        password = findViewById(R.id.pass);
        attemptsRemaining = findViewById(R.id.loginsRemaining);
        login = findViewById(R.id.buttonLogin);
        disable = 5;

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkLogin(username.getText().toString(), password.getText().toString());
            }
        });

    }

    private void checkLogin(String name, String pass){
        if (name.equals("Admin") && pass.equals("12345")){
            Intent homePage = new Intent(MainActivity.this, HomePageActivity.class);
            startActivity(homePage);
        } else {
            disable--;
            attemptsRemaining.setText(String.format("You have %d login attempts remaining!", disable));
            if (disable <= 0){
                login.setEnabled(false);
            }
        }
    }

}
