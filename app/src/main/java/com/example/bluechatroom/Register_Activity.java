package com.example.bluechatroom;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

import java.io.ByteArrayOutputStream;

public class Register_Activity extends AppCompatActivity {

    TextInputLayout FullNameEditText, UserNameEditText, EmailEditText, PhoneNoEditText, PasswordEditText;
    TextView signupBtn;
    Button RegisterBtn;
    RadioGroup genderGroup;
    RadioButton genderSelected, other;
    ProgressDialog progressDialog;
    StorageReference storageReference;
    DatabaseReference databaseReference;
    Bitmap bitmap = null;

    String downloadUrl = "";

    SharedPreferences sharedPreferences;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_register);

        signupBtn = findViewById(R.id.signupBtn);
        RegisterBtn = findViewById(R.id.RegisterBtn);

        FullNameEditText = findViewById(R.id.fullname);
        UserNameEditText = findViewById(R.id.username);
        EmailEditText = findViewById(R.id.email);
        PhoneNoEditText = findViewById(R.id.phoneno);
        PasswordEditText = findViewById(R.id.password);
        genderGroup = findViewById(R.id.genderGroup);
        other = findViewById(R.id.other);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Creating Account");

        databaseReference = FirebaseDatabase.getInstance().getReference().child("users");
        storageReference = FirebaseStorage.getInstance().getReference();

        RegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });

        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Register_Activity.this, Login_Activity.class);
                startActivity(intent);
                finish();

            }
        });
    }

    public void registerUser() {

        if (!validateName() | !validateEmail() | !validatePassword() | !validateUsername() | !validatePhoneNo() | !validateGender()) {
            return;
        } else {

            progressDialog.show();

            String name = FullNameEditText.getEditText().getText().toString();
            String username = UserNameEditText.getEditText().getText().toString();
            String email = EmailEditText.getEditText().getText().toString();
            String phone = PhoneNoEditText.getEditText().getText().toString();
            String password = PasswordEditText.getEditText().getText().toString();

            int selectedId = genderGroup.getCheckedRadioButtonId();
            genderSelected = findViewById(selectedId);

            String gender = genderSelected.getText().toString();

            if (gender.equals("Male")) {
                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.global_user_male_logo);
            } else if (gender.equals("Female")) {
                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.global_user_female_logo);
            } else {
                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.global_user_other_logo);
            }

            uploadImage();
        }
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

    private void uploadImage() {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] finalimg = baos.toByteArray();
        final StorageReference filePath;
        filePath = storageReference.child("users").child(finalimg + "png");
        final UploadTask uploadTask = filePath.putBytes(finalimg);
        uploadTask.addOnCompleteListener(Register_Activity.this, new OnCompleteListener<UploadTask.TaskSnapshot>() {
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
                                    insertData();
                                }
                            });
                        }
                    });
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(Register_Activity.this, "Failed To Upload a Profile Image", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void insertData() {

        String name = FullNameEditText.getEditText().getText().toString();
        String username = UserNameEditText.getEditText().getText().toString();
        String email = EmailEditText.getEditText().getText().toString();
        String phone = PhoneNoEditText.getEditText().getText().toString();
        String password = PasswordEditText.getEditText().getText().toString();
        String anonymous_status = "false";
        String online_status = "online";

        int selectedId = genderGroup.getCheckedRadioButtonId();
        genderSelected = findViewById(selectedId);
        String gender = genderSelected.getText().toString();

        userData UserData = new userData(name, username, email, phone, password, gender, downloadUrl, anonymous_status, online_status);

        databaseReference.child(username).setValue(UserData).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                progressDialog.dismiss();

                sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(KEY_NAME, name);
                editor.putString(KEY_USERNAME, username);
                editor.putString(KEY_EMAIL, email);
                editor.putString(KEY_NUMBER, phone);
                editor.putString(KEY_PASSWORD, password);
                editor.putString(KEY_GENDER, gender);
                editor.putString(KEY_DOWNLOADURL, downloadUrl);
                editor.putString(KEY_ANONYMOUS_STATUS, anonymous_status);
                editor.putString(KEY_ONLINE_STATUS, online_status);
                editor.putString(KEY_LOGGED_STATUS, "you have access");
                editor.apply();

                Intent intent = new Intent(Register_Activity.this, DashBoard.class);
                startActivity(intent);
                finishAffinity();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(Register_Activity.this, "Failed to Create Account", Toast.LENGTH_SHORT).show();
            }
        });
    }

}