package uk.ac.tees.a0174604.carpital;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ComponentName;
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
import androidx.core.content.IntentCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;


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

    private Button updateBtn;

    private static final int CAMERA_PERMISSION_CODE = 1;
    ActivityResultLauncher<Uri> takePictureLauncher;
    private  Uri imageUri;

    private String phoneNumber;
    private String email;
    private String tempProfileImg;
    private String name;

    private TextInputLayout userName;
    private TextInputLayout userEmail;





    private String LOG_TAG = EditProfileFragment.class.getSimpleName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        uploadImg = rootView.findViewById(R.id.pickImage);
        profileImg = rootView.findViewById(R.id.user_photo);
        uploadImgGal = rootView.findViewById(R.id.chooseGallery);
        updateBtn = rootView.findViewById(R.id.update_button);
        userName = rootView.findViewById(R.id.user);
        userEmail = rootView.findViewById(R.id.email);

        imageUri = createUri();
        registerPictureLauncher();

        SessionManager sessionManager = new SessionManager(getActivity(), SessionManager.SESSION_USERSESSION);
        if (sessionManager.checkLogin()){
//            get the user details
            HashMap<String,String> userDetails = sessionManager.getUsersDetailFromSession();
            phoneNumber = userDetails.get(SessionManager.KEY_PHONENUMBER);
            email = userDetails.get(SessionManager.KEY_EMAIL);
            tempProfileImg = userDetails.get(SessionManager.KEY_PROFILEIMG);
            name = userDetails.get(SessionManager.KEY_NAME);

            if(!tempProfileImg.isEmpty()){
                Glide.with(profileImg.getContext())
                        .load(tempProfileImg)
                        .placeholder(R.drawable.user_img)
                        .centerCrop()
                        .error(R.drawable.user_img)
                        .into(profileImg);

            }

            userName.getEditText().setText(name);
            userEmail.getEditText().setText(email);

        }


//        update user profile
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!validateFields(userName) || !validateFields(userEmail)){
                    return;
                }
                changeUserName();
                changeUserEmail();
                uploadImg();

//                end user session as values have changed
                sessionManager.logoutUserFromSession();


                SessionManager newManager = new SessionManager(getActivity(), SessionManager.SESSION_USERSESSION);

//update changes from db to session
                Query checkUser = FirebaseDatabase.getInstance().getReference("Users").orderByChild("phoneNumber").equalTo(phoneNumber);

                checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            String nameNew = snapshot.child(phoneNumber).child("name").getValue(String.class);
                            String emailNew = snapshot.child(phoneNumber).child("email").getValue(String.class);
                            String pwd = snapshot.child(phoneNumber).child("password").getValue(String.class);
                            String profileImgNew = snapshot.child(phoneNumber).child("profilePicture").getValue(String.class);

                            newManager.createLoginSession(nameNew, emailNew, phoneNumber, pwd, profileImgNew);

                            Intent intent = new Intent(getActivity(),SuccessActivity.class);
                            String message = "Profile Successfully Updated";
                            intent.putExtra(SellFragment.EXTRA_MESSAGE,message);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            getActivity().finish();

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

////working code
//                Intent intent = new Intent(getActivity(),LoginActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(intent);

//                Intent intent = new Intent(getActivity(),HomeActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(intent);
            }
        });

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
                                updateProfilePicture(task.getResult().toString());
                            }
                        }
                    });

                    Toast.makeText(getActivity(), "Profile Successfully updated, please login", Toast.LENGTH_SHORT).show();
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

    private void updateProfilePicture(String url) {
//        check database
        FirebaseDatabase.getInstance().getReference("Users").child(phoneNumber).child("profilePicture").setValue(url);

    }

    //    create Uri for message*
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


    public void changeUserName(){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.child(phoneNumber).child("name").setValue(userName.getEditText().getText().toString().trim());
    }

    public void changeUserEmail(){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.child(phoneNumber).child("email").setValue(userEmail.getEditText().getText().toString().trim());
    }

    //    use singleton pattern to instantiate the fragment
    public static EditProfileFragment newInstance(){
        if (unique == null){
            unique = new EditProfileFragment();
        }
        return unique;
    }
}