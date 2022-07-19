package com.example.bluechatroom.ui.search;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bluechatroom.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SearchFragment extends Fragment {

    RecyclerView userRecycler;
    LinearLayout NoData;
    ArrayList<userData> users;
    ArrayList<AddFriendData> friends;
    UserAdapter userAdapter;

    userData userData;
    AddFriendData addFriendData;

    DatabaseReference databaseReference, checkfriend;

    ProgressDialog progressDialog;

    SharedPreferences sharedPreferences;

    private static final String SHARED_PREF_NAME = "mypref";
    private static final String KEY_USERNAME = "username";

    String shared_username;

    SearchView searchView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        progressDialog = new ProgressDialog(getContext());

        userRecycler = view.findViewById(R.id.userRecycler);
        NoData = view.findViewById(R.id.NoData);
        searchView = view.findViewById(R.id.searchView);

        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setQueryHint("Search Here");

        sharedPreferences = this.getActivity().getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        shared_username = sharedPreferences.getString(KEY_USERNAME, null);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("users");
        checkfriend = FirebaseDatabase.getInstance().getReference().child("users").child(shared_username).child("friends");

        users = new ArrayList<>();
        friends = new ArrayList<>();

        getUsers();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                if (userAdapter != null) {

                    userAdapter.getFilter().filter(newText);

                }

                return false;
            }
        });

        return view;
    }

    private void getUsers() {
        progressDialog.setTitle("Please Wait...");
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        checkfriend.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                friends.clear();

                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                        addFriendData = snapshot.getValue(AddFriendData.class);

                        String username = addFriendData.getUsername().toString();

                        friends.add(addFriendData);
                    }
                }

                for (AddFriendData i : friends) {
                    Log.v("This Is Friend Username", i.getUsername());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Data Failed", Toast.LENGTH_SHORT).show();
            }
        });


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                users.clear();

                if (!dataSnapshot.exists()) {
                    NoData.setVisibility(View.VISIBLE);
                    userRecycler.setVisibility(View.GONE);
                } else {
                    NoData.setVisibility(View.GONE);
                    userRecycler.setVisibility(View.VISIBLE);

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                        userData = snapshot.getValue(userData.class);

                        String username = userData.getUsername().toString();

                        if (!userData.getUsername().equals(shared_username)) {

                            users.add(userData);

                        }
                    }

                    for (userData i : users) {
                        Log.v("This Is Your Username", i.getUsername());
                    }

                    userRecycler.setHasFixedSize(true);
                    userRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
                    userAdapter = new UserAdapter(users, getContext());
                    userRecycler.setAdapter(userAdapter);
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