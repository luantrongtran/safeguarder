package ifn701.safeguarder.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import ifn701.safeguarder.Constants;
import ifn701.safeguarder.Observation.AccidentListAdapter;
import ifn701.safeguarder.Observation.ObservationDetailed;
import ifn701.safeguarder.R;
import ifn701.safeguarder.backend.myApi.model.Accident;
import ifn701.safeguarder.backend.myApi.model.AccidentList;
import ifn701.safeguarder.webservices.GetNotificationListService;
import ifn701.safeguarder.webservices.IGetNotificationListService;

public class NotificationActivity extends AppCompatActivity implements IGetNotificationListService{

    TextView msg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        msg = (TextView) findViewById(R.id.msg);

        GetNotificationListService getNotificationListService
                = new GetNotificationListService(getApplicationContext(), this);
        getNotificationListService.execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_notification, menu);
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

    private void showAccidentByUserIdList(final List<Accident> accidentList) {

        final ListView accidentObsList = (ListView) findViewById(R.id.myAccidentObsList);

        AccidentListAdapter accidentListAdapter
                = new AccidentListAdapter(getApplicationContext(), accidentList);
        accidentObsList.setAdapter(accidentListAdapter);

        accidentObsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Go back to MapsActivity and pan to the accident position.
                Accident accident = accidentList.get(position);
                Intent intent = new Intent();
                intent.putExtra(Constants.notification_activity_intent_result_accident_lat
                        , accident.getLat());
                intent.putExtra(Constants.notification_activity_intent_result_accident_lon,
                        accident.getLon());
                intent.putExtra("lon", accident.getLon());
                setResult(RESULT_OK, intent);
                finish();
            }
        });

    }

    @Override
    public void onNotificationListUpdated(List<Accident> accidentList) {
        if(accidentList == null || accidentList.size() == 0) {
            msg.setVisibility(View.VISIBLE);
        } else {
            msg.setVisibility(View.GONE);
        }
        showAccidentByUserIdList(accidentList);
    }

    public void goBack(View view) {
        finish();
    }
}
