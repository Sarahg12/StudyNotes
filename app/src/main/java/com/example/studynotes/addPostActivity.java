package com.example.studynotes;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class addPostActivity extends AppCompatActivity {

        EditText fname, title, text;
        Button submitButton;
        ImageView img_logo;
        Button btnup;
        private Uri fileUri;
        String picturePath;
        Uri selectedImage;
        Bitmap photo;
        String ba1;
        public static String URL = "Paste your URL here";
        private File imageFile;
        boolean isSelected = false;

    protected static final int INTENT_CAMERA = 401;
        protected static final int INTENT_GALLERY = 301;
        protected static final int PERMISSION_REQUEST_CAMERA_GALLERY = 101;

    @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_add_post);

            fname = findViewById(R.id.user_name);
            title = findViewById(R.id.post_title);
            text = findViewById(R.id.post_text);

            submitButton = findViewById(R.id.submit_button);

            submitButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //TODO
                }
            });

            text.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    switch (event.getAction() & MotionEvent.ACTION_MASK){
                        case MotionEvent.ACTION_UP:
                            v.getParent().requestDisallowInterceptTouchEvent(false);
                            break;
                    }
                    return false;
                }
            });




        img_logo= (ImageView) findViewById(R.id.Imageprev);

        btnup = (Button) findViewById(R.id.upload);
        btnup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                galleryIntent();
            }
        });


        }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==PERMISSION_REQUEST_CAMERA_GALLERY){
            if(grantResults.length>0)
                if(grantResults[0]== getPackageManager().PERMISSION_GRANTED){
                    galleryIntent();
                }//end inner if
        }//end outer if

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK)
            if (requestCode == INTENT_GALLERY) {
                try{
                    final Uri imageUri=data.getData();
                    final InputStream imageStream = getContentResolver()
                            .openInputStream(imageUri);
                    final Bitmap selectedImage= BitmapFactory.decodeStream(imageStream);
                    Glide.with(this).load(selectedImage).apply(RequestOptions.centerCropTransform()).into(img_logo);
                    // img_profile.setImageBitmap(selectedImage);
                    isSelected=true;
                    fromBitmapToFile(selectedImage);
                }//end try
                catch (FileNotFoundException e){
                    e.printStackTrace();
                }//end catch
            }//end if
    }//end of onActivityResult()

    private void fromBitmapToFile(Bitmap bitmap) {
        File filesDir = getFilesDir();
        String name1="";
        imageFile =new File(filesDir, "atheer.png");
        OutputStream os;
        try{
            os=new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.PNG,100 ,os);
            os.flush();
            os.close();
        }//end try
        catch (Exception e){
            Log.e(getClass().getSimpleName(),"Error writing bitmap",e);
        }//end catch
    }//end fromBitmapToFile


    private void galleryIntent() {
        Intent photoPickerPhoto = new Intent(Intent.ACTION_PICK);
        photoPickerPhoto.setType("image/*");
        startActivityForResult(photoPickerPhoto, INTENT_GALLERY);
    }//end galleryIntent

















    ////////////////////////

    private void upload() {
        // Image location URL
        Log.e("path", "----------------" + picturePath);

        // Image
        Bitmap bm = BitmapFactory.decodeFile(picturePath);
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 90, bao);
        byte[] ba = bao.toByteArray();
        //ba1 = Base64.encodeBytes(ba);

        Log.e("base64", "-----" + ba1);

        // Upload image to server
        //TODO
//        new uploadToServer().execute();

    }

    private void clickpic() {
        // Check Camera
        if (getApplicationContext().getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA)) {
            // Open default camera
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

            // start the image capture Intent
            startActivityForResult(intent, 100);

        } else {
            Toast.makeText(getApplication(), "Camera not supported", Toast.LENGTH_LONG).show();
        }
    }

}








//    public class uploadToServer extends AsyncTask<Void, Void, String> {
//
//        private ProgressDialog pd = new ProgressDialog(MainActivity.this);
//        protected void onPreExecute() {
//            super.onPreExecute();
//            pd.setMessage("Wait image uploading!");
//            pd.show();
//        }
//
//        @Override
//        protected String doInBackground(Void... params) {
//
//            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
//            nameValuePairs.add(new BasicNameValuePair("base64", ba1));
//            nameValuePairs.add(new BasicNameValuePair("ImageName", System.currentTimeMillis() + ".jpg"));
//            try {
//                HttpClient httpclient = new DefaultHttpClient();
//                HttpPost httppost = new HttpPost(URL);
//                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
//                HttpResponse response = httpclient.execute(httppost);
//                String st = EntityUtils.toString(response.getEntity());
//                Log.v("log_tag", "In the try Loop" + st);
//
//            } catch (Exception e) {
//                Log.v("log_tag", "Error in http connection " + e.toString());
//            }
//            return "Success";
//
//        }
//
//        protected void onPostExecute(String result) {
//            super.onPostExecute(result);
//            pd.hide();
//            pd.dismiss();
//        }
//    }
//}