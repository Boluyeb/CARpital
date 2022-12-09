package uk.ac.tees.a0174604.carpital;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SalvageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SalvageFragment extends Fragment {
    private static SalvageFragment unique;
    private Button getReportBtn;

    private RequestQueue queue;
    private String LOG_TAG = SalvageFragment.class.getSimpleName();
    private TextInputLayout carVin, carMake, carModel, carYear, carManu, carTrim, carTrans;
    private Button clearReportBtn;
    private ImageView resultImage;
    private LinearLayout vehicleReport;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_salvage, container, false);
        getReportBtn = rootView.findViewById(R.id.get_report);
        carVin = rootView.findViewById(R.id.car_vin);
        carMake= rootView.findViewById(R.id.car_make);
        carModel = rootView.findViewById(R.id.car_model);
        carYear = rootView.findViewById(R.id.car_year);
        carManu = rootView.findViewById(R.id.car_manufacturer);
        carTrim = rootView.findViewById(R.id.car_trim);
        carTrans = rootView.findViewById(R.id.car_transmission);
        clearReportBtn = rootView.findViewById(R.id.clear_report);
        resultImage = rootView.findViewById(R.id.result_img);
        vehicleReport = rootView.findViewById(R.id.vehicle_report);

        queue = VolleySingleton.getInstance(getActivity()).getmRequestQueue();





        getReportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!validateFields(carVin)){
                    return;
                }
               String userCarVin = carVin.getEditText().getText().toString();

//                String url = "https://api.carmd.com/v3.0/decode?vin=1GNALDEK9FZ108495";
                String url = "https://api.carmd.com/v3.0/decode?vin="+userCarVin;

//                url for image checker.
                String urlImage = "https://api.carmd.com/v3.0/image?vin="+userCarVin;

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject jsonObject = response.getJSONObject("data");
                            String year = jsonObject.getString("year");
                            String make = jsonObject.getString("make");
                            String model = jsonObject.getString("model");
                            String manufacturer = jsonObject.getString("manufacturer");
                            String trim = jsonObject.getString("trim");
                            String transmission = jsonObject.getString("transmission");

                            String result = year + " " + make + " " + model + " " + manufacturer + " " + trim + " " + transmission;
                            Log.d(LOG_TAG,result);

//                            set all the values to ui

                            carMake.getEditText().setText(make);
                            carMake.getEditText().setEnabled(false);

                            carModel.getEditText().setText(model);
                            carModel.getEditText().setEnabled(false);

                            carYear.getEditText().setText(year);
                            carYear.getEditText().setEnabled(false);

                            carManu.getEditText().setText(manufacturer);
                            carManu.getEditText().setEnabled(false);

                            carTrim.getEditText().setText(trim);
                            carTrim.getEditText().setEnabled(false);

                            carTrans.getEditText().setText(transmission);
                            carTrans.getEditText().setEnabled(false);

//                            4T3BA3BB4DU049354
//                            5TFEV54198X043410
//                            1GNALDEK9FZ108495

                            saveData();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
//                        Log.d(LOG_TAG, String.valueOf(response));
                    }
                }, new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(LOG_TAG, error.toString());
                        Toast.makeText(getActivity(),"VIN does not exist", Toast.LENGTH_SHORT).show();
                    }
                }){
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        HashMap header = new HashMap<>();
                        header.put("content-type","application/json");
                        header.put("authorization","Basic M2YyNTUxZjgtZmYzMC00OTcwLTk4YjUtYTc3OThhNTI3MTVl");
                        header.put("partner-token", "4b3b572dec7e428583ae106e9f124393");
                        return header;
                    }
                };
                queue.add(jsonObjectRequest);

//get vehicle image
                JsonObjectRequest jsonObjectRequestImg = new JsonObjectRequest(Request.Method.GET, urlImage, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject jsonObject = response.getJSONObject("data");
                            String imgUrl = jsonObject.getString("image");

                            String newImgUrl = addChar(imgUrl, 's', 4);

                            //load image using glide

                            if (!imgUrl.isEmpty()) {
                                Glide.with(resultImage.getContext())
                                        .load(newImgUrl)
                                        .placeholder(R.drawable.vehicle_img_item)
                                        .error(R.drawable.vehicle_img_item)

                                        .into(resultImage);

                            }

                            Log.d(LOG_TAG,newImgUrl);



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
//                        Log.d(LOG_TAG, String.valueOf(response));
                    }
                }, new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(LOG_TAG, error.toString());
                        Toast.makeText(getActivity(),"VIN Image does not exist", Toast.LENGTH_SHORT).show();
                    }
                }){
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        HashMap header = new HashMap<>();
                        header.put("content-type","application/json");
                        header.put("authorization","Basic M2YyNTUxZjgtZmYzMC00OTcwLTk4YjUtYTc3OThhNTI3MTVl");
                        header.put("partner-token", "4b3b572dec7e428583ae106e9f124393");
                        return header;
                    }
                };
                queue.add(jsonObjectRequestImg);

                if(vehicleReport.getVisibility() == View.INVISIBLE){
                    vehicleReport.setVisibility(View.VISIBLE);
                }

            }

        });

        clearReportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(vehicleReport.getVisibility() == View.VISIBLE){
                    vehicleReport.setVisibility(View.INVISIBLE);
                }
            }
        });
        return rootView;
    }

    //    use singleton pattern to instantiate the fragment
    public static SalvageFragment newInstance(){
        if (unique == null){
            unique = new SalvageFragment();
        }
        return unique;
    }

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

//    convert string to https
    private String addChar(String str, char ch, int position) {
        return str.substring(0, position) + ch + str.substring(position);
    }

//    save data for data persistence
    private void saveData(){
        String vin_txt = carVin.getEditText().getText().toString().trim();
        String carMake_txt = carMake.getEditText().getText().toString().trim();
        String carModel_txt = carModel.getEditText().getText().toString().trim();
        String carYear_txt = carYear.getEditText().getText().toString().trim();
        String carManu_txt = carManu.getEditText().getText().toString().trim();
        String carTrim_txt = carTrim.getEditText().getText().toString().trim();
        String carTrans_txt = carTrans.getEditText().getText().toString().trim();

//        if(vin_txt.isEmpty()){

            ReportModel model = new ReportModel();

            model.setVin(vin_txt);
            model.setCarMake(carMake_txt);
            model.setCarModel(carModel_txt);
            model.setCarYear(carYear_txt);
            model.setCarManu(carManu_txt);
            model.setCarTrim(carTrim_txt);
            model.setCarTrans(carTrans_txt);



            ReportDatabaseClass.getDatabase(getActivity().getApplicationContext()).getDao().insertAllData(model);

//            carVin.getEditText().setText("");
//        carMake.getEditText().setText("");
//        carModel.getEditText().setText("");
//        carYear.getEditText().setText("");
//        carManu.getEditText().setText("");
//        carTrim.getEditText().setText("");
//        carTrans.getEditText().setText("");
            Log.d(LOG_TAG, "Data Successfully saved");

//        }
    }
}