package uk.ac.tees.a0174604.carpital;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class OnBoarding extends AppCompatActivity {

    ViewPager viewPager;
    LinearLayout dotsLayout;
//    log any errors
    public static String LOG_TAG = OnBoarding.class.getSimpleName();

//    object of the slide adapter
    OnboardingSlidesAdapter sliderAdapter;

//    create navigation dots
    TextView[] dots;

    Button getStartedBtn;

//     animation
    Animation animation;

//    get current slide position
    private int currentPosition;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        remove status bar
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_on_boarding);

        //        hooks
        viewPager =  findViewById(R.id.slider);
        dotsLayout =  findViewById(R.id.dots);
        getStartedBtn = findViewById(R.id.get_started_button);
//
//      call adapter object and pass the OnBoarding activity through it
        sliderAdapter = new OnboardingSlidesAdapter(this);

//        viewPager.set
        viewPager.setAdapter(sliderAdapter);

//        initiate dot position at 0
        addDots(0);
        viewPager.addOnPageChangeListener(changeListener);
    }

//    this button advances to the login page
    public void skip(View view){
        Intent intent =  new Intent(this,LoginActivity.class);
        startActivity(intent);
        finish();
    }

//    this button advances slide on the onboarding page
    public void next(View view){
        viewPager.setCurrentItem(currentPosition + 1);
    }

//    to create dots
    private void addDots(int position) {
        dots = new TextView[4];
        dotsLayout.removeAllViews();

//        loop through dots for each page
        for(int i=0; i<dots.length; i++){
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226"));
            dots[i].setTextSize(35);

            dotsLayout.addView(dots[i]);
        }
        if (dots.length>0) {
            dots[position].setTextColor(getResources().getColor(R.color.md_theme_light_onPrimary));
        }
    }

//    a listener to watch out for page change
    ViewPager.OnPageChangeListener changeListener = new ViewPager.OnPageChangeListener(){

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            addDots(position);
            currentPosition = position;

//            hide button until last slide
       if (position == 0){
                getStartedBtn.setVisibility(View.INVISIBLE);
            }
            else if (position == 1){
                getStartedBtn.setVisibility(View.INVISIBLE);
            }
            else if (position == 2){
                getStartedBtn.setVisibility(View.INVISIBLE);
            }
            else {
                animation = AnimationUtils.loadAnimation(OnBoarding.this,R.anim.bottom_animation);
//                animation for button
                getStartedBtn.setAnimation(animation);
                getStartedBtn.setVisibility(View.VISIBLE);
            }

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
}