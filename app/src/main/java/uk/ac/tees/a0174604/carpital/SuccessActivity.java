package uk.ac.tees.a0174604.carpital;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SuccessActivity extends AppCompatActivity {

    private TextView successMessage;
    private Button goHomeBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success);

        successMessage = findViewById(R.id.success_message);
        goHomeBtn = findViewById(R.id.go_to_home);

        Intent intent = getIntent();
       String message = intent.getStringExtra(SellFragment.EXTRA_MESSAGE);
        successMessage.setText(message);

        goHomeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SuccessActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();

            }
        });

    }
}