package zip5001.my.com.zip;

import android.content.Context;
import android.util.ArraySet;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import zip5001.my.com.zip.Fragment.TabsArrayClass;
import zip5001.my.com.zip.activities.LoginActivity;
import zip5001.my.com.zip.activities.MainActivity;

public class DatabaseOperations {

    final public static String ONLINE = "online";
    final public static String OFFLINE = "offline";
    private static DatabaseReference removeF;
    private static String UF = LoginActivity.UserName + " and ";
    private static String FU = " and " + LoginActivity.UserName;
    private static boolean toAdd = false;
    private static boolean found = false;
    private static DatabaseReference addF;
    private static String friend;
    public static Set<String> deleteRoomUsers = new HashSet<>();
    public static ArrayList<String> deleteChat = new ArrayList<>();

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

    public static void addFriend(String friendName, Context con) {
        final Context context = con;
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Room_Status");
        ref.push().child(LoginActivity.UserName);
        ref.child(LoginActivity.UserName).child(friendName).setValue("YES");

        addF = FirebaseDatabase.getInstance().getReference("Room");
        addF.push().child(LoginActivity.UserName);
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
    }

    public static void removeFriend(Context con) {
        final Context context = con;
        removeF = FirebaseDatabase.getInstance().getReference("Room").child(LoginActivity.UserName);
        Query query = removeF.orderByValue();
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (deleteRoomUsers.contains(snapshot.getValue().toString())) {
                        String key = snapshot.getKey();
                        removeF.child(key).setValue(null);
                        deleteRoomUsers.remove(snapshot.getValue().toString());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                AlertBox.builder(context, "Error", databaseError.getMessage(), "OK");
            }
        });
    }


    public static void removeChat(Context con) {
        final Context context = con;
        for (String friend : deleteChat) {
            removeF = FirebaseDatabase.getInstance().getReference("Chat").child(LoginActivity.UserName + " and " + friend);
            Query query = removeF.orderByValue();
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String key = snapshot.getKey();
                        removeF.child(key).setValue(null);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    AlertBox.builder(context, "Error", databaseError.getMessage(), "OK");
                }
            });
        }
    }
}
