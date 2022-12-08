package uk.ac.tees.a0174604.carpital;

import static android.app.Activity.RESULT_OK;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
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
import android.widget.ImageView;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.UploadTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Collections;
import java.util.UUID;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SellFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SellFragment extends Fragment {
    private static SellFragment unique;
    private TextInputLayout location;
    private Button addListing;
    private String city;
    private DatabaseReference databaseReference;

    public static final String EXTRA_MESSAGE = "Carpital.SettingsFragment.SellFragment";

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

//    image variables
    private ImageView carSellPic;
    private Button chooseCar;
    private Uri imageUri;

//    text input layout
    private TextInputLayout carMakeBox, carModelBox, carYearBox, carCateBox, carAmtBox;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_sell, container, false);
        location = rootView.findViewById(R.id.car_location);
        carMakeBox = rootView.findViewById(R.id.car_make);
        carModelBox = rootView.findViewById(R.id.car_model);
        carYearBox = rootView.findViewById(R.id.car_year);
        carCateBox = rootView.findViewById(R.id.car_cate);
        carAmtBox = rootView.findViewById(R.id.car_cost);


        addListing = rootView.findViewById(R.id.submit_listing);
        carMakeComplete = rootView.findViewById(R.id.carMake_complete);
        carModelComplete = rootView.findViewById(R.id.carModelComplete);
        carYearComplete = rootView.findViewById(R.id.carYearComplete);
        carCateComplete = rootView.findViewById(R.id.carCateComplete);

        carSellPic = rootView.findViewById(R.id.sell_photo);
        chooseCar = rootView.findViewById(R.id.choose_photo);





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
                if(!validateFields(carMakeBox) || !validateFields(carModelBox) || !validateFields(carYearBox) || !validateFields(carCateBox)
                || !validateFields(location) || !validateFields(carAmtBox)){
                    return;
                }
                uploadImg();

            }
        });


        chooseCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoIntent = new Intent(Intent.ACTION_PICK);
                photoIntent.setType("image/*");
                startActivityForResult(photoIntent, 1);
//                Log.d(LOG_TAG, imageUri.toString());
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

             city = place.getName();


        }
        else if(requestCode == 1 && resultCode == Activity.RESULT_OK && data !=null){
            imageUri =  data.getData();
            getImageInImageView();
        }
        else if(resultCode == AutocompleteActivity.RESULT_ERROR){
//            Initailise status
            Status status = Autocomplete.getStatusFromIntent(data);
//            Display toast
            Toast.makeText(getActivity(), status.getStatusMessage(), Toast.LENGTH_SHORT).show();
        }

    }


    private void getImageInImageView() {
        Bitmap bitmap = null;
        try{
            bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
        } catch(IOException e){
            e.printStackTrace();
        }
        carSellPic.setImageBitmap(bitmap);
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
//
//    private void filterByCarMake(JSONObject jsonObject, String carMake){
//
//    }

    //    use singleton pattern to instantiate the fragment
    public static SellFragment newInstance(){
        if (unique == null){
            unique = new SellFragment();
        }
        return unique;

    }


//        upload image to firebase storage
private void uploadImg() {
    ProgressDialog progressDialog = new ProgressDialog(getActivity());
    progressDialog.setTitle("Updating...");
    progressDialog.show();

//        store image in firebase storage
    FirebaseStorage.getInstance().getReference("images/"+ UUID.randomUUID().toString()).putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
        @Override
        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
            if(task.isSuccessful()){
//                    get image url and set it to user.
                task.getResult().getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if(task.isSuccessful()){
                            String vehicleImg = task.getResult().toString();
                            String vehicleMake = carMakeBox.getEditText().getText().toString().trim();
                            String vehicleModel = carModelBox.getEditText().getText().toString().trim();
                            String vehicleYear = carYearBox.getEditText().getText().toString().trim();
                            String vehicleCate = carCateBox.getEditText().getText().toString().trim();
                            String vehicleLocation = city;
                            String vehicleCost = "£"+carAmtBox.getEditText().getText().toString().trim();

//                            helper class for car recently added
                            RecentDomain newCar = new RecentDomain(vehicleMake, vehicleModel, vehicleYear, vehicleCate, vehicleLocation, vehicleCost, vehicleImg);

                            databaseReference = FirebaseDatabase.getInstance().getReference();

                            String id = databaseReference.push().getKey();

                            databaseReference.child("Cars").child(id).setValue(newCar).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){

                                        Intent intent = new Intent(getActivity(),SuccessActivity.class);
                                        String message = "Listing Successfully Created";
                                        intent.putExtra(EXTRA_MESSAGE,message);
                                        startActivity(intent);
                                        getActivity().finish();

                                    }
                                    else {
                                        Toast.makeText(getActivity(), "Listing not created please try again", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });


                        }
                    }
                });


            } else {
                Toast.makeText(getActivity(), task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
            progressDialog.dismiss();
        }
    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
        @Override
        public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
            double progress = 100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount();
            progressDialog.setMessage("Uploaded" +(int) progress + "%");
        }
    });
}


    //    validate for empty fields
//    validate for empty values
    private boolean validateFields(TextInputLayout inputVal) {
//        get the name as a string
        String val = inputVal.getEditText().getText().toString().trim();

        if (val.isEmpty()) {
            inputVal.setError("This field is required");
            return false;
        } else {
            inputVal.setError(null);
            inputVal.setErrorEnabled(false);
            return true;
        }
    }


}