package uk.ac.tees.a0174604.carpital;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

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



    private HomeFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         rootView = inflater.inflate(R.layout.fragment_home, container, false);

         timeGreeting = rootView.findViewById(R.id.day_text);
         userName = rootView.findViewById(R.id.user_name);

//         recycler view for brandlist
        recyclerViewBrandList();

        recyclerViewRecent();

//        get time of the day
        getTimeOfDay();

//        start session
      sessionInfo();



        return rootView;
    }

    private void recyclerViewRecent() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewRecentList = rootView.findViewById(R.id.recycler_recent);
        recyclerViewRecentList.setLayoutManager(linearLayoutManager);

        ArrayList<RecentDomain> recentList = new ArrayList<>();
        recentList.add(new RecentDomain("Audi", "S5", "audi", "£35,000", "Quattro"));
        recentList.add(new RecentDomain("Mercedes", "S560", "benz", "£105,000", "4-Matic"));
        recentList.add(new RecentDomain("Porsche", "911", "porshe", "£110,000", "Convertible"));
        recentList.add(new RecentDomain("Rolls Royce", "Cullinan", "rr", "£560,000", "V12"));
        recentList.add(new RecentDomain("Volkswagen", "Golf", "vw", "£20,000", "TDI"));

        recentAdapter = new RecentAdapter(recentList);

        recyclerViewRecentList.setAdapter(recentAdapter);

    }

    private void recyclerViewBrandList() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewBrandList = rootView.findViewById(R.id.recycler_brand);
        recyclerViewBrandList.setLayoutManager(linearLayoutManager);

//        pass the java class for the
        ArrayList<BrandDomain> brandList = new ArrayList<>();
        brandList.add(new BrandDomain("Mercedes","benz_logo"));
        brandList.add(new BrandDomain("BMW","bmw_logo"));
        brandList.add(new BrandDomain("Toyota","toyota_log"));
        brandList.add(new BrandDomain("Peugeot","peugeot_logo"));
        brandList.add(new BrandDomain("Audi","audi_log"));
        brandList.add(new BrandDomain("Jaguar","jaguar_logo"));
        brandList.add(new BrandDomain("Mazda","mazda_logo"));

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