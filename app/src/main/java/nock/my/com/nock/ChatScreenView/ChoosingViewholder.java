package nock.my.com.nock.ChatScreenView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.HashSet;
import java.util.Set;

import nock.my.com.nock.DatabaseOperations;
import nock.my.com.nock.Fragment.TabsArrayClass;
import nock.my.com.nock.Fragment.ViewPagerCreation;
import nock.my.com.nock.R;
import nock.my.com.nock.activities.ChooseMate;
import nock.my.com.nock.activities.LoginActivity;
import nock.my.com.nock.activities.MainActivity;


public class ChoosingViewholder extends RecyclerView.ViewHolder {

    TextView userIDs;
    int position;
    int id;
    public static Set<View> viewArray = new HashSet<>();
    private Context context;
    private Intent in;
    public static boolean LongClickheld = false;

    public ChoosingViewholder(View itemView, Context context) {
        super(itemView);
        this.context = context;
        userIDs = itemView.findViewById(R.id.userIDtextView);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("my", "LongClickValue= " + LongClickheld);
                if (!LongClickheld) {
                    in = new Intent(ChoosingViewholder.this.context, MainActivity.class);
                    in.putExtra(MainActivity.GETFRIEND, ChoosingViewholder.this.position);
                    in.putExtra(MainActivity.GETID, ChoosingViewholder.this.id);
                    ChoosingViewholder.this.context.startActivity(in);
                } else {
                    if (viewArray.contains(view)) {
                        view.setBackgroundColor(Color.WHITE);
                        viewArray.remove(view);
                        if (id == ViewPagerCreation.ROOM) {
                            DatabaseOperations.deleteRoom.remove(TabsArrayClass.RoomUsers.get(ChoosingViewholder.this.position));
                        } else if (id == ViewPagerCreation.MESSAGES) {
                            DatabaseOperations.deleteNew.remove(TabsArrayClass.MessagesUsers.get(ChoosingViewholder.this.position));
                        } else if(id== ViewPagerCreation.USERS_B){
                            DatabaseOperations.deleteUsers_B.remove(TabsArrayClass.Users.get(ChoosingViewholder.this.position));
                        }else {
                            DatabaseOperations.deleteUsers_G.remove(TabsArrayClass.UsersGirl.get(ChoosingViewholder.this.position));
                        }
                    }else {
                        viewArray.add(view);
                        view.setBackgroundColor(Color.GRAY);
                        Log.d("my", "click after long click!!!!!!!!!!!!!!!!!!!!");
                        if (id == ViewPagerCreation.ROOM) {
                            DatabaseOperations.deleteRoom.add(TabsArrayClass.RoomUsers.get(ChoosingViewholder.this.position));
                        } else if (id == ViewPagerCreation.MESSAGES) {
                            DatabaseOperations.deleteNew.add(LoginActivity.UserName + " and " + TabsArrayClass.MessagesUsers.get(ChoosingViewholder.this.position));
                        } else if(id==ViewPagerCreation.USERS_B){
                            DatabaseOperations.deleteUsers_B.add(LoginActivity.UserName + " and " + TabsArrayClass.Users.get(ChoosingViewholder.this.position));
                        }else {
                            DatabaseOperations.deleteUsers_G.add(LoginActivity.UserName + " and " + TabsArrayClass.UsersGirl.get(ChoosingViewholder.this.position));
                        }
                    }
                }
            }
        });

        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ChooseMate mate = new ChooseMate();
                mate.goVisible();
                v.setBackgroundColor(Color.GRAY);
                LongClickheld = true;
                viewArray.add(v);
                Log.d("my", "LongClick!!!!!!!!!!!!!!!!!!!");

                if (id == ViewPagerCreation.ROOM) {
                    DatabaseOperations.deleteRoom.clear();
                    DatabaseOperations.deleteRoom.add(TabsArrayClass.RoomUsers.get(ChoosingViewholder.this.position));
                } else if (id == ViewPagerCreation.MESSAGES) {
                    DatabaseOperations.deleteNew.clear();
                    DatabaseOperations.deleteNew.add(LoginActivity.UserName + " and " + TabsArrayClass.MessagesUsers.get(ChoosingViewholder.this.position));
                } else if(id==ViewPagerCreation.USERS_B){
                    DatabaseOperations.deleteUsers_B.clear();
                    DatabaseOperations.deleteUsers_B.add(LoginActivity.UserName + " and " + TabsArrayClass.Users.get(ChoosingViewholder.this.position));
                }else{
                    DatabaseOperations.deleteUsers_G.clear();
                    DatabaseOperations.deleteUsers_G.add(LoginActivity.UserName+ " and " + TabsArrayClass.UsersGirl.get(ChoosingViewholder.this.position));
                }
                return true;
            }
        });
    }
}
