package uk.ac.tees.a0174604.carpital;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class ForgotPasswordActivity extends AppCompatActivity {

    private ForgotPasswordFragment forgotPasswordFragment;
    private FrameLayout mainFrame;
    private ImageView backToLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        mainFrame = (FrameLayout) findViewById(R.id.fragment_holder);
        backToLogin = findViewById(R.id.forgot_back_btn);

        backToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

//        instantiate and create fragment
        forgotPasswordFragment = ForgotPasswordFragment.newInstance();
        setFragment(forgotPasswordFragment);

    }


//    create fragment
    public void setFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_holder, fragment).addToBackStack(fragment.toString()).commit();

    }
}