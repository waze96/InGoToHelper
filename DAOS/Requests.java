package com.example.ausias.ingotohelper.DAOS;

import android.graphics.Bitmap;

/**
 * This class represents a Friendly Request, it has a Bitmap photo from the user that send the petition,
 * her name and surname, and her id on the dataBase.
 * Created by Eduard Masip on 15/05/17.
 */

public class Requests {
    /**
     * Photo of the user who send the request
     */
    private Bitmap photo;
    /**
     * Name and Surname of the user who send the request
     */
    private String nom;
    /**
     * Id from Database of the user who send the request
     */
    private int idSolicitant;

    /**
     * Constructor request
     * @param photo
     * @param nom
     * @param idSolicitant
     */
    public Requests(Bitmap photo, String nom, int idSolicitant){
        this.photo = photo;
        this.nom = nom;
        this.idSolicitant = idSolicitant;
    }

    /**
     * Getter for the Request photo
     * @return Bitmap with the photo
     */
    public Bitmap getPhoto() {
        return photo;
    }

    /**
     * Getter for the Name and Surname
     * @return String name and surname of the user
     */
    public String getNom() {
        return nom;
    }

    /**
     * Getter for the User ID
     * @return int with the ID of the User that sends this request
     */
    public int getIdSolicitant(){ return idSolicitant;}
}
