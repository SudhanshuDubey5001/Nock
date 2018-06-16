package nock.my.com.nock.activities;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import nock.my.com.nock.ChatScreenView.AdapterClass;
import nock.my.com.nock.ChatScreenView.loadMoreMessages;
import nock.my.com.nock.DatabaseOperations;
import nock.my.com.nock.Fragment.TabsArrayClass;
import nock.my.com.nock.Fragment.ViewPagerCreation;
import nock.my.com.nock.MessageRecieveClass;
import nock.my.com.nock.R;
import nock.my.com.nock.SharedPrefs;

public class MainActivity extends AppCompatActivity implements loadMoreMessages {

    public static final String GETFRIEND = "friend";
    public static final String GETID = "id";

    boolean firstTime = true;
    public static boolean NOTI_Enable = false;

    ArrayList<String> MassStorageMsg = new ArrayList<>();
    ArrayList<String> MassStorageKey = new ArrayList<>();
    EditText msgField;
    AdapterClass adapterClass = new AdapterClass(this);
    public static String friendUserName;

    DatabaseReference ref;
    static String children1;
    static String children2;
    String msgText;
    RecyclerView recyclerView;
    public static MediaPlayer mpRecieve;
    MediaPlayer mpSend;
    String status;
    int noOfMessages;
    ChildEventListener childEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d("my", "MainActivity started!!");

        //notification---->
        MessageRecieveClass.value = false;
        Intent in = new Intent(this, MessageRecieveClass.class);
        stopService(in);
        SharedPrefs.setNoti(false);

