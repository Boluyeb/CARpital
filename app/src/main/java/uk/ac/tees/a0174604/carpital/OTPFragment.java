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

import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OTPFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OTPFragment extends Fragment {
    PinView pinFromUser;

    private FirebaseAuth mAuth;
//    sent code
    String codeBySystem;

    Button goToNextScreen;

    private String TAG = OTPFragment.class.getSimpleName();

    LinearLayout signupLayout;

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

//        get phone number from the signup activity
        String phoneNum = getArguments().getString("phoneNo");

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
                            Intent intent =  new Intent(getActivity(), SignupSecondActivity.class);
                            startActivity(intent);

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


    //    a method that will be used to instatntiate the fragment
    public static OTPFragment newInstance() {
        return new OTPFragment();
    }

}