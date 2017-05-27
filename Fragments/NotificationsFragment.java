package com.example.ausias.ingotohelper.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ausias.ingotohelper.R;

/**
 * This Fragment should load the messages notifications of the Users that have 'Fills' at the same
 * school of the User.
 * Created by Eduard on 14/05/17.
 */

public class NotificationsFragment extends Fragment{
    private LinearLayout linearLayout;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_notifications, container, false);
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView text = new TextView(getActivity());
        text.setText("notifiiiications!");
        linearLayout = (LinearLayout) view.findViewById(R.id.linearLayoutNotifications);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        linearLayout.addView(text, layoutParams);
    }
}
