package com.example.shahzad.ridesafepk;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class Register extends AppCompatActivity implements View.OnClickListener{

    EditText etName, etEmail, etPassword, etPhone, etNic, etUserType;
    TextView tvImage;
    Button btnRegister, btnLogin;
    ProgressDialog pDialog;
    User user;

    private static final int RESULT_LOAD_IMAGE_FROM_GALLERY = 1;
    private static final int RESULT_LOAD_IMAGE_FROM_CAMERA = 2;

    ImageView imageToUpload;
    Uri selectedImage;
    Bitmap imageFromCamera;
    Bitmap photo;



    private Uri fileUri;
    String picturePath;


    private static final int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    AlertDialog.Builder builder;
    final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        etName = (EditText) findViewById(R.id.etName);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);
        etPhone = (EditText) findViewById(R.id.etPhone);
        etNic = (EditText) findViewById(R.id.etNic);

        imageToUpload  = (ImageView) findViewById(R.id.imageToUpload);

        tvImage = (TextView) findViewById(R.id.tvImage);
        tvImage.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){

                selectImage();
            }
        });

        btnRegister = (Button) findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(this);

        // btnLogin = (Button) findViewById(R.id.btnLogin);
        // btnLogin.setOnClickListener(this);
    }


    @Override
    protected void onStart() {
        super.onStart();
        if (AppStatus.getInstance(this).isOnline()) {

        } else {
            Toast.makeText(this, "You are not online!!!!", Toast.LENGTH_LONG).show();
        }
    }



    private void selectImage() {
        final CharSequence[] items = { "Take Photo", "Choose from Gallery", "Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(Register.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                    startActivityForResult(intent, REQUEST_CAMERA);
                } else if (items[item].equals("Choose from Gallery")) {
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(
                            Intent.createChooser(intent, "Select File"),
                            SELECT_FILE);
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CAMERA) {
                Bitmap thumbnail = (Bitmap) data.getExtras().get("data");

                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

                File destination = new File(Environment.getExternalStorageDirectory(),
                        System.currentTimeMillis() + ".jpg");

                FileOutputStream fo;
                try {
                    destination.createNewFile();
                    fo = new FileOutputStream(destination);
                    fo.write(bytes.toByteArray());
                    fo.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                imageToUpload.setImageBitmap(thumbnail);

            } else if (requestCode == SELECT_FILE) {
                selectedImage = data.getData();
                String[] projection = {MediaStore.MediaColumns.DATA};
                CursorLoader cursorLoader = new CursorLoader(this, selectedImage, projection, null, null,
                        null);


                Cursor cursor = cursorLoader.loadInBackground();
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
                cursor.moveToFirst();

                String selectedImagePath = cursor.getString(column_index);

                Bitmap bm;
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(selectedImagePath, options);
                final int REQUIRED_SIZE = 200;
                int scale = 1;
                while (options.outWidth / scale / 2 >= REQUIRED_SIZE
                        && options.outHeight / scale / 2 >= REQUIRED_SIZE)
                    scale *= 2;
                options.inSampleSize = scale;
                options.inJustDecodeBounds = false;
                bm = BitmapFactory.decodeFile(selectedImagePath, options);

                imageToUpload.setImageBitmap(bm);
            }
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btnRegister:
                String name     = etName.getText().toString();
                String email    = etEmail.getText().toString();
                String password = etPassword.getText().toString();
                String phone    = etPhone.getText().toString();
                String nic      = etNic.getText().toString();
                String userType = "";
                RadioButton passenger = (RadioButton) findViewById(R.id.radio_passenger);
                RadioButton driver = (RadioButton) findViewById(R.id.radio_driver);
                String image = "55akasdfadphpoijpiojasdfasdfasdfasdfasdfasdfasdfasdf";

                if( imageToUpload.getDrawable() != null){

                   Bitmap bitmapImage = ((BitmapDrawable) imageToUpload.getDrawable()).getBitmap();
                    if(bitmapImage !=null)
                    {
                        image = User.getEncoded64ImageStringFromBitmap(bitmapImage);
                    }
                    else{
                        image = "55akasdfadphpoijpiojasdfasdfasdfasdfasdfasdfasdfasdf";
                    }
                }


                if(passenger.isChecked()){
                    userType = "Passenger";
                }
                else if (driver.isChecked()){
                    userType = "Driver";
                }

                if(name.equals("")) {
                    Toast.makeText(getApplicationContext(), "Please enter your name", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(!User.isValidEmail(email)){
                    Toast.makeText(getApplicationContext(),"Please enter valid email address",Toast.LENGTH_SHORT).show();
                    return;
                }
                else if (password.equals("")) {
                    Toast.makeText(getApplicationContext(), "Please enter password", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(phone.equals("")) {
                    Toast.makeText(getApplicationContext(), "Please enter phone", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(!User.isValidPhoneNumber(phone)) {
                    Toast.makeText(getApplicationContext(), "Please enter valid phone number, > 6 and less than 13", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(nic.equals("")) {
                    Toast.makeText(getApplicationContext(), "Please enter NIC Number", Toast.LENGTH_SHORT).show();
                    return;
                }
               // else if(!User.isValidNicNumber(nic)){
                 //   Toast.makeText(getApplicationContext(), "Valid NIC number required, xxxxx-xxxxxxx-x", Toast.LENGTH_SHORT).show();
                   // return;
                //}
                else if (userType.equals("")) {
                    Toast.makeText(getApplicationContext(), "Please select user type", Toast.LENGTH_SHORT).show();
                    return;
                }
                else {
                    user = new User(name, email, password, phone, nic, userType, image);
                    new RegisterUser(user).execute();
                }
                break;

            case R.id.btnLogin:
                startActivity(new Intent(this, Login.class));
                break;

            }
    }


    public  class RegisterUser extends AsyncTask<Void, Void, User> {
        User user;

        public  RegisterUser(User u)
        {
            this.user = u;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Register.this);
            pDialog.setMessage("Creating Account...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected  User doInBackground(Void... params){

            User returnUser = null;

            try {
                WebserviceHandler service = new WebserviceHandler(getApplicationContext());
                User result = service.RegisterPost(getApplicationContext(), user);
               //User result = service.Register(getApplicationContext(), user);
                if(result!=null) {
                    returnUser = new User(result.id, result.name, result.email, result.password, result.phone, result.nic, result.userType, result.street, result.city, result.country,result.lat, result.lng, result.is_login, result.is_vehicle_added, result.reg_id,result.isError, result.errorMessage, result.image);
                }
            }catch (IOException e) {
                e.printStackTrace();
            }

            return  returnUser;
        }

        @Override
        protected void onPostExecute(User user) {
            pDialog.dismiss();
            goToNextActivityRegister(user);
            super.onPostExecute(user);

        }
    }

    public void goToNextActivityRegister(User user){
        Log.d("return user", user + "");

        if(user == null){
            Toast.makeText(getApplicationContext(),"Error creating user",Toast.LENGTH_SHORT).show();
        }
        else if(user.isError == 1){
            Toast.makeText(getApplicationContext(),user.errorMessage,Toast.LENGTH_SHORT).show();
        }
        else if(user.isError == 0) {
            User.IsLoggedIn = true;
            User.loggedInUserId = user.id;
            User.loggedInUserType = user.userType;
            startActivity(new Intent(this,AddAddress.class));
        }
        else{
            Toast.makeText(getApplicationContext(),"Network Error", Toast.LENGTH_LONG).show();
        }
    }





}
