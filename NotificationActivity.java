package com.example.ausias.ingotohelper;

import android.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.ausias.ingotohelper.Fragments.NotificationsFragment;
import com.example.ausias.ingotohelper.Fragments.RequestsFragment;

public class NotificationActivity extends AppCompatActivity {
    FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.frameNotifications, new NotificationsFragment())
                .commit();
    }

    public void onClickRequests(View view){
        fragmentManager.beginTransaction()
                .replace(R.id.frameNotifications, new RequestsFragment())
                .commit();
    }

    public void onClickNotifications(View view){
        fragmentManager.beginTransaction()
                .replace(R.id.frameNotifications, new NotificationsFragment())
                .commit();
    }
}
