package uk.ac.tees.a0174604.carpital;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hbb20.CountryCodePicker;

import java.util.HashMap;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class LoginActivity extends AppCompatActivity {

    private Button loginBtn;
    TextView login_welcome;
    Button goSignupBtn;
    private CheckBox rememberMe;

    //    fields for validation
    private TextInputLayout phoneNum;
    private TextInputLayout pwd;
    private CountryCodePicker countryCodePicker;
    private TextInputEditText phoneNumEdit;
    private TextInputEditText pwdEdit;

    //    forgot password btn
    private Button forgotBtn;

    //    log tag
    String TAG = LoginActivity.class.getSimpleName();
    //progress bar
    private RelativeLayout progressBar;

    //    Broadcast receiver to check internet connection
    BroadcastReceiver broadcastReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginBtn = findViewById(R.id.loginBtn);
        login_welcome = findViewById(R.id.login_welcome);
        goSignupBtn = findViewById(R.id.go_signup);
        phoneNum = findViewById(R.id.phoneNumber);
        phoneNumEdit = findViewById(R.id.phone_input);
        pwd = findViewById(R.id.password);
        pwdEdit = findViewById(R.id.pwd_input);

        countryCodePicker = findViewById(R.id.country_code);
        forgotBtn = findViewById(R.id.forgot_password);
        progressBar = findViewById(R.id.progress_bar);
        broadcastReceiver = new CheckConnectionReceiver();
        rememberMe = findViewById(R.id.remeber_check);
//        countryCodePicker.setCountryPreference("+44");

//        check if phone number and password is already stored in shared preference phone number and if so get it and display it
        SessionManager sessionManagerFind = new SessionManager(LoginActivity.this, SessionManager.SESSION_REMEMBERME);
        if(sessionManagerFind.checkRemember()){
            HashMap<String,String> rememberDetails = sessionManagerFind.getDetailsFromRememberSession();
            phoneNumEdit.setText(rememberDetails.get(SessionManager.KEY_REMEMBERPHONENUMBER));
            pwdEdit.setText(rememberDetails.get(SessionManager.KEY_REMEMBERPWD));
            Log.d(TAG,"found me");
            Log.d(TAG,rememberDetails.get(SessionManager.KEY_REMEMBERPWD));

        }


        forgotBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });


        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                check internet connection
                registerNetworkBroadcast();

//                validate fields
                if (!validateFields(phoneNum) || !validateFields(pwd)) {
                    return;
                } else {
                    progressBar.setVisibility(View.VISIBLE);

//                  get the data the user enters in the login page
                    String userPhoneNo = phoneNum.getEditText().getText().toString().trim();
                    String userPwd = pwd.getEditText().getText().toString().trim();

                    //            trim the first zero here
                    if (userPhoneNo.charAt(0) == '0') {
                        userPhoneNo = userPhoneNo.substring(1);
                    }

//                    get the country code
                    String countryCode = countryCodePicker.getSelectedCountryCodeWithPlus();

                    Log.d(TAG, countryCode);

//                    join the country code with the phone number
                    String userFullPhoneNo = countryCode + userPhoneNo;


                    if(rememberMe.isChecked()){
                        SessionManager sessionManagerRemember = new SessionManager(LoginActivity.this, SessionManager.SESSION_REMEMBERME);
//                        store in shared preference
                        sessionManagerRemember.createRemeberMeSession(userPhoneNo, userPwd);
                        Log.d(TAG, "doing this");
                    }

//                  firebase  database query... fetching the data using phonenumber
                    Query checkUser = FirebaseDatabase.getInstance().getReference("Users").orderByChild("phoneNumber").equalTo(userFullPhoneNo);

//                    fetch the user data
                    checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
//                            check if data exists
                            if (snapshot.exists()) {
                                phoneNum.setError(null);
                                phoneNum.setErrorEnabled(false);

//                                get the password from database and ensure it matches the password in the database.
                                String dbPwd = snapshot.child(userFullPhoneNo).child("password").getValue(String.class);

                                BCrypt.Result result = BCrypt.verifyer().verify(userPwd.toCharArray(), dbPwd);

                                if (result.verified) {
                                    pwd.setError(null);
                                    pwd.setErrorEnabled(false);

////                                   get the remaining data from the database
                                    String name = snapshot.child(userFullPhoneNo).child("name").getValue(String.class);
                                    String email = snapshot.child(userFullPhoneNo).child("email").getValue(String.class);
                                    String pNum = snapshot.child(userFullPhoneNo).child("phoneNumber").getValue(String.class);
                                    String profileImg = snapshot.child(userFullPhoneNo).child("profilePicture").getValue(String.class);

//                                    Toast.makeText(LoginActivity.this, name+"\n"+email+"\n"+pNum, Toast.LENGTH_LONG).show();
//
//                                    Create a Session
                                    SessionManager sessionManager = new SessionManager(LoginActivity.this, SessionManager.SESSION_USERSESSION);
//                                  create login session by adding data into shared preferences
                                    sessionManager.createLoginSession(name, email, pNum, dbPwd, profileImg);

                                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                    startActivity(intent);
                                    finish();


                                }
//                            password in correct
                                else {
                                    pwd.setError("Password entered is incorrect");
                                    Log.d(TAG, "password doesn't match");
                                    progressBar.setVisibility(View.INVISIBLE);
                                }
                            }
//                            the account created doesn't exist
                            else {
//                                Toast.makeText(LoginActivity.this, "This user doesn't exist", Toast.LENGTH_SHORT).show();
                                Log.d(TAG, "user doesn't exist");
                                phoneNum.setError("This user doesn't exist");
                                progressBar.setVisibility(View.INVISIBLE);
                            }

                        }


                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(LoginActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        });


    }

    public void goSignup(View view) {
        Intent intent = new Intent(this, SignUpActivity.class);

        //        add transition.
        Pair[] pairs = new Pair[3];

//        for the backbutton transition
        pairs[0] = new Pair<View, String>(login_welcome, "transition_title_text");
        pairs[1] = new Pair<View, String>(goSignupBtn, "back_arrow_btn");
        pairs[2] = new Pair<View, String>(loginBtn, "transition_next_login");

//        add transitions to activity
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(LoginActivity.this, pairs);

//        start activity
        startActivity(intent, options.toBundle());


    }

    //    validate for empty fields
//    validate for empty values
    private boolean validateFields(TextInputLayout inputVal) {
//        get the name as a string
        String val = inputVal.getEditText().getText().toString().trim();

        if (val.isEmpty()) {
            inputVal.setError("This field is required");
            return false;
        } else {
            inputVal.setError(null);
            inputVal.setErrorEnabled(false);
            return true;
        }
    }

    //    observer pattern
//    register for network broadcast receiver
    protected void registerNetworkBroadcast() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            registerReceiver(broadcastReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
    }

    protected void unregisterNetwork() {
        try {
            unregisterReceiver(broadcastReceiver);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterNetwork();
    }
}