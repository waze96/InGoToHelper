package com.example.ausias.ingotohelper.DAOS;

import com.example.ausias.ingotohelper.Interfaces.IDAO_Fill;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;

/**
 * This Class is a Data Access Object from a {@link Fill}. Implements the interface IDAO_Fill. Is responsible
 * for obtaining and storing {@link Fill} data on Database.
 * Created by Eduard on 11/04/17.
 */
public class FillDAO_MySQL implements IDAO_Fill {

    private Connection con;

    /**
     * Void constructor for FillDAO
     */
    public FillDAO_MySQL() {

    }

    /**
     * Open the connection to database Server
     */
    private void open() {
        InetAddress ia = null;
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
     * Save a {@link Fill} passed as a parameter to the database
     * @param fill The {@link Fill} to be saved.
     * @return String with a message to know if the Insert has been succesfull or no.
     */
    @Override
    public String savefill(Fill fill) {
        EscolaDAO_MySQL escolaDAO = new EscolaDAO_MySQL();

        String nom = fill.getNom();
        String cog = fill.getCognom();
        int idEscola = escolaDAO.getCodeByName(fill.getEscola());
        int idPare = fill.getIdPare();
        String curs = fill.getCurs();
        String etapa = fill.getEtapa();

        open();
        PreparedStatement prep = null;
        try {

            prep = con.prepareStatement("INSERT INTO fill (nom, cognom1, escola, tutor, curs, etapa) values (?,?,?,?,?,?)");
            prep.setString(1, nom);
            prep.setString(2, cog);
            prep.setInt(3, idEscola);
            prep.setInt(4, idPare);
            prep.setString(5, curs);
            prep.setString(6, etapa);
            prep.executeUpdate();
            prep.close();
            close();
            return "success";
        } catch (SQLException e) {
            e.printStackTrace();
        }
        close();
        return "error";
    }

    /**
     * Get id's of {@link Tutor} that have {@link Fill} in the school with the id passed as a parameter
     * @param id ID of the school that you want get the {@link Tutor}
     * @return ArrayList of Integers that contains a ID's of these {@link Tutor}
     */
    @Override
    public ArrayList<Integer> getTutorsBySchoolCode(int id) {
        ArrayList<Integer> tutors = new ArrayList<Integer>();
        open();
        PreparedStatement prep;
        try {
            prep = con.prepareStatement("SELECT tutor FROM fill WHERE escola=?;");
            prep.setInt(1, id);
            ResultSet result = prep.executeQuery();
            while(result.next()) {
                tutors.add(result.getInt(1));
            }
            result.close();
            prep.close();
            close();
            return tutors;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        close();
        return null;
    }

    /**
     * Gets the id's of the Schools where the {@link Tutor} (id) passed as a parameter have {@link Fill}
     * @param id ID of the {@link Tutor} that you want get the school's ID.
     * @return ArrayList of Integers that contains a ID's of these School's
     */
    @Override
    public ArrayList<Integer> getCodeSchoolByTutorId(int id) {
        ArrayList<Integer> schools = new ArrayList<Integer>();
        open();
        PreparedStatement prep;
        try {
            prep = con.prepareStatement("SELECT escola FROM fill WHERE tutor=?;");
            prep.setInt(1, id);
            ResultSet result = prep.executeQuery();
            while(result.next()) {
                schools.add(result.getInt(1));
            }
            result.close();
            prep.close();
            close();
            return schools;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        close();
        return null;
    }

    /**
     * This method obtains an ArrayList of {@link Fill} from the id passed as parameter
     * @param id The identifier of {@link Tutor}
     * @return ArrayList<Fill> with all childs of this User.
     */
    @Override
    public ArrayList<Fill> getFillsByTutorID(int id) {
        ArrayList<Fill> fills = new ArrayList<>();
        EscolaDAO_MySQL escolaDAO = new EscolaDAO_MySQL();
        open();
        PreparedStatement prep;
        try {
            prep = con.prepareStatement("SELECT * FROM fill WHERE tutor=?;");
            prep.setInt(1, id);
            ResultSet result = prep.executeQuery();
            while(result.next()) {
                String nomFill = result.getString(2);
                String cognomFill = result.getString(3);
                int idEscola = result.getInt(4);
                String nomEscola = escolaDAO.getName(idEscola);
                int tutor = result.getInt(5);
                String curs = result.getString(6);
                String etapa = result.getString(7);
                Fill f = new Fill(nomFill ,cognomFill, nomEscola, tutor, curs, etapa);
                fills.add(f);
            }
            result.close();
            prep.close();
            close();
            return fills;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        close();
        return null;
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
