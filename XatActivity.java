package com.example.ausias.ingotohelper;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.ausias.ingotohelper.Fragments.SearchFragment;
import com.example.ausias.ingotohelper.Fragments.XatsFragment;
/**
 * Created by ausias on 28/04/17.
 */

public class XatActivity extends AppCompatActivity{
    FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xat);
        fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                    .replace(R.id.frameXats, new XatsFragment())
                    .commit();
    }

    public void onClickXats(View view){
        fragmentManager.beginTransaction()
                .replace(R.id.frameXats, new XatsFragment())
                .commit();
    }

    public void onClickSearch(View view){
        fragmentManager.beginTransaction()
                .replace(R.id.frameXats, new SearchFragment())
                .commit();
    }
}
