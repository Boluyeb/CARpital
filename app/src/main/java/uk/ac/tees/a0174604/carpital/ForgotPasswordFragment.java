package uk.ac.tees.a0174604.carpital;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hbb20.CountryCodePicker;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ForgotPasswordFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ForgotPasswordFragment extends Fragment {

    //    unique variable for single pattern to ensure that the object is created only one
    private static ForgotPasswordFragment unique;


    private TextInputLayout phoneNumber;
    private CountryCodePicker countryCodePicker;
    private Button nextBtn;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_forgot_password, container, false);

        phoneNumber = rootView.findViewById(R.id.phoneNumber);
        countryCodePicker = rootView.findViewById(R.id.country_code);
        nextBtn = rootView.findViewById(R.id.nextBtn);

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                get user input
                String userPhoneNum = phoneNumber.getEditText().getText().toString().trim();

//                validate for empty
                if (userPhoneNum.isEmpty()){
                    phoneNumber.setError("This field is required");
                }
                else {
                    phoneNumber.setError(null);
                    phoneNumber.setErrorEnabled(false);

                    //            trim the first zero here
                    if (userPhoneNum.charAt(0)=='0'){
                        userPhoneNum = userPhoneNum.substring(1);
                    }
                    String countryCode = countryCodePicker.getSelectedCountryCodeWithPlus();
                    String userFullPhoneNum = countryCode+userPhoneNum;

//                    check if user exists
                    Query checkUser = FirebaseDatabase.getInstance().getReference("Users").orderByChild("phoneNumber").equalTo(userFullPhoneNum);

//                    fetch user data
                    checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
//                            check if user exists
                            if(snapshot.exists()){
                                phoneNumber.setError(null);
                                phoneNumber.setErrorEnabled(false);

//                                pass the fullphonenumber to the next fragment
                                Bundle bundle = new Bundle();
                                bundle.putString("phoneNo",userFullPhoneNum);
//                                to know the fragment it is coming from
                                bundle.putString("whichActivity",ForgotPasswordActivity.class.getSimpleName());



////                                instantiate fragment
                                OTPFragment otpFragment = OTPFragment.getInstance();
//
//                              send the bundle
                                otpFragment.setArguments(bundle);
//
//                                set fragment
                                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                fragmentTransaction.replace(R.id.fragment_holder, otpFragment).addToBackStack(otpFragment.toString()).commit();




                            }
                            else {
                                phoneNumber.setError("This user doesn't exist");
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }
        });


        return rootView;
    }

//    use singleton pattern to instantiate the forgotpasswordfragment
    public static ForgotPasswordFragment newInstance(){
        if (unique == null){
            unique = new ForgotPasswordFragment();
        }
        return unique;

    }


}