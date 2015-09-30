package ifn701.safeguarder.activities;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import ifn701.safeguarder.AccidentManager;
import ifn701.safeguarder.Constants;
import ifn701.safeguarder.CustomSharedPreferences.EventFilterSharedPreferences;
import ifn701.safeguarder.R;

/**
 * Created by lua on 28/09/2015.
 */
public class AccidentTypeListViewAdapter extends ArrayAdapter<String> {

//    This class uses 2 static variables in AccidentManager including accidentTypeMarkerIds and
//    accidentTypeNames
    Context context;
    EventFilterSharedPreferences eventFilterSharedPreferences;
    View view;

    public AccidentTypeListViewAdapter(Context context) {
        super(context, -1, AccidentManager.sharedPreferencesIds);

        this.context = context;
        context.getResources().getStringArray(R.array.accidentType_array);
        eventFilterSharedPreferences = new EventFilterSharedPreferences(context);
    }

    public void setChecked(boolean value) {
        if(view == null) {
            return;
        }
        CheckBox cb = (CheckBox) view.findViewById(R.id.cbAccidentFilter);
        cb.setChecked(value);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater
                = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.event_filter_accident_type_list_item, parent, false);

        TextView tvAccidentTypeName = (TextView) view.findViewById(R.id.accidentTypeName);
        tvAccidentTypeName.setText(AccidentManager.accidentTypeNames[position]);

        ImageView ivAccidentTypeIcon = (ImageView) view.findViewById(R.id.accidentTypeIcon);
        ivAccidentTypeIcon.setImageResource(AccidentManager.accidentTypeMarkerIds[position]);

        CheckBox cb = (CheckBox) view.findViewById(R.id.cbAccidentFilter);

        Log.i(Constants.APPLICATION_ID, eventFilterSharedPreferences
                .getEventFilter(AccidentManager.sharedPreferencesIds[position]) + "");
        cb.setChecked(eventFilterSharedPreferences
                    .getEventFilter(AccidentManager.sharedPreferencesIds[position]));

        cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                eventFilterSharedPreferences
                        .setEventFilter(AccidentManager.sharedPreferencesIds[position], isChecked);
            }
        });

        return view;
    }
}
