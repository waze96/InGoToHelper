package com.example.ausias.ingotohelper;

import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.ausias.ingotohelper.DAOS.Tutor;
import com.example.ausias.ingotohelper.DAOS.UserDAO_MySQL;

import java.io.InputStream;

/**
 * This activity created when some user click on the menu 'Profile' or in MapsActivity click on some window of anybody
 * When the acitvity is created, be executed a AsynkTask thats load all the information of the User.
 * Display the profile Photo, name and surname, telephone, and address. When a user watches another profile, he can send a request to this user
 * to be friends. When another user is connected it will see the requests received and may accept or no.
 *
 * Created by Eduard
 */
public class PerfilActivity extends AppCompatActivity {
    /**
     * The ID of the User who will be running the App.
     */
    int idUsuari = MainActivity.getId();
    /**
     * The ID of the User which is looking profile
     */
    int idPerfil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);
        Bundle bundle = getIntent().getExtras();
        idPerfil = bundle.getInt("id");
        //If the profile isn't the actual User generates the FloatingActionButton 'AddFriend'

        if(idUsuari!=idPerfil){
            FloatingActionButton floatingActionButton = new FloatingActionButton(this);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            floatingActionButton.setImageResource(R.drawable.add_user);
            floatingActionButton.setBackgroundTintList(ColorStateList.valueOf(Color.YELLOW));
            floatingActionButton.setX(800);
            floatingActionButton.setY(650);
            floatingActionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addFriend(v);
                }
            });
            addContentView(floatingActionButton, params);
        }
        new LoadPerfil().execute(idPerfil);
    }

    /**
     * This function will be executed when a user clicks on the button 'Add Friend'.
     * Execute a {@link BackgroundWorker} and this establish a connection with a TCPServer and save the friend Petition.
     * @param view
     */
    public void addFriend(View view){
        BackgroundWorker bw = new BackgroundWorker(this);
        bw.execute("peticio", idUsuari, idPerfil);
    }

    /**
     * This AsyncTask receives the ID which you want to load the profile, do a query on the server and get all data of this user.
     * Later load all data on the activity.
     */
    private class LoadPerfil extends AsyncTask<Integer, Integer, Tutor> {

        UserDAO_MySQL tutorDAO = new UserDAO_MySQL();
        InputStream foto;

        @Override
        protected Tutor doInBackground(Integer... params) {
            Tutor tutor = tutorDAO.getTutor(params[0]);
            foto = tutorDAO.getPhoto(params[0]);
            return tutor;
        }

        @Override
        protected void onPostExecute(Tutor tutor) {
            super.onPostExecute(tutor);
            ImageView imageView = (ImageView) findViewById(R.id.imagePerfil);
            Bitmap bitmap = BitmapFactory.decodeStream(foto);
            if(bitmap!=null)
                imageView.setImageBitmap(Bitmap.createBitmap(bitmap));
            else {
                Drawable defaultProfile = getDrawable(R.drawable.default_profile);
                imageView.setImageDrawable(defaultProfile);
            }
            TextView tvNom = (TextView) findViewById(R.id.nomCognom);
            tvNom.setText(tutor.getNom() + " " + tutor.getCognom1());
            TextView tvTelef = (TextView) findViewById(R.id.telefPerfil);
            tvTelef.setText(tutor.getTelef());
            TextView tvEmail = (TextView) findViewById(R.id.emailPerfil);
            tvEmail.setText(tutor.getEmail());
            TextView tvMarker = (TextView) findViewById(R.id.markerPerfil);
            tvMarker.setText(tutor.getCarrer() + " " + tutor.getCodiPostal());
        }
    }
}