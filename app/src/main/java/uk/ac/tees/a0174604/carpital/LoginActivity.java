package uk.ac.tees.a0174604.carpital;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hbb20.CountryCodePicker;

public class LoginActivity extends AppCompatActivity {

    private Button loginBtn;
    TextView login_welcome;
    Button goSignupBtn;


    //    fields for validation
    private TextInputLayout phoneNum;
    private TextInputLayout pwd;
    private CountryCodePicker countryCodePicker;


    //    log tag
    String TAG = SignUpActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginBtn = findViewById(R.id.loginBtn);
        login_welcome = findViewById(R.id.login_welcome);
        goSignupBtn = findViewById(R.id.go_signup);
        phoneNum = findViewById(R.id.phoneNumber);
        pwd = findViewById(R.id.password);
        countryCodePicker = findViewById(R.id.country_code);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validateFields(phoneNum) || !validateFields(pwd)) {
                    return;
                } else {

//                  get the data the user enters in the login page
                    String userPhoneNo = phoneNum.getEditText().getText().toString().trim();

                    //            trim the first zero here
                    if (userPhoneNo.charAt(0) == '0') {
                        userPhoneNo = userPhoneNo.substring(1);
                    }

//                    get the country code
                    String countryCode = countryCodePicker.getSelectedCountryCodeWithPlus();
                    Log.d(TAG, countryCode);

//                    join the country code with the phone number
                    String userFullPhoneNo = countryCode + userPhoneNo;
                    Log.d(TAG, userFullPhoneNo);

                    String userPwd = pwd.getEditText().getText().toString().trim();

//                  firebase  database query... fetching the data using phonenumber
                    Query checkUser = FirebaseDatabase.getInstance().getReference("Users").orderByChild("phoneNumber").equalTo(userFullPhoneNo);

//                    fetch the user data
                    checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
//                            check if data exists
                            if(snapshot.exists()){
                                phoneNum.setError(null);
                                phoneNum.setErrorEnabled(false);

//                                get the password that exists in the database.
                                String dbPwd = snapshot.child(userFullPhoneNo).child("password").getValue(String.class);
                                if(dbPwd.equals(userPwd)){
                                    pwd.setError(null);
                                    pwd.setErrorEnabled(false);

//                                    test if the data is fetching properly
                                    String name = snapshot.child(userFullPhoneNo).child("name").getValue(String.class);
                                    String email = snapshot.child(userFullPhoneNo).child("email").getValue(String.class);
                                    String pNum = snapshot.child(userFullPhoneNo).child("phoneNumber").getValue(String.class);

                                    Toast.makeText(LoginActivity.this, name+"\n"+email+"\n"+pNum, Toast.LENGTH_LONG).show();


                                }
//                            password in correct
                                else {
                                    pwd.setError("Password entered is incorrect");
                                    Log.d(TAG, "password doesn't match");
                                }
                            }
//                            the account created doesn't exist
                            else {
//                                Toast.makeText(LoginActivity.this, "This user doesn't exist", Toast.LENGTH_SHORT).show();
                                Log.d(TAG, "user doesn't exist");
                                phoneNum.setError("This user doesn't exist");
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
}