        //message sounds------->
        mpSend = MediaPlayer.create(this, R.raw.send);
        mpRecieve = MediaPlayer.create(this, R.raw.recieve);

//       friend name------------>
        int id = getIntent().getIntExtra(GETID, 0);
        Log.d("my", "Mainactivity id: " + id);
        if (id == ViewPagerCreation.USERS_B) {
            friendUserName = TabsArrayClass.Users.get(getIntent().getIntExtra(GETFRIEND, 0));
        } else if (id == ViewPagerCreation.MESSAGES) {
            friendUserName = TabsArrayClass.MessagesUsers.get(getIntent().getIntExtra(GETFRIEND, 0));
        } else if(id==ViewPagerCreation.ROOM){
            friendUserName = TabsArrayClass.RoomUsers.get(getIntent().getIntExtra(GETFRIEND, 0));
        }else{
            friendUserName = TabsArrayClass.UsersGirl.get(getIntent().getIntExtra(GETFRIEND, 0));
        }
        Log.d("my", "friendKey=" + friendUserName);

//      Getting user status online/offline---------->
        DatabaseReference statusRef = FirebaseDatabase.getInstance().getReference("Status");
        Query query = statusRef.orderByKey();
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (snapshot.getKey().equals(friendUserName)) {
                        if (snapshot.getValue().equals(DatabaseOperations.ONLINE)) {
                            status = "ONLINE";
//                          setActionBar----->
                            setTitle(friendUserName + " " + status);
                        } else {
                            status = "OFFLINE";
//                          setActionBar----->
                            setTitle(friendUserName + " " + status);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

//      setting send message-------->
        msgField = findViewById(R.id.chatMsg);
        Button sendButton = findViewById(R.id.send);

//      Recycler View------------>
        recyclerView = findViewById(R.id.chatScreen);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapterClass);

//      Firebase adding chat column------------>
        children1 = LoginActivity.UserName + " and " + friendUserName;
        children2 = friendUserName + " and " + LoginActivity.UserName;

        ref = FirebaseDatabase.getInstance().getReference("Chat");
        ref.push().child(children1);
        ref.push().child(children2);

//      read the messages old and current-------------->
        DatabaseReference countRef = FirebaseDatabase.getInstance().getReference("Chat");
        countRef.child(children1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                noOfMessages = (int) dataSnapshot.getChildrenCount();
                Log.d("my", "no of messages: " + noOfMessages);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//              read all messages to mass storage first----->
                String msg = dataSnapshot.getValue().toString();
                String key = dataSnapshot.getKey();

                if(noOfMessages!=0) {
                    if (MassStorageMsg.size() < noOfMessages) {
                        Log.d("my", dataSnapshot.getValue().toString() + " add ho gaya");
                        Log.d("my", "MassStorage size: " + MassStorageMsg.size());
                        MassStorageMsg.add(msg);
                        MassStorageKey.add(key);
                    }

//              load 60 messages initially----------------------->
                    if (MassStorageMsg.size() == noOfMessages) {
                        if (firstTime) {
                            firstTime = false;
                            Log.d("my", "Load 60 messages initially");
                            loadMessages();
//                  then for new messages----------------------------------->
                        } else {
                            Log.d("my", "for new messages");
                            adapterClass.passMsgUSER(dataSnapshot.getKey(), dataSnapshot.getValue().toString());
                        }
                    }
                }else {
                    adapterClass.passMsgUSER(dataSnapshot.getKey(), dataSnapshot.getValue().toString());
                }
                adapterClass.notifyDataSetChanged();


//              For scrolling---------------------->
                recyclerView.post(new Runnable() {
                    @Override
                    public void run() {
                        recyclerView.smoothScrollToPosition(adapterClass.msg.size() + 1);
                    }
                });
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        if (!SharedPrefs.getPrefs().getBoolean(SharedPrefs.NOTI, true)) {
            ref.child(children1).addChildEventListener(childEventListener);
        }

//      push the message in database--------------->
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                msgText = msgField.getText().toString();
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat df = new SimpleDateFormat("hh:mm aaa");

                //pushing message to the database with a unique key--->
                if (!msgText.equals("")) {
                    ref.child(children1).child(ref.push().getKey() +
                            LoginActivity.UserName +
                            "Time" +
                            df.format(calendar.getTime()))
                            .setValue(msgText);

                    ref.child(children2).child(ref.push().getKey() +
                            LoginActivity.UserName +
                            "Time" +
                            df.format(calendar.getTime()))
                            .setValue("new:" + msgText);

                    msgField.setText(null);
                    mpSend.start();
                    adapterClass.notifyItemInserted(adapterClass.msg.size());
                }
            }
        });
    }

    private void loadMessages() {
        //first time---->
        if (MassStorageMsg.size() > 60) {
            int min = MassStorageMsg.size() - 60;
            for (int i = min; i < MassStorageMsg.size(); i++) {
                markRead(min, null);
                adapterClass.passMsgUSER(MassStorageKey.get(i), MassStorageMsg.get(i));
                Log.d("my", "here..." + MassStorageMsg.get(i));
                adapterClass.notifyDataSetChanged();
            }
        } else {
            for (int i = 0; i < MassStorageMsg.size(); i++) {
                markRead(0, null);
                adapterClass.passMsgUSER(MassStorageKey.get(i), MassStorageMsg.get(i));
                Log.d("my", MassStorageMsg.get(i));
                Log.d("my", "Size of massStorage: " + MassStorageMsg.size());
                adapterClass.notifyDataSetChanged();
            }
        }
    }

    String markRead(int min, String newMsg) {
        if (min > -1) {
            for (int i = min; i < MassStorageMsg.size(); i++) {
                if (MassStorageMsg.get(i).contains("new:")) {
                    String convertOld=MassStorageMsg.get(i).substring(4);
                    MassStorageMsg.set(i, convertOld);
                    ref.child(children1).child(MassStorageKey.get(i)).setValue(convertOld);
                }
            }
        } else {
            int index = newMsg.indexOf("new:");
            return newMsg.substring(index + 4);
        }
        return null;
    }

    @Override
    public void loadMore(int noOfTimes) {
        ArrayList<String> msg = new ArrayList<>();
        ArrayList<String> key = new ArrayList<>();
        int min = MassStorageMsg.size() - (60 * noOfTimes);
        int max = MassStorageMsg.size() - (60 * (noOfTimes - 1));
        if (min <= 0) {
            for (int i = 0; i < max; i++) {
                msg.add(MassStorageMsg.get(i));
                key.add(MassStorageKey.get(i));
            }
        } else {
            for (int i = min; i < max; i++) {
                msg.add(MassStorageMsg.get(i));
                key.add(MassStorageKey.get(i));
            }
        }
        adapterClass.adjustMsgArray(key, msg);
//        adapterClass.notifyDataSetChanged();
    }

    //  Prepare option menu in chat screen---------------------->
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.roombutton, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.AddOrRemove:
                DatabaseOperations.addFriend(this);
        }
        return super.onOptionsItemSelected(item);
    }

    //  handling user mood swings---------------->
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        adapterClass.msg.clear();
        adapterClass.key.clear();
        ref.child(children1).removeEventListener(childEventListener);
        friendUserName = null;
        Intent in = new Intent(this, ChooseMate.class);
        startActivity(in);
    }


    //  Make user go online------------------>
    @Override
    protected void onResume() {
        super.onResume();
        SharedPrefs.setNoti(false);
        DatabaseOperations.goOnline();
    }

    //  Make user go offline------------------>
    @Override
    protected void onUserLeaveHint() {
        super.onUserLeaveHint();
        Intent in = new Intent(this, MessageRecieveClass.class);
        startService(in);
        DatabaseOperations.goOffline();
    }

    //  Turn on the notification if user leaves the app----------------->
    @Override
    protected void onStop() {
        super.onStop();
        Intent in = new Intent(this, MessageRecieveClass.class);
        startService(in);
        SharedPrefs.setNoti(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Intent in = new Intent(this, MessageRecieveClass.class);
        startService(in);
        SharedPrefs.setNoti(true);
    }
}
