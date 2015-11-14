package ifn701.safeguarder.activities;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.InjectView;
import ifn701.safeguarder.Constants;
import ifn701.safeguarder.CustomSharedPreferences.UserInfoPreferences;
import ifn701.safeguarder.CustomSharedPreferences.UserSettingsPreferences;
import ifn701.safeguarder.R;
import ifn701.safeguarder.SignupActivity;
import ifn701.safeguarder.backend.myApi.model.User;
import ifn701.safeguarder.webservices.ILoginService;
import ifn701.safeguarder.webservices.LoginService;

public class LoginActivity extends AppCompatActivity implements ILoginService{
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;

    @InjectView(R.id.input_email) EditText _emailText;
    @InjectView(R.id.input_password) EditText _passwordText;
    @InjectView(R.id.btn_login) Button _loginButton;
    @InjectView(R.id.link_signup) TextView _signupLink;
//    @InjectView(R.id.clear_txt) ImageButton _clearTxt;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        UserInfoPreferences userInfoPreferences = new UserInfoPreferences(getApplicationContext());

        if(userInfoPreferences.getUserId() != Constants.sharedPreferences_integer_default_value){
            //User is already logged in
            Intent intent = new Intent(this, MapsActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        setContentView(R.layout.activity_login);
        ButterKnife.inject(this);
        // Font path
        String fontPath = "fonts/UbuntuCondensed-Regular.ttf";
        //String fontPathBold = "fonts/Ubuntu-B.ttf";

        // text view label
        TextView appName = (TextView) findViewById(R.id.appNametextView);
        EditText userEmail = (EditText) findViewById(R.id.input_email);
        EditText userPwd = (EditText) findViewById(R.id.input_password);
        TextView signuptext = (TextView) findViewById(R.id.link_signup);
        Button loginBtn = (Button) findViewById(R.id.btn_login);



        // Loading Font Face
        Typeface tf = Typeface.createFromAsset(getAssets(), fontPath);
        //Typeface tfbold = Typeface.createFromAsset(getAssets(),fontPath);

        // Applying font
        appName.setTypeface(tf);
        userEmail.setTypeface(tf);
        userPwd.setTypeface(tf);
        signuptext.setTypeface(tf);
        loginBtn.setTypeface(tf);

        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                EditText emailEdit = (EditText)findViewById(R.id.input_email);
                EditText passEdit = (EditText)findViewById(R.id.input_password);
                String email = emailEdit.getText().toString();
                String password = passEdit.getText().toString();
                validate();
                LoginService login = new LoginService(LoginActivity.this);
                login.execute(email,password);
//                final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
//                        R.style.AppTheme_Dialog);
//                progressDialog.setIndeterminate(true);
//                progressDialog.setMessage("Authenticating...");
//                progressDialog.show();
            }
        });

//        _clearTxt.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View view){
//                _emailText.setText("");
//            }
//        });
        _signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the SignUp activity
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
            }
        });
    }

//    public void login() {
//        Log.d(TAG, "Login");
//        if (!validate()) {
//            onLoginFailed();
//            return;
//        }
//        _loginButton.setEnabled(false);

//        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
//                R.style.AppTheme_Dialog);
//        progressDialog.setIndeterminate(true);
//        progressDialog.setMessage("Authenticating...");
//        progressDialog.show();

//        String email = _emailText.getText().toString();
//        String password = _passwordText.getText().toString();


    //LoginService.execute(email,password);
    // TODO: Implement your own authentication logic here.

//        new android.os.Handler().postDelayed(
//                new Runnable() {
//                    public void run() {
//                        // On complete call either onLoginSuccess or onLoginFailed
//                        onLoginSuccess();
//                        // onLoginFailed();
//                        progressDialog.dismiss();
//                    }
//                }, 3000);
//    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
    }

//    private Boolean exit = false;
//    @Override
//    public void onBackPressed() {
//        if (exit) {
//            finish(); // finish activity
//        } else {
//            Toast.makeText(this, "Press Back again to Exit.",
//                    Toast.LENGTH_SHORT).show();
//            exit = true;
//            new Handler()
//// {
////                @Override
////                public void close() {
////
////                }
////
////                @Override
////                public void flush() {
////
////                }
////
////                @Override
////                public void publish(LogRecord record) {
////
////                }
////            }
// .postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    exit = false;
//                }
//            }, 3 * 1000);
//
//        }
//
//    }

//    public void onLoginSuccess() {
//        _loginButton.setEnabled(true);
//
//        //finish();
//        Intent i = new Intent(this, MapsActivity.class);
//        startActivity(i);
//    }

//    public void onLoginFailed() {
//        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();
//
//        _loginButton.setEnabled(true);
//    }

//    @Override
//    public void onBackPressed() {
//        new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert).setTitle("Exit")
//                .setMessage("Are you sure you want to exit?")
//                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        finish();
//                        System.exit(0);
//                    }
//                }).setNegativeButton("No", null).show();
//    }

    public boolean validate() {
        boolean valid = true;

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("Enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("Password should contain at least 4 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }


    @Override
    public void processUserLogin(User user) {
        if(user != null) {
            UserInfoPreferences userInfoPreferences = new UserInfoPreferences(getApplicationContext());
            userInfoPreferences.setEmail(user.getEmail());
            userInfoPreferences.setPassword(user.getPassword());
            userInfoPreferences.setFullname(user.getFullName());
            userInfoPreferences.setUserId(user.getId());
            userInfoPreferences.setProfilePicture(user.getProfilePictureUrl());

            //Set up user settings into SharedPreferences
            UserSettingsPreferences userSettingsPreferences
                    = new UserSettingsPreferences(getApplicationContext());
            userSettingsPreferences.setUserSettings(user.getUserSetting());

            Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, MapsActivity.class);
            startActivity(intent);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//            finish();
        }
        else
        {
            Toast.makeText(LoginActivity.this, "Login failed, Please enter valid credentials", Toast.LENGTH_SHORT).show();
//            Intent signup = new Intent(this, SignupActivity.class);
//            startActivity(signup);
        }
    }
}
