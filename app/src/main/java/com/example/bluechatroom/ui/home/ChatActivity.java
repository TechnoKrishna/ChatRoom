package com.example.bluechatroom.ui.home;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bluechatroom.R;
import com.example.bluechatroom.ui.global.ChatAdapter;
import com.example.bluechatroom.ui.global.Send_Message_Modal;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {

    RecyclerView messageRecycler;
    LinearLayout NoData;
    EditText message_EditText;
    ImageView Send_Btn;
    CircleImageView profileImage;
    TextView username_Edittext, online_status_Edittext;
    String Message;

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

    DatabaseReference chat_reference;

    String username, name, email, downloadUrl, room_id, phone, online_status;

    List<Send_Message_Modal> chat_data;

    ChatAdapter chatAdapter;

    String shared_name, shared_username, shared_email, shared_number, shared_password, shared_gender, shared_downloadUrl, shared_anonymous_status, shared_online_status, shared_logged_status;

    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_chat);

        messageRecycler = findViewById(R.id.messageRecycler);
        NoData = findViewById(R.id.NoData);
        message_EditText = findViewById(R.id.message_edittext);
        Send_Btn = findViewById(R.id.send_btn);
        profileImage = findViewById(R.id.profileImage);
        username_Edittext = findViewById(R.id.username);
        online_status_Edittext = findViewById(R.id.online_status);

        username = getIntent().getStringExtra("username");
        name = getIntent().getStringExtra("name");
        email = getIntent().getStringExtra("email");
        downloadUrl = getIntent().getStringExtra("downloadUrl");
        room_id = getIntent().getStringExtra("room_id");
        phone = getIntent().getStringExtra("phone");

        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        shared_name = sharedPreferences.getString(KEY_NAME, null);
        shared_username = sharedPreferences.getString(KEY_USERNAME, null);
        shared_email = sharedPreferences.getString(KEY_EMAIL, null);
        shared_number = sharedPreferences.getString(KEY_NUMBER, null);
        shared_password = sharedPreferences.getString(KEY_PASSWORD, null);
        shared_gender = sharedPreferences.getString(KEY_GENDER, null);
        shared_downloadUrl = sharedPreferences.getString(KEY_DOWNLOADURL, null);
        shared_anonymous_status = sharedPreferences.getString(KEY_ANONYMOUS_STATUS, null);
        shared_online_status = sharedPreferences.getString(KEY_ONLINE_STATUS, null);
        shared_logged_status = sharedPreferences.getString(KEY_LOGGED_STATUS, null);

        databaseReference = FirebaseDatabase.getInstance().getReference("users");

        chat_reference = FirebaseDatabase.getInstance().getReference().child("chats").child(room_id).child("chats");

        Query query = databaseReference.orderByChild("username").equalTo(username);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                    online_status = dataSnapshot.child(username).child("online_status").getValue(String.class);

                    online_status_Edittext.setText(online_status);

                } else {
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {


            }
        });

        username_Edittext.setText(username);

        try {
            Picasso.get().load(downloadUrl).into(profileImage);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Send_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
            }
        });

        chat_reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chat_data = new ArrayList<>();

                if (!dataSnapshot.exists()) {
                    NoData.setVisibility(View.VISIBLE);
                    messageRecycler.setVisibility(View.GONE);
//                            pd.dismiss();
                } else {
                    NoData.setVisibility(View.GONE);
                    messageRecycler.setVisibility(View.VISIBLE);
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Send_Message_Modal data = snapshot.getValue(Send_Message_Modal.class);
                        chat_data.add(data);
                    }

                    messageRecycler.setHasFixedSize(true);
//                                messageRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
                    LinearLayoutManager linearLayoutManager;
                    linearLayoutManager = new LinearLayoutManager(getApplicationContext());
//                            linearLayoutManager.setReverseLayout(true);
                    linearLayoutManager.setStackFromEnd(true);
                    messageRecycler.setLayoutManager(linearLayoutManager);
                    chatAdapter = new ChatAdapter(chat_data, getApplicationContext());
                    messageRecycler.setAdapter(chatAdapter);
//                            pd.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
//                        pd.dismiss();
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void sendMessage() {

        try {
            Message = message_EditText.getText().toString();
        } catch (NullPointerException e) {
            Toast.makeText(getApplicationContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        if (!Message.isEmpty()) {

            String message, unique_id, username, time, downloadUrl;

            message = Message;
            unique_id = chat_reference.push().getKey();
            username = shared_username;

            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh.mm aa");
            time = dateFormat.format(new Date()).toString();

            downloadUrl = shared_downloadUrl;

            Send_Message_Modal send_message_modal = new Send_Message_Modal(message, unique_id, username, time, downloadUrl);

            chat_reference.child(unique_id).setValue(send_message_modal);

            message_EditText.setText(null);


        }
    }
}