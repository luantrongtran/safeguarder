package ifn701.safeguarder.activities;

/**
 * Created by mutanthybrid on 24/08/2015.
 */
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import ifn701.safeguarder.R;
import ifn701.safeguarder.backend.entities.accidentApi.model.Accident;
import ifn701.safeguarder.entities.MyOnItemSelectedListener;
import ifn701.safeguarder.webservices.AccidentService;

public class QuickTabFragment extends Fragment {

    private Spinner spinner;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        long l = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Date date = new Date(l);
        String str = sdf.format(date);

        View view = inflater.inflate(R.layout.fragment_quick_tab, container, false);
        EditText editText = (EditText) view.findViewById(R.id.q_text_time);
        editText.setText(str);

        /*
           Observation Level Drop Down List
         */
        //addListenerOnButton();
        spinner = (Spinner) view.findViewById(R.id.q_spinner_obslvl);


        //addListenerOnSpinnerItemSelection();
        //spinner = (Spinner) view.findViewById(R.id.spinner_obslvl);
        spinner.setOnItemSelectedListener(new MyOnItemSelectedListener());

        return view;
    }
}
