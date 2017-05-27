package com.example.ausias.ingotohelper.Interfaces;

import java.util.ArrayList;

/**
 * Interface DAO School, This interfaces describes the methods needed to get information
 * from any database.
 * Created by Eduard on 11/04/17.
 */

public interface IDAO_Escola {

    String user = "a15edumaspla_r";
    String pass = "rootroot";

    int getCodeByName(String nom);

    String getName (int id);

    ArrayList<String> getAllSchools ();

    double[] getLatLngByName(String name);
}
