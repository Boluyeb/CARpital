package uk.ac.tees.a0174604.carpital;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Collections;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SellFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SellFragment extends Fragment {
    private static SellFragment unique;
    private TextInputLayout location;
    private Button addListing;

    private String LOG_TAG = SellFragment.class.getSimpleName();
//    private TextView display1;
//    private TextView display2;
    private RequestQueue queue;

//    car make
    private ArrayList<String> carMakeList;
    private AutoCompleteTextView carMakeComplete;

//    car Model
    private ArrayList<String> carModelList;
    private AutoCompleteTextView carModelComplete;

//    car Year
    private ArrayList<String> carYearList;
    private AutoCompleteTextView carYearComplete;

//    car Category
    private ArrayList<String> carCateList;
    private AutoCompleteTextView carCateComplete;

//    private String carMakeFilter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_sell, container, false);
        location = rootView.findViewById(R.id.car_location);
        addListing = rootView.findViewById(R.id.submit_listing);
        carMakeComplete = rootView.findViewById(R.id.carMake_complete);
        carModelComplete = rootView.findViewById(R.id.carModelComplete);
        carYearComplete = rootView.findViewById(R.id.carYearComplete);
        carCateComplete = rootView.findViewById(R.id.carCateComplete);


//        stores car makes
        carMakeList = new ArrayList<>();
//        stores car models
        carModelList = new ArrayList<>();
//        stores car years
        carYearList = new ArrayList<>();
//        stores car categories
        carCateList = new ArrayList<>();

//        display1 = rootView.findViewById(R.id.display1);
//        display2 = rootView.findViewById(R.id.display2);


//        initailise google places
        Places.initialize(getActivity().getApplicationContext(),"AIzaSyD-XtwAkXvdcPtk31l2m9H1FFXE87TRBrY");

//        set text non focusable
        location.getEditText().setFocusable(false);
        location.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Initailise place field list
                List<Place.Field> fieldList = Arrays.asList(Place.Field.ADDRESS, Place.Field.LAT_LNG, Place.Field.NAME);
                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fieldList).build(getActivity());
                startActivityForResult(intent, 100);
            }
        });
//        volley library request queue
//        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue = VolleySingleton.getInstance(getActivity()).getmRequestQueue();



        //                instantiate the requestqueue;

        String url = "https://raw.githubusercontent.com/Boluyeb/carJsonApi/main/Car_Model_List.json";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
//                            results being the array
                    JSONArray jsonArray = response.getJSONArray("results");
//
                    for (int i =0; i < jsonArray.length(); i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String carMake = jsonObject.getString("Make");
                        String carModel = jsonObject.getString("Model");
                        String carYear = jsonObject.getString("Year");
                        String carCate = jsonObject.getString("Category");



                        if (!carMakeList.contains(carMake)){
                            carMakeList.add(carMake);
                        }

                        if (!carModelList.contains(carModel)){
                            carModelList.add(carModel);
                        }

                        if (!carYearList.contains(carYear)){
                            carYearList.add(carYear);
                        }

                        if (!carCateList.contains(carCate)){
                            carCateList.add(carCate);
                        }


//                       carMakeComplete
//                      .setOnItemClickListener(new AdapterView.OnItemClickListener(){
//
//                           @Override
//                           public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//
//                               carMakeFilter = carMakeComplete.getText().toString();
//                               Log.d(LOG_TAG, carMakeFilter);
//                               try {
//                                   if(jsonObject.getString("Make").equals(carMakeFilter)){
//                                       try {
//                                           String carModel = jsonObject.getString("Model");
//                                           if(!carModelList.contains(carModel)){
//                                               carModelList.add(carModel);
//                                           }
//                                       } catch (JSONException e) {
//                                           e.printStackTrace();
//                                       }
//
//                                   }
//                               } catch (JSONException e) {
//                                   e.printStackTrace();
//                               }
//                           }
//                       });

                    }
//                            Collections.sort(carMakeList);
                    getArrayAdapter(carMakeList, carMakeComplete);

                    getArrayAdapter(carModelList, carModelComplete);

                    getArrayAdapter(carYearList, carYearComplete);

                    getArrayAdapter(carCateList, carCateComplete);



                    Log.d(LOG_TAG, carMakeList.toString());
                    Log.d(LOG_TAG, carModelList.toString());
                    Log.d(LOG_TAG, carYearList.toString());
                    Log.d(LOG_TAG, carCateList.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(jsonObjectRequest);

//        add listing button
        addListing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });





        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK){
            Place place = Autocomplete.getPlaceFromIntent(data);
//            set address on text
            location.getEditText().setText(place.getAddress());
//         set locality name
//            display1.setText(String.format("Locality Name : %s", place.getName()));
////            set long and lat
//            display2.setText(String.valueOf(place.getLatLng()));
        }
        else if(resultCode == AutocompleteActivity.RESULT_ERROR){
//            Initailise status
            Status status = Autocomplete.getStatusFromIntent(data);
//            Display toast
            Toast.makeText(getActivity(), status.getStatusMessage(), Toast.LENGTH_SHORT).show();
        }

    }

//    array adapter for dropdown
    private void getArrayAdapter(ArrayList<String> carList, AutoCompleteTextView autoCompleteTextView){
        ArrayAdapter arrayAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_dropdown_item_1line, carList);
        autoCompleteTextView.setAdapter(arrayAdapter);
        autoCompleteTextView.setThreshold(1);
        autoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.d("beforeTextChanged", String.valueOf(charSequence));
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.d("onTextChanged", String.valueOf(charSequence));
            }

            @Override
            public void afterTextChanged(Editable editable) {
                Log.d("afterTextChanged", String.valueOf(editable));
            }
        });

    }

    private void filterByCarMake(JSONObject jsonObject, String carMake){

    }

    //    use singleton pattern to instantiate the fragment
    public static SellFragment newInstance(){
        if (unique == null){
            unique = new SellFragment();
        }
        return unique;
    }
}