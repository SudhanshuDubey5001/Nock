package nock.my.com.nock;

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
import android.widget.RemoteViews;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import nock.my.com.nock.activities.LoginActivity;
import nock.my.com.nock.activities.MainActivity;

public class MessageRecieveClass extends Service {

    String children1, children2;
    String userName, friendUsername;
    DatabaseReference ref;
    LinkedList<String> msgArry = new LinkedList<>();
    LinkedList<String> parentArry = new LinkedList<>();
    LinkedList<Long> timeArry = new LinkedList<>();
    String friends;
    ChildEventListener childEventListener;
    public static boolean value;
    //    static NotificationCompat.MessagingStyle style = new NotificationCompat.MessagingStyle("Nock");
    NotificationCompat.Builder builder;

//    private void push(String parent, String msg, long time) {
//        style.addMessage(new NotificationCompat.MessagingStyle.Message(msg,time,parent));
//    }

    private void pop() {
        msgArry.removeFirst();
        parentArry.removeFirst();
        timeArry.removeFirst();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        ref = FirebaseDatabase.getInstance().getReference("Chat");

        builder = new NotificationCompat.Builder(MessageRecieveClass.this)
                .setSmallIcon(R.mipmap.chaticon)
                .setPriority(NotificationCompat.PRIORITY_MAX);

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
                    int timeIndex = dataSnapshot.getKey().indexOf("Time");
                    String time = dataSnapshot.getKey().substring(timeIndex + 4, timeIndex + 12);

//                    SimpleDateFormat formatter = new SimpleDateFormat("hh:mm aaa");
//                    Date gmt = null;
//                    try {
//                        gmt = formatter.parse(time);
//                    } catch (ParseException e) {
//                        e.printStackTrace();
//                    }
//                    long millisTime = gmt.getTime();
//                    push(parentSender,msgInNoti,millisTime);

                    setNotification(builder, parentSender, msgInNoti, MessageRecieveClass.value);

//                    if (msgArry.size() > 5) {
//                        setNotification(builder, MessageRecieveClass.value);
//                        pop();
//                        push(parentSender, msgInNoti, millisTime);
//                    } else {
//                        push(parentSender, msgInNoti, millisTime);
//                    }
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

    public static Set<String> stopSenders = new HashSet<>();
    public static Set<String> stopSendersRedundantMsg = new HashSet<>();
    int i;

    private void setNotification(NotificationCompat.Builder builder, String parent, String msg, boolean enable) {
        builder.setContentTitle(parent)
                .setContentText(msg)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(msg));

        Log.d("my", "Values are: " + msg + "<-----Service class");

//      check if sender has already sent new message, if not then--->
        if (enable && !stopSenders.contains(parent)) {
            stopSenders.add(parent);
            Log.d("my", "notification no= " + i++);
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


