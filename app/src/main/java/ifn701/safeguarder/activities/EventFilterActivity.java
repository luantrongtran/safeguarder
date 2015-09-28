package ifn701.safeguarder.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListAdapter;
import android.widget.ListView;

import ifn701.safeguarder.R;

public class EventFilterActivity extends AppCompatActivity {

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
