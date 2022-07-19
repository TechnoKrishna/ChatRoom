package com.example.bluechatroom;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Objects;

public class DashBoard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

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

    String shared_name, shared_username, shared_email, shared_number, shared_password, shared_gender, shared_downloadUrl, shared_anonymous_status, shared_online_status;

    StorageReference storageReference;
    DatabaseReference databaseReference;

    private BottomNavigationView bottomNavigationView;
    private NavController navController;

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;

    ImageView headerImage;
    TextView headerName, headerEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        navController = Navigation.findNavController(this, R.id.frame_layout);

        drawerLayout = findViewById(R.id.drawerlayout);
        navigationView = findViewById(R.id.navigation_view);

        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.start, R.string.close);

        drawerLayout.addDrawerListener(toggle);

        toggle.syncState();

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        navigationView.setNavigationItemSelectedListener(this);

        NavigationUI.setupWithNavController(bottomNavigationView, navController);

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

        View headerView = navigationView.getHeaderView(0);
        headerName = headerView.findViewById(R.id.username);
        headerEmail = headerView.findViewById(R.id.email);
        headerImage = headerView.findViewById(R.id.profileImage);

        headerName.setText(shared_username);
        headerEmail.setText(shared_email);

//        btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
//
//                SharedPreferences.Editor editor = sharedPreferences.edit();
//                editor.clear();
//                editor.commit();
//
//                Intent intent = new Intent(DashBoard.this, Login_Activity.class);
//                startActivity(intent);
//                finish();
//            }
//        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.navigation_logout:
//                Toast.makeText(this, "Logout", Toast.LENGTH_SHORT).show();

                sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.commit();

                Intent intent = new Intent(DashBoard.this, Login_Activity.class);
                startActivity(intent);
                finish();

                break;
//            case R.id.navigation_video:
//                Toast.makeText(this, "Video Lectures", Toast.LENGTH_SHORT).show();
//                break;
//            case R.id.navigation_rate:
////                Toast.makeText(this, "Rate Us", Toast.LENGTH_SHORT).show();
//                Intent rate = new Intent(Intent.ACTION_VIEW);
//                rate.setData(Uri.parse("https://docs.google.com/forms/d/e/1FAIpQLSdQJmJH8AnDWHmXoWzi4EibK-m6hwDNZI-G73Ns85C0Usgjew/viewform?usp=sf_link"));
//                startActivity(rate);
//                break;
//            case R.id.navigation_ebook:
//                startActivity(new Intent(this, EBook.class));
////                Toast.makeText(this, "E Book", Toast.LENGTH_SHORT).show();
//                break;
//            case R.id.navigation_theme:
//                Toast.makeText(this, "Theme", Toast.LENGTH_SHORT).show();
//                break;
//            case R.id.navigation_website:
////                Toast.makeText(this, "Website", Toast.LENGTH_SHORT).show();
//                String url = "https://rsmpoly.org/";
//                Intent i = new Intent(Intent.ACTION_VIEW);
//                i.setData(Uri.parse(url));
//                startActivity(i);
//                break;
//            case R.id.navigation_share:
//                Toast.makeText(this, "Share", Toast.LENGTH_SHORT).show();
//                break;
        }
        return true;
    }

    @Override
    protected void onStart() {

        try {
            Picasso.get().load(shared_downloadUrl).into(headerImage);
        } catch (Exception e) {
        }

        databaseReference = FirebaseDatabase.getInstance().getReference().child("users");

        HashMap hp = new HashMap();

        hp.put("online_status", "online");

        databaseReference.child(shared_username).updateChildren(hp).addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });


        super.onStart();
    }

    @Override
    protected void onDestroy() {

        databaseReference = FirebaseDatabase.getInstance().getReference().child("users");

        HashMap hp = new HashMap();

        hp.put("online_status", "offline");

        databaseReference.child(shared_username).updateChildren(hp).addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

        super.onDestroy();
    }

    @Override
    public void onBackPressed() {

        new AlertDialog.Builder(DashBoard.this)
                .setTitle("Exit")
                .setMessage("Are you sure, You want to exit?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.finishAffinity(DashBoard.this);
                        finish();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setIcon(R.drawable.ic_warning_icon)
                .show();
    }
}