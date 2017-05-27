package com.example.ausias.ingotohelper;

import android.os.AsyncTask;
import android.util.Log;

import com.example.ausias.ingotohelper.DAOS.UserDAO_MySQL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * This AsyncTask obtains the Longitude and Latitude from the address in text format with the GoogleMaps API.
 * Get a JSON string with all the informations that the page returns, and get only the Lat and Lng.
 * Created by Eduard on 16/04/17.
 */

public class GeoCoderTask extends AsyncTask<String, Integer, Boolean> {

    UserDAO_MySQL userDAO;

    @Override
    protected Boolean doInBackground(String... params) {
        String cadena = "https://maps.googleapis.com/maps/api/geocode/json?address=";
        cadena += params[1] + " " + params[2] + " " + params[3];
        cadena = replace(cadena, cadena.length());

        URL url;
        try{
            url = new URL(cadena);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            StringBuilder result = new StringBuilder();

            //Prepare the input Stream
            InputStream is = new BufferedInputStream(connection.getInputStream());

            //Introduce this is on the BufferedReader
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));

            //SONObject, need one String and transfrom this BufferedReader to String through StringBuilder
            String line;
            while((line = reader.readLine()) != null){
                //Pass all information that we read in the StringBuilder
                result.append(line);
            }

            //Create a JSONobject to can access to the attributes that the obtained JSON have
            JSONObject respuestaJSON = new JSONObject(result.toString());

            //Acces on the result Array
            JSONArray resultJSON = respuestaJSON.getJSONArray("results");
            String lat ="";
            String lng = "";
            if(resultJSON.length()>0){
                lat = resultJSON.getJSONObject(0).getJSONObject("geometry").getJSONObject("location").getString("lat");
                lng = resultJSON.getJSONObject(0).getJSONObject("geometry").getJSONObject("location").getString("lng");
            }
            userDAO = new UserDAO_MySQL();
            userDAO.setLatLng(params[0], Double.parseDouble(lat), Double.parseDouble(lng));
            return true;
        }catch (MalformedURLException e){
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        Log.d("GEOCODERTASK", aBoolean.toString());
    }

    /**
     * Returns the string passed as argument formatted for a URL, without spaces, each space is replaced for the characters '%20'
     * because if you take a InputStream from a URL (.getInputStream()) with spaces this method throws a IOException.
     * @param s1 The String to be formatted
     * @param length The length of the first parameter
     * @return The formatted String
     */
    private String replace(String s1, int length) {
        char[] chars = s1.toCharArray();
        int spaceCount = 0;
        for (int i = 0; i < length; i++) {
            if (chars[i] == ' ') {
                spaceCount++;
            }
        }
        int newLength = length + 2 * spaceCount;
        char [] charsNew = new char [newLength];
        for (int i = length - 1; i >= 0; i--) {
            if (chars[i] == ' ') {
                charsNew[newLength - 1] = '0';
                charsNew[newLength - 2] = '2';
                charsNew[newLength - 3] = '%';
                newLength = newLength - 3;
            } else {
                charsNew[newLength - 1] = chars[i];
                newLength = newLength - 1;
            }
        }
        return String.valueOf(charsNew);
    }
}
