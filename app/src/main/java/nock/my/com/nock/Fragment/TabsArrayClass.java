package nock.my.com.nock.Fragment;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.ArrayList;

import nock.my.com.nock.ChatScreenView.ChoosingAdapter;
import nock.my.com.nock.activities.LoginActivity;

public class TabsArrayClass {
    public static ArrayList<String> Users = new ArrayList<>();
    public static ArrayList<String> UsersGirl = new ArrayList<>();
    public static ArrayList<String> MessagesUsers = new ArrayList<>();
    public static ArrayList<String> RoomUsers = new ArrayList<>();

    private DatabaseReference database;
    private ChoosingAdapter adapter;
    private volatile String friends;

    public void refreshUsers_B_Array(ChoosingAdapter adapter, int id) {
        this.adapter = adapter;
        adapter.listNames.clear();
        adapter.setArray(TabsArrayClass.Users, id);

        if (LoginActivity.ChooseMateScreenON) {
            DatabaseReference database = FirebaseDatabase.getInstance().getReference("Users");

            Query query = database.orderByValue();

            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String mates;
                    TabsArrayClass.Users.clear();

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                         Log.d("my","Abhi!!!!!!!!!!!!!!!!!!!!"+snapshot.getValue().toString());
                        if (snapshot.child("gender").getValue().toString().equals("male")) {
                            mates = snapshot.child("userName").getValue().toString();
                            if (!mates.equals(LoginActivity.UserName)) {
                                TabsArrayClass.Users.add(mates);
                                TabsArrayClass.this.adapter.notifyDataSetChanged();
                                Log.d("my", "userName=" + snapshot.child("userName").getValue().toString());
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    public void refreshUsers_G_Array(ChoosingAdapter adapter, int id) {
        this.adapter = adapter;
        adapter.listNames.clear();
        adapter.setArray(TabsArrayClass.UsersGirl, id);

        if (LoginActivity.ChooseMateScreenON) {
            DatabaseReference database = FirebaseDatabase.getInstance().getReference("Users");

            Query query = database.orderByKey();

            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String mates;
                    TabsArrayClass.UsersGirl.clear();

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        if (snapshot.child("gender").getValue().toString().equals("female")) {
                            mates = snapshot.child("userName").getValue().toString();
                            if (!mates.equals(LoginActivity.UserName)) {
                                TabsArrayClass.UsersGirl.add(mates);
                                TabsArrayClass.this.adapter.notifyDataSetChanged();
                                Log.d("my", "userName=" + snapshot.child("userName").getValue().toString());
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }
    }

    public void refreshMessageArray(ChoosingAdapter adapter, int id) {
        this.adapter = adapter;

        adapter.listNames.clear();
        adapter.setArray(TabsArrayClass.MessagesUsers, id);

        database = FirebaseDatabase.getInstance().getReference("Chat");

        Query query = database.orderByKey();
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String friendsKey;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    friendsKey = snapshot.getKey();
                    if (friendsKey.contains(LoginActivity.UserName)) {
                        Log.d("my", "Level 1 reached!");
                        int index = friendsKey.indexOf(LoginActivity.UserName);
                        if (index == 0) {
                            Log.d("my", "Level 2 reached!");
                            friends = friendsKey.substring(index + LoginActivity.UserName.length() + 5);
                            Query query = database.child(LoginActivity.UserName + " and " + friends).orderByValue();
                            query.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    boolean isthereNew = false;
//                                    TabsArrayClass.MessagesUsers.clear();
                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                        isthereNew = snapshot.getValue().toString().contains("new:");
                                        Log.d("my", "Values are: " + snapshot.getValue().toString());
                                        if (isthereNew) {
                                            int index = snapshot.getRef().getParent().toString().indexOf(LoginActivity.UserName);
                                            String and = "%20and%20";
                                            String parentSender = snapshot.getRef().getParent().toString().substring(index + LoginActivity.UserName.length() + and.length());
                                            TabsArrayClass.MessagesUsers.add(parentSender);
                                            Log.d("my", parentSender + " Added to Message array!!!!!");
                                            TabsArrayClass.this.adapter.notifyDataSetChanged();
                                            break;
                                        }
                                    }
                                    Log.d("my", "Level 3 reached! " + isthereNew);
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                }
                            });
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public void refreshRoomArray(ChoosingAdapter adapter, int id) {
        this.adapter = adapter;
        adapter.listNames.clear();
        adapter.setArray(TabsArrayClass.RoomUsers, id);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Room");
        ref.push().child(LoginActivity.UserName);
        Query query = ref.child(LoginActivity.UserName).orderByValue();
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                TabsArrayClass.RoomUsers.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (dataSnapshot.getChildrenCount() == 0) {
                        TabsArrayClass.this.adapter.notifyDataSetChanged();
                    } else {
                        TabsArrayClass.RoomUsers.add(snapshot.getValue().toString());
                        TabsArrayClass.this.adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}
