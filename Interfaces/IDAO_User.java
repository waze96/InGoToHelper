package com.example.ausias.ingotohelper.Interfaces;

import com.example.ausias.ingotohelper.DAOS.Tutor;

import java.io.InputStream;

/**
 * Interface DAO User, This interfaces describes the methods needed to get and save information
 * from any database.
 * Created by Eduard on 06/04/2017.
 */

public interface IDAO_User {
    String user = "a15edumaspla_r";
    String pass = "rootroot";

    String logIn (String email, String cipherPasswd);

    String saveTutor (Tutor tutor);

    InputStream getPhoto (int id);

    String getAddress (int id);

    double[] getLatLng (int id);

    void setLatLng (String dni, double lat, double lng);

    Tutor getTutor (int id);

    int getNumChild(int id);

    int getIdByEmail(String email);
}
