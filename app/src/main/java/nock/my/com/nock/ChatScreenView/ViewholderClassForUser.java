package nock.my.com.nock.ChatScreenView;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import nock.my.com.nock.R;


public class ViewholderClassForUser extends RecyclerView.ViewHolder {

    TextView text;
    TextView date;
//    TextView friendMsgField;

    public ViewholderClassForUser(View itemView) {
        super(itemView);
        text = itemView.findViewById(R.id.msgtext);
        date = itemView.findViewById(R.id.date);
//        friendMsgField = itemView.findViewById(R.id.friendmsgtext);
    }
}
