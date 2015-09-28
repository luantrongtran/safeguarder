package ifn701.safeguarder.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import ifn701.safeguarder.Constants;
import ifn701.safeguarder.CustomSharedPreferences.UserInfoPreferences;
import ifn701.safeguarder.CustomSharedPreferences.UserSettingsPreferences;
import ifn701.safeguarder.R;
import ifn701.safeguarder.backend.myApi.model.ResultCode;
import ifn701.safeguarder.backend.myApi.model.UserSetting;
import ifn701.safeguarder.webservices.IUpdateUserSettingService;
import ifn701.safeguarder.webservices.UpdateUserSettingService;

public class ZoneSettingActivity extends AppCompatActivity implements IUpdateUserSettingService {

    private double homeLat;
    private double homeLon;

    EditText addressEditor;
    EditText radiusEditor ;

    UserSetting userSetting;
    UserSettingsPreferences userSettingsPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zone_setting);

        radiusEditor = (EditText)findViewById(R.id.radiusTextEditor);
        addressEditor = (EditText)findViewById(R.id.addressTextEditor);

        userSettingsPreferences
                = new UserSettingsPreferences(getApplicationContext());
        radiusEditor.setText(userSettingsPreferences.getRadius() + "");
        addressEditor.setText(userSettingsPreferences.getHomeLocationAddress());
        homeLat = userSettingsPreferences.getHomeLocationLat();
        homeLon = userSettingsPreferences.getHomeLocationLon();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_zone_setting, menu);
        return true;
    }

    public void searchAddress(View view) {
        Intent intent = new Intent(this, HomeSettingActivity.class);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK){

            addressEditor.setText(data.getStringExtra(Constants.search_location_address));

            homeLat = data.getDoubleExtra(Constants.search_location_lat,
                    Constants.sharedPreferences_double_default_value);
            homeLon = data.getDoubleExtra(Constants.search_location_lon,
                    Constants.sharedPreferences_double_default_value);
        }
    }

    public void updateSettings(View view) {
        if(radiusEditor.getText().toString().trim().isEmpty()){
            radiusEditor.setText(Constants.sharedPreferences_default_radius+"");
            return;
        }

        float radius = Float.valueOf(radiusEditor.getText().toString());

        //Validate radius value
        if(radius  <= 0) {
            Toast.makeText(ZoneSettingActivity.this, R.string.zone_setting_invalid_radius,
                    Toast.LENGTH_SHORT).show();
            return;
        }

        UserInfoPreferences userInfoPreferences = new UserInfoPreferences(getApplicationContext());

        userSetting = new UserSetting();
        userSetting.setRadius(radius);
        userSetting.setHomeAddress(addressEditor.getText().toString());
        userSetting.setHomeLocationLat(homeLat);
        userSetting.setHomeLocationLon(homeLon);
        userSetting.setUserId(userInfoPreferences.getUserId());

        UpdateUserSettingService updateUserSettingService = new UpdateUserSettingService(this);
        updateUserSettingService.execute(userSetting);
    }

    private void updateUserSettingIntoSharedPreferences() {
        userSettingsPreferences.setUserSettings(userSetting);
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

    @Override
    public void onUserSettingsUpdated(ResultCode resultCode) {
        if(resultCode.getResult() == true) {
            updateUserSettingIntoSharedPreferences();
            Toast.makeText(ZoneSettingActivity.this, R.string.zone_setting_update_changes_successfully,
                    Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(ZoneSettingActivity.this, R.string.zone_setting_update_changes_failed,
                    Toast.LENGTH_SHORT).show();
        }
    }
}