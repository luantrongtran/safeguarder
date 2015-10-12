package ifn701.safeguarder;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.InjectView;
import ifn701.safeguarder.CustomSharedPreferences.UserInfoPreferences;
import ifn701.safeguarder.CustomSharedPreferences.UserSettingsPreferences;
import ifn701.safeguarder.backend.myApi.model.User;
import ifn701.safeguarder.webservices.ISignupService;
import ifn701.safeguarder.webservices.SignupService;

public class SignupActivity extends AppCompatActivity implements ISignupService  {
    private static final String TAG = "SignupActivity";

    @InjectView(R.id.input_name) EditText _nameText;
    @InjectView(R.id.input_email) EditText _emailText;
    @InjectView(R.id.input_password) EditText _passwordText;
    @InjectView(R.id.btn_signup) Button _signupButton;
    @InjectView(R.id.link_login) TextView _loginLink;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.inject(this);

        // Font path
        String fontPath = "fonts/UbuntuCondensed-Regular.ttf";
        //String fontPathBold = "fonts/Ubuntu-B.ttf";

        // text view label
        TextView appName = (TextView) findViewById(R.id.appNametextView);
        EditText userName = (EditText) findViewById(R.id.input_name);
        EditText userEmail = (EditText) findViewById(R.id.input_email);
        EditText userPwd = (EditText) findViewById(R.id.input_password);
        TextView logintext = (TextView) findViewById(R.id.link_login);
        Button signupBtn = (Button) findViewById(R.id.btn_signup);
        // Loading Font Face
        Typeface tf = Typeface.createFromAsset(getAssets(), fontPath);
        //Typeface tfbold = Typeface.createFromAsset(getAssets(),fontPathBold);

        // Applying font
        appName.setTypeface(tf);
        userName.setTypeface(tf);
        userEmail.setTypeface(tf);
        userPwd.setTypeface(tf);
        logintext.setTypeface(tf);
        signupBtn.setTypeface(tf);

        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText nameEdit = (EditText)findViewById(R.id.input_name);
                EditText registeremailEdit = (EditText)findViewById(R.id.input_email);
                EditText passEdit = (EditText)findViewById(R.id.input_password);
                String email = registeremailEdit.getText().toString();
                String password = passEdit.getText().toString();
                String name = nameEdit.getText().toString();
                SignupService signup = new SignupService((ISignupService) SignupActivity.this);
                signup.execute(name,email,password);
                validate();
//                signup();
            }
        });

        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                finish();
            }
        });
    }

//    public void signup() {
//        Log.d(TAG, "Signup");
//
//        if (!validate()) {
//            onSignupFailed();
//            return;
//        }
//
//        _signupButton.setEnabled(false);
//
//        final ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this,
//                R.style.AppTheme_Dialog);
//        progressDialog.setIndeterminate(true);
//        progressDialog.setMessage("Creating Account...");
//        progressDialog.show();
//
//        String name = _nameText.getText().toString();
//        String email = _emailText.getText().toString();
//        String password = _passwordText.getText().toString();
//
//        // TODO: Implement your own signup logic here.
//
//        new android.os.Handler().postDelayed(
//                new Runnable() {
//                    public void run() {
//                        // On complete call either onSignupSuccess or onSignupFailed
//                        // depending on success
//                        onSignupSuccess();
//                        // onSignupFailed();
//                        progressDialog.dismiss();
//                    }
//                }, 3000);
//    }


//    public void onSignupSuccess() {
//        _signupButton.setEnabled(true);
//        setResult(RESULT_OK, null);
//        //finish();
//        Intent i =new Intent(this, MapsActivity.class);
//        startActivity(i);
//    }
//
//    public void onSignupFailed() {
//        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();
//
//        _signupButton.setEnabled(true);
//    }

    public boolean validate() {
        boolean valid = true;

        String name = _nameText.getText().toString();
        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            _nameText.setError("at least 3 characters");
            valid = false;
        } else {
            _nameText.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }


    @Override
       public void processUserSignup(User user) {

//        if(user != null){
//            UserInfoPreferences userInfoPreferences = new UserInfoPreferences(getApplicationContext());
//            userInfoPreferences.setEmail(user.getEmail());
//            userInfoPreferences.setPassword(user.getPassword());
//            userInfoPreferences.setFullname(user.getFullName());
//            userInfoPreferences.setUserId(user.getId());
//
//            //Set up user settings into SharedPreferences
//            UserSettingsPreferences userSettingsPreferences
//                    = new UserSettingsPreferences(getApplicationContext());
//            userSettingsPreferences.setUserSettings(user.getUserSetting());
//
//            Toast.makeText(SignupActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
//            Intent intent = new Intent(this, MapsActivity.class);
//            startActivity(intent);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
////            finish();
//        }
//        else
//        {
//            Toast.makeText(SignupActivity.this, "Account already exist, Please Login", Toast.LENGTH_SHORT).show();
//            Intent login = new Intent(this, LoginActivity.class);
//            startActivity(login);
//        }
    }
}
