package ifn701.safeguarder.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import ifn701.safeguarder.CustomSharedPreferences.EventFilterSharedPreferences;
import ifn701.safeguarder.R;
import ifn701.safeguarder.entities.MyOnItemSelectedListener;

public class EventFilterActivity extends AppCompatActivity {
    EventFilterSharedPreferences eventFilterSharedPreferences;

    public static final int DISPLAY_ALL = -1;
    public static long[] timeFrames = {1*60*60*1000, 3*60*60*1000,
            5*60*60*1000, 7*60*60*1000, 24*60*60*1000};//millisecond

    CheckBox checkBoxEnablingFilterByTime;
    Spinner timeSelectionSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_filter);

        final ListView listView = (ListView) findViewById(R.id.listviewAccidentTypes);
        listView.setAdapter(new AccidentTypeListViewAdapter(getApplicationContext()));
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        CheckBox checkAll = (CheckBox)findViewById(R.id.cbCheckAllAccidentTypes);
        checkAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ListAdapter listAdapter = listView.getAdapter();
                for(int i = 0; i < listAdapter.getCount(); i++) {
                    View view = listView.getChildAt(i);
                    CheckBox cb = (CheckBox)view.findViewById(R.id.cbAccidentFilter);
                    cb.setChecked(isChecked);
                }
            }
        });

        checkBoxEnablingFilterByTime = (CheckBox)findViewById(R.id.cbFilterByTime);
        timeSelectionSpinner = (Spinner) findViewById(R.id.spinnerFilterByTime);
        eventFilterSharedPreferences = new EventFilterSharedPreferences(getApplicationContext());
        preLoadSettingsIfExisted();
        setUpEventListenerForTimeSelectionSpinner();
    }

    /**
     * Loading settings from SharedPreferences.
     */
    private void preLoadSettingsIfExisted() {
        long numOfHours = eventFilterSharedPreferences.getTimeSetting();
        if(numOfHours == -1) {
            //if the time setting is displaying all accidents
            checkBoxEnablingFilterByTime.setChecked(false);
            timeSelectionSpinner.setEnabled(false);//disable the spinner selecting hour.
        } else {
            checkBoxEnablingFilterByTime.setChecked(true);
            timeSelectionSpinner.setEnabled(true);//enable the spinner selecting hour.

            int selectedIndex = 0;
            for(int i = 0; i < timeFrames.length; i++) {
                if(numOfHours == timeFrames[i]) {
                    selectedIndex = i;
                    break;
                }
            }
            timeSelectionSpinner.setSelection(selectedIndex);
        }
    }

    private void setUpEventListenerForTimeSelectionSpinner() {
        checkBoxEnablingFilterByTime.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                timeSelectionSpinner.setEnabled(isChecked);

                if(isChecked) {
                    int selectedIndex = timeSelectionSpinner.getSelectedItemPosition();
                    eventFilterSharedPreferences.setTimeSetting(timeFrames[selectedIndex]);
                } else {
                    eventFilterSharedPreferences.setTimeSetting(DISPLAY_ALL);
                }
            }
        });

        timeSelectionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                eventFilterSharedPreferences.setTimeSetting(timeFrames[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_event_filter, menu);
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
}
