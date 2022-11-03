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

public class LoginActivity extends AppCompatActivity {

    Button loginBtn ;
    TextView login_welcome;
    Button goSignupBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginBtn = findViewById(R.id.loginBtn);
        login_welcome = findViewById(R.id.login_welcome);
        goSignupBtn = findViewById(R.id.go_signup);
    }

    public void goSignup(View view) {
        Intent intent = new Intent(this, SignUpActivity.class);

        //        add transition.
        Pair[] pairs = new Pair[3];

//        for the backbutton transition
        pairs[0] = new Pair<View,String>(login_welcome,"transition_title_text");
        pairs[1] = new Pair<View,String>(goSignupBtn,"back_arrow_btn");
        pairs[2] = new Pair<View,String>(loginBtn,"transition_next_login");

//        add transitions to activity
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(LoginActivity.this,pairs);

//        start activity
        startActivity(intent,options.toBundle());


    }
}