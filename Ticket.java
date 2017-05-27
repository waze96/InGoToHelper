package com.example.ausias.ingotohelper;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class Ticket extends AppCompatActivity {
    Spinner problems;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticcket);
        problems=(Spinner) findViewById(R.id.spinnerProblems);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapterProblems = ArrayAdapter.createFromResource(this,R.array.problems, android.R.layout.simple_spinner_item);
        // Apply the adapter to the spinner
        problems.setAdapter(adapterProblems);
    }
}
