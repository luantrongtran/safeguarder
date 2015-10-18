package ifn701.safeguarder.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import ifn701.safeguarder.CustomSharedPreferences.UserInfoPreferences;
import ifn701.safeguarder.GPSTracker;
import ifn701.safeguarder.MapsActivity;
import ifn701.safeguarder.R;
import ifn701.safeguarder.backend.myApi.model.Accident;
import ifn701.safeguarder.entities.MyOnItemSelectedListener;
import ifn701.safeguarder.entities.LoadImage;
import ifn701.safeguarder.webservices.AccidentService;
import ifn701.safeguarder.webservices.IAccidentService;

public class ReportActivity extends AppCompatActivity implements IAccidentService{

    private static final int SELECT_SINGLE_PICTURE = 101;
    public static final String IMAGE_TYPE = "image/*";
    private ImageView selectedImagePreview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        TextView reportNewAccidentPageTitle = (TextView) findViewById(R.id.report_page_title);
        reportNewAccidentPageTitle.setText(R.string.report_new_accident_page_title);

        long l = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Date date = new Date(l);
        String str = sdf.format(date);

        EditText currentTime = (EditText) findViewById(R.id.text_time);
        currentTime.setText(str);

        GPSTracker GPSTracker = new GPSTracker(getApplicationContext());
        EditText displayLocation = (EditText) findViewById(R.id.text_location);
        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(GPSTracker.getLatitude(),
                    GPSTracker.getLongitude(), 1);
            if(addresses != null & addresses.size() > 0) {
                Address address = addresses.get(0);
                displayLocation.setText(address.getAddressLine(0));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Spinner spinner_obslvl = (Spinner) findViewById(R.id.spinner_obslvl);
        Spinner spinner_accType = (Spinner) findViewById(R.id.spinner_accType);

        spinner_obslvl.setOnItemSelectedListener(new MyOnItemSelectedListener());
        spinner_accType.setOnItemSelectedListener(new MyOnItemSelectedListener());
    }

    public void setImageViewOne(View view) {
        selectedImagePreview = (ImageView) findViewById(R.id.btn_image1);
        createIntent();
    }

    public void setImageViewTwo(View view) {
        selectedImagePreview = (ImageView) findViewById(R.id.btn_image2);
        createIntent();
    }

    public void setImageViewThree(View view) {
        selectedImagePreview = (ImageView) findViewById(R.id.btn_image3);
        createIntent();
    }

    public void goBack(View view){
        Intent intent = new Intent(this, MapsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP);
        startActivity(intent);
        finish();
    }

