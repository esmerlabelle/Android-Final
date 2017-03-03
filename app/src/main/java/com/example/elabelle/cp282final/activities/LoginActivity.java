package com.example.elabelle.cp282final.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.elabelle.cp282final.R;
import com.example.elabelle.cp282final.helpers.LoginHelper;

public class LoginActivity extends AppCompatActivity {

    private String username, password, phone, email;
    //public MediaPlayer song;
    LoginHelper loginHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginHelper = new LoginHelper(this);
    }

    public void login(View view) {
        username = ((EditText) findViewById(R.id.txtUsername)).getText().toString();
        password = ((EditText) findViewById(R.id.txtPassword)).getText().toString();


        /*song = MediaPlayer.create(this, R.raw.pizza_plus_jingle);
        song.setLooping(false);
        song.start();*/

        String tbl_password = loginHelper.getUser(username);
        if (password.equals(tbl_password)) {
            Bundle all = new Bundle();
            all.putString("username", username);
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtras(all);
            startActivity(intent);
        }
    }

    public void register(View view) {
        username = ((EditText) findViewById(R.id.txtUsername)).getText().toString();
        password = ((EditText) findViewById(R.id.txtPassword)).getText().toString();
        phone = ((EditText) findViewById(R.id.txtPhone)).getText().toString();
        email = ((EditText) findViewById(R.id.txtEmail)).getText().toString();

        boolean result = loginHelper.saveUser(username, password, email, phone);

        if (result) {
            Toast.makeText(getApplicationContext(), "Successfully registered!", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(), "Failed to register!", Toast.LENGTH_LONG).show();
        }

    }
}