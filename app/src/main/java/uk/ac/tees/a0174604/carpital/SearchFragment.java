package uk.ac.tees.a0174604.carpital;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment {

    private static SearchFragment unique;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
              View rootView =  inflater.inflate(R.layout.fragment_search, container, false);
             return rootView;
    }

    //    use singleton pattern to instantiate the fragment
    public static SearchFragment newInstance(){
        if (unique == null){
            unique = new SearchFragment();
        }
        return unique;
    }
}