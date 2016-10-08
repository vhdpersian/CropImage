package com.example.ui.uitemplate;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import java.util.Locale;

import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;

public class CrpActivity extends AppCompatActivity {

    Button btnCapture;
    Button btnGallery;
    Button btnChooser;
    //keep track of camera capture intent
    final int CAMERA_CAPTURE = 1;
    final int PICK_FROM_FILE = 2;
    final int CROP_FROM_CAMERA = 3;
    private Uri picUri;
    private static Context context;
    Uri uriFile;
    ImageView imgCrop;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            if (requestCode == CAMERA_CAPTURE) {
                doCrop(uriFile);
            }

            if (requestCode == PICK_FROM_FILE) {
                uriFile=data.getData();
                String pathFile=ImageFilePath.getPath(CrpActivity.this,uriFile);
                File f=new File(pathFile);
                Uri uri= Uri.fromFile(f);
                doCrop(uri);

            }
            if (requestCode == CROP_FROM_CAMERA) {

                Uri imageUri = data.getData();
                try {

                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                    imgCrop.setImageBitmap(bitmap);
                    File file = new File(uriFile.getPath());
                    if (file.exists()) {
                        file.delete();
                    }
                } catch (Exception e) {

                    return;

                }

            }

        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crp);
        context = CrpActivity.this;

        btnCapture = (Button) findViewById(R.id.btnCapture);
        btnGallery = (Button) findViewById(R.id.btnGallery);
        imgCrop = (ImageView) findViewById(R.id.imgCrop);
        btnChooser = (Button) findViewById(R.id.btnChooser);

        btnGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent pickImageIntent = new Intent();
                pickImageIntent.setType("image/*");
                pickImageIntent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(pickImageIntent, "Complete action using"), PICK_FROM_FILE);
            }
        });

        btnCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (captureIntent.resolveActivity(getPackageManager()) != null) {
                    File photoFile = null;

                    try {
                        photoFile = createImageFile();
                        uriFile = Uri.fromFile(photoFile);

                    } catch (IOException ex) {
                        return;
                    }

                    if (photoFile != null && uriFile != null) {

                        captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriFile);
                        captureIntent.putExtra("return-data", true);
                        startActivityForResult(captureIntent, CAMERA_CAPTURE);
                    }
                }

            }

        });


        btnChooser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent pickIntent = new Intent(Intent.ACTION_PICK);
                pickIntent.setType("image/*");

                Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                String pickTitle = "Select or take a new Picture"; // Or get from strings.xml
                Intent chooserIntent = Intent.createChooser(pickIntent, pickTitle);
                chooserIntent.putExtra
                        (Intent.EXTRA_INITIAL_INTENTS, new Intent[]{takePhotoIntent});
               startActivityForResult(chooserIntent,12);
            }


        });
    }

    public String getRealPathFromURI(Uri contentURI, Activity context) {
        String[] projection = {MediaStore.Images.Media.DATA};
        @SuppressWarnings("deprecation")
        Cursor cursor = context.managedQuery(contentURI, projection, null,
                null, null);
        if (cursor == null)
            return null;
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        if (cursor.moveToFirst()) {
            String s = cursor.getString(column_index);
            // cursor.close();
            return s;
        }
        // cursor.close();
        return null;
    }

    private File createImageFile() throws IOException {
        String mCurrentPhotoPath;

        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }


    private void doCrop(Uri mImageCaptureUri) {
        // final ArrayList<CropOption> cropOptions = new ArrayList<CropOption>();

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setType("image/*");

        List<ResolveInfo> list = getPackageManager().queryIntentActivities(intent, 0);

        int size = list.size();

        if (size == 0) {
            Toast.makeText(this, "Can not find image crop app", Toast.LENGTH_SHORT).show();
            return;
        }

        intent.setData(mImageCaptureUri);

        intent.putExtra("outputX", 200);
        intent.putExtra("outputY", 200);
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("scale", true);
        intent.putExtra("return-data", true);

        if (size == 1) {
            Intent i = new Intent(intent);
            ResolveInfo res = list.get(0);

            i.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));

            startActivityForResult(i, CROP_FROM_CAMERA);
        }

    }

}
