package com.example.bluechatroom.ui.home;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bluechatroom.R;
import com.example.bluechatroom.ui.search.AddFriendData;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    RecyclerView userRecycler;
    LinearLayout NoData;
    List<AddFriendData> friends;
    FriendAdapter friendAdapter;

    ProgressDialog progressDialog;

//    userroom data;

    AddFriendData addFriendData;

    DatabaseReference databaseReference;

    SharedPreferences sharedPreferences;
    private static final String SHARED_PREF_NAME = "mypref";
    private static final String KEY_USERNAME = "username";

    String shared_username;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        progressDialog = new ProgressDialog(getContext());

        userRecycler = view.findViewById(R.id.userRecycler);
        NoData = view.findViewById(R.id.NoData);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("users");

        sharedPreferences = this.getActivity().getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        shared_username = sharedPreferences.getString(KEY_USERNAME, null);

        getFriends();

        return view;
    }

    private void getFriends() {

        progressDialog.setTitle("Please Wait...");
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        databaseReference.child(shared_username).child("friends").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                friends = new ArrayList<>();

                friends.clear();

                if (!dataSnapshot.exists()) {
                    NoData.setVisibility(View.VISIBLE);
                    userRecycler.setVisibility(View.GONE);
                    progressDialog.dismiss();
                } else {
                    NoData.setVisibility(View.GONE);
                    userRecycler.setVisibility(View.VISIBLE);

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                        addFriendData = snapshot.getValue(AddFriendData.class);

//                        String get_friend = data.getFriend().toString();
//                        String get_you = data.getYou().toString();
////
//                        if (get_friend.equals(shared_username)) {
//
//                            friends.add(data);
//
//                        } else if (get_you.equals(shared_username)) {
                        friends.add(addFriendData);
//                        }

                    }
                    userRecycler.setHasFixedSize(true);
                    userRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
                    friendAdapter = new FriendAdapter(friends, getContext());
                    userRecycler.setAdapter(friendAdapter);
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}