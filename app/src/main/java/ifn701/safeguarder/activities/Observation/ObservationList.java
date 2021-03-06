package ifn701.safeguarder.activities.Observation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import ifn701.safeguarder.CustomSharedPreferences.UserInfoPreferences;
import ifn701.safeguarder.R;
import ifn701.safeguarder.backend.myApi.model.Accident;
import ifn701.safeguarder.backend.myApi.model.AccidentList;
import ifn701.safeguarder.webservices.GetAccidentListByUserIdService;
import ifn701.safeguarder.webservices.IGetAccidentByUserIdService;

public class ObservationList extends Activity implements IGetAccidentByUserIdService {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_observation_list);

        GetAccidentListByUserIdService getAccidentByUserIdService = new GetAccidentListByUserIdService(this);
        UserInfoPreferences getMyUserId = new UserInfoPreferences(getApplicationContext());
        getAccidentByUserIdService.execute(getMyUserId.getUserId());

        msg = (TextView) findViewById(R.id.msg);

        TextView observationListPageTitle = (TextView) findViewById(R.id.report_list_title);
        observationListPageTitle.setText(R.string.observation_list_page_title);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_observation_list, menu);
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

    private void showAccidentByUserIdList(final AccidentList accidentList) {

        final ListView accidentObsList = (ListView) findViewById(R.id.myAccidentObsList);

        AccidentListAdapter accidentListAdapter = new AccidentListAdapter(getApplicationContext(), accidentList.getAccidentList());
        accidentObsList.setAdapter(accidentListAdapter);

        accidentObsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                System.out.print("INSIDE METHOD");
                Accident selectedAccident = accidentList.getAccidentList().get(position);
                Intent intent = new Intent(ObservationList.this, ObservationDetailed.class);
                intent.putExtra("AccidentID", selectedAccident.getId());
                startActivityForResult(intent, 1);
            }
        });

    }

    private TextView msg;

    @Override
    public void getAccidentListByUserIdData(AccidentList accidentList) {
        if (accidentList == null || accidentList.getAccidentList() == null) {
            /*
            Toast.makeText(ObservationList.this,
                    R.string.observation_list_activity_empty_accident_list,
                    Toast.LENGTH_SHORT).show();
            */
            msg.setVisibility(View.VISIBLE);
            return;
        }
        else {
            msg.setVisibility(View.GONE);
        }
        showAccidentByUserIdList(accidentList);
    }

    public void goBack(View view) {
        finish();
    }
}
