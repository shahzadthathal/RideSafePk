package com.example.shahzad.ridesafepk;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.provider.MediaStore;
import android.widget.Toast;

/**
 * Created by shahzad on 1/23/2016.
 */
public abstract class ImageUploader {



    public  abstract void imageFromCamera();
    public  abstract void imageFromGallery();

}
