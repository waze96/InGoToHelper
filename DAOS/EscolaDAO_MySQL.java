package com.example.ausias.ingotohelper.DAOS;

import com.example.ausias.ingotohelper.Interfaces.IDAO_Escola;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Properties;

/**
 * This Class is a Data Access Object from a school. Implements the interface IDAO_Escola and is responsible
 * for obtaining school data from Database.
 * Created by Eduard on 11/04/17.
 */

public class EscolaDAO_MySQL implements IDAO_Escola {

    private Connection con;

    /**
     * Void Constructor for EscolaDAO
     */
    public EscolaDAO_MySQL() {

    }

    /**
     * Open the connection to database Server
     */
    private void open() {
        InetAddress ia;
        try {
            ia = InetAddress.getByName("ns1.labs.iam.cat");
            String ip = ia.getHostAddress();
            String url="jdbc:mysql://"+ip+":3306/a15edumaspla_ineedhelp_prova";
            Class.forName("com.mysql.jdbc.Driver");
            Driver driver = DriverManager.getDriver(url);
            Properties properties = new Properties();
            properties.setProperty("user", user);
            properties.setProperty("password", pass);
            con = driver.connect(url, properties);
        } catch (UnknownHostException | ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Get the id of the school passed as a parameter.
     * @param nom The name of School that you want search
     * @return int ID of school, or null if the name isn't exist
     */
    @Override
    public int getCodeByName(String nom) {
        open();
        PreparedStatement prep;
        try {
            prep = con.prepareStatement("SELECT codi FROM escola WHERE nom=?;");
            prep.setString(1, nom);
            ResultSet result = prep.executeQuery();
            if(result.next()) {
                int code = result.getInt(1);
                result.close();
                prep.close();
                close();
                return code;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        close();
        return 0;
    }

    /**
     * Get the name of the School that have the ID passed as a parameter
     * @param id ID of the school that you want search
     * @return String of School name or null if not found.
     */
    @Override
    public String getName(int id) {
        open();
        PreparedStatement prep;
        try {
            prep = con.prepareStatement("SELECT nom FROM escola WHERE codi=?;");
            prep.setInt(1, id);
            ResultSet result = prep.executeQuery();
            if(result.next()) {
                String nom = result.getString(1);
                result.close();
                prep.close();
                close();
                return nom;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        close();
        return null;
    }

    /**
     * Get a ArrayList with all school names.
     * @return ArrayList of Strings
     */
    @Override
    public ArrayList<String> getAllSchools() {
        open();
        Statement statement;
        try {
            statement = con.createStatement();
            ResultSet result = statement.executeQuery("SELECT nom FROM escola;");
            ArrayList<String> escolesTutor = new ArrayList<String>();
            while(result.next()) {
                String nom = result.getString(1);
                escolesTutor.add(nom);
            }
            result.close();
            statement.close();
            close();
            return escolesTutor;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        close();
        return null;
    }

    /**
     * Get the Latitude and Longitude of a School.
     * @param name 'String' with the school name that you want search.
     * @return double[] with Latitude and Longitude
     */
    @Override
    public double[] getLatLngByName(String name) {
        double[] latLng = new double[2];
        open();
        PreparedStatement prep;
        try {
            prep = con.prepareStatement("SELECT lat, lng FROM escola WHERE nom LIKE ?;");
            prep.setString(1, name);
            ResultSet result = prep.executeQuery();
            if(result.next()) {
                latLng[0] = result.getDouble(1);
                latLng[1] = result.getDouble(2);
                result.close();
                prep.close();
                close();
                return latLng;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        close();
        return latLng;
    }

    /**
     * Close the connection with the database Server
     */
    private void close(){
        try {
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
