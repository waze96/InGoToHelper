package com.example.ausias.ingotohelper.DAOS;

import android.util.Log;

import com.example.ausias.ingotohelper.Interfaces.IDAO_User;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

/**
 * This Class is a Data Access Object from a {@link Tutor}. Implements the interface IDAO_User and is responsible
 * for obtaining and storing Tutor data on Database.
 * Created by Eduard on 04/04/2017.
 */

public class UserDAO_MySQL implements IDAO_User {
    private Connection con;

    /**
     * Void Constructor for UserDAO
     */
    public UserDAO_MySQL() {

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
     * This method checks the user identification to Log In in the application. Here it checks if the user eMail and
     * te ciphered Password coincide on the database.
     * @param email String with the User eMail
     * @param cipherPasswd String with the User password ciphered.
     * @return String to know the result of the identification. If is True this String contains her id and name.
     */
    public String logIn (String email, String cipherPasswd){
        if(email.isEmpty() || cipherPasswd.isEmpty()){
            return "empty";
        }
        else {
            open();
            PreparedStatement prep;
            try {
                prep = con.prepareStatement("SELECT id, nom FROM tutor WHERE email LIKE ? and pass LIKE ?;");
                prep.setString(1, email);
                prep.setString(2, cipherPasswd);
                ResultSet result = prep.executeQuery();
                if(result.next()) {
                    int id = result.getInt(1);
                    String nom = result.getString(2);
                    prep.close();
                    close();
                    return "Logintrue/" + id +"/"+nom;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        close();
        return "Loginfalse";
    }

    /**
     * Save a Tutor passed as a parameter to the database
     * @param tutor The {@link Tutor} to be saved.
     * @return String with a message to know if the Insert has been succesfull or no.
     */
    public String saveTutor (Tutor tutor){
        String passwd = tutor.getPass();
        String nom = tutor.getNom();
        String cog1 = tutor.getCognom1();
        String cog2 = tutor.getCognom2();
        String dni = tutor.getDni();
        String ciutat = tutor.getCiutat();
        String carrer = tutor.getCarrer();
        String codiPostal = tutor.getCodiPostal();
        String telefon = tutor.getTelef();
        String email = tutor.getEmail();
        byte[] foto = tutor.getFotoPerfil();
        if(email.isEmpty() || passwd.isEmpty()){
            return "Email or Password cannot be empty";
        }
        else {
            open();
            PreparedStatement prep;
            try {
                if(foto != null) {
                    prep = con.prepareStatement("INSERT INTO tutor (nom, cognom1, cognom2, dni, telef, ciutat, carrer, codipostal, email, pass, foto) values " +
                            "(?,?,?,?,?,?,?,?,?,?,?)");
                }
                else {
                    prep = con.prepareStatement("INSERT INTO tutor (nom, cognom1, cognom2, dni, telef, ciutat, carrer, codipostal, email, pass) values " +
                            "(?,?,?,?,?,?,?,?,?,?)");
                }
                prep.setString(1, nom);
                prep.setString(2, cog1);
                prep.setString(3, cog2);
                prep.setString(4, dni);
                prep.setString(5, telefon);
                prep.setString(6, ciutat);
                prep.setString(7, carrer);
                prep.setString(8, codiPostal);
                prep.setString(9, email);
                prep.setString(10, passwd);
                if(foto != null) {
                    InputStream is = new ByteArrayInputStream(foto);
                    prep.setBlob(11, is);
                }
                prep.executeUpdate();
                prep.close();
                close();
                return "done";
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        close();
        return "error";
    }

    /**
     * This method get the User photo of the User id passed as a parameter. The SQL Statement obtain a Blob type to the database
     * but this be treated for get the BinaryStream of this BLOB type.
     * @param id int of the User id that we want get the photo
     * @return InputStream of this photo
     */
    public InputStream getPhoto (int id) {
        open();
        PreparedStatement prep;
        try {
            prep = con.prepareStatement("SELECT foto FROM tutor WHERE id=?;");
            prep.setInt(1, id);
            ResultSet result = prep.executeQuery();
            if(result.next()) {
                InputStream is = result.getBlob(1).getBinaryStream();
                prep.close();
                close();
                return is;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (NullPointerException e){
            Log.d("USERDAO_MYSQL","DON'T HAVE PHOTO");
        }
        close();
        return null;
    }

    /**
     * This method return the Address of the User id passed as a parameter, it will return the address
     * in this format: 'address/city/postalCode'
     * @param id int of the User id that we want get her address
     * @return String with all attributes that compose a COMPLETE address.
     */
    @Override
    public String getAddress(int id) {
        open();
        PreparedStatement prep;
        try {
            prep = con.prepareStatement("SELECT carrer, ciutat, codipostal FROM tutor WHERE id=?;");
            prep.setInt(1, id);
            ResultSet result = prep.executeQuery();
            if(result.next()) {
                String address = result.getString(1);
                String city = result.getString(2);
                String postalCode = result.getString(3);
                result.close();
                prep.close();
                close();
                return address + "/" + city + "/" + postalCode;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        close();
        return null;
    }

    /**
     * Get the Latitude and Longitude of a User.
     * @param id int with the Tutor id that you want search.
     * @return double[] with Latitude and Longitude
     */
    @Override
    public double[] getLatLng(int id) {
        double[] latLng = new double[2];
        open();
        PreparedStatement prep;
        try {
            prep = con.prepareStatement("SELECT lat, lng FROM tutor WHERE id=?;");
            prep.setInt(1, id);
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
     * Set the Latitude and Longitude of a {@link Tutor}.
     * @param dni String with DNI of this Tutor
     * @param lat Double with the latitude
     * @param lng Double with the longitude
     */
    @Override
    public void setLatLng(String dni, double lat, double lng) {
        open();
        PreparedStatement prep;
        try {
            prep = con.prepareStatement("UPDATE tutor SET lat = ?, lng = ? WHERE dni LIKE ?;");
            prep.setDouble(1, lat);
            prep.setDouble(2, lng);
            prep.setString(3, dni);
            prep.executeUpdate();
            prep.close();
            close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        close();
    }

    /**
     * This function returns the object Tutor with the id passed as a parameter
     * @param id int with the id of the Tutor that we want.
     * @return {@link Tutor} that the SQL query return.
     */
    @Override
    public Tutor getTutor(int id) {
        InputStream is = null;
        open();
        PreparedStatement prep;
        try {
            prep = con.prepareStatement("SELECT * FROM tutor WHERE id=?;");
            prep.setInt(1, id);
            ResultSet result = prep.executeQuery();
            if(result.next()) {
                int idTutor = result.getInt(1);
                String nom = result.getString(2);
                String cognom = result.getString(3);
                String cognom2 = result.getString(4);
                String dni = result.getString(5);
                String ciutat = result.getString(6);
                String carrer = result.getString(7);
                String codipostal = result.getString(8);
                double lat = result.getDouble(9);
                double lng = result.getDouble(10);
                String telef = result.getString(11);
                String email = result.getString(12);
                Blob photo = result.getBlob(14);
                if(photo != null){
                    is = result.getBlob(14).getBinaryStream();
                }
                result.close();
                prep.close();
                close();
                return new Tutor(idTutor, nom, cognom, cognom2, dni, ciutat, carrer, codipostal, lat, lng, telef, email, is);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        close();
        return null;
    }

    @Override
    public int getNumChild(int id) {
        open();
        PreparedStatement prep;
        int cont = 0;
        try {
            prep = con.prepareStatement("SELECT * FROM fill WHERE tutor = ?;");
            prep.setInt(1, id);
            ResultSet result = prep.executeQuery();
            while(result.next()) {
                cont++;
            }
            result.close();
            prep.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        close();
        return cont;
    }

    @Override
    public int getIdByEmail(String email) {
        open();
        PreparedStatement prep;
        int cont = 0;
        try {
            prep = con.prepareStatement("SELECT id FROM tutor WHERE email LIKE ?;");
            prep.setString(1, email);
            ResultSet result = prep.executeQuery();
            if(result.next()) {
                int id = result.getInt(1);
                result.close();
                prep.close();
                return id;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        close();
        return 0;
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
