package zip5001.my.com.zip;

import android.content.Context;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import zip5001.my.com.zip.Fragment.ViewPagerCreation;
import zip5001.my.com.zip.activities.LoginActivity;
import zip5001.my.com.zip.activities.MainActivity;

public class DatabaseOperations {

    final public static String ONLINE = "online";
    final public static String OFFLINE = "offline";
    private static DatabaseReference removeF;
    private static boolean toAdd = false;
    private static DatabaseReference addF;
    private static Set<String> arr = new HashSet<>();
    public static Set<String> deleteRoom = new HashSet<>();
    public static Set<String> deleteNew = new HashSet<>();
    public static Set<String> deleteUsers = new HashSet<>();

    public static void goOnline() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Status");
        ref.child(LoginActivity.UserName).setValue(ONLINE);
    }

    public static void goOffline() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Status");
        ref.child(LoginActivity.UserName).setValue(OFFLINE);
    }

    public static void logOut() {
        LoginActivity.auth.signOut();
    }

    public static void addFriend(Context con) {
        final Context context = con;

        addF = FirebaseDatabase.getInstance().getReference("Room");
//      if not present in room--->
        addF.push().child(LoginActivity.UserName);
//      search for already present or not------>
        Query query = addF.child(LoginActivity.UserName).orderByValue();
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (snapshot.getValue().toString().equals(MainActivity.friendUserName)) {
                        toAdd = true;
                        break;
                    } else {
                        toAdd = false;
                    }
                }

                if (toAdd) {
                    AlertBox.builder(context, "Error", "Already present in your Room", "OK");
                } else {
                    addF.child(LoginActivity.UserName).push().setValue(MainActivity.friendUserName);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
//
//        addF.push().child(LoginActivity.UserName);
//        Query query = addF.child(LoginActivity.UserName).orderByValue();
//        query.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                    if (snapshot.getValue().toString().equals(MainActivity.friendUserName)) {
//                        toAdd = true;
//                        break;
//                    } else {
//                        toAdd = false;
//                    }
//                }
//
//                if (toAdd) {
//                    AlertBox.builder(context, "Error", "Already present in your Room", "OK");
//                } else {
//                    addF.child(LoginActivity.UserName).push().setValue(MainActivity.friendUserName);
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
    }

    public void remove(Set<String> arr, int id) {
        DatabaseOperations.arr.addAll(arr);
        Query query;

        if (id == ViewPagerCreation.ROOM) {
            removeF = FirebaseDatabase.getInstance().getReference("Room").child(LoginActivity.UserName);
            query = removeF.orderByValue();
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        if (DatabaseOperations.arr.contains(snapshot.getValue().toString())) {
                            String key = snapshot.getKey();
                            removeF.child(key).setValue(null);
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        } else {
            myarr.addAll(arr);
            Log.d("my", "yahan tak aa gaye..isRoom false hai");
            removeF = FirebaseDatabase.getInstance().getReference("Chat");
            for (String chatUser : arr) {
                Log.d("my",chatUser);
                query = removeF.child(chatUser).orderByValue();
                removeChat(query);
            }
        }
    }

    private Set<DeleteChatUser> user =new HashSet<>();
    private Set<String> myarr=new HashSet<>();
    long total =0;

    private void removeChat(Query query){
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("my","Count : "+dataSnapshot.getChildrenCount());
                DeleteChatUser myuser=new DeleteChatUser();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Log.d("my",snapshot.getValue().toString());
                    myuser.keys.add(snapshot.getKey());
                }
                user.add(myuser);
                total++;
                if (total==myarr.size()){
                    removetheChat();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("my",databaseError.getMessage());
            }
        });
    }

    private void removetheChat(){
        DatabaseReference ref=FirebaseDatabase.getInstance().getReference("Chat");
        Iterator itr=user.iterator();
        for (String chatUser:myarr){
            DeleteChatUser d=(DeleteChatUser)itr.next();
            for(String key:d.keys){
                ref.child(chatUser).child(key).setValue(null);
            }
        }
    }


    class DeleteChatUser{
        ArrayList<String> keys=new ArrayList<>();
    }


//    public static void removeChat(Context con) {
//        final Context context = con;
//        for (String friend : deleteNew) {
//            removeF = FirebaseDatabase.getInstance().getReference("Chat").child(LoginActivity.UserName + " and " + friend);
//            Query query = removeF.orderByValue();
//            query.addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot) {
//                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                        String key = snapshot.getKey();
//                        removeF.child(key).setValue(null);
//                    }
//                }
//
//                @Override
//                public void onCancelled(DatabaseError databaseError) {
//                    AlertBox.builder(context, "Error", databaseError.getMessage(), "OK");
//                }
//            });
//        }
//    }
}
