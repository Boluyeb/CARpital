package uk.ac.tees.a0174604.carpital;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chaos.view.PinView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

import at.favre.lib.crypto.bcrypt.BCrypt;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OTPFragment#getInstance} factory method to
 * create an instance of this fragment.
 */
public class OTPFragment extends Fragment {
    PinView pinFromUser;

//    unique variable for single pattern to ensure that the object is created only one
    private static OTPFragment unique;

//    variables for data passed from signup activity
    String phoneNum;
    String name;
    String email;
    String pwd;
    String whichActivity;


    private FirebaseAuth mAuth;
//    sent code
    String codeBySystem;

    Button goToNextScreen;

    TextView otpDesc;

    private String TAG = OTPFragment.class.getSimpleName();

    LinearLayout signupLayout;

    //    private constructor for fragment
    private OTPFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_o_t_p, container, false);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

//        hooks
        pinFromUser = rootView.findViewById(R.id.otp_pin);
        goToNextScreen = rootView.findViewById(R.id.verify_otp);
        otpDesc = rootView.findViewById(R.id.otpDesc);

//        data from the signup activity
         phoneNum = getArguments().getString("phoneNo");
         name = getArguments().getString("name");
         email = getArguments().getString("email");
         pwd = getArguments().getString("pwd");
        whichActivity = getArguments().getString("whichActivity");




//        display on otp screen
        otpDesc.setText("Code has been sent to "+phoneNum);

        sendVerificationCodeToUser(phoneNum);

//        go to

        goToNextScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userCode = pinFromUser.getText().toString();
//        verify user entered code
                if(!userCode.isEmpty()){
                    verifyCode(userCode);

                }
            }
        });

//
        return rootView;
    }

//    allows to send verification code to user
    private void sendVerificationCodeToUser(String phoneNum) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNum)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(getActivity())                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

//    create callbacks
        private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
    @Override

//     sent code
    public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
        super.onCodeSent(s, forceResendingToken);
        codeBySystem = s;
//        Toast.makeText(getActivity(),"Kindly check device for OTP", Toast.LENGTH_LONG).show();
    }

    @Override
//    auto fill by sms
    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
        String code = phoneAuthCredential.getSmsCode();
//        set the text from sms to the pin view 
        if(code!= null) {
            pinFromUser.setText(code);
            verifyCode(code);

        }
    }

    @Override
    public void onVerificationFailed(@NonNull FirebaseException e) {
//        failed verification
        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
    }
};

//    verifies the code the user enters
    private void verifyCode(String code) {
//        comparing the code sent and the code entered manually or set from sms
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codeBySystem,code);
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {

        FirebaseAuth.getInstance().getInstance().signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                            Log.d(TAG,"Cred is working ");

//                            check what the activity" is before moving to the next thing
                            if (whichActivity.equals("SignUpActivity")){

//                                store new users
                                storeNewUsersData();

                                Intent intent =  new Intent(getActivity(), LoginActivity.class);
                                startActivity(intent);

//                            close activity
                                getActivity().finish();
                            }
                            else if (whichActivity.equals("ForgotPasswordActivity")) {
                                //                                pass the fullphonenumber to the next fragment
                                Bundle bundle = new Bundle();
                                bundle.putString("phoneNo",phoneNum);
//                                to know the fragment it is coming from
                                bundle.putString("whichActivity",ForgotPasswordActivity.class.getSimpleName());

//                                                              instantiate next fragment

                                SetNewPasswordFragment setNewPasswordFragment = SetNewPasswordFragment.newInstance();

//                              send the bundle
                                setNewPasswordFragment.setArguments(bundle);
//
//                                set fragment
                                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                fragmentTransaction.replace(R.id.fragment_holder, setNewPasswordFragment).addToBackStack(setNewPasswordFragment.toString()).commit();

                            }
                            else if (whichActivity.equals("CustomSettingsActivity")){
                                Bundle bundle = new Bundle();
                                bundle.putString("phoneNo",phoneNum);
//                                to know the fragment it is coming from
                                bundle.putString("whichActivity",ForgotPasswordActivity.class.getSimpleName());

//                                                              instantiate next fragment

                                SetNewPasswordFragment setNewPasswordFragment = SetNewPasswordFragment.newInstance();

//                              send the bundle
                                setNewPasswordFragment.setArguments(bundle);
//
//                                set fragment
                                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                fragmentTransaction.replace(R.id.fragment_holder, setNewPasswordFragment).addToBackStack(setNewPasswordFragment.toString()).commit();

                            }

//

                } else {
                    Log.d(TAG,"Cred is not working ");
                    Toast.makeText(getActivity(), "Verification Code Entered Invalid.", Toast.LENGTH_LONG).show();

//                    configure try again button that might resend otp.

//                    going back to signup activity
//                    signupLayout = getActivity().findViewById(R.id.signup_layout);
//                    signupLayout.setVisibility(View.VISIBLE);

//                    close the fragment
//                    getChildFragmentManager().beginTransaction().remove(OTPFragment.this).commit();


                }

            }
        });


    }

    private void storeNewUsersData() {
//        start pointing to database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        point to the database reference (table)
        DatabaseReference reference = database.getReference("Users");

//        hashedpassword
        String hashPwd = BCrypt.withDefaults().hashToString(12, pwd.toCharArray());

//        add new users use the UserDbClass
        UserDbClass addNewUser = new UserDbClass(name,email,phoneNum,hashPwd,"");

//        set value and add user id as phone number
        reference.child(phoneNum).setValue(addNewUser);

    }

    //try singleton design approach to ensure that the instance is created only once.
    //    a method that will be used to instatntiate the fragment
    public static OTPFragment getInstance() {
        if (unique == null){
            unique = new OTPFragment();
        }
        return unique;
    }

}