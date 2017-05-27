package com.example.ausias.ingotohelper;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.example.ausias.ingotohelper.DAOS.Tutor;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class RegisterActivity extends AppCompatActivity {
    /**
     * EditText's of this layout
     */
    EditText nom, cognom1, cognom2, dni, ciutat, carrer, codiPostal, telefon, email, password;

    Bitmap foto , prova;
    /**
     * ImageView of this layout
     */
    ImageView photo = null;
    static int SELECT_IMAGE=0;

    /**
     * **OnCreate()**
     * Initialize the EditTexts and ImageView of this class
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        nom = (EditText) findViewById(R.id.nomText);
        cognom1 = (EditText) findViewById(R.id.cognomText);
        cognom2 = (EditText) findViewById(R.id.cognom2Text);
        dni = (EditText) findViewById(R.id.dniText);
        ciutat = (EditText) findViewById(R.id.ciutatText);
        carrer = (EditText) findViewById(R.id.carrerText);
        codiPostal = (EditText) findViewById(R.id.codipostalText);
        telefon = (EditText) findViewById(R.id.telefonText);
        email = (EditText) findViewById(R.id.correuText);
        password = (EditText) findViewById(R.id.PasswordText);
        photo = (ImageView) findViewById(R.id.Photo);
    }

    /**
     * Method that is executed when user 'click' the button "Upload Photo", open one activty to choose any photo
     * @param v ???
     */
    public void onClickPhoto(View v) {
        Intent openGalleryIntent = new Intent();
        openGalleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        openGalleryIntent.setType("image/*");
        startActivityForResult(Intent.createChooser(openGalleryIntent, "Select Picture"),SELECT_IMAGE);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == SELECT_IMAGE) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                if (data != null)
                {
                    try
                    {
                        foto = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
                        prova = BitmapFactory.decodeFile(data.getData().toString());
                        photo.setImageBitmap(foto);
                    } catch (IOException e)
                    {
                        e.printStackTrace();
                    }

                }
            } else if (resultCode == RESULT_CANCELED)
            {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show();
            }
        }
    }
    public String getPath(Uri uri)
    {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor == null) return null;
        int column_index =  cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String s=cursor.getString(column_index);
        cursor.close();
        return s;
    }

    /**
     * Create the String values of thes EditTexts, pass the password to MyCipher,
     * create a byte[] with the "compress" photo , and executes backgroundWorker() with a new Instance of Tutor with all this data.
     * @param v ????
     */
    public void saveRegister(View v){
        byte[] fotoBlob = null;
        MyCipher c = new MyCipher();
        String str_nom = nom.getText().toString();
        String str_cog1 = cognom1.getText().toString();
        String str_cog2 = cognom2.getText().toString();
        String str_dni = dni.getText().toString();
        String str_ciutat = ciutat.getText().toString();
        String str_carrer = carrer.getText().toString();
        String str_codiPostal = codiPostal.getText().toString();
        String str_telefon = telefon.getText().toString();
        String str_email = email.getText().toString();
        String str_password = c.cipher(password.getText().toString());
        if(foto!=null){
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            foto.compress(Bitmap.CompressFormat.PNG, 100, bos);
            fotoBlob = bos.toByteArray();
        }
        String type = "register";
        BackgroundWorker backgroundWorker = new BackgroundWorker(this);
        backgroundWorker.execute(type, new Tutor(0,str_nom, str_cog1, str_cog2, str_dni, str_ciutat, str_carrer, str_codiPostal, str_telefon, str_email, str_password, fotoBlob));
    }
}
