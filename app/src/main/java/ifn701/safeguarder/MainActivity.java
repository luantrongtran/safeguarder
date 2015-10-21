package ifn701.safeguarder;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

//import ifn701.safeguarder.backend.entities.accidentApi.model.*;
import ifn701.safeguarder.webservices.AccidentService;
import ifn701.safeguarder.webservices.IAccidentService;
import ifn701.safeguarder.webservices.ISayHi;
import ifn701.safeguarder.webservices.SayHi;

public class MainActivity extends AppCompatActivity implements ISayHi {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    public void sayHi(View view) {
        EditText nameEdt = (EditText)findViewById(R.id.txtName);
        String name = nameEdt.getText().toString();
        SayHi sh = new SayHi(this);
        sh.execute(name);
    }

//    public void accidentReport(View view) {
//        Accident acc = new Accident();
//        acc.setUserId(1);
//        acc.setName("AccidentName");
//        acc.setDescription("AccidentDescription");
//        acc.setLat(1.1f);
//        acc.setType("Accident Type");
//
//        AccidentService as = new AccidentService(this);
//        as.execute(acc);
//    }

    public void imageUpload(View view) {

    }

    @Override
    public void processData(String greeting) {
        Toast.makeText(this, greeting, Toast.LENGTH_SHORT).show();
    }
}