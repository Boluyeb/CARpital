package uk.ac.tees.a0174604.carpital;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashActivity extends AppCompatActivity {

    //    animation variables for splash screen
    Animation topAnim;
    Animation bottomAnim;
    ImageView image;
    TextView slogan;

    //        DURATION OF splash screen
    private static final int SPLASH_SCREEN = 5000;

//    using shared preferences to ensure the user can only see the onboarding page once
    SharedPreferences onBoardingScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //        change status bar color and make blend
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.seed));
        }

        //        animations
        topAnim = AnimationUtils.loadAnimation(this,R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(this,R.anim.bottom_animation);

//        get the view objects by id
        image = findViewById(R.id.logo);
        slogan = findViewById(R.id.slogan);

//        set animation
        image.setAnimation(topAnim);
        slogan.setAnimation(bottomAnim);

       //            handle delay
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

//                shared preferences for the onboarding screen
                onBoardingScreen = getSharedPreferences("onBoardingScreen",MODE_PRIVATE);

//                check if the user has come across the variable first time
                boolean  isFirstTime = onBoardingScreen.getBoolean("firstTime", true);

//              ensure user enters the onboarding once
                if(isFirstTime) {
                    SharedPreferences.Editor editor = onBoardingScreen.edit();
//                    set first time to false
                    editor.putBoolean("firstTime", false);
//                    commit changes to shared preferences
                    editor.commit();

                    Intent intent = new Intent(SplashActivity.this, OnBoarding.class);
                    startActivity(intent);
                    finish();
                }
                else {
                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }

////                comment later and use shared preferences
//                    Intent intent = new Intent(SplashActivity.this, OnBoarding.class);
//                    startActivity(intent);
//                    finish();


            }
        },SPLASH_SCREEN);
    }
}