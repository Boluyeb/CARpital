package uk.ac.tees.a0174604.carpital;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.database.core.view.Change;

public class HomeActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
//    to close all activities
    BroadcastReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        bottomNavigationView = findViewById(R.id.btn_Nav);

//        set home fragment as default
        setFragment(HomeFragment.newInstance());

//        bottom navigation
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch(item.getItemId()) {
                    case R.id.miHome:
                        //        hide map floating action button
                        if(SearchFragment.fab != null && SearchFragment.fab.getVisibility() == View.VISIBLE){
                            SearchFragment.fab.setVisibility(View.INVISIBLE);
                        }

                        setFragment(HomeFragment.newInstance());
                        break;

                    case R.id.miSearch:
                        setFragment(SearchFragment.newInstance());
                        break;

                    case R.id.miSell:
                        if(SearchFragment.fab != null && SearchFragment.fab.getVisibility() == View.VISIBLE){
                            SearchFragment.fab.setVisibility(View.INVISIBLE);
                        }


                        setFragment(SellFragment.newInstance());
                        break;

                    case R.id.miSalvage:
                        if(SearchFragment.fab != null && SearchFragment.fab.getVisibility() == View.VISIBLE){
                            SearchFragment.fab.setVisibility(View.INVISIBLE);
                        }


                        setFragment(SalvageFragment.newInstance());
                        break;

                    case R.id.miSettings:
                        if(SearchFragment.fab != null && SearchFragment.fab.getVisibility() == View.VISIBLE){
                            SearchFragment.fab.setVisibility(View.INVISIBLE);
                        }


                        setFragment(SettingsFragment.newInstance());
                        break;

                }

                return true;
            }
        });

//        broadcast receiver to end activity
//        IntentFilter filter = new IntentFilter();
//
//        filter.addAction("com.example.CUSTOM_INTENT");
//        registerReceiver(receiver, filter);
//        receiver = new BroadcastReceiver() {
//
//            @Override
//            public void onReceive(Context context, Intent intent) {
//                Log.d("==>","Broadcast Recieved.");
//                finish();
//
//            }
//        };
    }

//    set fragment
    private void setFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout,fragment);
        fragmentTransaction.commit();

    }

//    public void finish() {
//        super.finish();
//    }

}