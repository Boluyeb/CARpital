package uk.ac.tees.a0174604.carpital;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RecoverPasswordSuccessFragment#getInstance} factory method to
 * create an instance of this fragment.
 */
public class RecoverPasswordSuccessFragment extends Fragment {

    private Button goToLogin;
    //    unique variable for single pattern to ensure that the object is created only one
    private static RecoverPasswordSuccessFragment unique;

    //    use singleton pattern to instantiate the forgotpasswordfragment
    public static RecoverPasswordSuccessFragment getInstance(){
        if (unique == null){
            unique = new RecoverPasswordSuccessFragment();
        }
        return unique;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_recover_password_success, container, false);
        goToLogin = rootView.findViewById(R.id.go_to_login);

        goToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SessionManager logoutManager = new SessionManager(getActivity(),SessionManager.SESSION_USERSESSION);
//                check if logged in first.. check if preference exists
                if(logoutManager.checkLogin()) {
                    logoutManager.logoutUserFromSession();
                    FirebaseAuth.getInstance().signOut();
                    Intent intent = new Intent(getActivity(), LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
//                   end activity
                else{
                        getActivity().finish();
                    }


            }
        });

        return rootView;
    }
}