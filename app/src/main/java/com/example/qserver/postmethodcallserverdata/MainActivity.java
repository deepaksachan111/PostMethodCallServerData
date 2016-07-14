package com.example.qserver.postmethodcallserverdata;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;

import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    String responce ;
    private ImageView image;
    private Button uploadButton;
    private Bitmap bitmap;
    private Button selectImageButton;
    private TextView textView_imagepath;
    String picturePath;

    // number of images to select
    private static final int PICK_IMAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // find the views
        image = (ImageView) findViewById(R.id.uploadImage);
        uploadButton = (Button) findViewById(R.id.uploadButton);
        textView_imagepath = (TextView) findViewById(R.id.txtview_imagepath);

        // on click select an image
        selectImageButton = (Button) findViewById(R.id.selectImageButton);
        selectImageButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                selectImageFromGallery();

            }
        });

        final String url = "http://shine.quaeretech.com/ShineCityInfra.svc/Login/cfqc251/50U867";
        // when uploadButton is clicked
        uploadButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // new DownlineRecordDisplayAsyncTask().execute("http://clients.aksinteractive.com/PlanningWale/signUp.php");
                http://api.androidhive.info/contacts/


                new DownlineRecordDisplayAsyncTask().execute(url);
            }
        });
    }


    //

    public void selectImageFromGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"),
                PICK_IMAGE);
    }

    /**
     * Retrives the result returned from selecting image, by invoking the method
     * <code>selectImageFromGallery()</code>
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK
                && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            picturePath = cursor.getString(columnIndex);
            cursor.close();
            textView_imagepath.setText(picturePath);
            decodeFile(picturePath);

        }
    }

    /**
     * The method decodes the image file to avoid out of memory issues. Sets the
     * selected image in to the ImageView.
     *
     * @param filePath
     */
    public void decodeFile(String filePath) {
        // Decode image size
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, o);

        // The new size we want to scale to
        final int REQUIRED_SIZE = 1024;

        // Find the correct scale value. It should be the power of 2.
        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;
        while (true) {
            if (width_tmp < REQUIRED_SIZE && height_tmp < REQUIRED_SIZE)
                break;
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }

        // Decode with inSampleSize
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        bitmap = BitmapFactory.decodeFile(filePath, o2);

        image.setImageBitmap(bitmap);
    }


    private class DownlineRecordDisplayAsyncTask extends AsyncTask<String, Void, String> {


        private static final int REGISTRATION_TIMEOUT = 10 * 1000;
        private static final int WAIT_TIMEOUT = 50 * 1000;
        private final HttpClient httpclient = new DefaultHttpClient();
        final HttpParams params = httpclient.getParams();

        private String content = null;
        private boolean error = false;
        private ProgressDialog dialog = new ProgressDialog(MainActivity.this);

        protected void onPreExecute() {
            dialog.setMessage("Getting your data... Please wait...");
            dialog.setCancelable(true);
            dialog.show();
        }

        protected String doInBackground(String... urls) {

            String URL = null;

            postMethod("http://clients.aksinteractive.com/PlanningWale/signUp.php");

            //getMethod(urls[0]);

            return responce;
        }


        protected void onCancelled() {
            dialog.dismiss();
            Toast.makeText(MainActivity.this, "Connection Server Failed", Toast.LENGTH_LONG).show();

        }

        protected void onPostExecute(String content) {
            dialog.dismiss();

            if (error) {
                Toast.makeText(MainActivity.this, content, Toast.LENGTH_LONG).show();


                //displayCountryList(content);
            }else{

                try {
                    JSONObject jsonObject = new JSONObject(content);

                    JSONArray jsonArray = jsonObject.getJSONArray("contacts");
                    JSONObject jsonObject1 = jsonArray.getJSONObject(3);
                    JSONObject jsonPhone = jsonObject1.getJSONObject("phone");


                    String aa = jsonObject1.getString("id");
                    String aa2 = jsonObject1.getString("name");
                    String aa3 = jsonObject1.getString("email");
                    String aa4 = jsonObject1.getString("address");
                    String aa5 = jsonObject1.getString("gender");
                    String getphobne = jsonPhone.getString("mobile");
                    String gethome = jsonPhone.getString("home");
                    String office = jsonPhone.getString("office");




                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        }


    }

    public void getMethod( String url){


        HttpClient httpClient = new DefaultHttpClient();

        HttpGet httpGet = new HttpGet(url);

        try {
            HttpResponse httpResponse = httpClient.execute(httpGet);
            HttpEntity httpEntity = httpResponse.getEntity();
            responce = EntityUtils.toString(httpEntity);


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public String postMethod(String url){

        HttpResponse httpResponse = null;
        HttpClient httpClient = new DefaultHttpClient();
        // replace with your url
        HttpPost httpPost = new HttpPost(url);


        //Post Data
        List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
        nameValuePair.add(new BasicNameValuePair("type", "name"));
        nameValuePair.add(new BasicNameValuePair("deviceId", "dee@gmail.com"));
        nameValuePair.add(new BasicNameValuePair("userName", "test_user"));
        nameValuePair.add(new BasicNameValuePair("email", "deegggggggggggggggggggg@gmail.com"));
        nameValuePair.add(new BasicNameValuePair("phoneNumber", "123456789"));
        nameValuePair.add(new BasicNameValuePair("password", "1234567890"));
        nameValuePair.add(new BasicNameValuePair("profilePic", "picturePath"));




        //Encoding POST data
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));
        } catch (UnsupportedEncodingException e) {
            // log exception
            e.printStackTrace();
        }

        //making POST request.
        try {
             httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            responce = EntityUtils.toString(httpEntity);
            // write response to log
            Log.d("Http Post Response:", responce.toString());
        } catch (ClientProtocolException e) {
            // Log exception
            e.printStackTrace();
        } catch (IOException e) {
            // Log exception
            e.printStackTrace();
        }

        return responce ;
    }


}
/*

Escape Sequences
Escape Sequence Description
        \t  Insert a tab in the text at this point.
        \b  Insert a backspace in the text at this point.
        \n  Insert a newline in the text at this point.
        \r  Insert a carriage return in the text at this point.
        \f  Insert a formfeed in the text at this point.
        \'  Insert a single quote character in the text at this point.
        \"  Insert a double quote character in the text at this point.
        \\  Insert a backslash character in the text at this point.*/
