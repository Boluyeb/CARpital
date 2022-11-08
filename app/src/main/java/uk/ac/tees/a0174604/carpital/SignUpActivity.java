package uk.ac.tees.a0174604.carpital;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;

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
//        validate all fields

        if (!validateFields(name) | !validateFields(email) | !validateFields(phoneNumber) | !validateFields(password) | !validateFields(confirmPassword) | !validatePassword() | !validateEmail()) {
            return;
        }
        else {
            Intent intent = new Intent(this,SignupSecondActivity.class);
            startActivity(intent);
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