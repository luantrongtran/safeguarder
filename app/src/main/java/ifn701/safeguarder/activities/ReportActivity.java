package ifn701.safeguarder.activities;

import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import ifn701.safeguarder.Constants;
import ifn701.safeguarder.CustomSharedPreferences.UserInfoPreferences;
import ifn701.safeguarder.GPSTracker;
import ifn701.safeguarder.R;
import ifn701.safeguarder.backend.myApi.model.Accident;
import ifn701.safeguarder.entities.MyOnItemSelectedListener;
import ifn701.safeguarder.webservices.AccidentService;
import ifn701.safeguarder.webservices.IAccidentService;

public class ReportActivity extends AppCompatActivity implements IAccidentService{

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
    private EditText inputTime;
    private EditText inputDesc;

    private Spinner spinnerType;
    private Spinner spinnerObslvl;

    public void submitReport(View view) {
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
