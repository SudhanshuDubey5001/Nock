package zip5001.my.com.zip.activities;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import java.util.Calendar;

import zip5001.my.com.zip.AlertBox;
import zip5001.my.com.zip.ChatScreenView.AdapterClass;
import zip5001.my.com.zip.Fragment.TabsArrayClass;
import zip5001.my.com.zip.Fragment.ViewPagerCreation;
import zip5001.my.com.zip.MessageRecieveClass;
import zip5001.my.com.zip.R;
import zip5001.my.com.zip.SharedPrefs;
import zip5001.my.com.zip.DatabaseOperations;

public class MainActivity extends AppCompatActivity {

    public static final String GETFRIEND = "friend";
    public static final String GETID = "id";
    public static final String REC = "receive_message";

    public static boolean NOTI_Enable = false;

    EditText msgField;
    AdapterClass adapterClass = new AdapterClass();
    public static String friendUserName;

    DatabaseReference ref;
    static String children1;
    static String children2;
    String msgText;
    RecyclerView recyclerView;
    public static MediaPlayer mpRecieve;
    MediaPlayer mpSend;
    String status;
    boolean UserOpinion = false;
    boolean presentInRoomStatus;
    DatabaseReference refRoom;
    ChildEventListener childEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //notification---->
        NOTI_Enable = false;
        SharedPrefs.setNoti(NOTI_Enable);

        //message sounds------->
        mpSend = MediaPlayer.create(this, R.raw.send);
        mpRecieve = MediaPlayer.create(this, R.raw.recieve);

//       friend name------------>
        int id=getIntent().getIntExtra(GETID,0);
        if(id== ViewPagerCreation.USERS) {
            friendUserName = TabsArrayClass.Users.get(getIntent().getIntExtra(GETFRIEND, 0));
        }else if(id==ViewPagerCreation.MESSAGES){
            friendUserName = TabsArrayClass.MessagesUsers.get(getIntent().getIntExtra(GETFRIEND, 0));
        }else {
            friendUserName = TabsArrayClass.RoomUsers.get(getIntent().getIntExtra(GETFRIEND, 0));
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

//      Adding Room-status of friend if not present-------->
        refRoom = FirebaseDatabase.getInstance().getReference("Room_Status");
        refRoom.push().child(LoginActivity.UserName);
        Query q = refRoom.child(LoginActivity.UserName).orderByKey();
        q.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (snapshot.getKey().equals(friendUserName)) {
                        presentInRoomStatus = true;
                        Log.d("my", "mila mila..such mein!!");
                        break;
                    } else {
                        Log.d("my", "nahi mila");
                        presentInRoomStatus = false;
                    }
                }
                if (!presentInRoomStatus) {
                    refRoom.child(LoginActivity.UserName).child(friendUserName).setValue("NO");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                AlertBox.builder(MainActivity.this, "Error", databaseError.getMessage(), "OK");
            }
        });

//        read the messages old and current-------------->
        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if(dataSnapshot.getValue().toString().contains("new:")){
                    int index = dataSnapshot.getValue().toString().indexOf("new:");
                    String oldMsg= dataSnapshot.getValue().toString().substring(index+4);
                    ref.child(children1).child(dataSnapshot.getKey()).setValue(oldMsg);
                }

                adapterClass.notifyDataSetChanged();

//                  For scrolling---------------------->
                recyclerView.post(new Runnable() {
                    @Override
                    public void run() {
                        recyclerView.smoothScrollToPosition(adapterClass.msg.size() + 1);
                    }
                });

                adapterClass.passMsgUSER(dataSnapshot.getKey(), dataSnapshot.getValue().toString());
                Log.d("my", dataSnapshot.getValue().toString());
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
                            .setValue("new:"+msgText);

                    msgField.setText(null);
                    mpSend.start();
                    adapterClass.notifyItemInserted(adapterClass.msg.size());
                }
            }
        });
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
                DatabaseOperations.addFriend(friendUserName, this);
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
        friendUserName=null;
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
        DatabaseOperations.goOffline();
    }

    //  Turn on the notification if user leaves the app----------------->
    @Override
    protected void onStop() {
        super.onStop();
        SharedPrefs.setNoti(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharedPrefs.setNoti(true);
    }

}
