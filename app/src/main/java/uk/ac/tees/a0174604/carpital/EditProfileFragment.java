package uk.ac.tees.a0174604.carpital;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;


import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditProfileFragment extends Fragment {
     View rootView;
    private static EditProfileFragment unique;

    private ImageView profileImg;
    private String path;

//    upload image using camera
    private ImageView uploadImg;

    private ImageView uploadImgGal;

    private static final int CAMERA_PERMISSION_CODE = 1;
    ActivityResultLauncher<Uri> takePictureLauncher;
    private  Uri imageUri;


    private String LOG_TAG = EditProfileFragment.class.getSimpleName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        uploadImg = rootView.findViewById(R.id.pickImage);
        profileImg = rootView.findViewById(R.id.user_photo);
        uploadImgGal = rootView.findViewById(R.id.chooseGallery);

        imageUri = createUri();
        registerPictureLauncher();

//        upload image from gallery
        uploadImgGal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                implicit intent
                Intent photoIntent = new Intent(Intent.ACTION_PICK);
                photoIntent.setType("image/*");
                startActivityForResult(photoIntent, 1);
            }
        });


//upload image from camera.
        uploadImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkCameraPermissionAndOpenCamera();
            }
        });

        return rootView;
    }

//    create Uri for message
    private Uri createUri() {
        File imageFile = new File(getActivity().getApplicationContext().getFilesDir(), "camera_photo.jpg");
        return FileProvider.getUriForFile(
                getActivity().getApplicationContext(),
                "uk.ac.tees.a0174604.carpital.fileProvider",
                imageFile

        );
    }

//    register picture launcher for gallery


//    launch picture launcher
    private void registerPictureLauncher() {
        takePictureLauncher = registerForActivityResult(
                new ActivityResultContracts.TakePicture(),
                new ActivityResultCallback<Boolean>() {
                    @Override
                    public void onActivityResult(Boolean result) {
                        try{
                            if (result) {
                                profileImg.setImageURI(null);
                                profileImg.setImageURI(imageUri);
                                Log.d(LOG_TAG,imageUri.toString());
                            }
                        }catch (Exception exception){
                            exception.getStackTrace();
                        }
                    }
                }
        );
    }

//    checgk camera persmission
    private void checkCameraPermissionAndOpenCamera() {
        if (ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
        } else {
            takePictureLauncher.launch(imageUri);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == CAMERA_PERMISSION_CODE){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                takePictureLauncher.launch(imageUri);
            } else {
                Toast.makeText(getActivity(), "Camera permission denied, please allow permission to take picture", Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data !=null){
           imageUri =  data.getData();
            getImageInImageView();
//           profileImg.setImageURI(imageUri);
        }

    }

    private void getImageInImageView() {
        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        profileImg.setImageBitmap(bitmap);

    }

    //    use singleton pattern to instantiate the fragment
    public static EditProfileFragment newInstance(){
        if (unique == null){
            unique = new EditProfileFragment();
        }
        return unique;
    }
}