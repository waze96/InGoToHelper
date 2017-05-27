package com.example.ausias.ingotohelper.Fragments;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.ausias.ingotohelper.DAOS.Requests;
import com.example.ausias.ingotohelper.DAOS.Tutor;
import com.example.ausias.ingotohelper.DAOS.UserDAO_MySQL;
import com.example.ausias.ingotohelper.ListViewRequestAdapter;
import com.example.ausias.ingotohelper.MainActivity;
import com.example.ausias.ingotohelper.R;

import java.util.ArrayList;
import java.util.List;

/**
 * This Fragment loads on a ListView all the Requests that the actual User have and he has not
 * respoded. OnCreateView it call the innerClass GetInfoRequests that is an AsyncTask to load the info
 * requests.
 * Created by Eduard on 14/05/17.
 */

public class RequestsFragment extends Fragment{
    List<Requests> userRequests;
    ListView lv;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_requests, container, false);

        userRequests = new ArrayList<>();

        lv = (ListView) view.findViewById(R.id.lvRequests);
        new GetInfoRequests().execute(MainActivity.petitions);

        return view;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private class GetInfoRequests extends AsyncTask<ArrayList<Integer>, Integer, ArrayList<Tutor>> {

        UserDAO_MySQL tutorDAO = new UserDAO_MySQL();

        @Override
        protected ArrayList<Tutor> doInBackground(ArrayList<Integer>... params) {
            ArrayList<Tutor> tutors = new ArrayList<>();

            for (Integer id: params[0]) {
                tutors.add(tutorDAO.getTutor(id));
            }
            return tutors;
        }

        @Override
        protected void onPostExecute(ArrayList<Tutor> tutors) {
            super.onPostExecute(tutors);
            for (Tutor tutor:tutors) {
                Requests r;
                try{
                    r = new Requests(tutor.getFotoBitMap(),
                            tutor.getNom() + " " + tutor.getCognom1(), tutor.getId());
                }catch (NullPointerException e){
                    r = new Requests(null,
                            tutor.getNom() + " " + tutor.getCognom1(), tutor.getId());
                }
                userRequests.add(r);
            }
            ListViewRequestAdapter adapter = new ListViewRequestAdapter(getActivity(), userRequests);
            lv.setAdapter(adapter);
        }
    }
}
