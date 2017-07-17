package com.example.csci567.dailyrentals;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ImageActivity extends AppCompatActivity {
    Bundle bundle;
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 10000;
    public Button uploadButton;
    public ImageView imageDisplay;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        bundle = getIntent().getExtras();

        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (shouldShowRequestPermissionRationale(
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                // Explain to the user why we need to read the contacts
            }

            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);

            // MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE is an
            // app-defined int constant that should be quite unique

        }

        uploadButton = (Button) findViewById(R.id.upload_button);
        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                Intent chooserIntent = Intent.createChooser(intent,"Select picture to upload");
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{takePicture});
                startActivityForResult(chooserIntent,999);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String selectedPath;
        Bitmap image = null;
        imageDisplay = (ImageView) findViewById(R.id.image_display);
        if (resultCode == RESULT_OK) {
            Uri selectedImage = null;

            if (requestCode == 999) {
                selectedImage = data.getData();
                selectedPath = getPath(selectedImage);
                Toast.makeText(this, "Selected Path: " + selectedPath, Toast.LENGTH_LONG).show();


                try {
                    image = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Picasso.with(getApplicationContext()).load(selectedImage).fit().centerCrop().into(imageDisplay);
                //uploadImage(image);
            }
        }

        Button next = (Button) findViewById(R.id.next_button);
        final Bitmap finalImage = image;
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newActivityintent = new Intent(ImageActivity.this, DetailsActivity.class);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                finalImage.compress(Bitmap.CompressFormat.JPEG, 25, baos);
                byte[] imageBytes = baos.toByteArray();
                String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
                bundle.putString("bitmap", encodedImage);
                newActivityintent.putExtras(bundle);
                startActivity(newActivityintent);
            }
        });

    }

    private String getPath(Uri selectedImage) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(selectedImage, projection, null, null, null);
        int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(columnIndex);
    }
}