    private  void createIntent() {
        Intent intent = new Intent();
        intent.setType(IMAGE_TYPE);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,
                getString(R.string.select_picture)), SELECT_SINGLE_PICTURE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_SINGLE_PICTURE) {

                Uri selectedImageUri = data.getData();
                try {
                    selectedImagePreview.setImageBitmap(new LoadImage(selectedImageUri, getContentResolver()).getBitmap());
                } catch (IOException e) {
                    Log.e(ReportActivity.class.getSimpleName(), "Failed to load image", e);
                }
            }
        } else {
            Toast.makeText(getApplicationContext(), R.string.msg_failed_to_get_intent_data, Toast.LENGTH_LONG).show();
            Log.d(ReportActivity.class.getSimpleName(), "Failed to get intent data, result code is " + resultCode);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_report, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private EditText inputName;
    private Spinner spinnerType;
    private Spinner spinnerObslvl;
    private Bitmap bitmap;
    public void submitReport(View view) {

        EditText inputTime;
        EditText inputDesc;
        ImageView inputImage1 = (ImageView) findViewById(R.id.btn_image1);
        ImageView inputImage2 = (ImageView) findViewById(R.id.btn_image2);
        ImageView inputImage3 = (ImageView) findViewById(R.id.btn_image3);

        if(inputImage1.getDrawable() != null) {
            bitmap = ((BitmapDrawable)inputImage1.getDrawable()).getBitmap();
            sendImage(bitmap);
        }
        if(inputImage2.getDrawable() != null) {
            bitmap = ((BitmapDrawable)inputImage2.getDrawable()).getBitmap();
            sendImage(bitmap);
        }
        if(inputImage3.getDrawable() != null) {
            bitmap = ((BitmapDrawable)inputImage3.getDrawable()).getBitmap();
            sendImage(bitmap);
        }

        UserInfoPreferences userInfo = new UserInfoPreferences(getApplicationContext());

        inputName = (EditText) findViewById(R.id.text_name);
        inputTime = (EditText) findViewById(R.id.text_time);
        inputDesc = (EditText) findViewById(R.id.text_desc);

        spinnerType = (Spinner) findViewById(R.id.spinner_accType);
        spinnerObslvl = (Spinner) findViewById(R.id.spinner_obslvl);

        Accident acc = new Accident();

        String validate = null;
        validate = validateForm();

        if(!validate.isEmpty()) {
            Toast.makeText(ReportActivity.this, validate, Toast.LENGTH_SHORT).show();
            return;
        }

        acc.setUserId(userInfo.getUserId());
        acc.setType(spinnerType.getSelectedItem().toString());
        acc.setName(inputName.getText().toString());
        acc.setDescription(inputDesc.getText().toString());

        String str = inputTime.getText().toString();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        long currentTime = 0;

        try {
            currentTime = sdf.parse(str).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        acc.setTime(currentTime);

        GPSTracker gpsTracker = new GPSTracker(getApplicationContext());
        double lat = gpsTracker.getLatitude();
        double lon = gpsTracker.getLongitude();
        acc.setLat(lat);
        acc.setLon(lon);

        if(spinnerObslvl.getSelectedItem().toString().equals("Highest")) {
            acc.setObservationLevel(4);
        }
        if(spinnerObslvl.getSelectedItem().toString().equals("High")) {
            acc.setObservationLevel(3);
        }
        if(spinnerObslvl.getSelectedItem().toString().equals("Medium")) {
            acc.setObservationLevel(2);
        }
        if(spinnerObslvl.getSelectedItem().toString().equals("Low")) {
            acc.setObservationLevel(1);
        }

        AccidentService as = new AccidentService(this);
        as.execute(acc);
    }

    private void sendImage(Bitmap currentBitmap) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            currentBitmap.compress(Bitmap.CompressFormat.JPEG, 90, baos);
            byte[] bitmapData = baos.toByteArray();
            String bitmapEncoded = android.util.Base64.encodeToString(bitmapData, android.util.Base64.DEFAULT);
            String urlParameters = "message=" + bitmapEncoded;
            //NEED TO CHANGE THIS REQUEST TO INTEGRATE WITH JAVA
            String request = "http://www.mutanthybrid.com/images/UploadToServer.php";
            URL url = new URL(request);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setInstanceFollowRedirects(false);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("charset", "utf-8");
            connection.setRequestProperty("Content-Length", "" + Integer.toString(urlParameters.getBytes().length));
            connection.setUseCaches(false);

            DataOutputStream dos = new DataOutputStream(connection.getOutputStream());
            dos.writeBytes(urlParameters);

            int code = connection.getResponseCode();
            System.out.println(code);
            dos.flush();
            dos.close();
            connection.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void processReport() {
        Toast.makeText(this, "SendReportTest", Toast.LENGTH_SHORT).show();
        this.finish();
    }

    private String validateForm() {
        String str = "";

        if(inputName.getText().toString().isEmpty()) {
            str += "Enter accident name\n";
        }
        if(spinnerObslvl.getSelectedItemPosition() == 0) {
            str += "Select observation level\n";
        }
        if(spinnerType.getSelectedItemPosition() == 0) {
            str += "Select accident type";
        }
        return str;
    }
}
