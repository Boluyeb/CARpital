package uk.ac.tees.a0174604.carpital;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    private static HomeFragment unique;

//    recycler view adapters
  private  RecyclerView.Adapter brandAdapter;
  private  RecyclerView.Adapter recentAdapter;

//  recyclerviews
    private RecyclerView recyclerViewBrandList;
    private RecyclerView recyclerViewRecentList;

    private View rootView;

//    hooks
    private TextView timeGreeting;
    private TextView userName;

//    database reference
    private DatabaseReference databaseReference;

    private ImageView getReportHistoryBtn;

    private HomeFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         rootView = inflater.inflate(R.layout.fragment_home, container, false);

         timeGreeting = rootView.findViewById(R.id.day_text);
         userName = rootView.findViewById(R.id.user_name);

//         go to hsitory report on home page
        getReportHistoryBtn = rootView.findViewById(R.id.salvage_notes);


        getReportHistoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ReportHistoryActivity.class);
                startActivity(intent);
            }
        });

//         recycler view for brandlist
        recyclerViewBrandList();

        recyclerViewRecent();

//        get time of the day
        getTimeOfDay();

//        start session
      sessionInfo();

//      database ref



        return rootView;
    }

    private void recyclerViewRecent() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewRecentList = rootView.findViewById(R.id.recycler_recent);
        recyclerViewRecentList.setLayoutManager(linearLayoutManager);

        ArrayList<RecentDomain> recentList = new ArrayList<>();
//

        databaseReference = FirebaseDatabase.getInstance().getReference("Cars");

        recentAdapter = new RecentAdapter(recentList);
        recyclerViewRecentList.setAdapter(recentAdapter);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot child: snapshot.getChildren()) {
                    RecentDomain newCar = child.getValue(RecentDomain.class);
                    recentList.add(newCar);
                }
                recentAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
//                Toast.makeText(getActivity(), "Cars Error", Toast.LENGTH_SHORT).show();
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });



    }

    private void recyclerViewBrandList() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewBrandList = rootView.findViewById(R.id.recycler_brand);
        recyclerViewBrandList.setLayoutManager(linearLayoutManager);

//        pass the java class for the
        ArrayList<BrandDomain> brandList = new ArrayList<>();
        brandList.add(new BrandDomain("Mercedes","benz_logo"));
        brandList.add(new BrandDomain("Mazda","mazda_logo"));
        brandList.add(new BrandDomain("Toyota","toyota_log"));
        brandList.add(new BrandDomain("Honda","honda_one"));
        brandList.add(new BrandDomain("Land Rover","landrover"));
        brandList.add(new BrandDomain("Audi","audi_log"));
        brandList.add(new BrandDomain("Jaguar","jaguar_logo"));
        brandList.add(new BrandDomain("Volvo","volvo"));
        brandList.add(new BrandDomain("Peugeot","peugeot_logo"));


//        adapter
        brandAdapter = new BrandAdapter(brandList);

//        set the recycler view
        recyclerViewBrandList.setAdapter(brandAdapter);
    }

    //    use singleton pattern to instantiate the fragment
    public static HomeFragment newInstance(){
        if (unique == null){
            unique = new HomeFragment();
        }
        return unique;

    }

//    get time and date
   private void getTimeOfDay(){
       Calendar c = Calendar.getInstance();
       int timeOfDay = c.get(Calendar.HOUR_OF_DAY);

       if(timeOfDay >= 0 && timeOfDay < 12){
           timeGreeting.setText(getResources().getString(R.string.good_morning));
       }else if(timeOfDay >= 12 && timeOfDay < 16){
           timeGreeting.setText(getResources().getString(R.string.good_afternoon));
       }else if(timeOfDay >= 16 && timeOfDay < 21){
           timeGreeting.setText(getResources().getString(R.string.good_evening));
       }else if(timeOfDay >= 21 && timeOfDay < 24){
           timeGreeting.setText(getResources().getString(R.string.good_night));
       }

   }

   //      get session info
    private void sessionInfo(){
        //        start session
        //         get session content as session already created when logged in
        SessionManager sessionManager = new SessionManager(getActivity(), SessionManager.SESSION_USERSESSION);

        HashMap<String,String> userDetails = sessionManager.getUsersDetailFromSession();
        String name = userDetails.get(SessionManager.KEY_NAME);
        userName.setText(name);
    }
}