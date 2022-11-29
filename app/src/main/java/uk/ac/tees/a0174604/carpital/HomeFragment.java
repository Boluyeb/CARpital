package uk.ac.tees.a0174604.carpital;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

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

    private HomeFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         rootView = inflater.inflate(R.layout.fragment_home, container, false);

//         recycler view for brandlist
        recyclerViewBrandList();

        recyclerViewRecent();


        return rootView;
    }

    private void recyclerViewRecent() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewRecentList = rootView.findViewById(R.id.recycler_recent);
        recyclerViewRecentList.setLayoutManager(linearLayoutManager);

        ArrayList<RecentDomain> recentList = new ArrayList<>();
        recentList.add(new RecentDomain("Audi", "S5", "audi", "£35,000", "Quattro"));
        recentList.add(new RecentDomain("Mercedes Benz", "S560", "benz", "£105,000", "4-Matic"));
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
        brandList.add(new BrandDomain("Mercedes","benz_log"));
        brandList.add(new BrandDomain("BMW","bmw_log"));
        brandList.add(new BrandDomain("Toyota","toyota_log"));
        brandList.add(new BrandDomain("Peugeot","peugeot_log"));
        brandList.add(new BrandDomain("Audi","audi_log"));

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

}