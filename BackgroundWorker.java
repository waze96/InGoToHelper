package com.example.ausias.ingotohelper;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.ausias.ingotohelper.DAOS.Fill;
import com.example.ausias.ingotohelper.DAOS.FillDAO_MySQL;
import com.example.ausias.ingotohelper.DAOS.UserDAO_MySQL;
import com.example.ausias.ingotohelper.DAOS.Tutor;

/**
 * This AsynkTask do a diferent work, since check user LogIn to save new {@link Tutor} or {@link Fill} on the database and
 * depends of the result of this methods, load a new activity or call a new AsyncTask to obtain the Latitude and Longitude or
 * just inform with a Toast that the result of the method. The BackgroundWorker create a new Thread in addition to user interface
 * to prevent this crash.
 * This new Thread is responsible to make querys, inserts, updates or open a socket to connect from the server.
 * Created by Eduard on 25/03/2017.
 */

public class BackgroundWorker extends AsyncTask<Object,Void,String> {

    /**
     * Context from the activity that calls this method, it is necessary to make some actions
     * onPostExecute.
     */
    Context context;

    /**
     * Data Access Object from Tutor. {@link UserDAO_MySQL}
     */
    UserDAO_MySQL userDao;

    /**
     * Data Access Object from Fill. {@link FillDAO_MySQL}
     */
    FillDAO_MySQL fillDao;
    String result;

    BackgroundWorker(Context ctx){
        context = ctx;
        userDao = new UserDAO_MySQL();
        fillDao = new FillDAO_MySQL();
    }

    /**
     * This method is the New Thread, and it filter the action that to do. It receive as parameter a Array of Objects.
     * Depends of the first Object (String) that it receives, will perfom some actions or others.
     * @param params Array of Objects, in this it can receive since String to Object {@link Tutor}.
     * @return String with information about methods have worked or not and if worked a useful information to be treated.
     */
    @Override
    protected String doInBackground(Object... params) {
        String type = (String) params[0];
        if(type.equals("login")) {
            MyCipher c = new MyCipher();
            String user_email = (String) params[1];
            String password = (String) params[2];

            //Cipher the password received as a parameter to be compared with the database
            String cipherPasswd = c.cipher(password);
            result = userDao.logIn(user_email, cipherPasswd);
            if(result.contains("Logintrue")) {
                String[] values = result.split("/");

                //Get the number of children User from the userID received after call the method of userDAO.logIn(), on String result
                result += "/" + userDao.getNumChild(Integer.parseInt(values[1]));
            }
            return result;
        }
        else if(type.equals("register")){
            Tutor user = (Tutor) params[1];
            result =  userDao.saveTutor(user);
            result += "/"+ user.getDni() +"/"+ user.getCarrer() +"/"+ user.getCiutat() +"/"+ user.getCodiPostal();
            return result;
        }
        else if(type.equals("saveFill")){
            Fill child = (Fill) params[1];
            result =  fillDao.savefill(child);
            return result;
        }
        else if(type.equals("peticio")){
            int idSolicita = (Integer) params[1];
            int idAccepta = (Integer) params[2];
            TCPClient client = new TCPClient();
            return client.enviaPeticio(idSolicita, idAccepta);
        }
        else if(type.equals("getNotifications")){
            int idUser = (Integer) params[1];
            TCPClient client = new TCPClient();
            return client.getNotifications(idUser);
        }
        else if(type.equals("accepta")) {
            int idAccepta = (int) params[1];
            int idSolicita = (int) params[2];
            TCPClient client = new TCPClient();
            return client.acceptRequest(idAccepta,idSolicita);
        }
        return null;
    }

    /**
     * This method is executed on User Interface and is responsible to open new Activities, show Toasts or call a new AsyncTask.
     * Most times it opens a new activity on the intent put extra information of the actual user.
     * @param result String with the result of the methods executed on the other Thread, it can bring Error message or Successful message with useful information.
     */
    @Override
    protected void onPostExecute(String result) {
        //In this case it looks if user has registered a child. If not have any child, it open the Activity GestioFills, else it open MainActivity.
       if(result.contains("Logintrue")) {
            String[] values = result.split("/");
            if(values[3].contains("0")) {
                Intent intent = new Intent(context, GestioFillsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("id", Integer.parseInt(values[1]));
                context.startActivity(intent);
            }
            else{
                Intent intent = new Intent(context, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("id", Integer.parseInt(values[1]));
                context.startActivity(intent);
                Toast.makeText(context, "Welcome : " + values[2], Toast.LENGTH_LONG).show();
            }
       }
       else if(result.contains("Loginfalse")) {
           Toast.makeText(context, "Log In not Succes..", Toast.LENGTH_LONG).show();
       }
       //If contains done it means that the user has successfully registered. And it execute a new AsyncTask with the data of user live to obtain the coordinates
       else if(result.contains("done")){
            String[] values = result.split("/");
            new GeoCoderTask().execute(values[1], values[2], values[3], values[4]);
            Intent intent = new Intent(context, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
       }
       else if(result.contains("Petitions")){
           String[] values = result.split("/");
           for (String id: values) {
               if(!id.contains("Petitions")){
                   MainActivity.petitions.add(Integer.parseInt(id));
               }
           }
       }
       else{
           Toast.makeText(context, result, Toast.LENGTH_LONG).show();
       }
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
}
