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

public class SignUpActivity extends AppCompatActivity {

    ImageView signup_backBtn;
    TextView signup_welcome;
    Button signup_nextBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        signup_backBtn = findViewById(R.id.signup_back_button);
        signup_welcome = findViewById(R.id.signup_welcome);
        signup_nextBtn = findViewById(R.id.next_signup_btn);
    }

    public void nextSignupScreen(View view) {
        Intent intent = new Intent(this,SignupSecondActivity.class);

//        add transition.
        Pair[] pairs = new Pair[3];

//        for the backbutton transition
        pairs[0] = new Pair<View,String>(signup_backBtn,"back_arrow_btn");
        pairs[1] = new Pair<View,String>(signup_welcome,"transition_title_text");
        pairs[2] = new Pair<View,String>(signup_nextBtn,"transition_next_login");

//        add transitions to activity
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SignUpActivity.this,pairs);

//        start activity
        startActivity(intent,options.toBundle());



    }
}