package uk.ac.tees.a0174604.carpital;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.text.Html;
import android.widget.LinearLayout;
import android.widget.TextView;

public class OnBoarding extends AppCompatActivity {

    ViewPager viewPager;
    LinearLayout dotsLayout;

//    object of the slide adapter
    OnboardingSlidesAdapter sliderAdapter;

//    create navigation dots
    TextView[] dots;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_boarding);

        //        hooks
        viewPager =  findViewById(R.id.slider);
        dotsLayout =  findViewById(R.id.dots);
//
//      call adapter object and pass the OnBoarding activity through it
        sliderAdapter = new OnboardingSlidesAdapter(this);

//        viewPager.set
        viewPager.setAdapter(sliderAdapter);

//        initiate dot position at 0
        addDots(0);
        viewPager.addOnPageChangeListener(changeListener);
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
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
}