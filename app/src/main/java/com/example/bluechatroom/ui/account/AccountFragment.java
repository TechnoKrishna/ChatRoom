package com.example.bluechatroom.ui.account;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.bluechatroom.R;
import com.example.bluechatroom.ui.search.userData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class AccountFragment extends Fragment {

    TextInputLayout FullNameEditText, UserNameEditText, EmailEditText, PhoneNoEditText, PasswordEditText;
    TextView signupBtn;
    CircleImageView updateImage;
    Button UpdateBtn;
    RadioGroup genderGroup;
    RadioButton genderSelected, male, female, other;
    ProgressDialog progressDialog;
    StorageReference storageReference, deleteReference;
    DatabaseReference databaseReference;
    Bitmap bitmap = null;
    int selectedId;
    private final int REQ = 1;

    String downloadUrl = "", removeOld;

    SharedPreferences sharedPreferences;
    String shared_name, shared_username, shared_email, shared_number, shared_password, shared_gender, shared_downloadUrl, shared_anonymous_status, shared_online_status;

    private static final String SHARED_PREF_NAME = "mypref";
    private static final String KEY_NAME = "name";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_NUMBER = "number";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_GENDER = "gender";
    private static final String KEY_DOWNLOADURL = "downloadUrl";
    private static final String KEY_ANONYMOUS_STATUS = "anonymous_status";
    private static final String KEY_ONLINE_STATUS = "online_status";
    private static final String KEY_LOGGED_STATUS = "logged_status";

    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_account, container, false);

        sharedPreferences = getContext().getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        shared_name = sharedPreferences.getString(KEY_NAME, null);
        shared_username = sharedPreferences.getString(KEY_USERNAME, null);
        shared_email = sharedPreferences.getString(KEY_EMAIL, null);
        shared_number = sharedPreferences.getString(KEY_NUMBER, null);
        shared_password = sharedPreferences.getString(KEY_PASSWORD, null);
        shared_gender = sharedPreferences.getString(KEY_GENDER, null);
        shared_downloadUrl = sharedPreferences.getString(KEY_DOWNLOADURL, null);
        shared_anonymous_status = sharedPreferences.getString(KEY_ANONYMOUS_STATUS, null);
        shared_online_status = sharedPreferences.getString(KEY_ONLINE_STATUS, null);

        signupBtn = view.findViewById(R.id.signupBtn);
        UpdateBtn = view.findViewById(R.id.UpdateBtn);
        updateImage = view.findViewById(R.id.updateImage);

        FullNameEditText = view.findViewById(R.id.fullname);
        UserNameEditText = view.findViewById(R.id.username);
        EmailEditText = view.findViewById(R.id.email);
        PhoneNoEditText = view.findViewById(R.id.phoneno);
        PasswordEditText = view.findViewById(R.id.password);
        genderGroup = view.findViewById(R.id.genderGroup);
        male = view.findViewById(R.id.male);
        female = view.findViewById(R.id.female);
        other = view.findViewById(R.id.other);

        try {
            Picasso.get().load(shared_downloadUrl).into(updateImage);
        } catch (Exception e) {
            e.printStackTrace();
        }

        FullNameEditText.getEditText().setText(shared_name);
        UserNameEditText.getEditText().setText(shared_username);
        EmailEditText.getEditText().setText(shared_email);
        PhoneNoEditText.getEditText().setText(shared_number);
        PasswordEditText.getEditText().setText(shared_password);

        if (shared_gender.equals("Male")) {
            male.setChecked(true);
        } else if (shared_gender.equals("Female")) {
            female.setChecked(true);
        } else if (shared_gender.equals("Other")) {
            other.setChecked(true);
        } else {
            Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
        }

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Updating User");

        databaseReference = FirebaseDatabase.getInstance().getReference().child("users");
        storageReference = FirebaseStorage.getInstance().getReference();

        removeOld = shared_downloadUrl;
        deleteReference = FirebaseStorage.getInstance().getReferenceFromUrl(removeOld);

        updateImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });

        UpdateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdateUser();
            }
        });

        return view;
    }

    private void checkValidation() {
        if (bitmap == null) {
            UpdateData();
        } else {
            uploadImage();
        }
    }

    public void UpdateUser() {

        if (!validateName() | !validateEmail() | !validatePassword() | !validateUsername() | !validatePhoneNo() | !validateGender()) {
            return;
        } else {

            progressDialog.show();

            String name = FullNameEditText.getEditText().getText().toString();
            String username = UserNameEditText.getEditText().getText().toString();
            String email = EmailEditText.getEditText().getText().toString();
            String phone = PhoneNoEditText.getEditText().getText().toString();
            String password = PasswordEditText.getEditText().getText().toString();

            selectedId = genderGroup.getCheckedRadioButtonId();
            genderSelected = view.findViewById(selectedId);

            String new_gender = genderSelected.getText().toString();

//            if (!shared_gender.equals(new_gender)) {
//                if (new_gender.equals("Male")) {
//                    bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.global_user_male_logo);
//                } else if (new_gender.equals("Female")) {
//                    bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.global_user_female_logo);
//                } else {
//                    bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.global_user_other_logo);
//                }
//            }

            checkValidation();

        }

    }

    private void uploadImage() {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] finalimg = baos.toByteArray();
        final StorageReference filePath;
        filePath = storageReference.child("users").child(finalimg + "png");
        final UploadTask uploadTask = filePath.putBytes(finalimg);
        uploadTask.addOnCompleteListener(getActivity(), new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()) {
                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    downloadUrl = String.valueOf(uri);
                                    deleteOldImage();
                                    UpdateImageData(downloadUrl);
                                }
                            });
                        }
                    });
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(getContext(), "Failed To Upload a Profile Image", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void deleteOldImage() {
        deleteReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), "Something Went Wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void UpdateImageData(String downloadUrl) {

        String name = FullNameEditText.getEditText().getText().toString();
        String username = UserNameEditText.getEditText().getText().toString();
        String email = EmailEditText.getEditText().getText().toString();
        String phone = PhoneNoEditText.getEditText().getText().toString();
        String password = PasswordEditText.getEditText().getText().toString();
        String gender = genderSelected.getText().toString();

        HashMap hp = new HashMap();

        hp.put("name", name);
        hp.put("username", username);
        hp.put("email", email);
        hp.put("phone", phone);
        hp.put("password", password);
        hp.put("gender", gender);
        hp.put("downloadUrl", downloadUrl);

        userData UserData = new userData(name, username, email, phone, password, gender, downloadUrl);

        databaseReference.child(username).updateChildren(hp).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                progressDialog.dismiss();

                sharedPreferences = getContext().getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(KEY_NAME, name);
                editor.putString(KEY_USERNAME, username);
                editor.putString(KEY_EMAIL, email);
                editor.putString(KEY_NUMBER, phone);
                editor.putString(KEY_PASSWORD, password);
                editor.putString(KEY_GENDER, gender);
                editor.putString(KEY_DOWNLOADURL, downloadUrl);
                editor.putString(KEY_LOGGED_STATUS, "you have access");
                editor.apply();

//                Intent intent = new Intent(Register_Activity.this, DashBoard.class);
//                startActivity(intent);
//                finishAffinity();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(getActivity(), "Failed to Create Account", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void UpdateData() {

        String name = FullNameEditText.getEditText().getText().toString();
        String username = UserNameEditText.getEditText().getText().toString();
        String email = EmailEditText.getEditText().getText().toString();
        String phone = PhoneNoEditText.getEditText().getText().toString();
        String password = PasswordEditText.getEditText().getText().toString();
        String gender = genderSelected.getText().toString();

        HashMap hp = new HashMap();

        hp.put("name", name);
        hp.put("username", username);
        hp.put("email", email);
        hp.put("phone", phone);
        hp.put("password", password);
        hp.put("gender", gender);

        userData UserData = new userData(name, username, email, phone, password, gender);

        databaseReference.child(username).updateChildren(hp).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                progressDialog.dismiss();

                sharedPreferences = getContext().getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(KEY_NAME, name);
                editor.putString(KEY_USERNAME, username);
                editor.putString(KEY_EMAIL, email);
                editor.putString(KEY_NUMBER, phone);
                editor.putString(KEY_PASSWORD, password);
                editor.putString(KEY_GENDER, gender);
                editor.putString(KEY_LOGGED_STATUS, "you have access");
                editor.apply();

//                Intent intent = new Intent(Register_Activity.this, DashBoard.class);
//                startActivity(intent);
//                finishAffinity();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(getActivity(), "Failed to Create Account", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private Boolean validateName() {
        String val = FullNameEditText.getEditText().getText().toString();

        if (val.isEmpty()) {
            FullNameEditText.setError("Field cannot be empty");
            return false;
        } else {
            FullNameEditText.setError(null);
            FullNameEditText.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validateUsername() {
        String val = UserNameEditText.getEditText().getText().toString();
        String noWhiteSpace = "\\A\\w{4,20}\\z";

        if (val.isEmpty()) {
            UserNameEditText.setError("Field cannot be empty");
            return false;
        } else if (val.length() >= 15) {
            UserNameEditText.setError("Username too long");
            return false;
        } else if (!val.matches(noWhiteSpace)) {
            UserNameEditText.setError("White Space are not Allowed");
            return false;
        } else {
            UserNameEditText.setError(null);
            UserNameEditText.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validateEmail() {
        String val = EmailEditText.getEditText().getText().toString();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (val.isEmpty()) {
            EmailEditText.setError("Field cannot be empty");
            return false;
        } else if (!val.matches(emailPattern)) {
            EmailEditText.setError("Invalid email address");
            return false;
        } else {
            EmailEditText.setError(null);
            EmailEditText.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validatePhoneNo() {
        String val = PhoneNoEditText.getEditText().getText().toString();

        if (val.isEmpty()) {
            PhoneNoEditText.setError("Field cannot be empty");
            return false;
        } else {
            if (val.length() == 10) {
                PhoneNoEditText.setError(null);
                PhoneNoEditText.setErrorEnabled(false);
                return true;
            } else {
                PhoneNoEditText.setError("Invalid Number");
                return false;
            }
        }
    }

    private Boolean validatePassword() {
        String val = PasswordEditText.getEditText().getText().toString();

        String passwordVal = "^" +
                //"(?=.*[0-9])" +       // at least 1 digit
                // "(?=.*[a-z])" +      // at least 1 lower case letter
                // "(?=.*[A-Z])" +      // at least 1 upper case letter
                "(?=.*[a-zA-Z])" +      // any letter
                "(?=.*[@#$%^&+=])" +    // at least 1 special character
                "(?=\\S+$)" +           // no white spaces
                ".{4,}" +               // at least 4 characters
                "$";

        if (val.isEmpty()) {
            PasswordEditText.setError("Field cannot be empty");
            return false;
        } else if (!val.matches(passwordVal)) {
            PasswordEditText.setError("Password is too Weak");
            return false;
        } else {
            PasswordEditText.setError(null);
            PasswordEditText.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateGender() {

        int id = genderGroup.getCheckedRadioButtonId();

        if (id == -1) {
            other.setError("select Item");
            return false;
        } else {
            other.setError(null);
            return true;
        }
    }

    private void openGallery() {
        Intent pickImage = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickImage, REQ);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), uri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            updateImage.setImageBitmap(bitmap);
        }
    }

}