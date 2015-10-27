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

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import ifn701.safeguarder.CustomSharedPreferences.CurrentLocationPreferences;
import ifn701.safeguarder.CustomSharedPreferences.UserInfoPreferences;
import ifn701.safeguarder.GPSTracker;
import ifn701.safeguarder.MapsActivity;
import ifn701.safeguarder.R;
import ifn701.safeguarder.backend.myApi.model.Accident;
import ifn701.safeguarder.entities.MyOnItemSelectedListener;
import ifn701.safeguarder.entities.LoadImage;
import ifn701.safeguarder.entities.blob_images.BlobImage;
import ifn701.safeguarder.entities.blob_images.BlobImageArray;
import ifn701.safeguarder.webservices.AccidentService;
import ifn701.safeguarder.webservices.IAccidentService;
import ifn701.safeguarder.webservices.image_uploader_service.IImageUploadService;
import ifn701.safeguarder.webservices.image_uploader_service.ImageUploadService;

public class ReportActivity extends AppCompatActivity implements IAccidentService,
        IImageUploadService{

    private static final int SELECT_SINGLE_PICTURE = 101;
    public static final String IMAGE_TYPE = "image/*";
    private ImageView selectedImagePreview;

    /**
     *
     */
    int selectedImagePreviewIndex = -1;
    /**
     * indicates if the images are selected.
     */
    private boolean[] isSelected = new boolean[3];

    Accident accident;

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

        spinnerType = (Spinner) findViewById(R.id.spinner_accType);
        spinnerObslvl = (Spinner) findViewById(R.id.spinner_obslvl);
        inputName = (EditText) findViewById(R.id.text_name);
    }

    public void setImageViewOne(View view) {
        selectedImagePreviewIndex = 0;
        selectedImagePreview = (ImageView) findViewById(R.id.btn_image1);
        createIntent();
    }

    public void setImageViewTwo(View view) {
        selectedImagePreviewIndex = 1;
        selectedImagePreview = (ImageView) findViewById(R.id.btn_image2);
        createIntent();
    }

    public void setImageViewThree(View view) {
        selectedImagePreviewIndex = 2;
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
                    isSelected[selectedImagePreviewIndex] = true;
                } catch (IOException e) {
                    Log.e(ReportActivity.class.getSimpleName(), "Failed to load image", e);
                }
            }
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

    /**
     * Upload selected images for the Report
     * @return the number of images selected
     */
    public int uploadImages() {
        ImageView inputImage1 = (ImageView) findViewById(R.id.btn_image1);
        ImageView inputImage2 = (ImageView) findViewById(R.id.btn_image2);
        ImageView inputImage3 = (ImageView) findViewById(R.id.btn_image3);

        Bitmap bm1 = null;
        Bitmap bm2 = null;
        Bitmap bm3 = null;
        int sum = 0;
        if(isSelected[0] && inputImage1.getDrawable() != null) {
            bm1 = ((BitmapDrawable)inputImage1.getDrawable()).getBitmap();
            sum++;
        }
        if(isSelected[1] && inputImage2.getDrawable() != null) {
            bm2 = ((BitmapDrawable)inputImage2.getDrawable()).getBitmap();
            sum++;
        }
        if(isSelected[2] && inputImage3.getDrawable() != null) {
            bm3 = ((BitmapDrawable)inputImage3.getDrawable()).getBitmap();
            sum++;
        }

        if(sum == 0) {
            //if no image selected
            return 0;
        }

        ImageUploadService imageUploadService = new ImageUploadService(this);
        imageUploadService.execute(bm1, bm2, bm3);

        return sum;
    }

    /**
     * Subimt report includes two steps. The first step is uploading selected images. The second
     * step is received the url of uploaded images and submitForm()
     * @param view
     */
    public void submitReport(View view) {
//        inputName = (EditText) findViewById(R.id.text_name);
//        spinnerType = (Spinner) findViewById(R.id.spinner_accType);
//        spinnerObslvl = (Spinner) findViewById(R.id.spinner_obslvl);
        accident = new Accident();

        //Validate form
        String validate = null;
        validate = validateForm();
        if(!validate.isEmpty()) {
            Toast.makeText(ReportActivity.this, validate, Toast.LENGTH_SHORT).show();
            return;
        }

        //1st step: Uploading images
        int noOfImages = uploadImages();
        if(noOfImages == 0) {
            //if no image selected, submit the form immediately
            submitForm();
            return;
        }
        //if there are selected images. The form will be submited after uploading images. In particular,
        //the form will be submitted in onImagesUploaded() method
    }

    public void submitForm() {
        EditText inputTime = (EditText) findViewById(R.id.text_time);
        EditText inputDesc = (EditText) findViewById(R.id.text_desc);
        UserInfoPreferences userInfo = new UserInfoPreferences(getApplicationContext());

        accident.setUserId(userInfo.getUserId());
        accident.setType(spinnerType.getSelectedItem().toString());
        accident.setName(inputName.getText().toString());
        accident.setDescription(inputDesc.getText().toString());

        String str = inputTime.getText().toString();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        long currentTime = 0;

        try {
            currentTime = sdf.parse(str).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        accident.setTime(currentTime);

//        GPSTracker gpsTracker = new GPSTracker(getApplicationContext());
//        double lat = gpsTracker.getLatitude();
//        double lon = gpsTracker.getLongitude();
        CurrentLocationPreferences currentLocationPreferences
                = new CurrentLocationPreferences(getApplicationContext());
        double lat = currentLocationPreferences.getLat();
        double lon = currentLocationPreferences.getLon();
        accident.setLat(lat);
        accident.setLon(lon);

        if(spinnerObslvl.getSelectedItem().toString().equals("Highest")) {
            accident.setObservationLevel(4);
        }
        if(spinnerObslvl.getSelectedItem().toString().equals("High")) {
            accident.setObservationLevel(3);
        }
        if(spinnerObslvl.getSelectedItem().toString().equals("Medium")) {
            accident.setObservationLevel(2);
        }
        if(spinnerObslvl.getSelectedItem().toString().equals("Low")) {
            accident.setObservationLevel(1);
        }

        AccidentService as = new AccidentService(this);
        as.execute(accident);
    }

    @Override
    public void onNewAccidentReported(Accident accident) {
        if(accident == null) {
            Toast.makeText(ReportActivity.this, R.string.new_accident_failed_to_report,
                    Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(ReportActivity.this, R.string.new_accident_reported,
                    Toast.LENGTH_SHORT).show();
        }
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

    @Override
    public void onImagesUploaded(BlobImageArray blobImageArray) {
        List<BlobImage> lst = blobImageArray.data;
        for(int i = 0; i < lst.size(); i++) {
            BlobImage blobImage = lst.get(i);
            if(i == 0) {
                accident.setImage1(blobImage.servingUrl);
            } else if(i == 1) {
                accident.setImage2(blobImage.servingUrl);
            } else if (i == 2) {
                accident.setImage3(blobImage.servingUrl);
            }
        }

        //Make sure there is no null value
        if(accident.getImage1() == null) {
            accident.setImage1("");
            accident.setImage2("");
            accident.setImage3("");
        } else if (accident.getImage2() == null) {
            accident.setImage2("");
            accident.setImage3("");
        } else if (accident.getImage3() == null) {
            accident.setImage3("");
        }

        submitForm();
    }
}
