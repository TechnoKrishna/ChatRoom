package com.example.bluechatroom.ui.search;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bluechatroom.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter implements Filterable {

    private ArrayList<userData> users;
    private ArrayList<userData> usersfull;
    private Context context;

    public String name, username, email, phone, password, gender, downloadUrl;

    public UserAdapter(List<userData> friends, Context context) {
        this.usersfull = (ArrayList<userData>) friends;
        this.users = new ArrayList<>(usersfull);
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        View view;

        view = layoutInflater.inflate(R.layout.friend_item_layout, parent, false);

        return new UserViewAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        UserViewAdapter userViewAdapter = (UserViewAdapter) holder;

        userData item = users.get(position);

        name = item.getName();
        username = item.getUsername();
        email = item.getEmail();
        phone = item.getPhone();
        password = item.getPassword();
        gender = item.getGender();
        downloadUrl = item.getDownloadUrl();

        userViewAdapter.T_name.setText(name);
        userViewAdapter.T_username.setText(username);
        userViewAdapter.T_email.setText(email);

        try {
            Picasso.get().load(item.getDownloadUrl()).into(userViewAdapter.T_imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }

        userViewAdapter.T_cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                name = item.name;
                username = item.username;
                email = item.email;
                phone = item.phone;
                password = item.password;
                gender = item.gender;
                downloadUrl = item.downloadUrl;

                Intent intent = new Intent(context, Friend_detail.class);

                intent.putExtra("name", name);
                intent.putExtra("username", username);
                intent.putExtra("email", email);
                intent.putExtra("phone", phone);
                intent.putExtra("password", password);
                intent.putExtra("gender", gender);
                intent.putExtra("downloadUrl", downloadUrl);

                context.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return users.size();
    }


    @Override
    public Filter getFilter() {
        return userFilter;
    }

    private Filter userFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {

            ArrayList<userData> filterUserList = new ArrayList<>();

            if (charSequence == null || charSequence.length() == 0) {
                filterUserList.addAll(usersfull);
            } else {
                String filterPattern = charSequence.toString().toLowerCase().trim();

                for (userData UserData : usersfull) {

                    if (UserData.username.toLowerCase().contains(filterPattern)) {

                        filterUserList.add(UserData);

                    }
                }
            }

            FilterResults results = new FilterResults();

            results.values = filterUserList;
            results.count = filterUserList.size();
            return results;

        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {

            users.clear();
            users.addAll((ArrayList) filterResults.values);
            notifyDataSetChanged();
        }
    };


    public class UserViewAdapter extends RecyclerView.ViewHolder {

        private CardView T_cardView;
        private TextView T_name, T_email, T_username;
        private ImageView T_imageView;

        public UserViewAdapter(@NonNull View itemView) {
            super(itemView);

            T_name = itemView.findViewById(R.id.name);
            T_email = itemView.findViewById(R.id.email);
            T_username = itemView.findViewById(R.id.username);
            T_imageView = itemView.findViewById(R.id.userImage);
            T_cardView = itemView.findViewById(R.id.cardView);

        }
    }
}


//public class UserAdapter extends RecyclerView.Adapter {
//
//    // YOUR DETAIL
//
//    SharedPreferences sharedPreferences;
//    private static final String SHARED_PREF_NAME = "mypref";
//    private static final String KEY_NAME = "name";
//    private static final String KEY_USERNAME = "username";
//    private static final String KEY_EMAIL = "email";
//    private static final String KEY_NUMBER = "number";
//    private static final String KEY_DOWNLOADURL = "downloadUrl";
//
//    String shared_name, shared_username, shared_email, shared_number, shared_downloadUrl;
//
//    // YOUR DETAIL END'S
//
//    // FRIEND DETAIL
//
//    String friend_name, friend_username, friend_email, friend_downloadUrl, friend_phone;
//
//    // FRIEND DETAIL END'S
//
//    DatabaseReference adding_friend_sender, adding_friend_receiver, chat_room;
//
//    // ROOM ID
//
//    String room_id;
//
//    // ROOM ID END
//
//    AddFriendData addingFriendData;
//    CreateRoom createRoom;
//
//    private List<userData> users;
//    private List<AddFriendData> friends;
//    private Context context;
//
//
//
//    public UserAdapter(List<userData> users, List<AddFriendData> friends, Context context) {
//        this.users = users;
//        this.context = context;
//        this.friends = friends;
//    }
//
//    @Override
//    public int getItemViewType(int position) {
//
//        if (friends.isEmpty()) {
//            return 2;
//        } else {
//
//            for (AddFriendData addFriendData : friends) {
//
//                String Find_user = users.get(position).getUsername();
//                String Find_friend = addFriendData.getUsername();
//
//                if (Find_friend.equals(Find_user)) {
//                    Toast.makeText(context, "Friend  : " + Find_user + " : " + Find_friend , Toast.LENGTH_SHORT).show();
////                    friends.remove(0);
////                    users.remove(0);
//                    return 1;
//                } else {
//                    Toast.makeText(context, "Not Friend  : " + Find_user + " : " + Find_friend , Toast.LENGTH_SHORT).show();
//                    return 2;
//                }
//            }
//        }
//        return 0;
//    }
//
//
//    @NonNull
//    @Override
//    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//
//        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
//        View view;
//
//        if (viewType == 1) {
//            Toast.makeText(context, "View Type 1", Toast.LENGTH_SHORT).show();
//            view = layoutInflater.inflate(R.layout.user_already_friend_layout, parent, false);
//            return new AlreadyViewAdapter(view);
//        } else if (viewType == 2) {
//            Toast.makeText(context, "View Type 2", Toast.LENGTH_SHORT).show();
//            view = LayoutInflater.from(context).inflate(R.layout.user_item_layout, parent, false);
//            return new UserViewAdapter(view);
//        }
//
//        Toast.makeText(context, "View Type 3", Toast.LENGTH_SHORT).show();
//        view = LayoutInflater.from(context).inflate(R.layout.item_layout, parent, false);
//        return new UserViewAdapter(view);
//
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
//
//        sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
//        shared_name = sharedPreferences.getString(KEY_NAME, null);
//        shared_username = sharedPreferences.getString(KEY_USERNAME, null);
//        shared_email = sharedPreferences.getString(KEY_EMAIL, null);
//        shared_number = sharedPreferences.getString(KEY_NUMBER, null);
//        shared_downloadUrl = sharedPreferences.getString(KEY_DOWNLOADURL, null);
//
//        if (friends.isEmpty()) {
//
//            if (holder instanceof UserViewAdapter) {
//
//                UserViewAdapter userViewAdapter = (UserViewAdapter) holder;
//
//                userData item = users.get(position);
//
//                userViewAdapter.name.setText(item.getName());
//                userViewAdapter.username.setText(item.getUsername());
//                userViewAdapter.email.setText(item.getEmail());
//
//                try {
//                    Picasso.get().load(item.getDownloadUrl()).into(userViewAdapter.imageView);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//                adding_friend_sender = FirebaseDatabase.getInstance().getReference().child("users");
//                adding_friend_receiver = FirebaseDatabase.getInstance().getReference().child("users");
//                chat_room = FirebaseDatabase.getInstance().getReference().child("chats");  // .child(uniqueKey)
//                final String uniqueKey = chat_room.push().getKey();
//
//                userViewAdapter.addFriend.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//
//                        friend_name = item.name;
//                        friend_username = item.username;
//                        friend_email = item.email;
//                        friend_downloadUrl = item.downloadUrl;
//                        friend_phone = item.phone;
//
//                        addingFriendData = new AddFriendData(friend_username, friend_name, friend_email, friend_downloadUrl, uniqueKey, friend_phone);
//
//                        adding_friend_sender.child(shared_username).child("friends").child(friend_username).setValue(addingFriendData).addOnSuccessListener(new OnSuccessListener<Void>() {
//                            @Override
//                            public void onSuccess(Void unused) {
//
//                                Toast.makeText(view.getContext(), "Friend Added in Your List", Toast.LENGTH_SHORT).show();
//
//                            }
//                        }).addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//
//                                Toast.makeText(view.getContext(), "Error in Adding to Your List", Toast.LENGTH_SHORT).show();
//
//                            }
//                        });
//
//                        addingFriendData = new AddFriendData(shared_username, shared_name, shared_email, shared_downloadUrl, uniqueKey, shared_number);
//
//                        adding_friend_receiver.child(friend_username).child("friends").child(shared_username).setValue(addingFriendData).addOnSuccessListener(new OnSuccessListener<Void>() {
//                            @Override
//                            public void onSuccess(Void unused) {
//
//                                Toast.makeText(view.getContext(), "you Added in Friend List", Toast.LENGTH_SHORT).show();
//
//                            }
//                        }).addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//
//                                Toast.makeText(view.getContext(), "Error in Adding to Friend List", Toast.LENGTH_SHORT).show();
//
//                            }
//                        });
//
//                        createRoom = new CreateRoom(uniqueKey);
//
//                        chat_room.child(uniqueKey).setValue(createRoom).addOnSuccessListener(new OnSuccessListener<Void>() {
//                            @Override
//                            public void onSuccess(Void unused) {
//
//                                Toast.makeText(view.getContext(), "you Created Char Room", Toast.LENGTH_SHORT).show();
//
//                            }
//                        }).addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//
//                                Toast.makeText(view.getContext(), "Error in Creating Chat Room", Toast.LENGTH_SHORT).show();
//
//                            }
//                        });
//                    }
//                });
//
//            } else {
//                Toast.makeText(context, "User View 1 Cast Can't be Done", Toast.LENGTH_SHORT).show();
//            }
//
//        } else {
//
//            for (AddFriendData addFriendData : friends) {
//
//                String Find_user = users.get(position).getUsername();
//                String Find_friend = addFriendData.getUsername();
//
//                if (Find_friend.equals(Find_user)) {
//
//                    if (holder instanceof AlreadyViewAdapter) {
//
//                        AlreadyViewAdapter userAlreadyViewAdapter = (AlreadyViewAdapter) holder;
//
//                        userData item = users.get(position);
//
//                        userAlreadyViewAdapter.name.setText(item.getName());
//                        userAlreadyViewAdapter.username.setText(item.getUsername());
//                        userAlreadyViewAdapter.email.setText(item.getEmail());
//
//                        try {
//                            Picasso.get().load(item.getDownloadUrl()).into(userAlreadyViewAdapter.imageView);
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    } else {
//                        Toast.makeText(context, "Already View Cast Can't be Done", Toast.LENGTH_SHORT).show();
//                    }
//
//                } else {
//
//                    if (holder instanceof  UserViewAdapter) {
//
//                        UserViewAdapter userViewAdapter = (UserViewAdapter) holder;
//
//                        userData item = users.get(position);
//
//                        userViewAdapter.name.setText(item.getName());
//                        userViewAdapter.username.setText(item.getUsername());
//                        userViewAdapter.email.setText(item.getEmail());
//
//                        try {
//                            Picasso.get().load(item.getDownloadUrl()).into(userViewAdapter.imageView);
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//
//                        adding_friend_sender = FirebaseDatabase.getInstance().getReference().child("users");
//                        adding_friend_receiver = FirebaseDatabase.getInstance().getReference().child("users");
//                        chat_room = FirebaseDatabase.getInstance().getReference().child("chats");  // .child(uniqueKey)
//                        final String uniqueKey = chat_room.push().getKey();
//
//                        userViewAdapter.addFriend.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//
//                                friend_name = item.name;
//                                friend_username = item.username;
//                                friend_email = item.email;
//                                friend_downloadUrl = item.downloadUrl;
//                                friend_phone = item.phone;
//
//                                addingFriendData = new AddFriendData(friend_username, friend_name, friend_email, friend_downloadUrl, uniqueKey, friend_phone);
//
//                                adding_friend_sender.child(shared_username).child("friends").child(friend_username).setValue(addingFriendData).addOnSuccessListener(new OnSuccessListener<Void>() {
//                                    @Override
//                                    public void onSuccess(Void unused) {
//
//                                        Toast.makeText(view.getContext(), "Friend Added in Your List", Toast.LENGTH_SHORT).show();
//
//                                    }
//                                }).addOnFailureListener(new OnFailureListener() {
//                                    @Override
//                                    public void onFailure(@NonNull Exception e) {
//
//                                        Toast.makeText(view.getContext(), "Error in Adding to Your List", Toast.LENGTH_SHORT).show();
//
//                                    }
//                                });
//
//                                addingFriendData = new AddFriendData(shared_username, shared_name, shared_email, shared_downloadUrl, uniqueKey, shared_number);
//
//                                adding_friend_receiver.child(friend_username).child("friends").child(shared_username).setValue(addingFriendData).addOnSuccessListener(new OnSuccessListener<Void>() {
//                                    @Override
//                                    public void onSuccess(Void unused) {
//
//                                        Toast.makeText(view.getContext(), "you Added in Friend List", Toast.LENGTH_SHORT).show();
//
//                                    }
//                                }).addOnFailureListener(new OnFailureListener() {
//                                    @Override
//                                    public void onFailure(@NonNull Exception e) {
//
//                                        Toast.makeText(view.getContext(), "Error in Adding to Friend List", Toast.LENGTH_SHORT).show();
//
//                                    }
//                                });
//
//                                createRoom = new CreateRoom(uniqueKey);
//
//                                chat_room.child(uniqueKey).setValue(createRoom).addOnSuccessListener(new OnSuccessListener<Void>() {
//                                    @Override
//                                    public void onSuccess(Void unused) {
//
//                                        Toast.makeText(view.getContext(), "you Created Char Room", Toast.LENGTH_SHORT).show();
//
//                                    }
//                                }).addOnFailureListener(new OnFailureListener() {
//                                    @Override
//                                    public void onFailure(@NonNull Exception e) {
//
//                                        Toast.makeText(view.getContext(), "Error in Creating Chat Room", Toast.LENGTH_SHORT).show();
//
//                                    }
//                                });
//                            }
//                        });
//
//                    } else {
//                        Toast.makeText(context, "User View 2 Cast Can't be Done", Toast.LENGTH_SHORT).show();
//                    }
//                }
//            }
//
//        }
//
//    }
//
//    @Override
//    public int getItemCount() {
//        return users.size();
//    }
//
//    public class AlreadyViewAdapter extends RecyclerView.ViewHolder {
//
//        private TextView name, email, username;
//        private ImageView imageView;
//        private Button addFriend;
//
//        public AlreadyViewAdapter(@NonNull View itemView) {
//            super(itemView);
//
//            name = itemView.findViewById(R.id.name);
//            email = itemView.findViewById(R.id.email);
//            username = itemView.findViewById(R.id.username);
//            imageView = itemView.findViewById(R.id.userImage);
//            addFriend = itemView.findViewById(R.id.addFriend);
//
//        }
//    }
//
//    public class UserViewAdapter extends RecyclerView.ViewHolder {
//
//        private TextView name, email, username;
//        private ImageView imageView;
//        private Button addFriend;
//
//        public UserViewAdapter(@NonNull View itemView) {
//            super(itemView);
//
//            name = itemView.findViewById(R.id.name);
//            email = itemView.findViewById(R.id.email);
//            username = itemView.findViewById(R.id.username);
//            imageView = itemView.findViewById(R.id.userImage);
//            addFriend = itemView.findViewById(R.id.addFriend);
//
//        }
//    }
//
//}

