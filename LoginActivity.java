package com.example.ausias.ingotohelper;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

/**
 * The first activity on the app, Have two editText to insert the email and password, and three buttons for LogIn,
 * create a new account "Register" or send a ticket.
 */
public class LoginActivity extends AppCompatActivity {
    EditText emailEt, passwordEt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        emailEt = (EditText) findViewById(R.id.etEmail);
        passwordEt = (EditText) findViewById(R.id.etPassword);
    }

    /**
     * Method is executed when LogIn button is pressed.
     * If Log In is correct enter to the mainActivity, else inform user.
     * @param v
     */
    public void logIn(View v){
        String email = emailEt.getText().toString();
        String password = passwordEt.getText().toString();
        String type = "login";
        BackgroundWorker backgroundWorker = new BackgroundWorker(this);
        backgroundWorker.execute(type, email, password);
    }

    /**
     * Executed when Register button is pressed.
     * Open registerActivity(for new users).
     * @param v
     */
    public void register(View v){
        startActivity(new Intent(this, RegisterActivity.class));
    }

    /**
     * Executed when Ticket button is pressed.
     * Open TicketActivity(for send a problem).
     * @param v
     */
    public void ticket(View v){
        startActivity(new Intent(this, Ticket.class));
    }
}
