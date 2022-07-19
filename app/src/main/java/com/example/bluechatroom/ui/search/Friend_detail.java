package com.example.bluechatroom.ui.search;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bluechatroom.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Friend_detail extends AppCompatActivity {

    TextInputLayout FullNameEditText, UserNameEditText, EmailEditText, PhoneNoEditText;
    CircleImageView ProfileImage;
    Button AddBtn;
    RadioGroup genderGroup;
    RadioButton genderSelected, male, female, other;
    ProgressDialog progressDialog;
    DatabaseReference databaseReference;

    AddFriendData addingFriendData;
    CreateRoom createRoom;

    DatabaseReference adding_friend_sender, adding_friend_receiver, chat_room;

    SharedPreferences sharedPreferences;
    String shared_name, shared_username, shared_email, shared_number, shared_password, shared_gender, shared_downloadUrl, shared_anonymous_status, shared_online_status;

    public String name, username, email, phone, password, gender, downloadUrl;

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

    List<String> friends = new ArrayList<>();
    DatabaseReference checkfriend;
    com.example.bluechatroom.ui.search.userData userData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_detail);

        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        shared_name = sharedPreferences.getString(KEY_NAME, null);
        shared_username = sharedPreferences.getString(KEY_USERNAME, null);
        shared_email = sharedPreferences.getString(KEY_EMAIL, null);
        shared_number = sharedPreferences.getString(KEY_NUMBER, null);
        shared_password = sharedPreferences.getString(KEY_PASSWORD, null);
        shared_gender = sharedPreferences.getString(KEY_GENDER, null);
        shared_downloadUrl = sharedPreferences.getString(KEY_DOWNLOADURL, null);
        shared_anonymous_status = sharedPreferences.getString(KEY_ANONYMOUS_STATUS, null);
        shared_online_status = sharedPreferences.getString(KEY_ONLINE_STATUS, null);

        name = getIntent().getStringExtra("name");
        username = getIntent().getStringExtra("username");
        email = getIntent().getStringExtra("email");
        phone = getIntent().getStringExtra("phone");
        password = getIntent().getStringExtra("password");
        gender = getIntent().getStringExtra("gender");
        downloadUrl = getIntent().getStringExtra("downloadUrl");

        ProfileImage = findViewById(R.id.ProfileImage);
        AddBtn = findViewById(R.id.AddBtn);

        FullNameEditText = findViewById(R.id.fullname);
        UserNameEditText = findViewById(R.id.username);
        EmailEditText = findViewById(R.id.email);
        PhoneNoEditText = findViewById(R.id.phoneno);
        genderGroup = findViewById(R.id.genderGroup);
        male = findViewById(R.id.male);
        female = findViewById(R.id.female);
        other = findViewById(R.id.other);

        try {
            Picasso.get().load(downloadUrl).into(ProfileImage);
        } catch (Exception e) {
            e.printStackTrace();
        }

        FullNameEditText.getEditText().setText(name);
        UserNameEditText.getEditText().setText(username);
        EmailEditText.getEditText().setText(email);
        PhoneNoEditText.getEditText().setText(phone);

        if (gender.equals("Male")) {
            male.setChecked(true);
        } else if (gender.equals("Female")) {
            female.setChecked(true);
        } else if (gender.equals("Other")) {
            other.setChecked(true);
        } else {
            Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
        }


        checkfriend = FirebaseDatabase.getInstance().getReference().child("users").child(shared_username).child("friends");

        checkfriend.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                friends.clear();

                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                        userData = snapshot.getValue(userData.class);

                        String username = userData.getUsername();

                        friends.add(username);
                    }
                }

                if (!friends.isEmpty()) {
                    for (String i : friends) {

                        if (i.equals(username)) {
                            AddBtn.setText("Friend");
                            return;

                        } else {
                            AddBtn.setText("Add Friend");
                        }
                    }
                } else {
                    AddBtn.setText("Add Friend");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Data Failed", Toast.LENGTH_SHORT).show();
            }
        });

        AddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String user_status = AddBtn.getText().toString().toUpperCase();


                if (user_status.equals("ALREADY FRIEND")) {

                } else if (user_status.equals("ADD FRIEND")) {

                    adding_friend_sender = FirebaseDatabase.getInstance().getReference().child("users");
                    adding_friend_receiver = FirebaseDatabase.getInstance().getReference().child("users");
                    chat_room = FirebaseDatabase.getInstance().getReference().child("chats");  // .child(uniqueKey)
                    final String uniqueKey = chat_room.push().getKey();


                    addingFriendData = new AddFriendData(username, name, email, downloadUrl, uniqueKey, phone);

                    adding_friend_sender.child(shared_username).child("friends").child(username).setValue(addingFriendData).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {

                            Toast.makeText(view.getContext(), "Friend Added in Your List", Toast.LENGTH_SHORT).show();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            Toast.makeText(view.getContext(), "Error in Adding to Your List", Toast.LENGTH_SHORT).show();

                        }
                    });

                    addingFriendData = new AddFriendData(shared_username, shared_name, shared_email, shared_downloadUrl, uniqueKey, shared_number);

                    adding_friend_receiver.child(username).child("friends").child(shared_username).setValue(addingFriendData).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {

                            Toast.makeText(view.getContext(), "you Added in Friend List", Toast.LENGTH_SHORT).show();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            Toast.makeText(view.getContext(), "Error in Adding to Friend List", Toast.LENGTH_SHORT).show();

                        }
                    });

                    createRoom = new CreateRoom(uniqueKey);

                    chat_room.child(uniqueKey).setValue(createRoom).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {

                            Toast.makeText(view.getContext(), "you Created Char Room", Toast.LENGTH_SHORT).show();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            Toast.makeText(view.getContext(), "Error in Creating Chat Room", Toast.LENGTH_SHORT).show();

                        }
                    });

                } else {
                    Toast.makeText(Friend_detail.this, "Nope", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
}