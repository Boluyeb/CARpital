package uk.ac.tees.a0174604.carpital;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import at.favre.lib.crypto.bcrypt.BCrypt;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SetNewPasswordFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SetNewPasswordFragment extends Fragment {

    private TextInputLayout pwd;
    private TextInputLayout confirmPwd;
    private Button submitPwd;
    private RelativeLayout progressBar;

//    get phone number passed
    private String phoneNum;

    //    unique variable for single pattern to ensure that the object is created only one
    private static SetNewPasswordFragment unique;

    //    use singleton pattern to instantiate the forgotpasswordfragment
    public static SetNewPasswordFragment newInstance(){
        if (unique == null){
            unique = new SetNewPasswordFragment();
        }
        return unique;

    }

//    validate for empty values
    private boolean validateFields(TextInputLayout inputVal) {
    //        get the name as a string
        String val = inputVal.getEditText().getText().toString().trim();

        if (val.isEmpty()) {
            inputVal.setError("This field is required");
            return false;
        } else {
            inputVal.setError(null);
            inputVal.setErrorEnabled(false);
            return true;
        }
    }

//    validate password ensuring the passwords match
    private boolean validatePassword() {
//        get the name as a string
        String val = pwd.getEditText().getText().toString().trim();
        String checkPassword = "^" +
                "(?=.*[a-zA-Z@#$%^&*+=])" + //any letter
//                "(?=\\s+$)" + //no white spaces
                ".{6,}" + //at  least 6 characters
                "$";

        String confirmVal = confirmPwd.getEditText().getText().toString().trim();
// check if password matches requirements.
        if (!val.matches(checkPassword)) {
            pwd.setError("Password should contain at least 6 characters!");
            return false;
        } else if (!confirmVal.equals(val)) {
            confirmPwd.setError("Password must match");
            return false;
        } else {
            pwd.setError(null);
            pwd.setErrorEnabled(false);
            confirmPwd.setError(null);
            confirmPwd.setErrorEnabled(false);
            return true;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_set_new_password, container, false);

//        hooks
        pwd = rootView.findViewById(R.id.cred_pwd);
        confirmPwd = rootView.findViewById(R.id.cred_pwd_confirm);
        submitPwd = rootView.findViewById(R.id.submitBtn);
        progressBar = rootView.findViewById(R.id.progress_bar);

//        data from previous fragment
        phoneNum = getArguments().getString("phoneNo");

        submitPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validateFields(pwd) || !validateFields(confirmPwd) || !validatePassword()){
                    return;
                }
                else{
                    progressBar.setVisibility(View.VISIBLE);
//                    get user input
                    String userNewPwd = pwd.getEditText().getText().toString().trim();
//
//                    hashPassword
                    String hashPwd = BCrypt.withDefaults().hashToString(12, userNewPwd.toCharArray());

//                    update password in firebase database
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
                    databaseReference.child(phoneNum).child("password").setValue(hashPwd);

//                    launch fragment
                    RecoverPasswordSuccessFragment recoverPasswordSuccessFragment = RecoverPasswordSuccessFragment.getInstance();

//                    set fragment
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_holder, recoverPasswordSuccessFragment).addToBackStack(recoverPasswordSuccessFragment.toString()).commit();



                }
            }
        });

        return rootView;
    }
}