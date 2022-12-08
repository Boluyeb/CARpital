package uk.ac.tees.a0174604.carpital;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

//This class checks internet connection
public class CheckConnectionReceiver extends BroadcastReceiver {
    private String LOG_TAG = CheckConnectionReceiver.class.getSimpleName();
    private Context mContext;

    @Override
    public void onReceive(Context context, Intent intent) {
        mContext = context;
        if (isConnectedToInternet(context)){
//            Toast.makeText(context,"Internet Connected", Toast.LENGTH_LONG).show();
            Log.d(LOG_TAG,"There is internet connection");
        }
        else {
            Log.d(LOG_TAG,"No internet connection");
            showDialog();
        }
    }

    public  boolean isConnectedToInternet(Context context){
        try{

//        using connectivity manager
            ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            @SuppressLint("MissingPermission") NetworkInfo networkInfo = manager.getActiveNetworkInfo();

            return (networkInfo != null && networkInfo.isConnected());

        }catch(NullPointerException e){
            e.printStackTrace();
            return false;
        }

    }

//    dialog box
    public void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.alert_dialog_layout, null);
        Button cancelBtn = view.findViewById(R.id.cancel_btn);

        builder.setView(view);

        final Dialog dialog = builder.create();

//close the dialog box
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });



        dialog.show();
    }
}
