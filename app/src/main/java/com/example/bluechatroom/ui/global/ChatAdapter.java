package com.example.bluechatroom.ui.global;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bluechatroom.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatAdapter extends RecyclerView.Adapter {

    private List<Send_Message_Modal> chat_data;
    private Context context;

    SharedPreferences sharedPreferences;
    private static final String SHARED_PREF_NAME = "mypref";
    private static final String KEY_USERNAME = "username";

    String shared_username;

    public ChatAdapter(List<Send_Message_Modal> chat_data, Context context) {
        this.chat_data = chat_data;
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {

        sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);

        shared_username = sharedPreferences.getString(KEY_USERNAME, null);

        if (chat_data.get(position).getUsername().contains(shared_username)) {
            return 1;
        }
        return 0;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view;

        if (viewType == 1) {
            view = layoutInflater.inflate(R.layout.sender_message_layout, parent, false);
            return new SenderViewAdapter(view);
        }

        view = layoutInflater.inflate(R.layout.reciever_message_layout, parent, false);
        return new RecieverViewAdapter(view);

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (chat_data.get(position).getUsername().contains(shared_username)) {

            SenderViewAdapter senderViewAdapter = (SenderViewAdapter) holder;

            Send_Message_Modal item = chat_data.get(position);

            senderViewAdapter.message.setText(item.getMessage());
            senderViewAdapter.time.setText(item.getTime());

        } else {

            RecieverViewAdapter recieverViewAdapter = (RecieverViewAdapter) holder;

            Send_Message_Modal item = chat_data.get(position);

            recieverViewAdapter.username.setText(item.getUsername());
            recieverViewAdapter.message.setText(item.getMessage());
            recieverViewAdapter.time.setText(item.getTime());

            try {
                Picasso.get().load(item.getDownloadUrl()).into(((RecieverViewAdapter) holder).profileImage);
            } catch (Exception e) {
                e.printStackTrace();
            }


        }

    }

    @Override
    public int getItemCount() {
        return chat_data.size();
    }

    public class SenderViewAdapter extends RecyclerView.ViewHolder {

        TextView message, time;

        public SenderViewAdapter(@NonNull View itemView) {
            super(itemView);

            message = itemView.findViewById(R.id.message);
            time = itemView.findViewById(R.id.time);

        }
    }

    public class RecieverViewAdapter extends RecyclerView.ViewHolder {

        CircleImageView profileImage;
        TextView message, time, username;

        public RecieverViewAdapter(@NonNull View itemView) {
            super(itemView);

            username = itemView.findViewById(R.id.username);
            message = itemView.findViewById(R.id.message);
            time = itemView.findViewById(R.id.time);
            profileImage = itemView.findViewById(R.id.profileImage);

        }
    }

}