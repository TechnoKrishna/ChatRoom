package com.example.bluechatroom.ui.global;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bluechatroom.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GlobalFragment extends Fragment {

    RecyclerView messageRecycler;
    LinearLayout NoData;
    Spinner server_spinner;
    EditText message_EditText;
    ImageView Send_Btn;

    List<Send_Message_Modal> chat_data;

    ChatAdapter chatAdapter;

    String server = "global";

    ArrayList<String> servers;

    DatabaseReference server_reference, chat_reference;

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

    String Message;

    String shared_name, shared_username, shared_email, shared_number, shared_password, shared_gender, shared_downloadUrl, shared_anonymous_status, shared_online_status, shared_logged_status;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_global, container, false);

        messageRecycler = view.findViewById(R.id.messageRecycler);
        NoData = view.findViewById(R.id.NoData);
        server_spinner = view.findViewById(R.id.server_spinner);
        message_EditText = view.findViewById(R.id.message_edittext);
        Send_Btn = view.findViewById(R.id.send_btn);

        sharedPreferences = this.getActivity().getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);

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

        server_reference = FirebaseDatabase.getInstance().getReference().child("servers");

        Send_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
            }
        });

        servers = new ArrayList<>();

//        String server_id = "2";
//        String server_name = "asia";
//
//        Server_Modal server_modal = new Server_Modal(server_id, server_name);

//        server_reference.child(server_name).setValue(server_modal);

//        server_reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                servers.clear();
//
//                if (dataSnapshot.exists()) {
//
////                    String server_name = dataSnapshot.child(server).child("server_name").getValue(String.class);
//
//                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//
//                        Server_Modal data = snapshot.getValue(Server_Modal.class);
//
//                        String server_name = data.getServer_name().toString();
//
//                        servers.add(server_name);
//
//                    }
//
////                    String[] servers = new String[]{"global", "asia"};
//                    server_spinner.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, servers));
//
//                }
//
//            }
//
//        @Override
//        public void onCancelled (@NonNull DatabaseError error){
//            Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
//        }
//    });

        String[] servers = new String[]{"global", "asia", "africa", "europe", "north america", "south america", "australia"};
        server_spinner.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, servers));


        server_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                try {
                    server = server_spinner.getSelectedItem().toString();
                } catch (Exception e) {
                    Toast.makeText(getContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }

                server_reference.child(server).child("chats").addValueEventListener(new ValueEventListener() {
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
                            linearLayoutManager = new LinearLayoutManager(getContext());
//                            linearLayoutManager.setReverseLayout(true);
                            linearLayoutManager.setStackFromEnd(true);
                            messageRecycler.setLayoutManager(linearLayoutManager);
                            chatAdapter = new ChatAdapter(chat_data, getContext());
                            messageRecycler.setAdapter(chatAdapter);
//                            pd.dismiss();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
//                        pd.dismiss();
                        Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        return view;
    }

    private void sendMessage() {

        try {
            Message = message_EditText.getText().toString();
        } catch (NullPointerException e) {
            Toast.makeText(getContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        chat_reference = FirebaseDatabase.getInstance().getReference().child("servers").child(server).child("chats");

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