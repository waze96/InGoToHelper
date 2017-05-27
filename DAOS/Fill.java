package com.example.ausias.ingotohelper.DAOS;

/**
 * This class is a Object 'Fill', this represents the children of users in real life.
 * Created by Eduard on 27/04/2017.
 */

public class Fill {
    /**
     * Id that have on the database
     */
    private int id;
    /**
     * Father id
     */
    private int idPare;
    /**
     * Attributes for this object.
     */
    private String nom, cognom, escola, curs, etapa;

    public Fill(){

    }

    public Fill (String nom, String cognom, String escola, int pare, String curs, String etapa){
        this.nom = nom;
        this.cognom = cognom;
        this.escola = escola;
        this.etapa = etapa;
        this.idPare = pare;
        this.curs = curs;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEscola() {
        return escola;
    }

    public int getIdPare() {
        return idPare;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getCognom() {
        return cognom;
    }

    public void setCognom(String cognom) {
        this.cognom = cognom;
    }

    public String getCurs() {
        return curs;
    }

    public String getEtapa(){ return  etapa; }
}
