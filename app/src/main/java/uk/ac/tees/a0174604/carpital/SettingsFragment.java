package uk.ac.tees.a0174604.carpital;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment {
    private static SettingsFragment unique;
    private String name;
    private String phoneNumber;
    private String profileImage;
    private String email;
    private ImageView displayImage;
    String message;


//    intent message
    public static final String EXTRA_MESSAGE = "Carpital.SettingsFragment.EditProfile";
    public static final String EXTRA_PHONE = "Carpital.SettingsFragment.phoneNumber";
    public static final String EXTRA_EMAIL= "Carpital.SettingsFragment.email";
    public static final String EXTRA_PROFILEIMG= "Carpital.SettingsFragment.profileImg";
    public static final String EXTRA_NAME= "Carpital.SettingsFragment.name";

    private DialogInterface.OnClickListener dialogClickListener;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_settings, container, false);
        Button logout_btn = rootView.findViewById(R.id.logout_btn);

//        set the user name to appear on the profile page
        TextView userName = rootView.findViewById(R.id.user_name);
        TextView userPhone = rootView.findViewById(R.id.user_phone);

        displayImage = rootView.findViewById(R.id.user_photo);


//       edit profile
        ConstraintLayout editProfile = rootView.findViewById(R.id.edit_profile_btn);

        ConstraintLayout changePassword = rootView.findViewById(R.id.change_password);

        ConstraintLayout contactMe = rootView.findViewById(R.id.contact_me);

        ConstraintLayout deactivateAccount = rootView.findViewById(R.id.deactivate_acc);

//deactivate account
        deactivateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i){
                            case DialogInterface.BUTTON_POSITIVE:
                                Toast.makeText(getActivity(), "Your account has been deleted", Toast.LENGTH_SHORT).show();
//                                check if user exists
                                Query checkUser = FirebaseDatabase.getInstance().getReference("Users").orderByChild("phoneNumber").equalTo(phoneNumber);
                                checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (snapshot.exists()){
                                            for (DataSnapshot snapshot1 : snapshot.getChildren()){
                                                snapshot1.getRef().removeValue();

                                                Intent intent = new Intent(getActivity(), LoginActivity.class);
                                                startActivity(intent);
                                                getActivity().finish();
                                            }
                                        }

                                        else {
                                            Toast.makeText(getActivity(), "Cannot find your account", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                                break;
                            case DialogInterface.BUTTON_NEGATIVE:
                                dialogInterface.dismiss();
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Are you sure you want to delete your account?")
                        .setPositiveButton("Yes", dialogClickListener)

                        .setNegativeButton("No", dialogClickListener)

                        .show();
            }
        });

        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CustomSettingsActivity.class);
                message = "changePassword";
                intent.putExtra(EXTRA_MESSAGE, message);
                intent.putExtra(EXTRA_PHONE, phoneNumber);
                startActivity(intent);
                getActivity().finish();
            }
        });


//          contact me
        contactMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CustomSettingsActivity.class);
                message = "contactMe";
                intent.putExtra(EXTRA_MESSAGE, message);
                startActivity(intent);
                getActivity().finish();
            }
        });


//     edit me
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                start custom settings activity
                Intent intent = new Intent(getActivity(),CustomSettingsActivity.class);
//                pass message to the activity to know the custom settings to launch
                message = "editProfile";
                intent.putExtra(EXTRA_MESSAGE,message);
                intent.putExtra(EXTRA_PHONE, phoneNumber);
                intent.putExtra(EXTRA_EMAIL, email);
                intent.putExtra(EXTRA_PROFILEIMG, profileImage);
                intent.putExtra(EXTRA_NAME,name);
//                start the activity

//                working code
                startActivity(intent);
                getActivity().finish();



            }
        });


//        get user details
        SessionManager sessionManager = new SessionManager(getActivity(), SessionManager.SESSION_USERSESSION);
        if (sessionManager.checkLogin()){
//            get the user details
            HashMap<String,String> userDetails = sessionManager.getUsersDetailFromSession();
             name = userDetails.get(SessionManager.KEY_NAME);
             phoneNumber = userDetails.get(SessionManager.KEY_PHONENUMBER);
             profileImage = userDetails.get(SessionManager.KEY_PROFILEIMG);
             email = userDetails.get(SessionManager.KEY_EMAIL);


//load image using glide
             if(!profileImage.isEmpty()){
                 Glide.with(displayImage.getContext())
                         .load(profileImage)
                         .placeholder(R.drawable.user_img)
                         .centerCrop()
                         .error(R.drawable.user_img)
                         .into(displayImage);

             }

             userName.setText(name);
             userPhone.setText(phoneNumber);

        }


        logout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                check if session exists then end the session
                SessionManager logoutManager = new SessionManager(getActivity(),SessionManager.SESSION_USERSESSION);
//                check if logged in first.. check if preference exists
                if(logoutManager.checkLogin()){
                    logoutManager.logoutUserFromSession();
                    FirebaseAuth.getInstance().signOut();
                    Intent intent = new Intent(getActivity(),LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
//                   end activity
                    getActivity().finish();
                }
            }
        });

        return rootView;
    }

    //    use singleton pattern to instantiate the fragment
    public static SettingsFragment newInstance(){
        if (unique == null){
            unique = new SettingsFragment();
        }
        return unique;

    }



}