package zip5001.my.com.zip;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashSet;
import java.util.Set;

import zip5001.my.com.zip.activities.LoginActivity;
import zip5001.my.com.zip.activities.MainActivity;

public class MessageRecieveClass extends Service {

    String children1, children2;
    String userName, friendUsername;
    DatabaseReference ref;
    //    public AdapterClass adapterClass = new AdapterClass();
//    private ArrayList<String> notificationArrayMsg = new ArrayList<>();
//    private ArrayList<String> notificationArrayFriend = new ArrayList<>();
    String friends;
    ChildEventListener childEventListener;
    public static boolean value;

    @Override
    public void onCreate() {
        super.onCreate();

        ref = FirebaseDatabase.getInstance().getReference("Chat");

        userName = LoginActivity.UserName;
        friendUsername = MainActivity.friendUserName;

        children1 = userName + " and " + friendUsername;
        children2 = friendUsername + " and " + userName;

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

//        if (LoginActivity.auth != null) {       //check if User is logged IN or not
//            Query query = ref.orderByKey();
//            query.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot) {
//                    String friendsKey;
//                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                        friendsKey = snapshot.getKey();
//                        if (friendsKey.contains(LoginActivity.UserName)) {
//                            Log.d("my", "Level 1 reached!");
//                            int index = friendsKey.indexOf(LoginActivity.UserName);
//                            if (index == 0) {
//                                Log.d("my", "Level 2 reached!");
//                                friends = friendsKey.substring(index + LoginActivity.UserName.length() + 5);
//                                Query query = ref.child(LoginActivity.UserName + " and " + friends).orderByValue();
//                                query.addListenerForSingleValueEvent(new ValueEventListener() {
//                                    @Override
//                                    public void onDataChange(DataSnapshot dataSnapshot) {
//                                        if (SharedPrefs.getPrefs().getBoolean(SharedPrefs.NOTI, true)) {
//                                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                                                boolean isthereNew = snapshot.getValue().toString().contains("new:");
//                                                if (isthereNew) {
//                                                    notificationArrayFriend.clear();
//                                                    notificationArrayMsg.clear();
//                                                    int i = snapshot.getValue().toString().indexOf("new:");
//                                                    String msgInNoti = snapshot.getValue().toString().substring(i + 4);
//                                                    int index = snapshot.getRef().getParent().toString().indexOf(LoginActivity.UserName);
//                                                    String and = "%20and%20";
//                                                    String parentSender = snapshot.getRef().getParent().toString().substring(index + LoginActivity.UserName.length() + and.length());
//                                                    Log.d("my", "Msg: " + snapshot.getValue().toString() + " FROM: " + parentSender);
//                                                    notificationArrayMsg.add(msgInNoti);
//                                                    notificationArrayFriend.add(parentSender);
//                                                }
//                                            }
//
//                                            if (!notificationArrayMsg.isEmpty()) {
//                                                NotificationCompat.Builder builder = new NotificationCompat.Builder(MessageRecieveClass.this)
//                                                        .setSmallIcon(R.mipmap.chaticon)
//                                                        .setContentTitle(notificationArrayFriend.get(notificationArrayFriend.size() - 1))
//                                                        .setContentText(notificationArrayMsg.get(notificationArrayMsg.size() - 1))
//                                                        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
//                                                        .setPriority(NotificationCompat.PRIORITY_MAX);
//                                                Log.d("my", "Values are: " + notificationArrayMsg.get(notificationArrayMsg.size() - 1) + "<-----Service class");
//
//                                                NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//                                                Log.d("my", "New notification------> :D");
//                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                                                    assert manager != null;
//                                                    manager.notify(NotificationManager.IMPORTANCE_HIGH, builder.build());
//                                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                                                        NotificationChannel channel = new NotificationChannel("bu", "notification", NotificationManager.IMPORTANCE_HIGH);
//                                                        manager.createNotificationChannel(channel);
//                                                    }
//                                                } else {
//                                                    assert manager != null;
//                                                    manager.notify(0, builder.build());
//                                                }
//                                            }
//                                        }
//                                    }
//
//                                    @Override
//                                    public void onCancelled(DatabaseError databaseError) {
//                                    }
//                                });
//                            }
//                        }
//                    }
//                }
//
//                @Override
//                public void onCancelled(DatabaseError databaseError) {
//
//                }
//            });
//        }

//      for changes in each user for checking new messages------->
        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                boolean isthereNew = dataSnapshot.getValue().toString().contains("new:");
                if (isthereNew) {
                    int i = dataSnapshot.getValue().toString().indexOf("new:");
                    String msgInNoti = dataSnapshot.getValue().toString().substring(i + 4);
                    int index = dataSnapshot.getRef().getParent().toString().indexOf(LoginActivity.UserName);
                    String and = "%20and%20";
                    String parentSender = dataSnapshot.getRef().getParent().toString().substring(index + LoginActivity.UserName.length() + and.length());
                    Log.d("my", "Enable value: " + MessageRecieveClass.value);
                    setNotification(parentSender, msgInNoti, MessageRecieveClass.value);
                }
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


        if (LoginActivity.auth != null) {       //check if User is logged IN or not
            Query query = ref.orderByKey();
            query.addValueEventListener(new ValueEventListener() {
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
                                String childName = LoginActivity.UserName + " and " + friends;
                                ref.child(childName).addChildEventListener(childEventListener);
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        return super.onStartCommand(intent, flags, startId);
    }

    Set<String> stopSenders = new HashSet<>();
    Set<String> stopSendersRedundantMsg = new HashSet<>();

    private void setNotification(String parent, String msg, boolean enable) {
//      setting up notification attributes------>
        NotificationCompat.Builder builder = new NotificationCompat.Builder(MessageRecieveClass.this)
                .setSmallIcon(R.mipmap.chaticon)
                .setContentTitle(parent)
                .setContentText(msg)
                .setPriority(NotificationCompat.PRIORITY_MAX);
        Log.d("my", "Values are: " + msg + "<-----Service class");

//      check if sender has already sent new message, if not then--->
        if (enable && !stopSenders.contains(parent)) {
            stopSenders.add(parent);
//          set sound first------->
            builder = builder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
            showNotification(builder);
            //if sender has already sent then just show notification without sound, only for new messages other than notified--->
        } else if (enable && !stopSendersRedundantMsg.contains(msg)) {
            stopSendersRedundantMsg.add(msg);
            showNotification(builder);
        }
    }

    private void showNotification(NotificationCompat.Builder builder) {
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Log.d("my", "New notification------> :D");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            assert manager != null;
            manager.notify(NotificationManager.IMPORTANCE_HIGH, builder.build());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel("bu", "notification", NotificationManager.IMPORTANCE_HIGH);
                manager.createNotificationChannel(channel);
            }
        } else {
            assert manager != null;
            manager.notify(0, builder.build());
        }
    }
}


