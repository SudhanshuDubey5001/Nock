package zip5001.my.com.zip.ChatScreenView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import zip5001.my.com.zip.DatabaseOperations;
import zip5001.my.com.zip.Fragment.TabsArrayClass;
import zip5001.my.com.zip.Fragment.ViewPagerCreation;
import zip5001.my.com.zip.R;
import zip5001.my.com.zip.activities.ChooseMate;
import zip5001.my.com.zip.activities.LoginActivity;
import zip5001.my.com.zip.activities.MainActivity;

public class ChoosingViewholder extends RecyclerView.ViewHolder {

    TextView userIDs;
    int position;
    int id;
    private Context context;
    private Intent in;
    private boolean LongClickheld = false;

    ChoosingViewholder(View itemView, Context context) {
        super(itemView);
        this.context = context;

        userIDs = itemView.findViewById(R.id.userIDtextView);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!LongClickheld) {
                    in = new Intent(ChoosingViewholder.this.context, MainActivity.class);
                    in.putExtra(MainActivity.GETFRIEND, ChoosingViewholder.this.position);
                    in.putExtra(MainActivity.GETID, ChoosingViewholder.this.id);
                    ChoosingViewholder.this.context.startActivity(in);
                } else {
                    view.setBackgroundColor(Color.GRAY);
                    if (id == ViewPagerCreation.ROOM) {
                        DatabaseOperations.deleteRoom.add(TabsArrayClass.RoomUsers.get(ChoosingViewholder.this.position));
                    } else if (id == ViewPagerCreation.MESSAGES) {
                        DatabaseOperations.deleteNew.add(TabsArrayClass.MessagesUsers.get(ChoosingViewholder.this.position));
                    } else {
                        DatabaseOperations.deleteUsers.add(TabsArrayClass.Users.get(ChoosingViewholder.this.position));
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

                if (id == ViewPagerCreation.ROOM) {
                    DatabaseOperations.deleteRoom.clear();
                    DatabaseOperations.deleteRoom.add(TabsArrayClass.RoomUsers.get(ChoosingViewholder.this.position));
                } else if (id == ViewPagerCreation.MESSAGES) {
                    DatabaseOperations.deleteNew.clear();
                    DatabaseOperations.deleteNew.add(LoginActivity.UserName + " and " + TabsArrayClass.MessagesUsers.get(ChoosingViewholder.this.position));
                } else {
                    DatabaseOperations.deleteUsers.clear();
                    DatabaseOperations.deleteNew.add(LoginActivity.UserName + " and " + TabsArrayClass.Users.get(ChoosingViewholder.this.position));
                }
                return true;
            }
        });
    }
}
