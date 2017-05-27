package com.example.ausias.ingotohelper.Fragments;

import android.app.Fragment;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.example.ausias.ingotohelper.DAOS.EscolaDAO_MySQL;
import com.example.ausias.ingotohelper.DAOS.FillDAO_MySQL;
import com.example.ausias.ingotohelper.MainActivity;
import com.example.ausias.ingotohelper.PerfilActivity;
import com.example.ausias.ingotohelper.R;
import com.example.ausias.ingotohelper.DAOS.Tutor;
import com.example.ausias.ingotohelper.DAOS.UserDAO_MySQL;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

/**
 * This class extends of Fragment, and show on the Main Activity an Spinner and Map.
 * When user clicks in any school spinner this class execute a method to load all user for this school.
 * Created by Eduard on 07/04/2017.
 */

public class GmapFragment extends Fragment implements OnMapReadyCallback{
    private Spinner escoles;
    private ArrayList<String> spinnerArray;
    private GoogleMap mMap;
    private ArrayList<LatLng> marks;
    private LatLng latLngSchoolSelected = null;
    private ArrayList<Tutor> tutors;

    /**
     * Load the view on this fragment, and initialize both ArrayList, the spinner and the coordinates.
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        spinnerArray = new ArrayList<>();
        tutors = new ArrayList<>();
        FloatingActionButton floatingActionButton = new FloatingActionButton(getActivity());
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        floatingActionButton.setImageResource(R.drawable.chat);
        floatingActionButton.setBackgroundTintList(ColorStateList.valueOf(Color.YELLOW));
        floatingActionButton.setX(850);
        floatingActionButton.setY(1550);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("DIFUSSED", "jaja");
            }
        });
        getActivity().addContentView(floatingActionButton, params);
        marks = new ArrayList<>();
        return inflater.inflate(R.layout.fragment_gmaps, container, false);
    }

    /**
     * In this method I pass the fragment_gmaps layout, initialize the spinner and call AsyncTask to load the Strings
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        escoles = (Spinner) getActivity().findViewById(R.id.escoles);
        new GenerateSpinner().execute();

        //Control the Selected Item in Spinner
        escoles.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mMap.clear();
                tutors.clear();
                marks.clear();
                new GetLatLngFromSchool().execute(parent.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        MapFragment fragment = (MapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        fragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }

    /**
     *  This AsyncTask, do a connection on the database and gets ID of all schools that the actual User have childs, after gets the names for these schools and generate the
     *  Spinner to choose the school.
     */
    private class GenerateSpinner extends AsyncTask<Void, Integer, Void> {
        FillDAO_MySQL fillDAO = new FillDAO_MySQL();
        EscolaDAO_MySQL escolaDAO = new EscolaDAO_MySQL();

        @Override
        protected Void doInBackground(Void... params) {
            ArrayList<Integer> schoolsId = fillDAO.getCodeSchoolByTutorId(MainActivity.getId());
            ArrayList<String> schoolsName = new ArrayList<>();
            for (int idSchool : schoolsId) {
                schoolsName.add(escolaDAO.getName(idSchool));
            }
            spinnerArray = schoolsName;
            return null;
        }

        @Override
        protected void onPostExecute(Void strings) {
            super.onPostExecute(strings);
            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, spinnerArray);
            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            escoles.setAdapter(spinnerArrayAdapter);
        }
    }

    /**
     * This AsyncTask, receive as a parameter the selected school name, and find all the users with child in this school and the Lat, Lng for this School.
     * After get all the coordinates from users and add all of this coordinates on the ArrayList 'marks'.
     * OnPostExecute method, load in the maps all the marks from the ArrayList and focuses and center the camera on the LatLng School.
     */
    private class GetLatLngFromSchool extends AsyncTask<String, Integer, Boolean> {

        EscolaDAO_MySQL escolaDAO = new EscolaDAO_MySQL();
        FillDAO_MySQL fillDAO = new FillDAO_MySQL();
        UserDAO_MySQL tutorDAO = new UserDAO_MySQL();

        @Override
        protected Boolean doInBackground(String... params) {
            double[] latLngSchool = escolaDAO.getLatLngByName(params[0]);
            latLngSchoolSelected = new LatLng(latLngSchool[0], latLngSchool[1]);
            int code = escolaDAO.getCodeByName(params[0]);
            ArrayList<Integer> idTutors = fillDAO.getTutorsBySchoolCode(code);
            try {
                for (Integer id : idTutors) {
                    tutors.add(tutorDAO.getTutor(id));
                }
                return true;
            }catch (Exception e){
                e.printStackTrace();
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            for (Tutor tutor : tutors) {
                mMap.addMarker(new MarkerOptions().
                            position(new LatLng(tutor.getLat(), tutor.getLng())).
                            title(tutor.getNom() + " " + tutor.getCognom1()).snippet(tutor.getId()+"").
                            icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
            }
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngSchoolSelected, 12));

            mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                @Override
                public View getInfoWindow(Marker marker) {
                    return null;
                }

                @Override
                public View getInfoContents(Marker marker) {
                    View v = getActivity().getLayoutInflater().inflate(R.layout.info_user, null);
                    EditText etNameUser = (EditText) v.findViewById(R.id.nameUserMarker);
                    EditText etIdUser = (EditText) v.findViewById(R.id.idUserMarker);
                    etNameUser.setText(marker.getTitle());
                    etIdUser.setText(marker.getSnippet());
                    return v;
                }
            });

            mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                @Override
                public void onInfoWindowClick(Marker marker) {
                    Intent intent = new Intent(getActivity(), PerfilActivity.class);
                    intent.putExtra("id", Integer.parseInt(marker.getSnippet()));
                    startActivity(intent);
                }
            });
        }
    }
}
