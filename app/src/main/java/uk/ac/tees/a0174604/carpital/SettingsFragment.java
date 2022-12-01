package uk.ac.tees.a0174604.carpital;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_settings, container, false);
        Button logout_btn = rootView.findViewById(R.id.logout_btn);

//        set the user name to appear on the profile page
        TextView userName = rootView.findViewById(R.id.user_name);
        TextView userPhone = rootView.findViewById(R.id.user_phone);

//        get user details
        SessionManager sessionManager = new SessionManager(getActivity(), SessionManager.SESSION_USERSESSION);
        if (sessionManager.checkLogin()){
//            get the user details
            HashMap<String,String> userDetails = sessionManager.getUsersDetailFromSession();
             name = userDetails.get(SessionManager.KEY_NAME);
             phoneNumber = userDetails.get(SessionManager.KEY_PHONENUMBER);

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
                    Intent intent = new Intent(getActivity(),LoginActivity.class);
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