package com.example.ausias.ingotohelper.DAOS;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.InputStream;

/**
 * This class is a Object 'Tutor', this represents the users in real life. Contains two constructors.
 * Created by Eduard on 02/04/2017.
 */

public class Tutor {
    /**
     * Id that identifies it on the database
     */
    private int id;

    /**
     * Attributes of the User, to be saved on database
     */
    private String nom, cognom1, cognom2, dni, ciutat, carrer, codiPostal, telef, email, pass;

    /**
     * Latitude and Longitude of the user house, obtained through the GeoCoderTask class in which attributes
     * 'ciutat','carrer' and 'codiPostal' are passed and this class get the Latitude and Longitude using the
     * GoogleMaps GeoCoder API
     */
    private double lat, lng;

    /**
     * The array of bytes user photo
     */
    private byte[] fotoPerfil;

    /**
     * The user photo but in bitmap format.
     */
    private Bitmap fotoBitMap;

    public Tutor(int id, String nom, String cognom1, String cognom2, String dni, String ciutat, String carrer,
          String codiPostal, String telef, String email, String pass,  byte[] foto){
        this.id = id;
        this.nom = nom;
        this.cognom1 = cognom1;
        this.cognom2 = cognom2;
        this.dni = dni;
        this.ciutat = ciutat;
        this.carrer = carrer;
        this.codiPostal = codiPostal;
        this.telef = telef;
        this.email = email;
        this.pass = pass;
        this.fotoPerfil = foto;
    }

    Tutor(int id, String nom, String cognom1, String cognom2, String dni, String ciutat, String carrer,
          String codiPostal, double lat, double lng, String telef, String email, InputStream foto){
        this.id = id;
        this.nom = nom;
        this.cognom1 = cognom1;
        this.cognom2 = cognom2;
        this.dni = dni;
        this.ciutat = ciutat;
        this.carrer = carrer;
        this.codiPostal = codiPostal;
        this.lat = lat;
        this.lng = lng;
        this.telef = telef;
        this.email = email;
        this.fotoBitMap = BitmapFactory.decodeStream(foto);
    }

    public int getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public String getCognom1() {
        return cognom1;
    }

    public String getCognom2() {
        return cognom2;
    }

    public String getDni() {
        return dni;
    }

    public String getCiutat() {
        return ciutat;
    }

    public String getCarrer() {
        return carrer;
    }

    public String getCodiPostal() {
        return codiPostal;
    }

    public String getTelef() {
        return telef;
    }

    public String getEmail() {
        return email;
    }

    public String getPass() {
        return pass;
    }

    public byte[] getFotoPerfil() {
        return fotoPerfil;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    public Bitmap getFotoBitMap() {
        return fotoBitMap;
    }
}
