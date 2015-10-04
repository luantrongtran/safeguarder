package ifn701.safeguarder.Observation;

import android.content.Context;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import ifn701.safeguarder.CustomSharedPreferences.CurrentLocationPreferences;
import ifn701.safeguarder.R;
import ifn701.safeguarder.backend.myApi.model.Accident;

/**
 * Created by mutanthybrid on 23/09/2015.
 */
public class AccidentListAdapter extends ArrayAdapter<Accident> {

    private Context context;

    public AccidentListAdapter(Context context, List<Accident> accidents) {
        super(context, 0, accidents);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Accident accident = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.listviewitem_accidentbyuserlist, parent, false);
        }

        TextView tvName = (TextView) convertView.findViewById(R.id.tvName);
        TextView tvObslvl = (TextView) convertView.findViewById(R.id.tvObslvl);
        TextView tvTime = (TextView) convertView.findViewById(R.id.tvTime);

        String[] obs = getContext().getResources().getStringArray(R.array.observationlevel_array);
        String obsName = obs[accident.getObservationLevel()];

        if(accident.getObservationLevel() == 1) {
            tvObslvl.setTextColor(Color.parseColor("#009900"));
        }
        else if(accident.getObservationLevel() == 2) {
            tvObslvl.setTextColor(Color.parseColor("#FFCC66"));
        }
        else if(accident.getObservationLevel() == 3) {
            tvObslvl.setTextColor(Color.parseColor("#FF6600"));
        }
        else if(accident.getObservationLevel() == 4) {
            tvObslvl.setTextColor(Color.parseColor("#FF0000"));
        }

        tvName.setText(accident.getName());
        tvObslvl.setText(obsName);


        long l = accident.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Date date = new Date(l);
        String time = sdf.format(date);

        tvTime.setText(time);

        return convertView;
    }
}
