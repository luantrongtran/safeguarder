package ifn701.safeguarder.activities;

import android.media.effect.Effect;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import ifn701.safeguarder.GPSTracker;
import ifn701.safeguarder.R;
import ifn701.safeguarder.backend.myApi.model.Accident;
import ifn701.safeguarder.entities.PagerAdapter;
import ifn701.safeguarder.webservices.AccidentService;
import ifn701.safeguarder.webservices.IAccidentService;

public class ReportActivity extends AppCompatActivity implements IAccidentService {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Detailed"));
        tabLayout.addTab(tabLayout.newTab().setText("Quick"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final PagerAdapter adapter = new PagerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private EditText inputName = null;
    private EditText inputType = null;
    private EditText inputTime = null;
    private EditText inputDesc = null;

    private Spinner spinner = null;
    private int inputObslvl = 0;

    public void submitDetailedReport(View view) {
        inputName = (EditText)findViewById(R.id.text_name);
        inputType = (EditText)findViewById(R.id.text_type);
        inputTime = (EditText) findViewById(R.id.text_time);
        inputDesc = (EditText) findViewById(R.id.text_desc);

        spinner = (Spinner) findViewById(R.id.spinner_obslvl);

        sendData();
    }

    public void submitQuickReport(View view) {
        inputName = (EditText)findViewById(R.id.q_text_name);
        inputType = null;
        inputTime = (EditText) findViewById(R.id.q_text_time);
        inputDesc = (EditText) findViewById(R.id.q_text_desc);

        spinner = (Spinner) findViewById(R.id.q_spinner_obslvl);

        sendData();
    }

    private void sendData() {
        String str = inputTime.getText().toString();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        long currentTime = 0;

        try {
            currentTime = sdf.parse(str).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String convertSpinner = spinner.getSelectedItem().toString();

        if(convertSpinner.equals("Highest")) {
            inputObslvl = 4;
        }
        if(convertSpinner.equals("High")) {
            inputObslvl = 3;
        }
        if(convertSpinner.equals("Medium")) {
            inputObslvl = 2;
        }
        if(convertSpinner.equals("Low")) {
            inputObslvl = 1;
        }

        Accident acc = new Accident();
        acc.setUserId(1);
        acc.setName(inputName.getText().toString());
        acc.setDescription(inputDesc.getText().toString());
        acc.setType(inputType.getText().toString());
        acc.setTime(currentTime);
        acc.setObservationLevel(inputObslvl);

        GPSTracker gpsTracker = new GPSTracker(getApplicationContext());
        double lat = gpsTracker.getLatitude();
        double lon = gpsTracker.getLongitude();
        acc.setLat(lat);
        acc.setLon(lon);

        AccidentService as = new AccidentService(this);
        as.execute(acc);
    }

    @Override
    public void processReport() {
        Toast.makeText(this, "SendReportTest", Toast.LENGTH_SHORT).show();
    }
}