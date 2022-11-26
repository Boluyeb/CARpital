package uk.ac.tees.a0174604.carpital;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.database.core.view.Change;

public class HomeActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

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
                        setFragment(HomeFragment.newInstance());
                        break;

                    case R.id.miSearch:
                        setFragment(SearchFragment.newInstance());
                        break;

                    case R.id.miSell:
                        setFragment(SellFragment.newInstance());
                        break;

                    case R.id.miSalvage:
                        setFragment(SalvageFragment.newInstance());
                        break;

                    case R.id.miSettings:
                        setFragment(SettingsFragment.newInstance());
                        break;

                }

                return true;
            }
        });
    }

//    set fragment
    private void setFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout,fragment);
        fragmentTransaction.commit();

    }

}