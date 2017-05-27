package com.example.ausias.ingotohelper.Interfaces;

import com.example.ausias.ingotohelper.DAOS.Fill;

import java.util.ArrayList;

/**
 * Interface DAO Fill, This interfaces describes the methods needed to get and save information
 * from any database.
 * Created by Eduard on 14/04/2017.
 */

public interface IDAO_Fill {

    String user = "a15edumaspla_r";
    String pass = "rootroot";

    String savefill(Fill fill);

    ArrayList<Integer> getTutorsBySchoolCode(int id);

    ArrayList<Integer> getCodeSchoolByTutorId (int id);

    ArrayList<Fill> getFillsByTutorID(int id);
}
