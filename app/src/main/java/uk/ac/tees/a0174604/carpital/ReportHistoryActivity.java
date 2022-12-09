package uk.ac.tees.a0174604.carpital;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class ReportHistoryActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ImageView goBackBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_history);

        recyclerView = findViewById(R.id.recycler_report);
        goBackBtn = findViewById(R.id.back_home);

        getData();

        goBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                finish();
            }
        });



    }

    private void getData(){
        recyclerView.setAdapter(new ReportHistoryAdapter(getApplicationContext(), ReportDatabaseClass.getDatabase(getApplicationContext()).getDao().getAllData()));
    }
}