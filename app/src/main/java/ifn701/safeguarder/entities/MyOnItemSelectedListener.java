package ifn701.safeguarder.entities;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Toast;

/**
 * Created by mutanthybrid on 7/09/2015.
 */
public class MyOnItemSelectedListener implements OnItemSelectedListener {

    /*
        Selected Item
     */
    @Override
    public void onItemSelected(AdapterView parent, View view, int pos, long id) {
        //Toast.makeText(parent.getContext(), "Selected Level: " + parent.getItemAtPosition(pos).toString(), Toast.LENGTH_SHORT).show();
    }

    public void onNothingSelected(AdapterView parent) {

    }
}
