package uk.ac.tees.a0174604.carpital;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hbb20.CountryCodePicker;

import org.w3c.dom.Text;

public class SignUpActivity extends AppCompatActivity {

    ImageView signup_backBtn;
    TextView signup_welcome;
    Button submitBtn;

    //  variables for validation
    TextInputLayout name;
    TextInputLayout email;
    TextInputLayout phoneNumber;
    TextInputLayout password;
    TextInputLayout confirmPassword;
    CountryCodePicker countryCodePicker;

//    linear layout
    LinearLayout signupLayout;

    String TAG = SignUpActivity.class.getSimpleName();

    private RelativeLayout progressBar;

    //    Broadcast receiver to check internet connection
    BroadcastReceiver broadcastReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

//        hooks for animation
        signup_backBtn = findViewById(R.id.signup_back_button);
        signup_welcome = findViewById(R.id.signup_welcome);
        submitBtn = findViewById(R.id.submit_btn);

//        hooks for retreiving data from signup form
        name = findViewById(R.id.username);
        email = findViewById(R.id.user_email);
        phoneNumber = findViewById(R.id.phoneNumber);
        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confirm_password);
        countryCodePicker = findViewById(R.id.country_code);
        progressBar = findViewById(R.id.progress_bar);
        broadcastReceiver = new CheckConnectionReceiver();





//        go back to previous page
        signup_backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                finish();
            }
        });
    }

    public void submitLogin(View view) {

        //                check internet connection
        registerNetworkBroadcast();

//        validate all fields
        if (!validateFields(name) || !validateFields(email) || !validateFields(phoneNumber) || !validateFields(password) || !validateFields(confirmPassword) || !validatePassword() || !validateEmail()) {
            return;
        }


        else {

            progressBar.setVisibility(View.VISIBLE);
            signupLayout = findViewById(R.id.signup_layout);


//
//                        Intent intent = new Intent(this,SignupSecondActivity.class);
//            startActivity(intent);

            //                                get the user's input in string form
            String userName = name.getEditText().getText().toString().trim();
            String userEmail = email.getEditText().getText().toString().trim();


            String userPhoneNo = phoneNumber.getEditText().getText().toString().trim();

            //            trim the first zero for phone number
            if (userPhoneNo.charAt(0)=='0'){
                userPhoneNo = userPhoneNo.substring(1);
            }

            String countryCode = countryCodePicker.getSelectedCountryCodeWithPlus();

            Log.d(TAG,countryCode);
            String userFullPhoneNo = countryCode+userPhoneNo;
            Log.d(TAG,userFullPhoneNo);

            String userPwd = password.getEditText().getText().toString().trim();
            String userConfirmPwd = confirmPassword.getEditText().getText().toString().trim();

//            check if user exists
            Query checkUser = FirebaseDatabase.getInstance().getReference("Users").orderByChild("phoneNumber").equalTo(userFullPhoneNo);

//            fetch user data
            checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()) {
                        phoneNumber.setError("This user already exists");
                        progressBar.setVisibility(View.INVISIBLE);

                    }
                    else {
                        phoneNumber.setError(null);
                        phoneNumber.setErrorEnabled(false);
                        signupLayout.setVisibility(View.INVISIBLE);
                        progressBar.setVisibility(View.INVISIBLE);

                        //            pass all fields from activity to otp fragment
                        Bundle bundle =  new Bundle();
                        bundle.putString("name", userName);
                        bundle.putString("email", userEmail);
                        bundle.putString("phoneNo", userFullPhoneNo);
                        bundle.putString("pwd", userPwd);
                        bundle.putString("confirmPwd", userConfirmPwd);
                        //                                to know the fragment it is coming from
                        bundle.putString("whichActivity",SignUpActivity.class.getSimpleName());

//            launch the otp fragment
//            instantiate fragment
                        OTPFragment otpFragment = OTPFragment.getInstance();

                        ////            send the bundle
                        otpFragment.setArguments(bundle);

//              fragment manager
                        FragmentManager fragmentManager = getSupportFragmentManager();

//           start Fragment Transaction
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

//            create fragment
                        fragmentTransaction.replace(R.id.fragment_container, otpFragment).commit();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(SignUpActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });


        }

    }

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

//    validate email
private boolean validateEmail() {
//        get the name as a string
    String val = email.getEditText().getText().toString().trim();
//    email regex
    String checkEmail = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    if (!val.matches(checkEmail)) {
        email.setError("Invalid Email");
        return false;
    } else {
        email.setError(null);
        email.setErrorEnabled(false);
        return true;
    }
}

//    validate password

    private boolean validatePassword() {
//        get the name as a string
        String val = password.getEditText().getText().toString().trim();
        String checkPassword = "^" +
                "(?=.*[a-zA-Z@#$%^&*+=])" + //any letter
//                "(?=\\s+$)" + //no white spaces
                ".{6,}" + //at  least 6 characters
                "$";

        String confirmVal = confirmPassword.getEditText().getText().toString().trim();
// check if password matches requirements.
        if (!val.matches(checkPassword)) {
            password.setError("Password should contain at least 6 characters!");
            return false;
        } else if (!confirmVal.equals(val)) {
            confirmPassword.setError("Password must match");
            return false;
        } else {
            password.setError(null);
            password.setErrorEnabled(false);
            return true;
        }
    }

    //    observer pattern
//    register for network broadcast receiver
    protected void registerNetworkBroadcast() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            registerReceiver(broadcastReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
    }

    protected void unregisterNetwork() {
        try {
            unregisterReceiver(broadcastReceiver);
        }catch (IllegalArgumentException e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterNetwork();
    }




//    public void nextSignupScreen(View view) {
//        Intent intent = new Intent(this,SignupSecondActivity.class);
//
////        add transition.
//        Pair[] pairs = new Pair[3];
//
////        for the backbutton transition
//        pairs[0] = new Pair<View,String>(signup_backBtn,"back_arrow_btn");
//        pairs[1] = new Pair<View,String>(signup_welcome,"transition_title_text");
//        pairs[2] = new Pair<View,String>(signup_nextBtn,"transition_next_login");
//
////        add transitions to activity
//        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SignUpActivity.this,pairs);
//
////        start activity
//        startActivity(intent,options.toBundle());
//
//
//
//    }
}