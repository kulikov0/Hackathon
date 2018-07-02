package com.example.root.photogoal.ui;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.root.photogoal.Application.PhotoGoalApplication;
import com.example.root.photogoal.R;
import com.example.root.photogoal.response.UploadImageResponse;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import dmax.dialog.SpotsDialog;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final String MY_SETTINGS = "my_settings";

    String USER_DATA = "user_data";
    int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    Button btnSelect;
    ImageView userImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnSelect =  findViewById(R.id.btnSelectPhoto);
        btnSelect.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
        userImage = findViewById(R.id.ivImage);


        int Permission_All = 1;
        String[] Permissions = {
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.CAMERA };
        if(!hasPermissions(this, Permissions)){
            ActivityCompat.requestPermissions(this, Permissions, Permission_All);
        }


        SharedPreferences isUserLogged = this.getSharedPreferences(MY_SETTINGS, MODE_PRIVATE);
        String logged = isUserLogged.getString("logged", null);

        if (logged == null) {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        }
    }

    public static boolean hasPermissions(Context context, String... permissions){

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M && context!=null && permissions!=null){
            for(String permission: permissions){
                if(ActivityCompat.checkSelfPermission(context, permission)!= PackageManager.PERMISSION_GRANTED){
                    return  false;
                }
            }
        }
        return true;
    }


    private void selectImage() {
        final CharSequence[] items = {"Make Photo", "Choose from gallery",
                "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Choose photo");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Make Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQUEST_CAMERA);
                } else if (items[item].equals("Choose from gallery")) {
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(
                            Intent.createChooser(intent, "Choose file"),
                            SELECT_FILE);
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }


    }


    private void onCaptureImageResult(Intent data) {
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

        final SpotsDialog.Builder dlg = new SpotsDialog.Builder()
                .setContext(this)
                .setMessage("Please wait");
        final RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), destination);
        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file", destination.getName(), requestBody);
        RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), destination.getName());
        final String user_id = getSharedPreferences(USER_DATA, Context.MODE_PRIVATE).getString("id", "");
        final String session_id = getSharedPreferences(USER_DATA, Context.MODE_PRIVATE).getString("session", "");

        ((PhotoGoalApplication)getApplication()).provideUploadImageService()
                .sendPhoto(fileToUpload, filename, user_id, session_id)
                .enqueue(new Callback<UploadImageResponse>() {
                    @Override
                    public void onResponse(Call<UploadImageResponse> call, Response<UploadImageResponse> response) {
                        if (response.isSuccessful()) {
                            UploadImageResponse body = response.body();
                            Picasso.get().load("http://photogoal.cf" + body.getLink()).into(userImage);
                            
                        }


                    }

                    @Override
                    public void onFailure(Call<UploadImageResponse> call, Throwable t) {
                    }
                });

    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {
        Uri selectedImageUri = data.getData();
        String[] projection = {MediaStore.MediaColumns.DATA};
        Cursor cursor = managedQuery(selectedImageUri, projection, null, null,
                null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        cursor.moveToFirst();

        String selectedImagePath = cursor.getString(column_index);

        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        Bitmap bm;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(selectedImagePath, options);
        final int REQUIRED_SIZE = 800;
        int scale = 1;
        while (options.outWidth / scale / 2 >= REQUIRED_SIZE
                && options.outHeight / scale / 2 >= REQUIRED_SIZE)
            scale *= 2;
        options.inSampleSize = scale;
        options.inJustDecodeBounds = false;
        bm = BitmapFactory.decodeFile(selectedImagePath, options);

        bm.compress(Bitmap.CompressFormat.JPEG, 0, bos);

        File file = new File(selectedImagePath);
        RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), file);
        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
        RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), file.getName());
        final SpotsDialog.Builder dlg = new SpotsDialog.Builder()
                .setContext(this)
                .setMessage("Please wait");
        final String user_id = getSharedPreferences(USER_DATA, Context.MODE_PRIVATE).getString("id", "");
        final String session_id = getSharedPreferences(USER_DATA, Context.MODE_PRIVATE).getString("session", "");
        dlg.build().show();
        ((PhotoGoalApplication)getApplication()).provideUploadImageService()
                .sendPhoto(fileToUpload, filename, user_id, session_id)
                .enqueue(new Callback<UploadImageResponse>() {
                    @Override
                    public void onResponse(Call<UploadImageResponse> call, Response<UploadImageResponse> response) {
                        if (response.isSuccessful()) {
                            UploadImageResponse body = response.body();
                            Picasso.get().load(body.getLink()).into(userImage);
                            dlg.build().dismiss();
                        }

                    }

                    @Override
                    public void onFailure(Call<UploadImageResponse> call, Throwable t) {

                    }
                });
    }
}
