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
import android.widget.Toast;

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
        String phoneNumber = intent.getStringExtra(SettingsFragment.EXTRA_PHONE);


        if (message.equals("editProfile")){
            EditProfileFragment editProfileFragment = EditProfileFragment.newInstance();
            setFragment(editProfileFragment);
        }
        else if (message.equals("changePassword")){
//            OTPFragment otpFragment = OTPFragment.getInstance();
//            Bundle bundle = new Bundle();
//            bundle.putString("phoneNo",phoneNumber);
////                                to know the fragment it is coming from
//            bundle.putString("whichActivity",HomeActivity.class.getSimpleName());
//            setFragment(otpFragment);
//            Toast.makeText(CustomSettingsActivity.this,"This is working", Toast.LENGTH_SHORT).show();

            Bundle bundle = new Bundle();
            bundle.putString("phoneNo",phoneNumber);
//                                to know the fragment it is coming from
            bundle.putString("whichActivity",CustomSettingsActivity.class.getSimpleName());
            OTPFragment otpFragment = OTPFragment.getInstance();
//
//                              send the bundle
            otpFragment.setArguments(bundle);
//
//                                set fragment
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_holder, otpFragment).addToBackStack(otpFragment.toString()).commit();

//            do session manager for logout and do firebase logout
//            SessionManager sessionManager =


        }


    }

    //    create fragment
    public void setFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_holder, fragment).addToBackStack(fragment.toString()).commit();

    }
}