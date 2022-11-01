package uk.ac.tees.a0174604.carpital;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
                Intent intent = new Intent(SplashActivity.this, OnBoarding.class);
                startActivity(intent);
                finish();
            }
        },SPLASH_SCREEN);
    }
}