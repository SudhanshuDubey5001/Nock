package zip5001.my.com.zip.ChatScreenView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import zip5001.my.com.zip.DatabaseOperations;
import zip5001.my.com.zip.Fragment.TabsArrayClass;
import zip5001.my.com.zip.Fragment.ViewPagerCreation;
import zip5001.my.com.zip.R;
import zip5001.my.com.zip.activities.ChooseMate;
import zip5001.my.com.zip.activities.MainActivity;

public class ChoosingViewholder extends RecyclerView.ViewHolder {

    TextView userIDs;
    int position;
    int id;
    Context context;
    Intent in;
    boolean LongClickheld = false;

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
                        DatabaseOperations.deleteRoomUsers.add(TabsArrayClass.RoomUsers.get(ChoosingViewholder.this.position));
                    } else {
                        DatabaseOperations.deleteChat.add(TabsArrayClass.MessagesUsers.get(ChoosingViewholder.this.position));
                    }
                }
            }
        });

        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (id == ViewPagerCreation.ROOM) {
                    LongClickheld = true;
                    ChooseMate mate = new ChooseMate();
                    mate.goVisible();
                    DatabaseOperations.deleteRoomUsers.add(TabsArrayClass.RoomUsers.get(ChoosingViewholder.this.position));
                    v.setBackgroundColor(Color.GRAY);
                }else{
                    LongClickheld=true;
                    DatabaseOperations.deleteChat.clear();
                    ChooseMate mate = new ChooseMate();
                    mate.goVisible();
                    DatabaseOperations.deleteChat.add(TabsArrayClass.MessagesUsers.get(ChoosingViewholder.this.position));
                    v.setBackgroundColor(Color.GRAY);
                }
                return true;
            }
        });
    }
}
