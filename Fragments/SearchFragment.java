package com.example.ausias.ingotohelper.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ausias.ingotohelper.R;

/**
 * NOT IMPLEMENTED YET
 * Created by Eduard on 28/04/17.
 */
public class SearchFragment extends Fragment {
    private LinearLayout linearLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView text = new TextView(getActivity());
        text.setText("blablablabla");
        linearLayout = (LinearLayout) view.findViewById(R.id.linearLayoutSearch);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        linearLayout.addView(text, layoutParams);
    }
}
