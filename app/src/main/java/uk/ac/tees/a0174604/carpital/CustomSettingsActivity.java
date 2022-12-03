//This activity handles buttons clicked in the settings fragment
package uk.ac.tees.a0174604.carpital;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class CustomSettingsActivity extends AppCompatActivity {

    private ImageView setBackBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_settings);

        setBackBtn = findViewById(R.id.sett_back_btn);

//        go back to previous activity
        setBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();

            }
        });

//        get intent that started the activity
        Intent intent = getIntent();

//        get message from the intent
        String message = intent.getStringExtra(SettingsFragment.EXTRA_MESSAGE);

        if (message.equals("editProfile")){
            EditProfileFragment editProfileFragment = EditProfileFragment.newInstance();
            setFragment(editProfileFragment);
        }


    }

    //    create fragment
    public void setFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_holder, fragment).addToBackStack(fragment.toString()).commit();

    }
}