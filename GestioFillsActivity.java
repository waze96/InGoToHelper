package com.example.ausias.ingotohelper;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Space;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.ausias.ingotohelper.DAOS.EscolaDAO_MySQL;
import com.example.ausias.ingotohelper.DAOS.Fill;
import com.example.ausias.ingotohelper.DAOS.FillDAO_MySQL;

import java.util.ArrayList;

/**
 * @author Aitor Gonzalez & Edu Massip
 * @version 1.0
 */

public class GestioFillsActivity extends AppCompatActivity {
    /**
     * @param stringCurs String to save the data of the course of the child saving at the moment.
     * @param stringEscola String to save the school name data of the child saving at the moment.
     * @param stringEtapa String to save the data of the stage of the child saving at the moment.
     * @param stringNom String to save the name of the child saving at the moment.
     * @param stringCognom String to save the Lastname of the child saving at the moment.
     *
     */

    String stringCurs, stringEscola, stringEtapa, stringNom, stringCognom;
    ArrayList<Fill> fillsTutor;
    ArrayList<String> schoolNames;
    private Spinner escoles;
    LinearLayout item;
    ImageButton ibGo;
    int id;

    boolean delete=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestio_fills);
        item = (LinearLayout)findViewById(R.id.childrenLayout);
        item.removeAllViewsInLayout();
        Bundle bundle = getIntent().getExtras();
        id = bundle.getInt("id");
        ibGo = (ImageButton) findViewById(R.id.ibGoMain);
        ibGo.setBackgroundColor(Color.rgb(145,145,145));
        ibGo.setEnabled(false);
        schoolNames = new ArrayList<>();
        fillsTutor = new ArrayList<>();
        new GetFillsOfTutor().execute(MainActivity.id);
    }

    /**
     * Method to erase a child from the gestioFillsActivity
     * @param v the View of the actual activity
     */
    public void onClickBorrar(View v){
        delete=true;
        seleNom();
        delete=false;
    }

    /**
     * method to check in the BBDD the existent childs from this account and load them into the app
     */
    private void afeguirFillsExistents() {
        item = (LinearLayout)findViewById(R.id.childrenLayout);
        item.removeAllViews();
        for (Fill f : fillsTutor) {
            stringNom = f.getNom();
            stringCognom = f.getCognom();
            stringEscola = f.getEscola();
            stringEtapa = f.getEtapa();
            stringCurs = f.getCurs();
            addContent();
        }
    }

    /**
     * method to add a new child
     * @param v
     */
    public void onClickAddChild(View v){
        delete=false;
        seleNom();
    }

    public void onClickGoMain(View v){
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("id", id);
        Log.d("ID", ""+id);
        startActivity(intent);
    }

    /**
     * This method saves the data of a new child created by the user in a variable, so we can saves the changes in the BBDD
     *
     */

    private void guardarDadesFillAuxi() {
        Log.d("ARAENTRA GUARDAR", "DADESFILL");
        Fill f = new Fill(stringNom, stringCognom, stringEscola, id, stringCurs, stringEtapa);
        new BackgroundWorker(this).execute("saveFill", f);
        fillsTutor.add(f);
        new GetFillsOfTutor().execute(id);
    }

    /**
     * This method add to the main activity the components for see a new child in the screen.
     */
    private void addContent() {
        /**
         * @param item this parameter contains the main layout where we want to see the data of the childs.
         *
         */
        item = (LinearLayout)findViewById(R.id.childrenLayout);
        LinearLayout newLayH1=new LinearLayout(this);
        newLayH1.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout newLayH2=new LinearLayout(this);
        newLayH2.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout newLayH3=new LinearLayout(this);
        newLayH3.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout newLayV1=new LinearLayout(this);
        newLayV1.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams paramsFillLayout = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        paramsFillLayout.setMargins(0, 0, 0, 60);
        newLayV1.setLayoutParams(paramsFillLayout);
        LinearLayout.LayoutParams paramsTextosLayout = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        paramsTextosLayout.setMargins(0, 0, 50, 0); // llp.setMargins(left, top, right, bottom);

        TextView nomView=new TextView(this);
        nomView.setText(stringNom);
        nomView.setLayoutParams(paramsTextosLayout);
        newLayH1.addView(nomView);

        TextView cognomView=new TextView(this);
        cognomView.setText(stringCognom);
        cognomView.setLayoutParams(paramsTextosLayout);
        newLayH1.addView(cognomView);

        TextView escolaView=new TextView(this);
        escolaView.setText(stringEscola);
        escolaView.setLayoutParams(paramsTextosLayout);
        newLayH3.addView(escolaView);

        TextView etapaView=new TextView(this);
        etapaView.setText(stringEtapa);
        etapaView.setLayoutParams(paramsTextosLayout);
        newLayH2.addView(etapaView);

        TextView cursView=new TextView(this);
        cursView.setText(stringCurs);
        cursView.setLayoutParams(paramsTextosLayout);
        newLayH2.addView(cursView);

        Space space=new Space(this);
        newLayV1.addView(newLayH1);
        newLayV1.addView(newLayH2);
        newLayV1.addView(newLayH3);
        newLayV1.addView(space);

        item.addView(newLayV1);
        focusOnView();
    }

    /**
     * In this method we ask for the user to put the name of his child
     */
    private void seleNom() {

        final EditText nom=new EditText(this);
        nom.setInputType(InputType.TYPE_CLASS_TEXT |
                InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
        nom.setHint("nom fill");
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setView(nom);
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Siguiente",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        stringNom = nom.getText().toString();
                        dialog.cancel();
                        seleCog();
                    }
                });

        builder1.setNegativeButton(
                "cancelar",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    /**
     * In this method we ask for the user to put the Lastname of his child
     */

    private void seleCog() {
        final EditText cognom=new EditText(this);
        cognom.setInputType(InputType.TYPE_CLASS_TEXT |
                InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
        cognom.setHint("Cognom");
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setView(cognom);
        builder1.setCancelable(true);


        builder1.setNegativeButton(
                "cancelar",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        builder1.setPositiveButton(
                "Siguiente",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        stringCognom = cognom.getText().toString();
                        dialog.cancel();
                        seleEscola();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    /**
     * In this method we ask for the user to put the school of his child
     */

    private void seleEscola() {
        //crear una array (datos escola) apartir del nom de les escoles de la BBDD

        new GenerateSpinnerSchool().execute(this);
    }

    private void focusOnView(){
        final ScrollView sview = (ScrollView)findViewById(R.id.memeScroll);
        sview.post(new Runnable() {
            @Override
            public void run() {
                sview.fullScroll(android.view.View.FOCUS_DOWN);
            }
        });
    }

    /**
     * In this method we ask for the user to put the stage of his child
     */
    private void seleEtapa() {
        String[] datosEtapa={"Educacion Infantil","Educacion Primaria","Educacion Secundaria Obligatoria"};
        final ArrayAdapter<String> adpEtapa = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, datosEtapa);
        final Spinner spinnerEtapa = new Spinner(this);
        spinnerEtapa.setAdapter(adpEtapa);


        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setView(spinnerEtapa);
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Siguiente",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        stringEtapa = spinnerEtapa.getSelectedItem().toString();
                        dialog.cancel();
                        seleCurs(stringEtapa);
                    }
                });

        builder1.setNegativeButton(
                "Cancelar",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    /**
     * In this method we ask for the user to put the course of his child
     * @param stringEtapa this countains the stage of the child, used to show the options of the course.
     */
    private void seleCurs(String stringEtapa) {
        ArrayList<String> datosCursTemp= new ArrayList<String>();
        if(stringEtapa.equals("Educacion Infantil")){
            datosCursTemp.add("P3");
            datosCursTemp.add("P4");
            datosCursTemp.add("P5");

        }
        if(stringEtapa.equals("Educacion Primaria")){
            datosCursTemp.add("1r");
            datosCursTemp.add("2r");
            datosCursTemp.add("3r");
            datosCursTemp.add("4r");
            datosCursTemp.add("5r");
            datosCursTemp.add("6r");
        }
        if(stringEtapa.equals("Educacion Secundaria Obligatoria")){
            datosCursTemp.add("1r");
            datosCursTemp.add("2r");
            datosCursTemp.add("3r");
            datosCursTemp.add("4r");
        }
        String[] datosCurs=new String[datosCursTemp.size()];
        for(int i=0;i<datosCursTemp.size();i++){
            datosCurs[i]=datosCursTemp.get(i);
        }

        final ArrayAdapter<String> adpCurs = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, datosCurs);
        final Spinner spinnerCurs = new Spinner(this);
        spinnerCurs.setAdapter(adpCurs);


        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setView(spinnerCurs);
        builder1.setCancelable(true);
        Log.d("ARAENTRA SELE", "CURS");

        builder1.setPositiveButton(
                "GUARDAR",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        stringCurs = spinnerCurs.getSelectedItem().toString();
                        dialog.cancel();
                        guardarDadesFillAuxi();
                    }
                });

        builder1.setNegativeButton(
                "cancelar",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();

    }

    private class GenerateSpinnerSchool extends AsyncTask<Context, Integer, Void> {
        EscolaDAO_MySQL escolaDAO = new EscolaDAO_MySQL();
        Context ctx;

        @Override
        protected Void doInBackground(Context... params) {
            ctx = params[0];
            ArrayList<String> schoolsName = escolaDAO.getAllSchools();
            schoolNames = schoolsName;
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            String[] schools = new String[schoolNames.size()];
            schools = schoolNames.toArray(schools);
            ArrayAdapter<String> adpEscola = new ArrayAdapter<String>(ctx, android.R.layout.simple_spinner_item, schools);
            escoles = new Spinner(ctx);

            escoles.setAdapter(adpEscola);

            AlertDialog.Builder builderEscola = new AlertDialog.Builder(ctx);
            builderEscola.setView(escoles);
            builderEscola.setCancelable(true);

            builderEscola.setPositiveButton(
                    "Siguiente",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            stringEscola = escoles.getSelectedItem().toString();
                            dialog.cancel();
                            seleEtapa();
                        }
                    });

            builderEscola.setNegativeButton(
                    "Cancelar",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });

            AlertDialog alert11 = builderEscola.create();
            alert11.show();

        }
    }

    private class GetFillsOfTutor extends AsyncTask<Integer, Integer, ArrayList<Fill>> {
        FillDAO_MySQL fillDAO = new FillDAO_MySQL();

        @Override
        protected ArrayList<Fill> doInBackground(Integer... params) {
            ArrayList<Fill> fillsTutor = fillDAO.getFillsByTutorID(params[0]);
            return fillsTutor;
        }

        @Override
        protected void onPostExecute(ArrayList<Fill> fills) {
            super.onPostExecute(fills);
            fillsTutor = fills;
            afeguirFillsExistents();
            if(fillsTutor.isEmpty()) {
                ibGo.setBackgroundColor(Color.rgb(145,145,145));
                ibGo.setEnabled(false);
            }
            else {
                ibGo.setBackgroundColor(Color.rgb(101, 101, 101));
                ibGo.setEnabled(true);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        item = (LinearLayout)findViewById(R.id.childrenLayout);
        item.removeAllViews();
        fillsTutor.clear();
    }

    @Override
    protected void onStop() {
        super.onStop();
        item = (LinearLayout)findViewById(R.id.childrenLayout);
        item.removeAllViews();
        fillsTutor.clear();
    }
}
