package ifn701.safeguarder.Observation;

import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import ifn701.safeguarder.R;
import ifn701.safeguarder.backend.myApi.model.Accident;
import ifn701.safeguarder.webservices.GetAccidentService;
import ifn701.safeguarder.webservices.IGetAccidentService;

public class ObservationDetailed extends AppCompatActivity implements IGetAccidentService {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_observation_detailed);

        GetAccidentService getAccidentService = new GetAccidentService(this);
        getAccidentService.execute(getIntent().getIntExtra("AccidentID", -1));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_observation_detailed, menu);
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

    private void showAccidentDetails(Accident accident){

        EditText accType = (EditText) findViewById(R.id.obs_textType);
        EditText accWhen = (EditText) findViewById(R.id.obs_textWhen);
        EditText accLocation = (EditText) findViewById(R.id.obs_textLocation);
        EditText accObsLvl = (EditText) findViewById(R.id.obs_textObslvl);
        EditText accDesc = (EditText) findViewById(R.id.obs_textDesc);
        EditText accUser = (EditText) findViewById(R.id.obs_textUser);

        accType.setText(accident.getType());

        long timeRange = System.currentTimeMillis() - accident.getTime();
        int minutes = (int) ((timeRange / (1000*60)) % 60);
        int hours   = (int) ((timeRange / (1000*60*60)) % 24);
        int days = (int) ((timeRange / (1000*60*60*24)) % 7);

        /* //TESTING
        long currentTime = accident.getTime();
        Date date=new Date(currentTime);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        String dateText = sdf.format(date);
        */

        if(days < 1) {
            if(hours < 1) {
                if(minutes < 1) accWhen.setText("Few seconds ago");
                else if(minutes == 1) accWhen.setText("A minute ago");
                else if(minutes > 1) accWhen.setText(minutes + " minutes ago");
            }
            else if(hours == 1) {
                if(minutes < 1) accWhen.setText(hours + "hour ago");
                else if(minutes == 1)  accWhen.setText(hours + " hour " + minutes + " minutes ago");
                else if(minutes > 1)  accWhen.setText(hours + " hour " + minutes + " minutes ago");
            }
            else if(hours > 1) {
                if(minutes > 1)  accWhen.setText(hours + " hours " + minutes + " minutes ago");
                else if(minutes == 1)  accWhen.setText(hours + " hours " + minutes + " minutes ago");
                else if(minutes < 1) accWhen.setText(hours + "hours ago");
            }
        }
        else if(days == 1) accWhen.setText(days + " day ago");
        else if(days > 1 && days < 7) accWhen.setText(days + " days ago");
        else if(days == 7) accWhen.setText("A week ago");
        else if(days > 7) accWhen.setText("Long ago");


        double accLon = accident.getLon();
        double accLat = accident.getLat();
        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(accLat, accLon, 1);
            if(addresses != null & addresses.size() > 0) {
                Address address = addresses.get(0);
                accLocation.setText(address.getLocality());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        if(accident.getObservationLevel().equals(1)) {
            accObsLvl.setText("Low");
        }
        if(accident.getObservationLevel().equals(2)) {
            accObsLvl.setText("Medium");
        }
        if(accident.getObservationLevel().equals(3)) {
            accObsLvl.setText("High");
        }
        if(accident.getObservationLevel().equals(4)) {
            accObsLvl.setText("Highest");
        }

        accDesc.setText(accident.getDescription());
        accUser.setText(accident.getUser().getFullName());
    }

    @Override
    public void getAccidentData(Accident accident) {
        showAccidentDetails(accident);
    }
}