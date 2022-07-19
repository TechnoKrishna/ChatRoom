package com.example.bluechatroom;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Login_Activity extends AppCompatActivity {

    TextInputLayout UserNameEditText, PasswordEditText;
    CheckBox logged_in;
    Button LoginBtn;
    TextView RecoverBtn, SignupBtn;
    ProgressDialog progressDialog;
    Boolean Logged_Status = true;

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
        setContentView(R.layout.activity_login);

        UserNameEditText = findViewById(R.id.username);
        PasswordEditText = findViewById(R.id.password);
        logged_in = findViewById(R.id.checkbox);
        LoginBtn = findViewById(R.id.loginBtn);
        RecoverBtn = findViewById(R.id.recoverBtn);
        SignupBtn = findViewById(R.id.signupBtn);

        progressDialog = new ProgressDialog(this);

        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);

        String shared_name = sharedPreferences.getString(KEY_NAME, null);
        String shared_username = sharedPreferences.getString(KEY_USERNAME, null);
        String shared_email = sharedPreferences.getString(KEY_EMAIL, null);
        String shared_number = sharedPreferences.getString(KEY_NUMBER, null);
        String shared_password = sharedPreferences.getString(KEY_PASSWORD, null);
        String shared_gender = sharedPreferences.getString(KEY_GENDER, null);
        String shared_downloadUrl = sharedPreferences.getString(KEY_DOWNLOADURL, null);
        String shared_anonymous_status = sharedPreferences.getString(KEY_ANONYMOUS_STATUS, null);
        String shared_online_status = sharedPreferences.getString(KEY_ONLINE_STATUS, null);
        String shared_logged_status = sharedPreferences.getString(KEY_LOGGED_STATUS, null);

        if (shared_logged_status != null) {

            Intent intent = new Intent(Login_Activity.this, DashBoard.class);
            startActivity(intent);
            finish();

        }

        LoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser();
            }
        });

        SignupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Login_Activity.this, Register_Activity.class);
                startActivity(intent);
//                finish();

            }
        });
    }

    private void loginUser() {
        if (!validateUsername() | !validatePassword()) {
            return;
        } else {
            isUser();
        }
    }

    private boolean validateUsername() {

        String val = UserNameEditText.getEditText().getText().toString();

        if (val.isEmpty()) {
            UserNameEditText.setError("Field cannot be empty");
            return false;
        } else {
            UserNameEditText.setError(null);
            UserNameEditText.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validatePassword() {

        String val = PasswordEditText.getEditText().getText().toString();

        if (val.isEmpty()) {
            PasswordEditText.setError("Field cannot be empty");
            return false;
        } else {
            PasswordEditText.setError(null);
            PasswordEditText.setErrorEnabled(false);
            return true;
        }
    }

    private void isUser() {

        progressDialog.setMessage("Wait For While");
        progressDialog.show();

        String userEnteredUsername = UserNameEditText.getEditText().getText().toString().trim();
        String userEnteredPassword = PasswordEditText.getEditText().getText().toString().trim();

        if (logged_in.isChecked()) {
            Logged_Status = true;
        } else {
            Logged_Status = false;
        }

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");

        Query checkUser = reference.orderByChild("username").equalTo(userEnteredUsername);

        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                    UserNameEditText.setError(null);
                    UserNameEditText.setErrorEnabled(false);

                    String passwordFromDB = dataSnapshot.child(userEnteredUsername).child("password").getValue(String.class);

                    if (passwordFromDB.equals(userEnteredPassword)) {

                        UserNameEditText.setError(null);
                        UserNameEditText.setErrorEnabled(false);

                        String nameFromDB = dataSnapshot.child(userEnteredUsername).child("name").getValue(String.class);
                        String usernameFromDB = dataSnapshot.child(userEnteredUsername).child("username").getValue(String.class);
                        String phoneNoFromDB = dataSnapshot.child(userEnteredUsername).child("phone").getValue(String.class);
                        String emailFromDB = dataSnapshot.child(userEnteredUsername).child("email").getValue(String.class);
                        String genderFromDB = dataSnapshot.child(userEnteredUsername).child("gender").getValue(String.class);
                        String downloadUrlFromDB = dataSnapshot.child(userEnteredUsername).child("downloadUrl").getValue(String.class);
                        String anonymousStatusFromDB = dataSnapshot.child(userEnteredUsername).child("anonymous_status").getValue(String.class);
                        String onlineStatusFromDB = dataSnapshot.child(userEnteredUsername).child("online_status").getValue(String.class);

                        if (Logged_Status) {
                            sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString(KEY_NAME, nameFromDB);
                            editor.putString(KEY_USERNAME, usernameFromDB);
                            editor.putString(KEY_EMAIL, emailFromDB);
                            editor.putString(KEY_NUMBER, phoneNoFromDB);
                            editor.putString(KEY_PASSWORD, passwordFromDB);
                            editor.putString(KEY_GENDER, genderFromDB);
                            editor.putString(KEY_DOWNLOADURL, downloadUrlFromDB);
                            editor.putString(KEY_ANONYMOUS_STATUS, anonymousStatusFromDB);
                            editor.putString(KEY_ONLINE_STATUS, onlineStatusFromDB);
                            editor.putString(KEY_LOGGED_STATUS, "you have access");
                            editor.apply();
                        } else {
//                            SharedPreferences.Editor editor = sharedPreferences.edit();
//                            editor.clear();
//                            editor.commit();
                            sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString(KEY_NAME, nameFromDB);
                            editor.putString(KEY_USERNAME, usernameFromDB);
                            editor.putString(KEY_EMAIL, emailFromDB);
                            editor.putString(KEY_NUMBER, phoneNoFromDB);
                            editor.putString(KEY_PASSWORD, passwordFromDB);
                            editor.putString(KEY_GENDER, genderFromDB);
                            editor.putString(KEY_DOWNLOADURL, downloadUrlFromDB);
                            editor.putString(KEY_ANONYMOUS_STATUS, anonymousStatusFromDB);
                            editor.putString(KEY_ONLINE_STATUS, onlineStatusFromDB);
                            editor.putString(KEY_LOGGED_STATUS, null);
                            editor.apply();
                        }

                        Intent intent = new Intent(getApplicationContext(), DashBoard.class);

                        intent.putExtra("name", nameFromDB);
                        intent.putExtra("username", usernameFromDB);
                        intent.putExtra("email", emailFromDB);
                        intent.putExtra("phoneNo", phoneNoFromDB);
                        intent.putExtra("password", passwordFromDB);

                        startActivity(intent);
                        progressDialog.dismiss();
                        finish();
                    } else {
                        PasswordEditText.setError("Wrong Password");
                        PasswordEditText.requestFocus();
                        progressDialog.dismiss();
                    }
                } else {
                    UserNameEditText.setError("No such User Exist");
                    UserNameEditText.requestFocus();
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        progressDialog.setMessage("Wait For While");
        progressDialog.show();

    }
}