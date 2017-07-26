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
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.nbsp.materialfilepicker.ui.FilePickerActivity;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ImageActivity extends AppCompatActivity {
    Bundle bundle;
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 10000;
    public Button uploadButton;
    public ImageView imageDisplay;
    String selectedPath;

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
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        imageDisplay = (ImageView) findViewById(R.id.image_display);
        if (resultCode == RESULT_OK) {
            Uri selectedImage;

            if (requestCode == 999) {
                selectedImage = data.getData();
                selectedPath = getRealPath(selectedImage);
                Toast.makeText(this, "Selected Path: " + selectedPath, Toast.LENGTH_LONG).show();
                Picasso.with(getApplicationContext()).load(selectedImage).fit().centerCrop().into(imageDisplay);
                //uploadImage(image);
            }
        }

        Button next = (Button) findViewById(R.id.next_button);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newActivityintent = new Intent(ImageActivity.this, DetailsActivity.class);
                bundle.putString("imagePath", selectedPath);
                newActivityintent.putExtras(bundle);
                startActivity(newActivityintent);

/*                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        File f = new File(data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH));
                        String content_type = getMimeType(f.getPath());

                        String file_path = f.getAbsolutePath();
                        OkHttpClient client = new OkHttpClient();
                        RequestBody fileBody = RequestBody.create(MediaType.parse(content_type),f);

                        RequestBody requestBody = new MultipartBody.Builder()
                                .setType(MultipartBody.FORM)
                                .addFormDataPart("Type", content_type)
                                .addFormDataPart("uploaded_file",file_path.substring(file_path.lastIndexOf('/') + 1), fileBody)
                                .build();

                        Request request = new Request.Builder()
                                .url("http://45.79.76.22:9080/EasyRentals/image/upload")
                                .post(requestBody)
                                .build();


                        try {
                            Response response = client.newCall(request).execute();

                            if (!response.isSuccessful()){
                                throw new IOException("Error:"+response);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });

                t.start();*/
            }
        });
    }

    private String getRealPath(Uri selectedImage) {
        return ImageFilePath.getPath(this, selectedImage);
    }

    private String getMimeType(String path) {
        String extension = MimeTypeMap.getFileExtensionFromUrl(path);
        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
    }

}
