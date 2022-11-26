package uk.ac.tees.a0174604.carpital;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import java.util.HashMap;

public class TestActivity extends AppCompatActivity {

    TextView textContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        textContent = findViewById(R.id.test_text);

//         get session content as session already created when logged in
        SessionManager sessionManager = new SessionManager(this, SessionManager.SESSION_USERSESSION);
        HashMap<String,String> userDetails = sessionManager.getUsersDetailFromSession();

//        get session values using
        String name = userDetails.get(SessionManager.KEY_NAME);
        String email = userDetails.get(SessionManager.KEY_EMAIL);
        String pNum = userDetails.get(SessionManager.KEY_PHONENUMBER);

        textContent.setText(name+"\n"+email+"\n"+pNum);







    }
}