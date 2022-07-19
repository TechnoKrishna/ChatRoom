package com.example.bluechatroom.ui.home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bluechatroom.R;
import com.example.bluechatroom.ui.search.AddFriendData;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.FriendViewAdapter> {

    private List<AddFriendData> friends;
    private Context context;

    SharedPreferences sharedPreferences;
    private static final String SHARED_PREF_NAME = "mypref";
    private static final String KEY_USERNAME = "username";

    String shared_username;

    String username, name, email, downloadUrl, room_id, phone;

    DatabaseReference databaseReference;

    public FriendAdapter(List<AddFriendData> friends, Context context) {
        this.friends = friends;
        this.context = context;
    }

    @NonNull
    @Override
    public FriendViewAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.friend_item_layout, parent, false);
        return new FriendViewAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendViewAdapter holder, int position) {

        databaseReference = FirebaseDatabase.getInstance().getReference().child("users");

        sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        shared_username = sharedPreferences.getString(KEY_USERNAME, null);

        AddFriendData item = friends.get(position);

        room_id = item.getRoom_id();
        username = item.getUsername();
        name = item.getName();
        email = item.getEmail();
        phone = item.getPhone();
        downloadUrl = item.getDownloadUrl();

        holder.name.setText(name);
        holder.username.setText(username);
        holder.email.setText(email);

        try {
            Picasso.get().load(item.getDownloadUrl()).into(holder.imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(context, "You Clicked", Toast.LENGTH_SHORT).show();

                room_id = item.room_id;
                username = item.username;
                name = item.name;
                email = item.email;
                phone = item.phone;
                downloadUrl = item.downloadUrl;

                Intent intent = new Intent(context, ChatActivity.class);

                intent.putExtra("room_id", room_id);
                intent.putExtra("username", username);
                intent.putExtra("name", name);
                intent.putExtra("email", email);
                intent.putExtra("phone", phone);
                intent.putExtra("downloadUrl", downloadUrl);

                context.startActivity(intent);


            }
        });

    }

    @Override
    public int getItemCount() {
        return friends.size();
    }

    public class FriendViewAdapter extends RecyclerView.ViewHolder {

        private CardView cardView;
        private TextView name, email, username;
        private ImageView imageView;

        public FriendViewAdapter(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            email = itemView.findViewById(R.id.email);
            username = itemView.findViewById(R.id.username);
            imageView = itemView.findViewById(R.id.userImage);
            cardView = itemView.findViewById(R.id.cardView);

        }
    }
}
