//This activity handles buttons clicked in the settings fragment
package uk.ac.tees.a0174604.carpital;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class CustomSettingsActivity extends AppCompatActivity {

    private ImageView setBackBtn;
//    private ProgressDialog EditProfileFragment.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_settings);

        setBackBtn = findViewById(R.id.sett_back_btn);

//        go back to previous activity
        setBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent intent =  new Intent(CustomSettingsActivity.this, HomeActivity.class);
               startActivity(intent);
               finish();

            }
        });

//        get intent that started the activity
        Intent intent = getIntent();

//        get message from the intent
        String message = intent.getStringExtra(SettingsFragment.EXTRA_MESSAGE);
        String phoneNumber = intent.getStringExtra(SettingsFragment.EXTRA_PHONE);
        String email = intent.getStringExtra(SettingsFragment.EXTRA_EMAIL);
        String name = intent.getStringExtra(SettingsFragment.EXTRA_NAME);
        String profilePicture = intent.getStringExtra(SettingsFragment.EXTRA_PROFILEIMG);


        if (message.equals("editProfile")){
            Bundle bundle = new Bundle();
            bundle.putString("phoneNo",phoneNumber);
            bundle.putString("email",email);
            bundle.putString("name",name);
            bundle.putString("profilePicture",profilePicture);

            EditProfileFragment editProfileFragment = EditProfileFragment.newInstance();

            editProfileFragment.setArguments(bundle);

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_holder, editProfileFragment).addToBackStack(editProfileFragment.toString()).commit();


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
        else if(message.equals("contactMe")){
            ContactFragment contactFragment = ContactFragment.newInstance();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_holder, contactFragment).addToBackStack(contactFragment.toString()).commit();

        }


    }

//    //    create fragment
//    public void setFragment(Fragment fragment){
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.replace(R.id.fragment_holder, fragment).addToBackStack(fragment.toString()).commit();
//
//    }
@Override
public void onDestroy() {
    super.onDestroy();
//    if (!EditProfileFragment.progressDialog.isShowing()){
//        EditProfileFragment.progressDialog.dismiss();
//    }
//    if (EditProfileFragment.progressDialog != null && EditProfileFragment.progressDialog.isShowing()){
//        EditProfileFragment.progressDialog.dismiss();
//    }

}

}